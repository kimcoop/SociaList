package service;

import edu.pitt.cs1635group3.Activities.HomeActivity;
import edu.pitt.cs1635group3.Activities.PendingInvitesActivity;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.User;
import zebrafish.util.JSONCustomList;
import zebrafish.util.JSONInvite;
import zebrafish.util.UIUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class InviteTask {
	public static final String TAG = "InviteTask";
	protected Context context;
	private static int GET_INVITES = 1;
	protected static int numInvites;
	protected ProgressDialog pd;

	private class DoInviteTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			String response = "";

			if (params[0] == GET_INVITES) {
				JSONInvite.getInvites(context);
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// textView.setText(result);
			Log.i(TAG, result);
			pd.dismiss();
			int uID = User.getCurrUser(context);
			int numInvites = Invite.getNumInvites(context, uID);
			HomeActivity.updateNumInvites(numInvites);
			PendingInvitesActivity.updateInvites();
			UIUtil.showMessage(context, numInvites+" invites found.");
		}
	}

	public void getInvites(Context c) {
		context = c;
		pd = ProgressDialog.show(context,"Checking for invites","Please wait...", true, false, null);
		DoInviteTask task = new DoInviteTask();
		task.execute(GET_INVITES);

	}
}