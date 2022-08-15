package senscript;

import java.util.Vector;

import device.BaseStation;
import device.SensorNode;
import simulation.WisenSimulation;
import user.User;

/** GETU will generate a table which represents all connected users with its concerned area sensors, active/end time and latency.
 * GETU is a new command.
 * GETU X
 * GETU will generate a table which represents all connected users with its concerned area sensors, active/end time and latency.
 * @author Chenjun Li
 *
 */
public class Command_GETU extends Command {
	
	protected String arg1 = "";
	
	public Command_GETU(SensorNode sensor, String arg1) {
		this.sensor = sensor ;
		this.arg1 = arg1 ;
	}
	
	@Override
	public double execute() {		
		String name = arg1;
		int height = 0;
		int width = 0;
		int max = 0;
		if(sensor.getClass().equals(BaseStation.class)) {
			Vector<User> users = sensor.getUsers();
			
			if(users.size() > 0) {
				height = users.size() + 1;
				for(User u : users) {
					int temp = u.getSensorsInsideArea().size();
					if(temp > max) {
						max = temp;
					}
				}
				width = max + 5;
				sensor.getScript().putTable(name, height, width);
				String [][] tab = sensor.getScript().getTable(name);
				tab[0][0] = "" + (height);
				for(int i =1; i < width; i++) {
					tab[0][i] = "X";
				}
				int i = 1;
					
				for(User u : users) {
					Vector<SensorNode> sensors = u.getSensorsInsideArea();
					int temp = sensors.size() + 5;
					tab[i][0] = "" + temp;
					tab[i][1] = u.getName();
					tab[i][2] = "" + u.getTimeStart();
					tab[i][3] = "" + u.getTimeEnd();
					tab[i][4] = "" + u.getTimeDelay();
					int j = 5;
					for(SensorNode s : sensors) {
						tab[i][j] = "" + s.getId();
						j++;
					}
					for(int k = j; k < width; k++) {
						tab[i][k] = "X";
					}
					i++;
				}

			} else {
				sensor.getScript().putTable(name, 2, 2);
				String [][] tab = sensor.getScript().getTable(name);
				tab[0][0] = "0";
				tab[0][1] = "0";
				tab[1][0] = "0";
				tab[1][1] = "0";
			}
			
		} else {
			sensor.getScript().putTable(name, 2, 2);
			String [][] tab = sensor.getScript().getTable(name);
			tab[0][0] = "0";
			tab[0][1] = "0";
			tab[1][0] = "0";
			tab[1][1] = "0";
		}

		
		WisenSimulation.simLog.add("S" + sensor.getId() + " TABLE "+name+"["+height+"]["+width+"]");
		
		return 0 ;
	}

	@Override
	public String toString() {
		return "GETU";
	}
}
