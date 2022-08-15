package user;

import java.awt.Graphics2D;
import java.util.Vector;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;


import device.SensorNode;
import map.MapLayer;

public class UserList {
	public static Vector<User> users = new Vector<User>();
	public static int currentUser = -1;
	public static int lastUser = -1;

	public UserList() {
		reset();
	}

	/**
	 * @author Yiwei --> fix by Bang Tran
	 */
	public static void reset() {
		currentUser = -1;
		lastUser = -1;
		users.removeAllElements();
	}

	public static void add(User u){
		users.add(u);
	}

	public static void hideAreas() {
		for(User u: users) {
			u.setShowArea(false);
		}
	}
	
	public static void showAreas() {
		for(User u: users) {
			u.setShowArea(true);
		}
	}
	
	public static void drawUserAreas(Graphics2D g){
		int i = -1;
		for(User u: users){
			i++;
			if(u.getShowArea())
				u.drawConcernedAreaOnMap(g);
			if(i == currentUser)
				u.drawUserIcon(g, true);
			else
				u.drawUserIcon(g, false);
		}

		if(currentUser >=0 )
			users.get(currentUser).drawBackgroundCurrentUser(g);
	}

	/**
	 * @author Yiwei Yao
	 * @param userData
	 * parse UserData and add into UserList
	 */
	public static void openFromDB(FindIterable<Document> userData) {

		//Routes.reset();
		reset();

		MongoCursor<Document> userDataIterator = userData.iterator();
		while(userDataIterator.hasNext()) {

			Document SelectedUser = userDataIterator.next();

			User user = new User(SelectedUser.getString("name"));
			//User user = getUser(SelectedUser.getString("name"));   //null exception bug
			user.setLatitude1(SelectedUser.getDouble("latitude1"));
			user.setLatitude2(SelectedUser.getDouble("latitude2"));
			user.setLongitude1(SelectedUser.getDouble("longitude1"));
			user.setLongitude2(SelectedUser.getDouble("longitude2"));
			user.setGeoLocation(SelectedUser.getDouble("locationLongitude"), SelectedUser.getDouble("locationLatitude"));
			user.selectedLocation = SelectedUser.getBoolean("selectedLocation");
			user.setSelectedArea(SelectedUser.getBoolean("selectedArea"));

			user.setTemperatureSensing(SelectedUser.getBoolean("temperatureSensing"));
			user.setHumiditySensing(SelectedUser.getBoolean("humiditySensing"));
			user.setGasSensing(SelectedUser.getBoolean("gasSensing"));
			user.setLightSensing(SelectedUser.getBoolean("lightSensing"));
			user.setWindLevelSensing(SelectedUser.getBoolean("windLevelSensing"));
			user.setWaterLevelSensing(SelectedUser.getBoolean("waterLevelSensing"));

			user.setDataEncrypted(SelectedUser.getBoolean("dataEncrypted"));
			user.setPreferredLatency(SelectedUser.getDouble("preferredLatency"));
			user.setPreferredThroughput(SelectedUser.getDouble("preferredThroughput"));
			user.setPreferredFrequency(SelectedUser.getDouble("preferredFrequency"));
//			user.startTime = (long)SelectedUser.getInteger("startTime");
//			user.endTime = (long)SelectedUser.getInteger("endTime");
			Object endTime = SelectedUser.get("endTime"); 
			Object startTime = SelectedUser.get("startTime"); // if any error happened , startTime may extend the limit of interger
			int temp = (int) endTime;
			user.endTime = (long) temp;
			temp = (int) startTime;
			user.startTime = (long) temp;


			users.add(user);
		}

		for(User u: users) {
			u.getSensorsInsideArea();
		}
	}

	public static void resetFromDB() {
		users = new Vector<User>();
	}

	/**
	 * @author Yiwei Yao
	 * @return User
	 * get user from name
	 */
	public static User getUser(String name) {
		User selectUser = null;
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).name.equals(name)) {
				selectUser = users.get(i);
			}
		}
		return selectUser;
	}
}
