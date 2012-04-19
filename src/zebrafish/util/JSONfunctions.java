package zebrafish.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class JSONfunctions {

	public static final String URL = Config.URL;
	public static final String TAG = "JSON FUNCTIONS";
	public static DBHelper db;
	public static final boolean NO_PUSH_TO_CLOUD = false;

	public static int getPK(String action) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", action));
		String result = postToCloud(params);

		return parseForInt(result);
	}

	/*
	 * PULL OUT SPECIFIC DATA FROM JSON
	 */

	public static int parseForInt(String result) {

		JSONObject jObj = null;
		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("GET PK", "Error parsing data " + e.toString());
		}

		int id = -1;

		try {
			id = jObj.getInt("id");
			// Log.d("GET PK", "id" + newPK);

		} catch (JSONException e) {
			Log.e(TAG, "Error in PostForInt: " + e.toString());
		}

		return id;

	}

	public static String parseForString(String result) {

		JSONObject jObj = null;
		String response = "";

		try { // try parse the string to a JSON object
			jObj = new JSONObject(result);
		} catch (JSONException e) {
			Log.e(TAG + "- getResponse", "Error parsing data " + e.toString());
		}

		try {
			response = jObj.getString("response");

		} catch (JSONException e) {
			Log.e(TAG + "- getResponse",
					"Error with posting item " + e.toString());
		}

		return response;
	}

	/*
	 * HANDLE POSTING OBJECTS
	 */

	public static String postToCloud(ArrayList<NameValuePair> params) {

		// initialize
		InputStream is = null;
		String result = "", resp = "";

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG + " - post", "Error in http connection " + e.toString());
		}

		result = getResult(is);
		return result;

	}

	public static JSONObject parseForJSONObject(String result) {

		JSONObject jObj = null;

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(result);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing data " + e.toString());
		}

		return jObj;
	}

	public static JSONObject postForJSONObject(String action, String objID) {

		ArrayList<NameValuePair> params = PostParams
				.formatParams(action, objID);
		String result = postToCloud(params);
		JSONObject jObj = parseForJSONObject(result);

		return jObj;
	}

	/*
	 * GET RESULTS FROM JSON
	 */

	public static String getResult(InputStream is) {
		// convert response to string
		String res = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			res = sb.toString();
		} catch (Exception e) {
			Log.e(TAG, "Error converting result " + e.toString());
		}
		return res;
	}

	public static JSONObject getJSONfromURL(Context context, String a) {
		int uID = User.getCurrUser(context);
		Log.i(TAG, "Getting JSON based on user ID: " + uID);
		return postForJSONObject(a, "" + uID);
	}

	public static JSONObject getJSONfromURL(String action, int objID) {
		return postForJSONObject(action, "" + objID);
	}

	public static JSONObject getJSONfromURL(String action, String objID) {
		return postForJSONObject(action, objID);
	}

	public static ArrayList<CustomList> getRefreshLists(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
}
