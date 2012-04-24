package service;

import java.util.ArrayList;

import edu.pitt.cs1635group3.Activities.HomeActivity;
import edu.pitt.cs1635group3.Activities.PendingInvitesActivity;
import edu.pitt.cs1635group3.Activities.SociaListActivity;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.CustomListAdapter;
import edu.pitt.cs1635group3.Adapters.InviteAdapter;
import zebrafish.util.IOUtil;
import zebrafish.util.JSONCustomList;
import zebrafish.util.JSONInvite;
import zebrafish.util.JSONUser;
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
	private ArrayList<Invite> currInvites;
	private Invite currInv; // invite to update, passed in by method updateInvite
	private int currInvId, currListId;
	protected ProgressDialog pd;
	private static String contact;
	
	private static int GET_INVITES = 1;
	private static int ACCEPT_INVITE = 2;
	private static int DECLINE_INVITE = 3;
	private static int ACCEPT_INVITES = 4;
	private static int SEND_INVITE_PHONE = 5;
	private static int SEND_INVITE_EMAIL = 6;

	private class DoInviteTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			
			if (IOUtil.isOnline(context)) {

				if (params[0] == GET_INVITES) {
					numInvites = (JSONInvite.getInvites(context)).size();
				} else if (params[0] == ACCEPT_INVITE) {
					JSONInvite.updateInvite(context, currInv);
				} else if (params[0] == DECLINE_INVITE) {
					JSONInvite.declineInvite(context, currInvId);
				} else if (params[0] == ACCEPT_INVITES) {
					JSONInvite.acceptInvites(context, currInvites);
				} else if (params[0] == SEND_INVITE_PHONE) {
					JSONUser.inviteByPhone(contact, currListId);
				} else if (params[0] == SEND_INVITE_EMAIL) {
					JSONUser.inviteByEmail(contact, currListId);
				}
			
			} else {
				IOUtil.informConnectionIssue(context);
			}

			return ""+params[0]; //inform onPostExecute method what operation was completed
		}

		@Override
		protected void onPostExecute(String result) {
			// based on what operation was completed, do something
			Log.i(TAG, result);

			if (pd != null && pd.isShowing()) { 
				pd.dismiss();
			}
			
			InviteAdapter adapter = PendingInvitesActivity.getAdapter();
			if (adapter != null) adapter.notifyDataSetChanged();
			String msg = "";
			
			if (result.equals(""+GET_INVITES)) {
				refreshActivity();
				HomeActivity.updateNumInvites(numInvites);
				msg = UIUtil.pluralize(numInvites, "invite", "found");
				UIUtil.showMessageShort(context, msg);
				
			} else if (result.equals(""+ACCEPT_INVITE)) {
				msg = UIUtil.pluralize(numInvites, "invite", "accepted");
				HomeActivity.updateNumInvites(numInvites);
				UIUtil.showMessageShort(context, msg);
				// Now also refresh adapter within My Lists (since new list will need to be pulled)
				new CustomListTask().refreshListsQuiet(context);
				
			} else if (result.equals(""+DECLINE_INVITE)) {
				HomeActivity.updateNumInvites();
				msg = UIUtil.pluralize(numInvites, "invite", "declined");
				UIUtil.showMessageShort(context, msg);
				
			} else if (result.equals(""+ACCEPT_INVITES)) {

				HomeActivity.updateNumInvites();
				// Now also refresh adapter within My Lists (since new list will need to be pulled)
				new CustomListTask().refreshListsQuiet(context);
			} else if (result.equals(""+SEND_INVITE_PHONE)) {
				msg = "Invite sent!";
				UIUtil.showMessageShort(context, msg);
			} else if (result.equals(""+SEND_INVITE_EMAIL)) {
				msg = "Invite sent!";
				UIUtil.showMessageShort(context, msg);
			}
		}
	}
	
	public void refreshActivity() {

		((Activity)context).finish();
		((Activity)context).startActivity(((Activity) context).getIntent());
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
		//pd = ProgressDialog.show(context,"Updating" ,"Please wait...", true, false, null);
		DoInviteTask task = new DoInviteTask();
		task.execute(ACCEPT_INVITE);
	}

	public void ignoreInvite(Context c, int invId) {
		context = c;
		currInvId = invId;
		//pd = ProgressDialog.show(context,"Updating", "Please wait...", true, false, null);
		DoInviteTask task = new DoInviteTask();
		task.execute(DECLINE_INVITE);
	}

	public void updateInvites(Context c,
			ArrayList<Invite> selectedInvites) {
		context = c;
		
		currInvites = selectedInvites;
		DoInviteTask task = new DoInviteTask();
		task.execute(ACCEPT_INVITES);
	}
	
	public void inviteByPhone(Context c, String pn, int listID) {
		context = c;
		contact = pn;
		currListId = listID;
		DoInviteTask task = new DoInviteTask();
		task.execute(SEND_INVITE_PHONE);
	}
	
	public void inviteByEmail(Context c, String email, int listID) {
		context = c;
		contact = email;
		currListId = listID;
		DoInviteTask task = new DoInviteTask();
		task.execute(SEND_INVITE_EMAIL);
	}
	
	
}