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

	private class DoItemTask extends AsyncTask<Item, Void, String> {
		@Override
		protected String doInBackground(Item... params) {
			String response = "";
			
			//if (IOUtil.isOnline(this.getBaseContext())) {

			for (Item item : params) {
				Log.i(TAG, "Updated " + item.getName());
				JSONItem.updateItem(item); // really will only be one item,
											// I think -KIm
			}
			
			//} else {
			//	IOUtil.informConnectionIssue(context);
			//}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// textView.setText(result);
			Log.i(TAG, result);
		}
	}

	public void update(Item i) {
		DoItemTask task = new DoItemTask();
		task.execute(i);

	}
}