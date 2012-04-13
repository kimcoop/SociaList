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
		post("deleteList", "" + id);
	}

	public static void deleteItem(int id) {
		post("deleteItem", "" + id);
	}

	public static void post(String action, String objID) {

		// initialize
		InputStream is = null;
		String result = "", resp = "";

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = PostParams.formatParams(action,
					objID);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG + " - post", "Error in http connection " + e.toString());
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
			ArrayList<NameValuePair> params = PostParams.formatParams(action,
					objID);
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

		JSONObject jObj = null;
		String response = "";

		try { // try parse the string to a JSON object
			jObj = new JSONObject(res);
		} catch (JSONException e) {
			Log.e(TAG + "- getResponse", "Error parsing data " + e.toString());
		}

		try {
			response = jObj.getString(which);

		} catch (JSONException e) {
			Log.e(TAG + "- getResponse",
					"Error with posting item " + e.toString());
		}

		return response;
	}

	/*
	 * LISTS
	 */

	public static void createList(CustomList list, int userID) {
		// This is called after we've already snatched the PK from the web
		// server.
		// So the list (even though we're currently creating it) already is
		// initialized on the server.

		updateList(list);
	} // end CreateList

	public static void updateList(CustomList list) {

		// initialize
		InputStream is = null;
		String result = "", resp = "";

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = PostParams.formatListParams(
					"updateList", list);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG, "POSTLIST: Error in http connection " + e.toString());
		}
		result = getResult(is);
		resp = getResponse(result, "response");
		Log.i(TAG, resp);

	} // end UpdateList

	/*
	 * ITEMS
	 */

	public static void createItem(Item i) {
		postItem("updateItem", i);
	}

	public static void updateItem(Item i) {
		postItem("updateItem", i);
	}

	public static void postItem(String action, Item i) { // pass the item back
															// to the server

		// Log.i(TAG + "- POST ITEM", "Posting: " + i.getName());

		// initialize
		InputStream is = null;
		String result = "", resp = "";

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = PostParams
					.formatParams(action, i);
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG + "- POST ITEM",
					"Error in http connection " + e.toString());
		}

		result = getResult(is);
		resp = getResponse(result, "response");
		Log.i(TAG, resp);

	} // end postItem

	public static JSONObject getJSONfromURL() {
		return getJSONfromURL(null);
	}

	public static JSONObject getJSONfromURL(String a) {
		return postForJSONObject(a, null);
	}

	public static JSONObject getJSONfromURL(String action, int objID) {
		return postForJSONObject(action, "" + objID);
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

	public static void getLists(Context context) {

		ArrayList<CustomList> myCustomLists = new ArrayList<CustomList>();
		JSONObject json = JSONfunctions.getJSONfromURL("getLists");

		try {
			JSONArray myLists = json.getJSONArray("lists");

			CustomList list;
			int listID;
			JSONObject e1;

			for (int i = 0; i < myLists.length(); i++) {
				e1 = myLists.getJSONObject(i);
				list = new CustomList(e1);

				myCustomLists.add(list);

				listID = list.getID();
				getListItems(context, listID); // pull the list's items from the
												// server
				getUsers(context, listID); // pull the list's users too

			}

			CustomList.insertOrUpdateLists(context, myCustomLists);

		} catch (JSONException e) {
			Log.e(TAG, "Error in getLists(): " + e.toString());
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

		// return listItems;
		Item.insertOrUdpateItems(context, listItems);

	}

	public static ArrayList<CustomList> getRefreshLists(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	public static int storeUser(String fname, String lname, String email, String pw) {
		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(); 
			params.add(new BasicNameValuePair("action", "storeUser"));
			params.add(new BasicNameValuePair("fname", fname));
			params.add(new BasicNameValuePair("lname", lname));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("pw", pw));
			httppost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(TAG + " - register user",
					"Error in http connection " + e.toString());
		}

		result = getResult(is);

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e(TAG, "storeUser(): Error " + e.toString());
		}

		int uID = -1;

		try {
			uID = jArray.getInt("response");

		} catch (JSONException e) {
			Log.e(TAG, "storeUser(): Error with getting user ID " + e.toString());
		}

		return uID;
	}

}
