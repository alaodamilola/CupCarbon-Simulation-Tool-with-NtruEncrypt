/**
 * @author Bang Tran UMB
 *
 *
 */

package user;

import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.bson.Document;

import device.DeviceList;
import device.SensorNode;
import map.MapLayer;
import device.BaseStation;
import device.CloudServer;
import device.Device;
import utilities.MapCalc;
import utilities.UColor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;


public class User {
	public String name = null;
	public boolean selectedArea = false;
	public boolean selectedLocation = false;
	public Color areaBoderColor=new Color(255, 0, 0);

	private double latitude1=0.0;
	private double latitude2=0.0;
	private double longitude1=0.0;
	private double longitude2=0.0;

	private double locationLatitude = 0.0;
	private double locationLongitude = 0.0;


	public boolean temperatureSensing= false;
	public boolean humiditySensing = false;
	public boolean gasSensing = false;
	public boolean lightSensing = false;
	public boolean windLevelSensing = false;
	public boolean waterLevelSensing = false;
	public boolean dataEncrypted = false;

	public double preferredLatency = 10.0; //>= 10 ms
	public double preferredThroughput = 0.0; //>= 0kbit
	public double preferredFrequency = 0; //1 minutes = 3600*1000 ms  - get data every seconds
	public long startTime = 0;					//start time to be activated (on simulation timeline)
	public long endTime = 64000;				//end time to be deactivated (on simulation timeline)
	public double lastReceivedTime = 0; // last received data time

	public double getLastReceivedTime() {
		return lastReceivedTime;
	}
	public void setLastReceivedTime(double lastReceivedTime) {
		this.lastReceivedTime = lastReceivedTime;
	}

	//Chenjun
	public BaseStation userStation = null;
	public Vector<String> userEvents = new Vector<String>();
	private boolean showArea = true;

	public User(String uname){
		Random r = new Random();
		this.areaBoderColor = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		this.name = uname;
	}
	public String getName(){ return name; }

	public void reset(){
		preferredFrequency = 0L;
		preferredLatency = 10.0;
		preferredThroughput = 0.0;
		latitude1 = longitude1 = latitude2 = longitude2 = 0.0;
		locationLatitude = locationLatitude = 0.0;
		selectedArea = false;
		selectedLocation = false;
		startTime = 0;
		endTime = 64000;

		areaBoderColor=new Color(255, 0, 0);

		//add by Chenjun
		if(userStation != null) {
			userStation.removeUser(this);
			userStation = null;
		}
		userEvents.clear();
		temperatureSensing= false;
		humiditySensing = false;
		gasSensing = false;
		lightSensing = false;
		windLevelSensing = false;
		waterLevelSensing = false;
		showArea = true;
	};

	/**
	 * @param lat1: Latitude of first coordinate
	 * @param long1: Longitude of first coordinate
	 * @param lat2: Latitude of second coordinate
	 * @param long2: Longitude of second coordinate
	 *
	 * Set the coordinates of concerned area
	 */
	public void setConcernedArea(double lat1, double long1, double lat2, double long2){
		latitude1 = Math.min(lat1, lat2);
		latitude2 = Math.max(lat1, lat2);
		longitude1 = Math.min(long1, long2);
		longitude2 = Math.max(long1, long2);

		selectedArea = true;
	}

	public void removeConcernedArea(){
		/*
s		int [] coord1 = MapCalc.geoToPixelMapA(latitude1, longitude1);
		int [] coord2 = MapCalc.geoToPixelMapA(latitude2, longitude2);
		int x1 = Math.min(coord1[0], coord2[0]);
		int y1 = Math.min(coord1[1], coord2[1]);
		int x2 = Math.max(coord1[0], coord2[0]);
		int y2 = Math.max(coord1[1], coord2[1]);
		//unmark all nodes inside the area

		if( !DeviceList.sensors.isEmpty() )
			for (SensorNode sensor : DeviceList.sensors) {
				int [] coord = MapCalc.geoToPixelMapA(sensor.getLatitude(), sensor.getLongitude());
				if(coord[1] > y1 && coord[0] > x1 &&   coord[1] < y2 && coord[0] < x2) {
					sensor.unmark();
				}
			}
		*/
		latitude1 = longitude1 = latitude2 = longitude2 = 0.0;
		selectedArea = false;
	}

