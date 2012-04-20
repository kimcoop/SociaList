package service;

import zebrafish.util.IOUtil;
import zebrafish.util.JSONUser;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class UserUpdateTask {
	public static final String TAG = "UserUpdateTask";
	protected Context context;

	private class DoUserUpdateTask extends AsyncTask<User, Void, String> {
		@Override
		protected String doInBackground(User... params) {
			
			if (IOUtil.isOnline(context)) {
			
				String response = "";
	
				for (User u : params) {
					Log.i(TAG, "Updated " + u.getName());
					JSONUser.updateUser(u);
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

	public void update(User i) {
		DoUserUpdateTask task = new DoUserUpdateTask();
		task.execute(i);

	}
}