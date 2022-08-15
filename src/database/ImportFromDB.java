package database;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import action.CupActionStack;
import buildings.BuildingList;
import cupcarbon.CupCarbon;
import device.DeviceList;
import map.NetworkParameters;
import markers.MarkerList;
import markers.Routes;
import project.Project;
import user.UserList;
import visibility.VisibilityLauncher;

/**
 * This Class is used to import data from database to client
 * @author Bang Tran
 *
 */
public class ImportFromDB {
	static String defaultDBName = "cs682";
	static String defaultProjectName = "cs682";
	static String defaultDevicesCollection = "devices";
	static String defaultUsersCollection = "users";
	static String defaultRadioModulesCollection = "radio_modules";
	static String defaultPrjPreferencesCollection = "proj_preferences";
	static String defaultSimulPreferencesCollection = "simul_preferences";

	/**
	 * Open project form database. 
	 * It will get deviceData, radioData, projectData, simulationData, userData, buildingData, markerData from database and call each openFromDB method.
	 * 
	 */
	public static void openProject() {
		CupCarbon.cupCarbonController.displayPermanentMessage_th("Reading database...");

		MongoCollection<Document> devices = DBMethods.getDB(defaultDBName).getCollection(defaultDevicesCollection);
		//MongoCollection<Document> radio_modules = DBMethods.getDB(defaultDBName).getCollection(defaultRadioModulesCollection);
		MongoCollection<Document> users = DBMethods.getDB(defaultDBName).getCollection(defaultUsersCollection);
		MongoCollection<Document> proj_preferences = DBMethods.getDB(defaultDBName).getCollection(defaultPrjPreferencesCollection);
		MongoCollection<Document> simul_preferences = DBMethods.getDB(defaultDBName).getCollection(defaultPrjPreferencesCollection);
		MongoCollection<Document> buildings = DBMethods.getDB(defaultDBName).getCollection("buildings");
		MongoCollection<Document> markers = DBMethods.getDB(defaultDBName).getCollection("markers");


		DeviceList.propagationsCalculated = false;
		CupActionStack.init();
		Project.reset();
		Project.setProjectName("DataBase Mode", defaultProjectName);
		FindIterable<Document> deviceData = DBMethods.findWithPrefix(devices, "device");
		FindIterable<Document> radioData = DBMethods.findWithPrefix(devices, "radio_module");
		FindIterable<Document> projectData = DBMethods.find(proj_preferences);
		FindIterable<Document> simulationData = DBMethods.find(simul_preferences);
		FindIterable<Document> userData = DBMethods.find(users);
		FindIterable<Document> buildingData = DBMethods.find(buildings);
		FindIterable<Document> markerData = DBMethods.find(markers);

		try {
			BuildingList.openFromDB(buildingData);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DeviceList.openFromDB(deviceData, radioData);
		MarkerList.openFromDB(markerData);
		Project.loadParametersFromDB(projectData);
		CupCarbon.cupCarbonController.loadSimulationParamsFromDB(simulationData);
		CupCarbon.cupCarbonController.simulationParametersApply();
		if(DeviceList.propagationsCalculated) {
			DeviceList.calculatePropagations();
			VisibilityLauncher.calculate();
		}
		if(NetworkParameters.displayAllRoutes) {
			MarkerList.reset();
			Routes.loadRoutes();
		}
		else{
			Routes.hideAll();
		}
//		// yiwei
//		UserList.reset();
		UserList.openFromDB(userData);
		CupCarbon.cupCarbonController.displayShortGoodMessage_th("Default workspace loaded");

		map.MapLayer.repaint();

	}


}
