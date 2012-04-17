package service;

import zebrafish.util.JSONfunctions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.SplashScreenActivity;
import edu.pitt.cs1635group3.Activities.Classes.Item;

public class SplashScreenTask {
	public static final String TAG = "SplashScreenTask";
	protected Context context;

	private class DoSplashScreenTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";

			if(params[0] == 1){
				JSONfunctions.getLists(context);
				JSONfunctions.getInvites(context);
			}
			else if(params[0] == 2){
				JSONfunctions.getLists(context);
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
		task.execute(1);

	}
	public void getLists(Context c) {
		context = c;
		DoSplashScreenTask task = new DoSplashScreenTask();
		task.execute(2);

	}
}