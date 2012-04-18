package service;

import zebrafish.util.JSONCustomList;
import zebrafish.util.JSONInvite;
import zebrafish.util.JSONUser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class SplashScreenTask {
	public static final String TAG = "SplashScreenTask";
	protected Context context;
	private static int LISTS_INVITES = 1; // Flag
	private static int LISTS = 2;

	private class DoSplashScreenTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";

			if(params[0] == LISTS_INVITES){
				JSONCustomList.getLists(context);
				JSONInvite.getInvites(context);
			}
			else if(params[0] == LISTS){
				JSONCustomList.getLists(context);
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// textView.setText(result);
			Log.i(TAG, result);
			((Activity)context).finish();
			((Activity)context).startActivity(new Intent(
					"edu.pitt.cs1635group3.HomeActivity"));
		}
	}

	public void getListsAndInvites(Context c) {
		context = c;
		DoSplashScreenTask task = new DoSplashScreenTask();
		task.execute(LISTS_INVITES);

	}
	public void getLists(Context c) {
		context = c;
		DoSplashScreenTask task = new DoSplashScreenTask();
		task.execute(LISTS);

	}
}