package service;

import zebrafish.util.IOUtil;
import zebrafish.util.JSONCustomList;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;

public class CustomListTask {
	public static final String TAG = "CustomListTask";
	protected Context context;
	
	private static int GET_LISTS = 1;

	private class DoListUpdateTask extends AsyncTask<CustomList, Void, String> {
		@Override
		protected String doInBackground(CustomList... params) {
			String response = "";
			
			if (IOUtil.isOnline(context)) {

			for (CustomList custlist : params) {
				Log.i(TAG, "Updated " + custlist.getName());
				JSONCustomList.updateList(custlist);
			}
			
			} else {
				IOUtil.informConnectionIssue(context);
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// textView.setText(result);
			Log.i(TAG, result);
		}
	}

	public void update(CustomList list) {
		DoListUpdateTask task = new DoListUpdateTask();
		task.execute(list);
	}
}