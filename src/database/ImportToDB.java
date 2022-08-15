package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;

import buildings.BuildingList;
import device.Device;
import device.DeviceList;
import device.SensorNode;
import markers.MarkerList;
import project.Project;
import simulation.SimulationInputs;
import markers.MarkerList;
import buildings.BuildingList;
import user.User;
import user.UserList;

/**
 * This Class is used to import data to database from Client, now deprecated.
 * @author Yiwei Yao
 * @deprecated use bang's Export To DB.
 *
 */
public class ImportToDB {

	/**
	 * @author Bang Tran
	 */
	public static void saveNetworkToDB(){

	}


	public static void saveProjectToDB() {
		MongoCollection<Document> project = DBMethods.getDB("iot").getCollection(Project.projectName);
		MongoCollection<Document> projectUser = DBMethods.getDB("iot_user").getCollection(Project.projectName);
		List<Document> otherDocuments = new ArrayList<Document>();
		DBMethods.dropCollection(project);
		try {
			project.insertOne(Project.saveParametersToDB());
		} catch(MongoException ex) {
			System.out.println(ex);
		}
		if(DeviceList.getSize()>0) {
			saveDevicesAndSensorsToDB();
		}
		if(MarkerList.size()>0) otherDocuments.addAll(MarkerList.saveToDB());
		if(BuildingList.size()>0) otherDocuments.add(BuildingList.saveToDB());
		otherDocuments.add(saveSimulationParams());
		project.insertMany(otherDocuments);
		saveUsersToDB(projectUser);
	}

	// ***************************************************************
	// below is helper method
	// ***************************************************************
	public static void saveUsersToDB(MongoCollection<Document> projectUser) {
		List<Document> userDocument = new ArrayList<Document>();
		User user;
		Vector<User> users = UserList.users;
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {

			user = iterator.next();				

			userDocument.add(user.saveToDB());
		}
		projectUser.insertMany(userDocument);
	}

	public static void saveDevicesAndSensorsToDB() {
		MongoCollection<Document> project = DBMethods.getDB("iot").getCollection(Project.projectName);
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
		project.insertMany(documents);
	}

	public static Document saveSimulationParams() {
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
		return document;
//		try {
//			PrintStream ps = new PrintStream(Project.projectPath + File.separator + "config" + File.separator + "simulationParams.cfg");
//			ps.println("simulationtime:" + SimulationInputs.simulationTime);
//			ps.println("mobility:" + SimulationInputs.mobilityAndEvents);
//			ps.println("simulationspeed:" + SimulationInputs.visualDelay);
//			ps.println("arrowspeed:" + SimulationInputs.arrowsDelay);
//			ps.println("log:" + SimulationInputs.displayLog);
//			ps.println("results:" + SimulationInputs.displayResults);
//			ps.println("acktype:" + SimulationInputs.ackType);
//			ps.println("ackproba:" + SimulationInputs.ackProba);
//			ps.println("acklinks:" + SimulationInputs.showAckLinks);
//			ps.println("ack:" + SimulationInputs.ack);
//			ps.println("symmetricalinks:" + SimulationInputs.symmetricalLinks);
//			ps.println("clockdrift:" + SimulationInputs.clockDrift);
//			ps.println("visibility:" + SimulationInputs.visibility);
//			ps.println("results_writing_period:" + SimulationInputs.resultsWritingPeriod);
//			ps.println("mac_layer:" + SimulationInputs.macLayer);
//			ps.println("macproba:" + SimulationInputs.macProba);
//			ps.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}
}
