package service;

import zebrafish.util.JSONfunctions;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Item;

public class ItemUpdateTask {
	public static final String TAG = "ItemUpdateTask";
	protected Context context;

	private class DoItemUpdateTask extends AsyncTask<Item, Void, String> {
		@Override
		protected String doInBackground(Item... params) {
			String response = "";

			for(Item item : params) {
				Log.i(TAG, "Updated " +item.getName());
				JSONfunctions.updateItem(item); // really will only be one item, I think -KIm
			}
			
			
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			//textView.setText(result);
			Log.i(TAG, result);
		}
	}
	
	public void update(Item i) {
		DoItemUpdateTask task = new DoItemUpdateTask();
		task.execute(i);

	}
}