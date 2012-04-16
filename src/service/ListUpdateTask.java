package service;

import zebrafish.util.JSONfunctions;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.Item;

public class ListUpdateTask {
	public static final String TAG = "ListUpdateTask";
	protected Context context;

	private class DoListUpdateTask extends AsyncTask<CustomList, Void, String> {
		@Override
		protected String doInBackground(CustomList... params) {
			String response = "";

			for(CustomList custlist : params) {
				Log.i(TAG, "Updated " +custlist.getName());
				JSONfunctions.updateList(custlist);
			}
			
			
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			//textView.setText(result);
			Log.i(TAG, result);
		}
	}
	
	public void update(CustomList list) {
		DoListUpdateTask task = new DoListUpdateTask();
		task.execute(list);

	}
}