package zebrafish.util;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;

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
	

	public static void createAsUpdate(CustomList currList) {
		ArrayList<NameValuePair> params = PostParams.formatListParams(
				"createAsUpdate", currList);

		JSONfunctions.postToCloud(params);
		
	} // end createAsUpdate
	
	public static void updateList(CustomList list) {

		ArrayList<NameValuePair> params = PostParams.formatListParams(
				"updateList", list);

		JSONfunctions.postToCloud(params);

	} // end UpdateList

	public static ArrayList<CustomList> browseForList(Context context,
			String CID) {
		JSONObject json = JSONfunctions.getJSONfromURL("browseForList", CID);
		ArrayList<CustomList> matchingLists = new ArrayList<CustomList>();

		try {
			JSONArray lists = json.getJSONArray("lists");

			if (lists.length() > 0) {

				JSONObject listObj;
				CustomList tempList;

				for (int i = 0; i < lists.length(); i++) {
					listObj = lists.getJSONObject(i);
					tempList = CustomList.parseJSONforTemplateCustomList(listObj); // parses items too
					matchingLists.add(tempList);

				} // end for
			}

		} catch (JSONException e) {
			Log.e(TAG, "browseForList(): " + e.toString());
		}

		return matchingLists;
	} // end browseForList
	


	public static int download(Context context) {
		// Return number of lists found.

		ArrayList<CustomList> myCustomLists = new ArrayList<CustomList>();
		ArrayList<Item> listItems = new ArrayList<Item>();
		ArrayList<User> listUsers = new ArrayList<User>();
		JSONObject json = JSONfunctions.getJSONfromURL(context, "getListsForUser");
		Log.i(TAG, json.toString() + "");
		try {
			JSONArray myLists = json.getJSONArray("lists");

			CustomList list;
			JSONObject e1;

			for (int i = 0; i < myLists.length(); i++) {
				e1 = myLists.getJSONObject(i);
				list = new CustomList(e1); // this will also parse for items and users (for each list)
				listItems = CustomList.parseForItems(e1.getJSONArray("items"));
				listUsers = CustomList.parseForUsers(e1.getJSONArray("users"));
				CustomList.setLinks(context, listItems);
				
				Item.insertOrUpdateItems(context, listItems, NO_PUSH_TO_CLOUD);
				User.insertOrUpdateUsers(list.getID(), context, listUsers, NO_PUSH_TO_CLOUD);
				
				
				myCustomLists.add(list);
			}

			CustomList.insertOrUpdateLists(context, myCustomLists,
					NO_PUSH_TO_CLOUD);

		} catch (JSONException e) {
			Log.e(TAG, "Error in getLists(): " + e.toString());
		}

		return myCustomLists.size();
		
	} // end getListsForUser(Context)


}
