package senscript;

import java.util.Vector;

import arduino.BeginInstructions;
import device.BaseStation;
import device.SensorNode;
import simulation.WisenSimulation;
import user.User;

/** return sensors in selectArea.
 * LED V1 V2
 * V1 returns an array of all sensor ID which all connected users have an interest in.
 * V1[0] is the number of sensors.
 * V2 returns an array of all connected users¡¯ concerned sensing values.
 * V2[0] is the number of values.
 * @author Chenjun
 */
public class Command_LED extends Command {

	protected String arg1 = null ;
	protected String arg2 = null ;
	
	
	public Command_LED(SensorNode sensor, String arg1, String arg2) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
		this.arg2 = arg2 ;
	}

	@Override
	public double execute() {
		String sensorList = sensor.getScript().getVariableValue(arg1);
		String eventsList = sensor.getScript().getVariableValue(arg2);
		if(sensor.getClass().equals(BaseStation.class)) {
			Vector<User> users = sensor.getUsers();
			Vector<SensorNode> sensors = new Vector<SensorNode>();
			Vector<String> events = new Vector<String>();
			for(User u : users) {
				Vector<SensorNode> temp = u.getSensorsInsideArea();
				for(SensorNode s : temp) {
					if(!sensors.contains(s)) {
						sensors.add(s);
					}
				}
				Vector<String> temp1 = u.getEvents();
				for(String t : temp1) {
					System.out.println(t);
					if(!events.contains(t)) {
						events.add(t);
					}
				}
			}
			int count = sensors.size();
			int count1 = events.size();
			sensor.getScript().putVector(sensorList, count+1);
			sensor.getScript().putVector(eventsList, count1+1);
			String [] vector = sensor.getScript().getVector(sensorList);
			String [] vector1 = sensor.getScript().getVector(eventsList);
			vector[0] = "" + (count +1);
			vector1[0] = "" + (count1 +1);
			int index = 1;
			for(SensorNode s : sensors) {
				vector[index] ="" +s.getId();
				index++;
			}
			index = 1;
			for(String t : events) {
				vector1[index] = "" + t;
				index++;
			}
			
		} else {
			sensor.getScript().putVector(sensorList, 1);
			sensor.getScript().putVector(eventsList, 1);
			String [] vector = sensor.getScript().getVector(sensorList);
			String [] vector1 = sensor.getScript().getVector(sensorList);
			vector[0] = ""+ 1; 
			vector1[0] = ""+ 1;
		}
		
		return 0;
	}

	@Override
	public String getArduinoForm() {
		BeginInstructions.add("pinMode("+arg1+", OUTPUT);");
		String s = "";
		String v = "'"+arg2.charAt(0)+"'";
		if(arg2.charAt(0)=='$') v = arg2.substring(1)+".charAt(0)";
		
		s = "\tdigitalWrite(13, 1-('1'-"+v+"));";
		
		return s;
	}
	
	@Override
	public String toString() {
		return "LED";
	}
	
}
