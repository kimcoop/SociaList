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
		
		return params;
		
	}
	


	public static ArrayList<NameValuePair> formatParams(String action, int objID) {
		return formatParams(action, ""+objID);
	}
	
	public static ArrayList<NameValuePair> formatParams(String action, String objID) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", action));

		if (objID != null) {
			params.add(new BasicNameValuePair("objID", objID));
		} else { // This is ok for now, but objID should be populated for getLists(user ID) and getUser(List ID)
			//Log.e(TAG, "TODO: Bad params. action: " +action+ "& objID: " + objID);
		}
		
		return params;		
	}
	
	public static ArrayList<NameValuePair> formatListParams(String action, CustomList myList) {
		 ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		if (myList != null) {

			params.add(new BasicNameValuePair("action", action));
			params.add(new BasicNameValuePair("id", "" + myList.getID()));
			params.add(new BasicNameValuePair("custom_id", ""
					+ myList.getCustomID()));
			params.add(new BasicNameValuePair("name", myList.getName()));
			params.add(new BasicNameValuePair("adder_id", ""
					+ myList.getCreator()));

		}
		
		return params;

	}

	
	
}
