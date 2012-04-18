package zebrafish.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.Invite;

public class JSONInvite {
	public static final String URL = Config.URL;
	public static final String TAG = "JSON Invite";
	public static DBHelper db;
	public static final boolean NO_PUSH_TO_CLOUD = false;

	public static void getInvites(Context context) { // insert all into db

		ArrayList<Invite> myInvites = new ArrayList<Invite>();

		JSONObject json = JSONfunctions.getJSONfromURL(context, "getInvites");
		Log.i(TAG, json.toString() + "");
		try {
			JSONArray myJSONInvites = json.getJSONArray("invites");

			Invite invite;
			JSONObject e1;

			for (int i = 0; i < myJSONInvites.length(); i++) {
				e1 = myJSONInvites.getJSONObject(i);
				invite = new Invite(e1);
				myInvites.add(invite);
			}

			Invite.insertOrUpdateInvites(context, myInvites, NO_PUSH_TO_CLOUD);

		} catch (JSONException e) {
			Log.e(TAG, "Error in getInvites(): " + e.toString());
		}

	} // end getInvites(Context)
	
}
