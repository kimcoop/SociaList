package edu.pitt.cs1635group3.Activities.Classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONUser;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class User {

	private int ID;
	private String email, first, last;
	private String pn; // phone
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

	public User(JSONArray e) {
		try {
			ID = e.getInt(0);
			first = e.getString(1);
			last = e.getString(2);
			email = e.getString(3);
		} catch (JSONException e1) {
			Log.i(TAG, "User parse error: " + e1.toString());
		}

	}

	public User(JSONObject e1) {
		try {
			ID = Integer.parseInt(e1.getString("id"));
			first = e1.getString("first");
			last = e1.getString("last");
			email = e1.getString("email");
		} catch (JSONException e2) {
			Log.i(TAG, "User parse error: " + e2.toString());
		}
	}

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

	public String getPhoneNumber() {
		return pn;
	}

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
		if (first == null)
			return email;
		else
			return first;
	}

	public String getLastName() {
		return last;
	}

	public String getName() {
		if (first == null)
			return email;
		else
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
		return id;
	}

	public static String getCurrUsername(Context context) {
		SharedPreferences prefs;

		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		String id = prefs.getString("name", "");
		return id;
	}

	public static String getCurrEmail(Context context) {
		SharedPreferences prefs;

		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		String id = prefs.getString("email", "");
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

	public static void insertOrUpdateUsers(int listID, Context context,
			ArrayList<User> users, boolean pushToCloud) {
		db = new DBHelper(context);
		db.open();

		for (User u : users) {
			db.insertOrUpdateUser(listID, u, pushToCloud);
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

	public static int storeUser(Context context, String name, String email,
			String pn) {
		Log.i(TAG, "name " + name + ". email " + email);
		int uID;
		uID = JSONUser.storeUser(context, name, email, pn);
		return uID;
	}

	public static String getUserByID(Context context, int uID) {
		db = new DBHelper(context);
		db.open();
		String name = db.getUserByID(uID).getName();
		db.close();
		return name;
	}

	public static String getPhoneNumber(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String phoneNumber = telephonyManager.getLine1Number();
		return phoneNumber;
	}

}