	/**
	 * @return Return an array of doubles {latitude1, longitude1, latitude2, longitude2}
	 * corresponding to the coordinates of concerned area.
	 */
	public double[] getConcernedArea(){
			double[] ret = {latitude1, longitude1, latitude2, longitude2};
			return ret;
	}

	/**
	 * Draw a rectangle covering user's concerned area and mark all sensor inside this rectangle
	 *
	 */
	public void drawConcernedAreaOnMap(Graphics2D g){
		int [] coord1 = MapCalc.geoToPixelMapA(latitude1, longitude1);
		int [] coord2 = MapCalc.geoToPixelMapA(latitude2, longitude2);

		int x1 = Math.min(coord1[0], coord2[0]);
		int y1 = Math.min(coord1[1], coord2[1]);
		int x2 = Math.max(coord1[0], coord2[0]);
		int y2 = Math.max(coord1[1], coord2[1]);

		g.setColor(areaBoderColor);
		g.setStroke(new BasicStroke(1.5f));
		g.drawRect(x1, y1, x2-x1, y2-y1);
//		//mark all nodes inside the area
//		if( !DeviceList.sensors.isEmpty() )
//			for (SensorNode sensor : DeviceList.sensors) {
//				int [] coord = MapCalc.geoToPixelMapA(sensor.getLatitude(), sensor.getLongitude());
//				if(coord[1] > y1 && coord[0] > x1 &&   coord[1] < y2 && coord[0] < x2) {
//					sensor.mark();
//				}
//		}
	}


	/**
	 *
	 * this function will show user icon on the map
	 */
	public void drawUserIcon(Graphics2D g, boolean currentUser){
		if(this.selectedLocation == false)
			 return;

		int[] coord = MapCalc.geoToPixelMapA(this.locationLatitude, this.locationLongitude);
		int x = coord[0];
		int y = coord[1];

		if(currentUser){
			Image image = new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/images/user_icon.png")).getImage();
			g.drawImage(image, x-15, y-15, null);
		} else {
			Image image = new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/images/location-map-pin.png")).getImage();

			g.drawImage(image, x-15, y-15, null);
		}


	}


	/**
	 *
	 * This function will draw the background of rectangle for Current user
	 */
	public void drawBackgroundCurrentUser(Graphics2D g){
		int [] coord1 = MapCalc.geoToPixelMapA(latitude1, longitude1);
		int [] coord2 = MapCalc.geoToPixelMapA(latitude2, longitude2);

		int x1 = Math.min(coord1[0], coord2[0]);
		int y1 = Math.min(coord1[1], coord2[1]);
		int x2 = Math.max(coord1[0], coord2[0]);
		int y2 = Math.max(coord1[1], coord2[1]);

		g.setColor(UColor.RED_TTRANSPARENT);
		g.fillRect(x1, y1, x2-x1, y2-y1);
	}

	/**
	 * Return list of sensors located in user's concerned area
	 */
	public Vector<SensorNode> getSensorsInsideArea(){
		Vector<SensorNode> insidedNodes = new Vector<SensorNode>();
		int [] coord1 = MapCalc.geoToPixelMapA(latitude1, longitude1);
		int [] coord2 = MapCalc.geoToPixelMapA(latitude2, longitude2);

		int x1 = Math.min(coord1[0], coord2[0]);
		int y1 = Math.min(coord1[1], coord2[1]);
		int x2 = Math.max(coord1[0], coord2[0]);
		int y2 = Math.max(coord1[1], coord2[1]);

		if( !DeviceList.sensors.isEmpty() )
			for (SensorNode sensor : DeviceList.sensors) {
				int [] coord = MapCalc.geoToPixelMapA(sensor.getLatitude(), sensor.getLongitude());
				if(coord[1] > y1 && coord[0] > x1 &&   coord[1] < y2 && coord[0] < x2) {
					if(insidedNodes == null)
						insidedNodes = new Vector<SensorNode>();
					insidedNodes.add(sensor);
				}
			}

		return insidedNodes;
	}

