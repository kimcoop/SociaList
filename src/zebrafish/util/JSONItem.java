package zebrafish.util;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.Item;

public class JSONItem {

	public static final String URL = Config.URL;
	public static final String TAG = "JSON Item";
	public static DBHelper db;
	public static final boolean NO_PUSH_TO_CLOUD = false;

	public static int getItemPK() {
		return JSONfunctions.getPK("getItemPK");
	} // get getItemPK

	public static void deleteItem(int id) {

		String action = "deleteItem";
		String objID = ""+id;
		
		ArrayList<NameValuePair> params = PostParams.formatParams(action,
				objID);
		
		JSONfunctions.post(params);
		
	} // end deleteItem

	public static void getListItems(Context context, int listID) {

		JSONObject json = JSONfunctions.getJSONfromURL("getItemsForList", listID);
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
						item1.setParent(listID);

						e2 = items.getJSONObject(i + 1);
						item2 = new Item(e2);
						item2.setParent(listID);

						item2.setPrev(item1.getID());
						item1.setNext(item2.getID());
						listItems.add(item1);
						listItems.add(item2);

						i += 1;

					} else {
						e1 = items.getJSONObject(i);
						item1 = new Item(e1);
						item1.setParent(listID);

						Item prev = listItems.get(i - 1);

						prev.setNext(item1.getID());
						item1.setPrev(prev.getID());
						listItems.add(item1);

					}
				}

				listItems.get(0).setPrev(
						listItems.get(listItems.size() - 1).getID()); // "Loop around"
				listItems.get(listItems.size() - 1).setNext(
						listItems.get(0).getID());
			}

		} catch (JSONException e) {
			Log.e(TAG, "PullItems(): " + e.toString());
		}

		// return listItems;
		Item.insertOrUpdateItems(context, listItems, NO_PUSH_TO_CLOUD);

	}
}
