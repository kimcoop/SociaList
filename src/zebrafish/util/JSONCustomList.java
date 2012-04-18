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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;

public class JSONCustomList {

	public static final String URL = Config.URL;
	public static final String TAG = "JSON CUSTOM LIST";
	public static DBHelper db;
	public static final boolean NO_PUSH_TO_CLOUD = false;

	public static int getListPK() {
		return JSONfunctions.getPK("getListPK");
	} // end getListPK


	public static void createList(CustomList list, int userID) {
		// This is called after we've already snatched the PK from the web
		// server.
		// So the list (even though we're currently creating it) already is
		// initialized on the server.

		updateList(list);
	} // end CreateList
	


	public static void deleteList(int id) {

		String action = "deleteList";
		String objID = ""+id;
		
		ArrayList<NameValuePair> params = PostParams.formatParams(action,
				objID);
		
		JSONfunctions.post(params);
		
	} // end deleteList

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
		//result = JSONfunctions.getResult(is);
		//resp = JSONfunctions.getResponse(result, "response");
		// Log.i(TAG, resp);

	} // end UpdateList

	public static ArrayList<CustomList> browseForList(Context context, String CID) {
		JSONObject json = JSONfunctions.getJSONfromURL("browseForList", CID);
		ArrayList<CustomList> matchingLists = new ArrayList<CustomList>();
		boolean strippedDown = true;

		try {
			JSONArray lists = json.getJSONArray("lists");

			if (lists.length() > 0) {

				JSONObject listObj;
				CustomList tempList;

				for (int i = 0; i < lists.length(); i++) {
						listObj = lists.getJSONObject(i);
						tempList = CustomList.parseJSONforCustomList(listObj, strippedDown); // parses items too
						matchingLists.add(tempList);
						
				} // end for
			}

		} catch (JSONException e) {
			Log.e(TAG, "browseForList(): " + e.toString());
		}

		return matchingLists;
	} // end browseForList

	public static void getLists(Context context) {

		ArrayList<CustomList> myCustomLists = new ArrayList<CustomList>();
		JSONObject json = JSONfunctions.getJSONfromURL(context, "getLists");
		Log.i(TAG, json.toString() + "");
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
				JSONItem.getListItems(context, listID); // pull the list's items from the
												// server
				JSONUser.getUsers(context, listID); // pull the list's users too

			}

			CustomList.insertOrUpdateLists(context, myCustomLists,
					NO_PUSH_TO_CLOUD);

		} catch (JSONException e) {
			Log.e(TAG, "Error in getLists(): " + e.toString());
		}

	} // end getLists(Context)
	
}
