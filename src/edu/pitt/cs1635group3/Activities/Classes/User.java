package edu.pitt.cs1635group3.Activities.Classes;

import java.util.ArrayList;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONUser;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class User {

	private int ID;
	private String email, first, last;
	private String deviceToken, deviceID;

	// CLASS VARIABLES
	private static final String TAG = "USER";
	private static DBHelper db;

	public User(int i, String f, String l, String e) {
		this.ID = i;
		this.first = f;
		this.last = l;
		this.email = e;
	}

	public User() {
	}

	/*
	 * SETTERS
	 */

	public void setDeviceToken(String s) {
		this.deviceToken = s;
	}

	public void setDeviceID(String s) {
		this.deviceID = s;
	}

	public void setFirstName(String s) {
		this.first = s;
	}

	public void setLastName(String s) {
		this.last = s;
	}

	public void setEmail(String s) {
		this.email = s;
	}

	public void setID(int i) {
		this.ID = i;
	}

	/*
	 * GETTERS
	 */

	public String getDeviceToken() {
		return deviceToken;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return first;
	}

	public String getLastName() {
		return last;
	}

	public String getName() {
		return first + " " + last;
	}

	public int getID() {
		return ID;
	}

	/*
	 * CLASS METHODS
	 */

	public static int getCurrUser(Context context) {
		SharedPreferences prefs;

		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		int id = prefs.getInt("userID", 0);
		Log.i(TAG, "Getting current user: " +id);
		return id;
	}

	public static int getUserByName(Context context, String rawAssignee) {
		// this method will be changing to ID rather than name
		int id;
		db = new DBHelper(context);
		db.open();
		id = db.getUserByName(rawAssignee);
		db.close();
		return id;
	}

	public static void insertOrUpdateUsers(Context context,
			ArrayList<User> users) {
		db = new DBHelper(context);
		db.open();

		for (User u : users) {
			db.insertOrUpdateUser(u);
		}
		db.close();
	}

	public static CharSequence[] getUsersForDialog(Context context, int id) {
		CharSequence[] users;
		db = new DBHelper(context);
		db.open();
		users = db.getUsersForDialog(id);
		db.close();
		return users;
	}
	
	public static ArrayList<User> getUsersForList(Context context, int id) {
		ArrayList<User> users;
		db = new DBHelper(context);
		db.open();
		users = db.getUsersForList(id);
		db.close();
		return users;
	}

	public static int storeUser(String name, String email, String pn) {
		int uID;
		uID = JSONUser.storeUser(name, pn, email);
		return uID;
	}
	
	public static int storeUser(String deviceId) {
		Log.i(TAG, "Storing user based on device id " +deviceId);
		int uID;
		uID = JSONUser.storeUser(deviceId);
		return uID;
	}

	public static String getUserByID(Context context, int uID) {
		db = new DBHelper(context);
		db.open();
		String name = db.getUserByID(uID).getName();
		db.close();
		return name;
	}

}