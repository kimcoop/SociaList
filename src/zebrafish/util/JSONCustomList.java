package zebrafish.util;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
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
		String objID = "" + id;

		ArrayList<NameValuePair> params = PostParams
				.formatParams(action, objID);

		JSONfunctions.postToCloud(params);

	} // end deleteList

	public static void updateList(CustomList list) {

		ArrayList<NameValuePair> params = PostParams.formatListParams(
				"updateList", list);

		JSONfunctions.postToCloud(params);

	} // end UpdateList

	public static ArrayList<CustomList> browseForList(Context context,
			String CID) {
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
					tempList = CustomList.parseJSONforCustomList(listObj,
							strippedDown); // parses items too
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
				JSONItem.getListItems(context, listID); // pull the list's items
														// from the
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
