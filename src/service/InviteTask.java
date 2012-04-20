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
	protected static int numInvites;
	private Invite currInv; // invite to update, passed in by method updateInvite
	protected ProgressDialog pd;
	
	private static int GET_INVITES = 1;
	private static int UPDATE_INVITE = 2;

	private class DoInviteTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {

			if (params[0] == GET_INVITES) {
				JSONInvite.getInvites(context);
			} else if (params[0] == UPDATE_INVITE) {
				JSONInvite.updateInvite(context, currInv);
			}

			return ""+params[0]; //inform onPostExecute method what operation was completed
		}

		@Override
		protected void onPostExecute(String result) {
			// based on what operation was completed, do something
			Log.i(TAG, result);
			
			if (result.equals(""+GET_INVITES)) {
				pd.dismiss();
				int uID = User.getCurrUser(context);
				int numInvites = Invite.getNumInvites(context, uID);
				HomeActivity.updateNumInvites(numInvites);
				PendingInvitesActivity.updateInvites();
				UIUtil.showMessage(context, numInvites+" invites found."); // todo - correct pluralization
			} else if (result.equals(""+UPDATE_INVITE)){
				pd.dismiss();
				int uID = User.getCurrUser(context);
				int numInvites = Invite.getNumInvites(context, uID);
				HomeActivity.updateNumInvites(numInvites);
				PendingInvitesActivity.updateInvites();
			}
		}
	}

	public void getInvites(Context c) {
		context = c;
		pd = ProgressDialog.show(context,"Checking for invites","Please wait...", true, false, null);
		DoInviteTask task = new DoInviteTask();
		task.execute(GET_INVITES);
	}
	
	public void updateInvite(Context c, Invite i) {
		context = c;
		currInv = i;
		pd = ProgressDialog.show(context,"Updating invites","Please wait...", true, false, null);
		DoInviteTask task = new DoInviteTask();
		task.execute(UPDATE_INVITE);
	}
	
	
}