	/**
	 *
	 * @return The nearest BaseStaion
	 */

	public BaseStation getNearestBaseStation(){
		if(this.selectedLocation == false) return null;
		if(DeviceList.sensors == null || DeviceList.sensors.size() == 0 ) return null;

		double distance = 0;
		Device res = null;

		for(Device d : DeviceList.sensors){
			if(d.getType() == Device.BASE_STATION){
				if (res == null){
					res = d;
					distance = MapLayer.distance(locationLongitude, locationLatitude, d.getLongitude(),d.getLatitude());
					continue;
				}

				if(distance > MapLayer.distance(locationLongitude, locationLatitude, d.getLongitude(),d.getLatitude()) ) {
					res = d;
					distance = MapLayer.distance(locationLongitude, locationLatitude, d.getLongitude(),d.getLatitude());
				}
			}
		}

		return (BaseStation) res;
	}

	/**
	 * @author Yiwei Yao
	 * @return
	 * return Document includes user properties.
	 */
	public Document saveToDB() {
		Document document = new Document();
		document.append("prefix", "user")
			.append("selectedArea", selectedArea)
			.append("selectedLocation", selectedLocation)
			.append("name", name)
			.append("latitude1", latitude1)
			.append("latitude2", latitude2)
			.append("longitude1", longitude1)
			.append("longitude2", longitude2)
			.append("locationLongitude", locationLongitude)
			.append("locationLatitude", locationLatitude)
			.append("temperatureSensing", temperatureSensing)
			.append("humiditySensing", humiditySensing)
			.append("gasSensing", gasSensing)
			.append("lightSensing", lightSensing)
			.append("windLevelSensing", windLevelSensing)
			.append("waterLevelSensing", waterLevelSensing)
			.append("dataEncrypted", dataEncrypted)
			.append("preferredLatency", preferredLatency)
			.append("preferredThroughput", preferredThroughput)
			.append("preferredFrequency", preferredFrequency)
			.append("startTime", startTime)
			.append("endTime", endTime)	;
		return document;
	}

