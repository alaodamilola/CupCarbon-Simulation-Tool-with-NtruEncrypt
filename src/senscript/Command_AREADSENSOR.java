package senscript;

import device.SensorNode;
import simulation.WisenSimulation;

/**
 * this class is used to return a string in format below to X
 * 5#Temperature#25.333#Gas#84.333бн#end
 * The first number is all nature events this sensor detected. Mostly one for each type, max number will be 6. The order of nature events is not guaranteed.
 * @author Chenjun Li
 *
 */
public class Command_AREADSENSOR extends Command {

	protected String arg =  "";
	
	public Command_AREADSENSOR(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg ;
	}

	@Override
	public double execute() {
		sensor.getBattery().consume(0.0001);
		String value = sensor.getSensorValues();
		WisenSimulation.simLog.add("S" + sensor.getId() + " READ SENSOR: "+value);
		sensor.getScript().addVariable(arg, value);
		return 0;
	}
	
	@Override
	public String toString() {
		return "AREADSENSOR";
	}
}
