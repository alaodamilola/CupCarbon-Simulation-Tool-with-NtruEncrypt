package database;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import action.CupActionStack;
import buildings.BuildingList;
import cupcarbon.CupCarbon;
import cupcarbon.CupCarbonController;
import device.DeviceList;
import map.NetworkParameters;
import markers.MarkerList;
import markers.Routes;
import project.Project;
import user.UserList;
import visibility.VisibilityLauncher;

/**
 * This class is used to export data from database to client. Now deprecated 
 * @author Yiwei Yao
 * @deprecated use bang's ImportFrom DB.
 *
 */
public class ExportToClient {
	//==== Bang Tran code
	static String defaultDBName = "cs682";
	static String defaultProjectName = "cs682";
	static String defaultDevicesCollection = "devices";
	static String defaultUsersCollection = "users";
	static String defaultRadioModulesCollection = "radio_modules";
	static String defaultPrjPreferencesCollection = "proj_preferences";
	static String defaultSimulPreferencesCollection = "simul_preferences";




	//==== Bang Tran end


	/**
	 * open project from database
	 * buildingData : prefix: building
	 * deviceData : prefix: device
	 * radioData : prefix: radio_module
	 * markData : prefix: mark
	 * projectData : prefix: project
	 * simulationData : prefix: simulation
	 * @param projectName
	 */
	public static void openProject(String projectName) {
		CupCarbon.cupCarbonController.displayPermanentMessage_th("Loading ...");
		MongoCollection<Document> project = DBMethods.getDB("iot").getCollection(projectName);
		MongoCollection<Document> projectUser = DBMethods.getDB("iot_user").getCollection(projectName);
		DeviceList.propagationsCalculated = false;
		System.out.println(projectName);
		System.out.println("Load From DataBase");
		CupActionStack.init();
		Project.reset();
		Project.setProjectName("DataBase Mode", projectName);
		FindIterable<Document> buildingData = DBMethods.findWithPrefix(project, "building");
		FindIterable<Document> deviceData = DBMethods.findWithPrefix(project, "device");
		FindIterable<Document> radioData = DBMethods.findWithPrefix(project, "radio_module");
		FindIterable<Document> markData = DBMethods.findWithPrefix(project, "mark");
		FindIterable<Document> projectData = DBMethods.findWithPrefix(project, "project");
		FindIterable<Document> simulationData = DBMethods.findWithPrefix(project, "simulation");
		FindIterable<Document> userData = DBMethods.findWithPrefix(projectUser, "user");
		try {
			BuildingList.openFromDB(buildingData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DeviceList.openFromDB(deviceData, radioData);
		MarkerList.openFromDB(markData);
		Project.loadParametersFromDB(projectData);
		CupCarbon.cupCarbonController.loadSimulationParamsFromDB(simulationData);
		CupCarbon.cupCarbonController.applyParameters();
		CupCarbon.cupCarbonController.saveButton.setDisable(false);
		if(DeviceList.propagationsCalculated) {
			DeviceList.calculatePropagations();
			VisibilityLauncher.calculate();
		}
		if(NetworkParameters.displayAllRoutes) {
			MarkerList.reset();
			Routes.loadRoutes();
		}
		else
			Routes.hideAll();
		UserList.openFromDB(userData);
		CupCarbon.cupCarbonController.displayShortGoodMessage_th("Project loaded");
	}

	//Bang Tran codes
	public static void openProject() {
		//CupCarbon.cupCarbonController.displayPermanentMessage_th("Reading database...");

		MongoCollection<Document> devices = DBMethods.getDB(defaultDBName).getCollection(defaultDevicesCollection);
		MongoCollection<Document> radio_modules = DBMethods.getDB(defaultDBName).getCollection(defaultRadioModulesCollection);
		MongoCollection<Document> users = DBMethods.getDB(defaultDBName).getCollection(defaultUsersCollection);
		MongoCollection<Document> proj_preferences = DBMethods.getDB(defaultDBName).getCollection(defaultPrjPreferencesCollection);
		MongoCollection<Document> simul_preferences = DBMethods.getDB(defaultDBName).getCollection(defaultPrjPreferencesCollection);
		MongoCollection<Document> buildings = DBMethods.getDB(defaultDBName).getCollection("buildings");
		MongoCollection<Document> markers = DBMethods.getDB(defaultDBName).getCollection("markers");


		DeviceList.propagationsCalculated = false;
		CupActionStack.init();
		Project.reset();
		Project.setProjectName("DataBase Mode", defaultProjectName);

		FindIterable<Document> deviceData = DBMethods.find(devices);
		FindIterable<Document> radioData = DBMethods.find(radio_modules);
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

		UserList.openFromDB(userData);
		CupCarbon.cupCarbonController.displayShortGoodMessage_th("Default workspace loaded");

		map.MapLayer.repaint();
	}
}
