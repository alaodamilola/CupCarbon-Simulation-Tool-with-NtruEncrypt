package device;

import sensorunit.StdSensorUnit;

/**
 * @deprecated we no loner use CloudServer anymore, instead we use Base Station to do the job of cloud server
 *
 */
public class CloudServer extends StdSensorNode {
	private boolean temperatureSensing= false;
	private boolean humiditySensing = false;
	private boolean gasSensing = false;
	private boolean lightSensing = false;
	private boolean windSensing = false;
	private boolean waterSensing = false;
	
	private double temperatureLatitude = 0;
	private double temperatureLongitude = 0;
	private double humidityLatitude = 0;
	private double humidityLongitude = 0;
	private double gasLatitude = 0;
	private double gasLongitude = 0;
	private double waterLatitude = 0;
	private double waterLongitude = 0;
	private double windLatitude = 0;
	private double windLongitude = 0;
	private double lightLatitude = 0;
	private double lightLongitude = 0;
	
	private double temperatureValue= 0.0;
	private double humidityValue = 0.0;
	private double gasValue = 0.0;
	private double lightValue = 0.0;
	private double windValue = 0.0;
	private double waterValue = 0.0;
	
	public CloudServer(double x, double y, double z, double radius, double radioRadius, double suRadius, int id) {
		super(x, y, z, radius, radioRadius, suRadius, id);		
	}
	
	public void setTemperature(double temperature, double pos1, double pos2) {
		temperatureSensing = true;
		temperatureLatitude = pos1;
		temperatureLongitude = pos2;
		temperatureValue = temperature;
	}
	
	public void setGas(double gas, double pos1, double pos2) {
		gasSensing = true;
		gasLatitude = pos1;
		gasLongitude = pos2;
		gasValue = gas;
	}

	public void setHumidity(double humidity, double pos1, double pos2) {
		humiditySensing = true;
		humidityLatitude = pos1;
		humidityLongitude = pos2;
		humidityValue = humidity;
	}
	
	public void setWater(double water, double pos1, double pos2) {
		waterSensing = true;
		waterLatitude = pos1;
		waterLongitude = pos2;
		waterValue = water;
	}
	
	public void setWind(double wind, double pos1, double pos2) {
		windSensing = true;
		windLatitude = pos1;
		windLongitude = pos2;
		windValue = wind;
	}
	
	public void setLightd(double light, double pos1, double pos2) {
		lightSensing = true;
		lightLatitude = pos1;
		lightLongitude = pos2;
		lightValue = light;
	}
	
	public double getTemperatureValue() {
		return temperatureValue;
	}
	
	public double getGasValue() {
		return gasValue;
	}
	
	public double getHumidityValue() {
		return humidityValue;
	}
	
	public double getWaterValue() {
		return waterValue;
	}
	
	public double getWindValue() {
		return windValue;
	}
	
	public double getLightValue() {
		return lightValue;
	}
	
	public boolean getTemperatureSensing() {
		return temperatureSensing;
	}
	
	public boolean getGasSensing() {
		return gasSensing;
	}
	
	public boolean getHumiditySensing() {
		return humiditySensing;
	}
	
	public boolean getWaterSensing() {
		return waterSensing;
	}
	
	public boolean getWindSensing() {
		return windSensing;
	}
	
	public boolean getLightSensing() {
		return lightSensing;
	}
	
	public double[] getTemperaturePos() {
		double[] result = new double[2];
		result[0] = temperatureLatitude;
		result[1] = temperatureLongitude;
		return result;
	}
	
	public double[] getGasPos() {
		double[] result = new double[2];
		result[0] = gasLatitude;
		result[1] = gasLongitude;
		return result;
	}
	
	public double[] getHumidityPos() {
		double[] result = new double[2];
		result[0] = humidityLatitude;
		result[1] = humidityLongitude;
		return result;
	}
	
	public double[] getWaterPos() {
		double[] result = new double[2];
		result[0] = waterLatitude;
		result[1] = waterLongitude;
		return result;
	}
	
	public double[] getWindPos() {
		double[] result = new double[2];
		result[0] = windLatitude;
		result[1] = windLongitude;
		return result;
	}
	
	public double[] getLightPos() {
		double[] result = new double[2];
		result[0] = lightLatitude;
		result[1] = lightLongitude;
		return result;
	}
	
	
}


