package zebrafish.util;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class JSONUser {
	

	public static final String URL = Config.URL;
	public static final String TAG = "JSON USEr";
	public static DBHelper db;
	public static final boolean NO_PUSH_TO_CLOUD = false;
	
	public static ArrayList<NameValuePair> userToParams(String name, String email, String pn) {
		Log.i(TAG, "userToParams: email " +email);
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "storeUser"));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("pn", pn));
		return params;
	}


	public static int storeUser(String name, String email, String pn) {

		ArrayList<NameValuePair> params = userToParams(name, email, pn);
		params.add(new BasicNameValuePair("action", "storeUser"));

		return storeUser(params);
	}

	public static int storeUser(ArrayList<NameValuePair> params) {
		// let user register or not register by taking generic paramater params
		
		String result = JSONfunctions.postToCloud(params);
		int uID = JSONfunctions.parseForInt(result);
		Log.i(TAG, "Stored user. User id from cloud is " +uID);
		return uID;
		
	} // end storeUser
	
	public static void updateUser(int id, String name, String email, String pn) {

		ArrayList<NameValuePair> params = userToParams(name, email, pn);
		params.add(new BasicNameValuePair("action", "updateUser"));

		updateUser(params);
		
	} // end updateUser


	public static void updateUser(User u) {
		ArrayList<NameValuePair> params = userToParams(u.getName(), u.getEmail(), u.getPhoneNumber());

		String result = JSONfunctions.postToCloud(params);
		Log.i(TAG, "User updated on cloud");
	}

	public static void updateUser(ArrayList<NameValuePair> params) {

		String result = JSONfunctions.postToCloud(params);
		Log.i(TAG, "User updated on cloud");
	}

	public static void getUsers(Context context, int listID) {
		// pull in users from the server. do this only once

		ArrayList<User> users = new ArrayList<User>();
		JSONObject json = JSONfunctions.postForJSONObject("getUsers", ""
				+ listID);

		try {
			JSONArray myUsers = json.getJSONArray("users");

			User u;

			for (int i = 0; i < myUsers.length(); i++) {
				JSONArray e = myUsers.getJSONArray(i);
				u = new User(e.getInt(0), e.getString(1), e.getString(2),
						e.getString(3));
				u.setDeviceToken(e.getString(4)); // todo remove
				u.setDeviceID(e.getString(5)); // todo remove - Kim

				users.add(u);
			}

		} catch (JSONException e) {
			Log.e(TAG, "Error in getUsers(): " + e.toString());
		}

		if (users != null)
			User.insertOrUpdateUsers(context, users); // insert into db all
														// users at once
		else
			Log.i(TAG, "No users for list: ID " + listID);

	}// end getUsers(Context)
	
	public static void inviteByPhone(String pn, int listID) {
		// during invite. check if there is a user acct with this phone number.
		// if there is, return the userID.
		// if there is not, creates a temp user acct linked to the pn so the user can register and collect the invite.
		Log.i(TAG, "!!IMPORTANT listID shuld not be 0: " +listID);

		ArrayList<NameValuePair> params = PostParams.formatParams("inviteByPhone", pn);
		params.add(new BasicNameValuePair("listID", ""+listID));
		inviteUser(params);
	}
	
	public static void inviteByEmail(String email, int listID) {
		// See comment for inviteByPhone
		Log.i(TAG, "!!IMPORTANT listID shuld not be 0: " +listID);
		
		ArrayList<NameValuePair> params = PostParams.formatParams("inviteByEmail", email);
		params.add(new BasicNameValuePair("listID", ""+listID));
		inviteUser(params);
	}
	
	public static void inviteUser(ArrayList<NameValuePair> params) {
		String result = JSONfunctions.postToCloud(params);
		Log.i(TAG, "invited user! : "+result);
	}
	

}
