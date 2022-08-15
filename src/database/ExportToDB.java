package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;

import device.Device;
import device.DeviceList;
import device.SensorNode;
import project.Project;
import simulation.SimulationInputs;
import user.User;
import user.UserList;

/**
 * This class is used to export data to database from client.
 * @author Bang Tran
 *
 */
public class ExportToDB {
	static String defaultDBName = "cs682";
	static String defaultProjectName = "cs682";
	static String defaultDevicesCollection = "devices";
	static String defaultUsersCollection = "users";
	static String defaultRadioModulesCollection = "radio_modules";
	static String defaultPrjPreferencesCollection = "proj_preferences";
	static String defaultSimulPreferencesCollection = "simul_preferences";


	public static void saveProjectToDB() {
		MongoCollection<Document> proj_preferences = DBMethods.getDB(defaultDBName).getCollection(defaultPrjPreferencesCollection);

		//Save project preferences to DB
		try {
			if(Project.projectName.equals(""))
				Project.projectName = defaultProjectName;
			DBMethods.emptyCollection(proj_preferences);
			proj_preferences.insertOne(Project.saveParametersToDB());
		} catch(MongoException ex) {
			System.out.println(ex);
		}

		saveSimulationParams();
		saveUsersToDB();
		saveDevicesAndSensorsToDB();

	}


	/**
	 * save every user preferences to database
	 */
	public static void saveUsersToDB() {
		if(UserList.users.size() == 0)		return;
		MongoCollection<Document> usersCollection = DBMethods.getDB(defaultDBName).getCollection(defaultUsersCollection);

		List<Document> userDocument = new ArrayList<Document>();
		User user;
		Vector<User> users = UserList.users;
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			user = iterator.next();
			userDocument.add(user.saveToDB());
		}

		DBMethods.emptyCollection(usersCollection);
		usersCollection.insertMany(userDocument);
	}

	/**
	 * save every sensors and devices to databases
	 */
	public static void saveDevicesAndSensorsToDB() {
		if(DeviceList.sensors.size() <= 0) return;


		MongoCollection<Document> devicesCollection = DBMethods.getDB(defaultDBName).getCollection(defaultDevicesCollection);
		DBMethods.emptyCollection(devicesCollection);

		List<Document> documents = new ArrayList<Document>();
		Device device;
		Vector<Device> devices = DeviceList.devices;

		for (Iterator<Device> iterator = devices.iterator(); iterator.hasNext();) {
			device = iterator.next();
			documents.addAll(device.saveToDB());
		}

		SensorNode sensor;
		Vector<SensorNode> sensors = DeviceList.sensors;
		for (Iterator<SensorNode> iterator = sensors.iterator(); iterator.hasNext();) {
			sensor = iterator.next();
			documents.addAll(sensor.saveToDB());
		}
		devicesCollection.insertMany(documents);
	}

	/**
	 * Save the simulation parameters to database
	 */
	public static void saveSimulationParams() {
		MongoCollection<Document> simulPreferencesCollection = DBMethods.getDB(defaultDBName).getCollection(defaultSimulPreferencesCollection);

		Document document = new Document();
		document.append("prefix", "simulation")
			.append("simulationtime", SimulationInputs.simulationTime)
			.append("mobility", SimulationInputs.mobilityAndEvents)
			.append("simulationspeed", SimulationInputs.visualDelay)
			.append("arrowspeed", SimulationInputs.arrowsDelay)
			.append("log", SimulationInputs.displayLog)
			.append("results", SimulationInputs.displayResults)
			.append("acktype", SimulationInputs.ackType)
			.append("ackproba", SimulationInputs.ackProba)
			.append("acklinks", SimulationInputs.showAckLinks)
			.append("ack", SimulationInputs.ack)
			.append("symmetricalinks", SimulationInputs.symmetricalLinks)
			.append("clockdrift", SimulationInputs.clockDrift)
			.append("visibility", SimulationInputs.visibility)
			.append("results_writing_period", SimulationInputs.resultsWritingPeriod)
			.append("mac_layer", SimulationInputs.macLayer)
			.append("macproba", SimulationInputs.macProba);

		List<Document> otherDocuments = new ArrayList<Document>();
		otherDocuments.add(document);

		DBMethods.emptyCollection(simulPreferencesCollection);
		simulPreferencesCollection.insertMany(otherDocuments);
	}
}
