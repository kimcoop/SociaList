package edu.pitt.cs1635group3.Activities.Classes;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import zebrafish.util.DBHelper;
import android.content.Context;
import android.util.Log;

public class Invite {

	public int ID, userID; 
	public int listID;
	public int pending;
	public String inviteDate;
	public String listName = "";

	// CLASS VARIABLES
	private static final String TAG = "Invite";
	private static DBHelper db;
	private static boolean PUSH_TO_CLOUD = true;

	/*
	 * CONSTRUCTORS
	 */

	public Invite() {
	}

	public Invite(int id, int lID, String lname, int p, String invited) { // manual
																	// constructor
		ID = id;
		listID = lID;
		listName = lname;
		pending = p;
		inviteDate = invited;
		Log.i(TAG, "invite constructor called not from JSON");
	}

	public Invite(Context context, JSONObject e) { // on first pull from cloud

		try {
			ID = e.getInt("id");
			listID = e.getInt("list_id");
			userID = User.getCurrUser(context);
			listName = e.getString("list_name");
			inviteDate = e.getString("invite_date");
			pending = e.getInt("pending"); // by default
			Log.i(TAG, "Pending : " +pending);

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
		pending = i;
	}

	public void setInviteDate(String string) {
		inviteDate = string;
	}

	public void setListName(String str) {
		listName = str;
	}

	public void accept(Context context) {
		pending = 0; // no longer pending -> accepted
		db = new DBHelper(context);
		db.open();
		db.updateInvite(context, this, PUSH_TO_CLOUD);
		db.close();
	}
	
	public void ignore(Context context) {
		db = new DBHelper(context);
		db.open();
		db.ignoreInvite(context, this.ID, PUSH_TO_CLOUD);
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

	public int isPending() {
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

			db.insertOrUpdateInvite(context, inv, pushToCloud);

		}

		db.close();

	}

	public static int getNumInvites(Context context, int uID) {
		ArrayList<Invite> invs = getInvites(context, uID);
		return (invs==null? 0 : invs.size());
	}

	public static void accept(Context context, ArrayList<Invite> selectedInvites) {
		db = new DBHelper(context);
		db.open();
		db.acceptInvites(context, selectedInvites, PUSH_TO_CLOUD);
		db.close();
		
	}

}