	/**
	 * setter and getter helpers
	 *
	 */
	public boolean isSelectedArea() {
		return selectedArea;
	}
	public void setSelectedArea(boolean selectedArea) {
		this.selectedArea = selectedArea;
	}
	public double getLatitude1() {
		return latitude1;
	}
	public void setLatitude1(double latitude1) {
		this.latitude1 = latitude1;
	}
	public double getLatitude2() {
		return latitude2;
	}
	public void setLatitude2(double latitude2) {
		this.latitude2 = latitude2;
	}
	public double getLongitude1() {
		return longitude1;
	}
	public void setLongitude1(double longitude1) {
		this.longitude1 = longitude1;
	}
	public double getLongitude2() {
		return longitude2;
	}
	public void setLongitude2(double longitude2) {
		this.longitude2 = longitude2;
	}
	public boolean isTemperatureSensing() {
		return temperatureSensing;
	}
	public void setTemperatureSensing(boolean temperatureSensing) {
		this.temperatureSensing = temperatureSensing;
		if(temperatureSensing) {
			if(userEvents.contains("Temperature")) {
				return;
			} else {
				userEvents.add("Temperature");
			}
		}
	}
	public boolean isHumiditySensing() {
		return humiditySensing;
	}
	public void setHumiditySensing(boolean humiditySensing) {
		this.humiditySensing = humiditySensing;
		if(humiditySensing) {
			if(userEvents.contains("Humidity")) {
				return;
			} else {
				userEvents.add("Humidity");
			}
		}
	}
	public boolean isGasSensing() {
		return gasSensing;
	}
	public void setGasSensing(boolean gasSensing) {
		this.gasSensing = gasSensing;
		if(gasSensing) {
			if(userEvents.contains("Gas")) {
				return;
			} else {
				userEvents.add("Gas");
			}
		}
	}
	public boolean isLightSensing() {
		return lightSensing;
	}
	public void setLightSensing(boolean lightSensing) {
		this.lightSensing = lightSensing;
		if(lightSensing) {
			if(userEvents.contains("Light")) {
				return;
			} else {
				userEvents.add("Light");
			}
		}
	}
	public boolean isWindLevelSensing() {
		return windLevelSensing;
	}
	public void setWindLevelSensing(boolean windLevelSensing) {
		this.windLevelSensing = windLevelSensing;
		if(windLevelSensing) {
			if(userEvents.contains("Wind")) {
				return;
			} else {
				userEvents.add("Wind");
			}
		}
	}
	public boolean isWaterLevelSensing() {
		return waterLevelSensing;
	}
	public void setWaterLevelSensing(boolean waterLevelSensing) {
		this.waterLevelSensing = waterLevelSensing;
		if(waterLevelSensing) {
			if(userEvents.contains("Water")) {
				return;
			} else {
				userEvents.add("Water");
			}
		}
	}
	public boolean isDataEncrypted() {
		return dataEncrypted;
	}
	public void setDataEncrypted(boolean dataEncrypted) {
		this.dataEncrypted = dataEncrypted;
	}
	public double getPreferredLatency() {
		return preferredLatency;
	}
	public void setPreferredLatency(double preferredLatency) {
		this.preferredLatency = preferredLatency;
	}
	public double getPreferredThroughput() {
		return preferredThroughput;
	}
	public void setPreferredThroughput(double preferredThroughput) {
		this.preferredThroughput = preferredThroughput;
	}
	public double getPreferredFrequency() {
		return preferredFrequency;
	}
	public void setPreferredFrequency(double preferredFrequency) {
		this.preferredFrequency = preferredFrequency;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double[] getGeoLocation(){
		double[] location = {locationLongitude, locationLatitude};
		return location;
	}
	public void setGeoLocation(double lon, double lat ){
		this.selectedLocation = true;
		locationLatitude = lat;
		locationLongitude = lon;
	}
	public void unsetGeoLocation(){
		this.selectedLocation = false;
		locationLatitude = 0.0;
		locationLongitude = 0.0;
	}

	public double getLatitude(){
		return locationLatitude;
	}

	//add by Chenjun
	public void setBaseStation(BaseStation userStation) {
		this.userStation = userStation;
		if(userStation != null) {
			this.userStation.addUser(this);
		} else {
			System.out.println("No base station exsist");
		}
	}
	public BaseStation getBaseStation() {
		return userStation;
	}
	public void addEvent(String s) {
		if(s.equals("Temperature"))
			temperatureSensing = true;
		if(s.equals("Water"))
			waterLevelSensing = true;
		if(s.equals("Wind"))
			windLevelSensing = true;
		if(s.equals("Gas"))
			gasSensing = true;
		if(s.equals("Light"))
			lightSensing = true;
		if(s.equals("Humidity"))
			humiditySensing = true;
		
		if(userEvents.contains(s)) {
			return;
		} else {
			userEvents.add(s);
		} 
	}
	public void removeEvent(String s) {
		if(s.equals("Temperature"))
			temperatureSensing = false;
		if(s.equals("Water"))
			waterLevelSensing = false;
		if(s.equals("Wind"))
			windLevelSensing = false;
		if(s.equals("Gas"))
			gasSensing = false;
		if(s.equals("Light"))
			lightSensing = false;
		if(s.equals("Humidity"))
			humiditySensing = false;
		if(userEvents.contains(s)) {
			userEvents.remove(s);
		}
	}
	public Vector<String> getEvents() {
		return userEvents;
	}
	public void setTimeStart(long t) {
		startTime = t;
	}
	public long getTimeStart() {
		return startTime;
	}
	public void setTimeEnd(long t) {
		endTime = t;
	}
	public long getTimeEnd() {
		return endTime;
	}
	public void setTimeDelay(long t) {
		preferredFrequency = t;
	}
	public double getTimeDelay() {
		return preferredFrequency;
	}
	
	public boolean getShowArea() {
		return showArea;
	}
	
	public void setShowArea(boolean t) {
		showArea = t;
	}
}
