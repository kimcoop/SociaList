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

	// CLASS VARIABLES
	private static final String TAG = "Invite";
	private static DBHelper db;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Invite(int id, int lID, String invited) { // manual constructor
		ID = id;
		listID = lID;
		inviteDate = invited;
		pending = true;
	}

	public Invite(JSONObject e) { // on first pull from cloud

		try {
			ID = e.getInt("id");
			listID = e.getInt("list_id");
			inviteDate = e.getString("invite_date");

			int isPending = e.getInt("pending");
			pending = (isPending == 1 ? true : false);

		} catch (JSONException e1) {
			Log.i(TAG, "Parse problem:" + e.toString());
		}

	}
	
	/*
	 * CLASS METHODS
	 */

	public static ArrayList<Invite> getInvites(Context context, int userID) {
		
		ArrayList<Invite> invites = new ArrayList<Invite>();
		db = new DBHelper(context);
		db.open();
		invites = db.getUserInvites(userID);
		db.close();
		return invites;

		
		
	}

}
