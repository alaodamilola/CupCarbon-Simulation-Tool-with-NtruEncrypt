package senscript;

import java.util.Arrays;
import java.util.Vector;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import cupcarbon.CupCarbon;
import database.DBMethods;
import device.BaseStation;
import device.DeviceList;
import device.SensorNode;
import simulation.WisenSimulation;
import user.User;

/** This method is used to print data to database in "result" collection
 * 
 * edited by Chenjun
 * 
 * handle printdb command
 * printdb String[] arg
 * example:
 * printdb hello 12 23 34
 * 
 * in Mongodb:
 * sensorid: XXX
 * 12:31:00 4/19/20: hello 12 23 34
 * @author Yiwei Yao
 */
public class Command_PRINTDB extends Command {
	
	protected String data = "";
	protected String [] args ;

	public Command_PRINTDB(SensorNode sensor, String [] args) {
		this.sensor = sensor ;
		this.args = args ;
	}
	
	@Override
	public double execute() {
		WisenSimulation.simLog.add("S" + sensor.getId() + " PRINTDB "+Arrays.toString(args));
		Vector<User> users = null;
		String [] tab = null;
		
		if(sensor.getClass().equals(BaseStation.class) && !sensor.getUsers().isEmpty()) {
			users = sensor.getUsers();
		} else {
			return 0;
		}
		
		String symbole = "#";
		if(args[1].charAt(0)=='!') {
			symbole = "" + args[1].charAt(1);
			data = sensor.getScript().getVariableValue(args[2]);			
		} else
			data = sensor.getScript().getVariableValue(args[1]);
		if(data==null) {
			String errMessage = "[CupCarbon ERROR] (S"+sensor.getId()+"): The first argument of RDATA cannot be null";
			System.err.println(errMessage);
			CupCarbon.cupCarbonController.displayShortErrMessageTh("ERROR");
			throw new SenScriptException(errMessage);
		} else {
			tab = data.split(symbole);
		}
		
		for(User u : users) {
			// duration
			if(WisenSimulation.time < u.getTimeStart() || WisenSimulation.time > u.getTimeEnd()) {
				System.out.println(u.getName() + " is not ready for time: " + WisenSimulation.time+ " Reason: Not in the duration.");
				continue;
			}
			// frequency
			if(WisenSimulation.time - u.getLastReceivedTime() < u.getPreferredFrequency() && u.getLastReceivedTime() > 0) {
				System.out.println(u.getName() + " is not ready for time: " + WisenSimulation.time + " Reason: frequency is not satisfied.");
				continue;
			}
			
			
			u.setLastReceivedTime(WisenSimulation.time);
			Vector<SensorNode> nodes = u.getSensorsInsideArea();
			int sensorID = Integer.parseInt(tab[2]);
			SensorNode s = DeviceList.getSensorNodeById(sensorID);
			// get rid of routers
			if(!s.getScriptFileName().contains("Sensor")) {
				System.out.println("ignore routers " + "name: " + s.getName());
				continue;
			}
			if(nodes.contains(s)) {
				MongoCollection<Document> project = DBMethods.getDB("cs682").getCollection("result");
				Document document = new Document();
				
				String userID = u.getName();
				String simTime = tab[tab.length-1];
				String lantency = tab[tab.length-2];
				String location = tab[4] + " " + tab[5];
				
				System.out.println("Recording data from sensor: " + sensorID + " for user: " + userID);
				
				document.put("User ID", userID);
				document.put("Simulation time", simTime);
				document.put("Sensor ID", sensorID);
				document.put("Sensor location", location);
				document.put("Lantency", lantency);
				
				int nums = Integer.parseInt(tab[7]);
				for(int i = 0; i < nums; i++) {
					String temp = tab[7 + 2*i +1];
					if(temp.equals("Temperature") && u.temperatureSensing) {
						document.put("Temperature", tab[7 + 2*i +2]);
						System.out.println("Recording Temperature Value For User: " + userID);
					}
					if(temp.equals("Gas") && u.gasSensing) {
						document.put("Gas", tab[7 + 2*i +2]);
						System.out.println("Recording Gas Value For User: " + userID);
					}
					if(temp.equals("Humidity") && u.humiditySensing) {
						document.put("Humidity", tab[7 + 2*i +2]);
						System.out.println("Recording Humidity Value For User: " + userID);
					}
					if(temp.equals("Light") && u.lightSensing) {
						document.put("Lighting", tab[7 + 2*i +2]);
						System.out.println("Recording Lighting Value For User: " + userID);
					}
					if(temp.equals("Water") && u.waterLevelSensing) {
						document.put("Water Level", tab[7 + 2*i +2]);
						System.out.println("Recording Water Level Value For User: " + userID);
					}
					if(temp.equals("Wind") && u.windLevelSensing) {
						document.put("Wind Level", tab[7 + 2*i +2]);
						System.out.println("Recording Wind Level Value For User: " + userID);
					}
				}
				project.insertOne(document);
			}
		}
		

		return 0;
		
	}
	
	@Override
	public String toString() {
		return "PRINTDB";
	}

}
