package service;

import zebrafish.util.IOUtil;
import zebrafish.util.JSONItem;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.Item;

public class ItemTask {
	public static final String TAG = "ItemUpdateTask";
	protected Context context;
	private static Item currItem;
	private static int UPDATE_ITEM = 1;
	private static int DELETE_ITEM = 2;

	private class DoItemTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";
			
			if (params[0]==UPDATE_ITEM) {

					JSONItem.updateItem(currItem);
			
			} else if (params[0]==DELETE_ITEM) {

				JSONItem.deleteItem(currItem.getID());
			}

			return ""+params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			
			Log.i(TAG, "Item " +currItem.getName()+ " updated or deleted");
			
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
}