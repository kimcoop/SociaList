package edu.pitt.cs1635group3.Activities.Classes;

import java.sql.Date;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import zebrafish.util.DBHelper;

public class Invite {

	public int ID;
	public int listID;
	public boolean pending;
	public String inviteDate;
	public String listName = "";

	// CLASS VARIABLES
	private static final String TAG = "Invite";
	private static DBHelper db;
	private static boolean PUSH_TO_CLOUD = true;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Invite() {}
	
	public Invite(int id, int lID, String lname, String invited) { // manual constructor
		ID = id;
		listID = lID;
		listName = lname;
		inviteDate = invited;
		pending = true;
	}

	public Invite(JSONObject e) { // on first pull from cloud

		try {
			ID = e.getInt("id");
			listID = e.getInt("list_id");
			inviteDate = e.getString("invite_date");
			listName = e.getString("list_name");
			pending = true; // by default

		} catch (JSONException e1) {
			Log.i(TAG, "Parse problem:" + e.toString());
		}

	}
	
	/*
	 * SETTERS
	 */
	
	public void setID(int id) {
		ID = id;
	}
	
	public void setListID(int lid) {
		listID = lid;
	}
	
	public void setPending(int i) {
		pending = (i==1? true : false);
	}

	public void setInviteDate(String string) {
		inviteDate = string;
	}
	
	public void setListName(String str) {
		listName = str;
	}
	
	public void accept(Context context) {
		pending = false; // no longer pending -> accepted
		db = new DBHelper(context);
		db.open();
		db.updateInvite(this, PUSH_TO_CLOUD);
		db.close();
	}
	
	/*
	 * GETTERS
	 */
	
	public int getID() {
		return ID;
	}
	
	public int getListID() {
		return listID;
	}
	
	public boolean isPending() {
		return pending;
	}
	
	public String getInviteDate() {
		return inviteDate;
	}

	public String getListName() {
		return listName;
	}
	
	/*
	 * CLASS METHODS
	 */
	
	public static void ignore(Context context, Invite i) {
		db = new DBHelper(context);
		db.open();
		db.ignoreInvite(i.getID());
		db.close();
	}

	public static ArrayList<Invite> getInvites(Context context, int userID) {
		
		ArrayList<Invite> invites = new ArrayList<Invite>();
		db = new DBHelper(context);
		db.open();
		invites = db.getUserInvites();
		db.close();
		return invites;
		
	}

	public static void insertOrUpdateInvites(Context context,
			ArrayList<Invite> invites, boolean pushToCloud) {
		
		db = new DBHelper(context);
		db.open();
		
		for (Invite inv : invites) {
		
			db.insertOrUpdateInvite(inv, pushToCloud);
		
		}
		
		db.close();
		
	}

}
