package service;

import java.util.ArrayList;

import edu.pitt.cs1635group3.Activities.HomeActivity;
import edu.pitt.cs1635group3.Activities.PendingInvitesActivity;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.InviteAdapter;
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
	private int currInvId;
	protected ProgressDialog pd;
	
	private static int GET_INVITES = 1;
	private static int ACCEPT_INVITE = 2;
	private static int DECLINE_INVITE = 3;

	private class DoInviteTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {

			if (params[0] == GET_INVITES) {
				JSONInvite.getInvites(context);
			} else if (params[0] == ACCEPT_INVITE) {
				JSONInvite.updateInvite(context, currInv);
			} else if (params[0] == DECLINE_INVITE) {
				JSONInvite.declineInvite(context, currInvId);
			}

			return ""+params[0]; //inform onPostExecute method what operation was completed
		}

		@Override
		protected void onPostExecute(String result) {
			// based on what operation was completed, do something
			Log.i(TAG, result);
			

			pd.dismiss();
			InviteAdapter adapter = PendingInvitesActivity.grabAdapter();
			adapter.notifyDataSetChanged();
			
			if (result.equals(""+GET_INVITES)) {
				int numInvites = Invite.getNumInvites(context, User.getCurrUser(context));
				HomeActivity.updateNumInvites(numInvites);
				UIUtil.showMessage(context, numInvites+" invites found."); // todo - correct pluralization
			} else if (result.equals(""+ACCEPT_INVITE)) {
				UIUtil.showMessage(context, "Invite accepted."); // todo - correct pluralization
			} else if (result.equals(""+DECLINE_INVITE)) {
				UIUtil.showMessage(context, "Invite declined."); // todo - correct pluralization
			}
		}
	}

	public void getInvites(Context c) {
		context = c;
		pd = ProgressDialog.show(context,"Checking for invites", "Please wait...", true, false, null);
		DoInviteTask task = new DoInviteTask();
		task.execute(GET_INVITES);
	}
	
	public void updateInvite(Context c, Invite i) {
		context = c;
		currInv = i;
		pd = ProgressDialog.show(context,"Updating" ,"Please wait...", true, false, null);
		DoInviteTask task = new DoInviteTask();
		task.execute(ACCEPT_INVITE);
	}

	public void ignoreInvite(Context c, int invId) {
		context = c;
		currInvId = invId;
		pd = ProgressDialog.show(context,"Updating", "Please wait...", true, false, null);
		DoInviteTask task = new DoInviteTask();
		task.execute(DECLINE_INVITE);
	}
	
	
}