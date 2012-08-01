package service;

import zebrafish.util.IOUtil;
import zebrafish.util.JSONItem;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import edu.pitt.cs1635group3.Activities.InsideListActivity;
import edu.pitt.cs1635group3.Activities.Classes.Item;

public class ItemTask {
	public static final String TAG = "ItemUpdateTask";
	protected Context context;
	private static Item currItem;
	private static EditText input;
	private static boolean refresh = false;
	private static int UPDATE_ITEM = 1;
	private static int DELETE_ITEM = 2;
	private static int ADD_ITEM = 3;

	private class DoItemTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";
			
			if (params[0]==UPDATE_ITEM) {

					JSONItem.updateItem(currItem);
			
			} else if (params[0]==DELETE_ITEM) {

				JSONItem.deleteItem(currItem.getID());
				
			} else if (params[0]==ADD_ITEM) {

				InsideListActivity.addItem(input);
			}
			

			return ""+params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			
			Log.i(TAG, "Item " +currItem.getName()+ " updated or deleted");
			
			//Update the list so we can see the newly added Item
			if(refresh){
				InsideListActivity.adapter.notifyDataSetChanged();
			}
		}
	}

	public void update(Item i) {
		currItem = i;
		DoItemTask task = new DoItemTask();
		task.execute(UPDATE_ITEM);

	}

	public void deleteItem(Item i) {
		currItem = i;
		DoItemTask task = new DoItemTask();
		task.execute(DELETE_ITEM);
		
	}
	
	public void addItem(EditText i) {
		input = i;
		refresh = true;
		DoItemTask task = new DoItemTask();
		task.execute(ADD_ITEM);
		
	}
}