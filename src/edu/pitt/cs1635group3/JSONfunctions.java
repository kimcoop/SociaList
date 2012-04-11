package edu.pitt.cs1635group3;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONfunctions {

	public static final String URL = "http://www.zebrafishtec.com/server.php";

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
			Log.e("MYSQL", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

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
			Log.e("GET PK", "Error with posting item " + e.toString());
		}

		return newPK;
	}

	public static void deleteList(int id) {
		deleteObject("deleteList", id);
	}

	public static void deleteItem(int id) {
		deleteObject("deleteItem", id);
	}

	public static void deleteObject(String action, int id) {// can use this
															// function for
															// lists and items,
															// so just pass
															// action and ID
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
			params.add(new BasicNameValuePair("id", "" + id));

			httppost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("MYSQL DELETION", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("POST ITEM", "Error parsing data " + e.toString());
		}

		try {
			String response = jArray.getString("response");
			Log.d("POST ITEM", "response" + response);

		} catch (JSONException e) {
			Log.e("POST ITEM", "Error with posting item " + e.toString());
		}

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

	public static void postList(String action, CustomList i) { // pass the item
																// back
																// to the server

		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

			if (i != null) {

				params.add(new BasicNameValuePair("action", action));
				params.add(new BasicNameValuePair("id", "" + i.getID()));
				params.add(new BasicNameValuePair("custom_id", ""
						+ i.getCustomID()));
				params.add(new BasicNameValuePair("name", i.getName()));
				params.add(new BasicNameValuePair("adder_id", ""
						+ i.getCreator()));

				httppost.setEntity(new UrlEncodedFormEntity(params));

			}

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("PHP LIST", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("PHP LIST", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("PHP LIST", "Error parsing data " + e.toString());
		}

		try {
			String response = jArray.getString("response");
			Log.d("PHP LIST", "response: " + response);

		} catch (JSONException e) {
			Log.e("PHP LIST", "Error with posting item " + e.toString());
		}

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

		Log.i("JSON ITEM", "Attempting to post item " + i.getName());

		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

			if (i != null) {

				if (action == "updateItem") { // if item is to be updated, we
												// need to also pass its ID and
												// add_date
					params.add(new BasicNameValuePair("action", "updateItem"));
					params.add(new BasicNameValuePair("add_date", i
							.getCreationDate()));
					params.add(new BasicNameValuePair("id", "" + i.getID()));
				} else {
					params.add(new BasicNameValuePair("action", "createItem"));
				}
				// the following attributes will need to be updated regardless
				// of action
				params.add(new BasicNameValuePair("parent_id", ""
						+ i.getParentID()));
				params.add(new BasicNameValuePair("name", i.getName()));
				params.add(new BasicNameValuePair("adder_id", ""
						+ i.getCreator()));

				if (i.getQuantity() == 0)
					i.setQuantity(1);

				params.add(new BasicNameValuePair("quantity", ""
						+ i.getQuantity()));
				params.add(new BasicNameValuePair("assignee_id", ""
						+ i.getAssignee()));
				params.add(new BasicNameValuePair("assigner_id", ""
						+ i.getAssigner()));
				params.add(new BasicNameValuePair("notes", i.getNotes()));

				if (i.isCompleted())
					params.add(new BasicNameValuePair("completed", "1"));
				else
					params.add(new BasicNameValuePair("completed", "0"));

				params.add(new BasicNameValuePair("completion_date", i
						.getCompletionDate()));

				Log.i("PREV AND NEXT ID", "For item " + i.getName() + " are "
						+ (i.getPrev() > 2 ? i.getPrev() : "NONE") + " and "
						+ (i.getNext() > 2 ? i.getNext() : "NONE"));

				params.add(new BasicNameValuePair("prev_id", "" + i.getPrev()));
				params.add(new BasicNameValuePair("next_id", "" + i.getNext()));

				httppost.setEntity(new UrlEncodedFormEntity(params));

			}

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("POST ITEM", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("POST ITEM", "Error parsing data " + e.toString());
		}

		try {
			String response = jArray.getString("response");
			Log.d("POST ITEM", "response: " + response);

		} catch (JSONException e) {
			Log.e("POST ITEM", "Error with posting item " + e.toString());
		}

	} // end postItem

	public static JSONObject getJSONfromURL() {
		return getJSONfromURL(null);
	}

	public static JSONObject getJSONfromURL(String a) {
		return getJSONfromURL(a, null);
	}

	public static JSONObject getJSONfromURL(String action, String listID) {

		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);

			if (action != null) {

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("action", action));

				if (listID != null) {
					params.add(new BasicNameValuePair("listID", listID));
				}

				httppost.setEntity(new UrlEncodedFormEntity(params));

			}

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jArray;
	} // end getJSON

}
