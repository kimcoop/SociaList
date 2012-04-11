package zebrafish.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.User;

import android.content.Context;
import android.util.Log;

public class JSONfunctions {

	public static final String URL = "http://www.zebrafishtec.com/server.php";
	public static final String TAG = "JSONFUNCTIONS";
	public static DBHelper db;

	public static int getItemPK() {
		return getPK("getItemPK");
	}

	public static int getListPK() {
		return getPK("getListPK");
	}

	public static int getPK(String action) {

		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("action", action));
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG + " - getPK", "Error in http connection " + e.toString());
		}

		result = getResult(is);

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("GET PK", "Error parsing data " + e.toString());
		}

		int newPK = -1;

		try {
			newPK = jArray.getInt("id");
			// Log.d("GET PK", "id" + newPK);

		} catch (JSONException e) {
			Log.e("GET PK", "Error with getting PK " + e.toString());
		}

		return newPK;
	}

	public static void deleteList(int id) {
		post("deleteList", id);
	}

	public static void deleteItem(int id) {
		post("deleteItem", id);
	}
	
	public static void post(String action, int id) {
		post(action, ""+id); // convert int to String to pass
	}
	
	public static void post(String action, String objID) {

		// initialize
		InputStream is = null;
		String result = "", resp = "";
		
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = PostParams.formatParams(action, objID);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG +" - post", "Error in http connection " + e.toString());
		}
		

		result = getResult(is);
		resp = getResponse(result, "response");
		Log.i(TAG, resp);
		
		
	}
	
	public static JSONObject postForJSONObject(String action, String objID) {
		InputStream is = null;
		JSONObject jObj = null;
		String result = "";
		
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = PostParams.formatParams(action, objID);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG, "Error in http connection " + e.toString());
		}

		result = getResult(is);

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(result);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing data " + e.toString());
		}
		
		return jObj;
	}
	
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
	
	public static String getResponse(String res, String which) {

		JSONObject jArray = null;
		String response = "";
		
		try { // try parse the string to a JSON object
			jArray = new JSONObject(res);
		} catch (JSONException e) {
			Log.e(TAG + "- getResponse", "Error parsing data " + e.toString());
		}

		try {
			response = jArray.getString(which);
			Log.d(TAG + "- getResponse", "ALL CLEAR: " + response);

		} catch (JSONException e) {
			Log.e(TAG + "- getResponse", "Error with posting item " + e.toString());
		}
		
		return response;
	}
	
	/*
	 * LISTS
	 */

	public static void createList(CustomList list) {
		postList("createList", list);
	}

	public static void updateList(CustomList list) {
		postList("updateList", list);
	}

	public static void postList(String action, CustomList c) { // pass the item
																// back
																// to the server

		// initialize
		InputStream is = null;
		String result = "", resp = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = PostParams.formatListParams(action, c);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("PHP LIST", "Error in http connection " + e.toString());
		}
		result = getResult(is);
		resp = getResponse(result, "response");
		Log.i(TAG, resp);

	} // end List methods

	/*
	 * ITEMS
	 */

	public static void createItem(Item i) {
		postItem("createItem", i);
	}

	public static void updateItem(Item i) {
		postItem("updateItem", i);
	}

	public static void postItem(String action, Item i) { // pass the item back
															// to the server

		Log.i(TAG + "- POST ITEM", "Posting: " + i.getName());

		// initialize
		InputStream is = null;
		String result = "", resp ="";

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params = PostParams.formatParams(action, i);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG + "- POST ITEM", "Error in http connection " + e.toString());
		}

		result = getResult(is);
		resp = getResponse(result, "response");
		Log.i(TAG, resp);

	} // end postItem

	public static JSONObject getJSONfromURL() {
		return getJSONfromURL(null);
	}

	public static JSONObject getJSONfromURL(String a) {
		return getJSONfromURL(a, null);
	}


	public static JSONObject getJSONfromURL(String action, int listID) {
		return getJSONfromURL(action, ""+listID);
	}
	
	public static JSONObject getJSONfromURL(String action, String listID) {
		return postForJSONObject(action, listID);
	} // end getJSON
	
	public static ArrayList<User> getUsers(Context context) {
		// pull in users from the server. do this only once

		ArrayList<User> sharedUsers = new ArrayList<User>();
		JSONObject json = JSONfunctions.getJSONfromURL("getUsers");

		try {
			JSONArray myUsers = json.getJSONArray("users");

			User u;

			db = new DBHelper(context);
			db.open();

			for (int i = 0; i < myUsers.length(); i++) {
				JSONArray e = myUsers.getJSONArray(i);
				u = new User(e.getInt(0), e.getString(1), e.getString(2),
						e.getString(3));
				u.setDeviceToken(e.getString(4));
				u.setDeviceID(e.getString(5));
				db.insertOrUpdateUser(u);
				sharedUsers.add(u);
			}

			db.close();
		} catch (JSONException e) {
			Log.e(TAG, "Error in getUsers(): " + e.toString());
		}

		return sharedUsers;

	}// end getUsers(Context);
	


	public static void getLists(Context context) {

		ArrayList<CustomList> myCustomLists = new ArrayList<CustomList>();
		ArrayList<Item> items = null;

		JSONObject json = JSONfunctions.getJSONfromURL("getLists");

		try {
			JSONArray myLists = json.getJSONArray("lists");

			CustomList list;
			JSONObject e1;

			db = new DBHelper(context);
			db.open();

			for (int i = 0; i < myLists.length(); i++) {
				e1 = myLists.getJSONObject(i);
				list = new CustomList(e1);

				myCustomLists.add(list);
				db.insertOrUpdateList(list);

				getListItems(context, list.getID()); // pull the list's items from the server

			}
			db.close();

		} catch (JSONException e) {
			Log.e(TAG, "Error in getLists(): " +e.toString());
		}

	} // end getLists(Context)
	

	public static void getListItems(Context context, int listID) {

		JSONObject json = getJSONfromURL("getItemsForList", listID);
		ArrayList<Item> listItems = new ArrayList<Item>();

		try {
			JSONArray items = json.getJSONArray("items");

			if (items.length() >= 1) {

				JSONObject e1, e2;
				Item item1, item2;

				for (int i = 0; i < items.length(); i++) {

					if (i == 0) {
						e1 = items.getJSONObject(i);
						item1 = new Item(e1);
						item1.setParent(context, listID);

						e2 = items.getJSONObject(i + 1);
						item2 = new Item(e2);
						item2.setParent(context, listID);

						item2.setPrev(context, item1.getID());
						item1.setNext(context, item2.getID());
						listItems.add(item1);
						listItems.add(item2);

						i += 1;

					} else {
						e1 = items.getJSONObject(i);
						item1 = new Item(e1);
						item1.setParent(context, listID);

						Item prev = listItems.get(i - 1);

						prev.setNext(context, item1.getID());
						item1.setPrev(context, prev.getID());
						listItems.add(item1);

					}
				}

				listItems.get(0).setPrev(context, 
						listItems.get(listItems.size() - 1).getID()); // "Loop around":

				listItems.get(listItems.size() - 1).setNext(context, 
						listItems.get(0).getID()); // and
			}

		} catch (JSONException e) {
			Log.e(TAG, "PullItems(): " + e.toString());
		}
	}

}
