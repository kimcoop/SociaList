package service;

import zebrafish.util.JSONItem;
import zebrafish.util.JSONUser;
import zebrafish.util.JSONfunctions;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class UserUpdateTask {
	public static final String TAG = "UserUpdateTask";
	protected Context context;

	private class DoUserUpdateTask extends AsyncTask<User, Void, String> {
		@Override
		protected String doInBackground(User... params) {
			String response = "";

			for (User u : params) {
				Log.i(TAG, "Updated " + u.getName());
				JSONUser.updateUser(u);
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