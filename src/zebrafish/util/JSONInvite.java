package zebrafish.util;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import service.InviteTask;

import android.content.Context;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Invite;

public class JSONInvite {
	public static final String URL = Config.URL;
	public static final String TAG = "JSON Invite";
	public static DBHelper db;
	public static final boolean NO_PUSH_TO_CLOUD = false;
	
	public static ArrayList<NameValuePair> inviteToParams(Invite inv) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("objID", ""+inv.getID()));
		params.add(new BasicNameValuePair("pending", ""+inv.isPending()));
		return params;
	}

	public static ArrayList<Invite> getInvites(Context context) { // insert all into db

		ArrayList<Invite> myInvites = new ArrayList<Invite>();

		JSONObject json = JSONfunctions.getJSONfromURL(context, "getInvites");
		Log.i(TAG, json.toString() + "");
		try {
			JSONArray myJSONInvites = json.getJSONArray("invites");

			Invite invite;
			JSONObject e1;

			for (int i = 0; i < myJSONInvites.length(); i++) {
				e1 = myJSONInvites.getJSONObject(i);
				invite = new Invite(context, e1);
				myInvites.add(invite);
			}

			Invite.insertOrUpdateInvites(context, myInvites, NO_PUSH_TO_CLOUD);

		} catch (JSONException e) {
			Log.e(TAG, "Error in getInvites(): " + e.toString());
		}
		
		return myInvites;

	} // end getInvites(Context)

	public static void updateInvite(Context context, Invite currInv) {
			Log.i(TAG, "updateInvite");
		
			ArrayList<NameValuePair> params = inviteToParams(currInv);
			params.add(new BasicNameValuePair("action", "updateInvite"));
			
			String s = JSONfunctions.postToCloud(params);
			Log.d(TAG, s);
		
	}	// end updateInvite

	public static void declineInvite(Context context, int currInvId) {
		Log.i(TAG, "declineInvite");
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("objID", ""+currInvId));
		params.add(new BasicNameValuePair("action", "declineInvite"));
		
		String s = JSONfunctions.postToCloud(params);
		Log.d(TAG, s);
		
	} // end declineInvite

	public static void acceptInvites(Context context,
			ArrayList<Invite> currInvites) {
		
		for (Invite inv : currInvites) {
			updateInvite(context, inv);
		}
		
	}

}
