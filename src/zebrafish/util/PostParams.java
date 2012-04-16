package zebrafish.util;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;
import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.Item;

public class PostParams {

	public static final String TAG = "POST PARAMS";

	public static ArrayList<NameValuePair> formatParams(String action, Item i) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		if (action == "updateItem") { // if item is to be updated, we
										// need to also pass its ID and
										// add_date
			params.add(param("action", "updateItem"));
			params.add(param("objID", "" + i.getID()));

			params.add(param("add_date", i.getCreationDate()));
		} else {
			params.add(param("action", "createItem"));
		}
		// the following attributes will need to be updated regardless
		// of action
		params.add(param("parent_id", "" + i.getParentID()));
		params.add(param("name", i.getName()));
		params.add(param("adder_id", "" + i.getCreator()));

		if (i.getQuantity() == 0)
			i.setQuantity(1);

		params.add(param("quantity", "" + i.getQuantity()));
		params.add(param("assignee_id", "" + i.getAssignee()));
		params.add(param("assigner_id", "" + i.getAssigner()));
		params.add(param("notes", i.getNotes()));

		if (i.isCompleted())
			params.add(param("completed", "1"));
		else
			params.add(param("completed", "0"));

		params.add(param("completion_date", i.getCompletionDate()));

		Log.i("PREV AND NEXT ID",
				"For item " + i.getName() + " are "
						+ (i.getPrev() > 2 ? i.getPrev() : "NONE") + " and "
						+ (i.getNext() > 2 ? i.getNext() : "NONE"));

		params.add(param("prev_id", "" + i.getPrev()));
		params.add(param("next_id", "" + i.getNext()));

		return params;

	}

	public static ArrayList<NameValuePair> formatParams(String action, String id) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(param("action", action));

		if (id != null) {
			params.add(param("objID", id));
		}
		
		if (action.equals("getLists")) {

			Log.d(TAG, "TAKE USER ID: action: " + action + "&objID: " + id);
		}

		return params;
	}

	public static ArrayList<NameValuePair> formatListParams(String action,
			CustomList myList) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		if (myList != null) {

			params.add(param("action", action));
			params.add(param("id", "" + myList.getID()));
			params.add(param("custom_id", "" + myList.getCustomID()));
			params.add(param("name", myList.getName()));
			params.add(param("adder_id", "" + myList.getCreator()));
			// creation date handled as now() by MySQL

		}

		return params;
	}

	public static BasicNameValuePair param(String param, String content) {
		return new BasicNameValuePair(param, content);
	}

}
