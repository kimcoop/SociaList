package service;

import zebrafish.util.IOUtil;
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
	private static int INVITES = 3;
	private static int INITIAL_DOWNLOAD = 4;

	private class DoSplashScreenTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";
			
			if (IOUtil.isOnline(context)) {

				if (params[0] == LISTS_INVITES) {
					JSONCustomList.getLists(context);
					JSONInvite.getInvites(context);
				} else if (params[0] == LISTS) {
					JSONCustomList.getLists(context);
				} else if (params[0] == INVITES) {
					Log.i(TAG, "getting invites");
					JSONInvite.getInvites(context);
				} else if (params[0] == INITIAL_DOWNLOAD) {
					Log.i(TAG, "Pulling initial download based on user ID ");
					JSONCustomList.download(context);
				}

			} else {
				IOUtil.informConnectionIssue(context);
			}
			
			return "SplashScreenTask complete: " +params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i(TAG, result);
			((Activity) context).finish();
			((Activity) context).startActivity(new Intent(
					"edu.pitt.cs1635group3.HomeActivity"));
		}
	}
	
	public void download(Context c) {
		context = c;
		DoSplashScreenTask task = new DoSplashScreenTask();
		task.execute(INITIAL_DOWNLOAD);
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

	public void getInvites(Context c) {
		context = c;
		DoSplashScreenTask task = new DoSplashScreenTask();
		task.execute(INVITES);
	}
}