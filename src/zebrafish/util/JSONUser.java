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


	public static int storeUser(String name, String email, String pn) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "storeUser"));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("pn", pn));

		return storeUser(params);
	}

	public static int storeUser(ArrayList<NameValuePair> params) {
		// let user register or not register by taking generic paramater params
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG + " - register user",
					"Error in http connection " + e.toString());
		}

		result = JSONfunctions.getResult(is);

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e(TAG, "storeUser(): Error " + e.toString());
		}

		int uID = -1;

		try {
			uID = Integer.parseInt(jArray.getString("response"));

		} catch (JSONException e) {
			Log.e(TAG,
					"storeUser(): Error with getting user ID " + e.toString());
		}

		return uID;
	} // end storeUser
	


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
	

}
