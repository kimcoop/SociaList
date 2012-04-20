package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import service.InviteTask;

import zebrafish.util.UIUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.InviteAdapter;

public class PendingInvitesActivity extends SherlockListActivity { // ListActivity
	private ArrayList<Invite> invites;
	private static ArrayAdapter<Invite> adapter;
	private ArrayList<Invite> selectedInvites;

	private Context context;
	private static final String TAG = "PendingInvitesActivity";
	private static int userID;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_layout);
		context = this;

		userID = User.getCurrUser(context);
		invites = Invite.getInvites(context, userID);

		getSupportActionBar();
		setTitle("Invites");

		adapter = new InviteAdapter(this, R.layout.generic_row, invites);
		lv = getListView();

		setListAdapter(adapter);
		lv.setClickable(true);
		lv.setTextFilterEnabled(true);

	}

	public void acceptSelected(View v) {
		selectedInvites = ((InviteAdapter) adapter).getSelected();
		for (Invite i : selectedInvites) {
			i.accept(context);
			invites.remove(i);
		}
		String pluralizer = "invite";
		if (selectedInvites.size() > 1)
			pluralizer += "s";
		UIUtil.showMessage(context, pluralizer + " accepted.");
		adapter.notifyDataSetChanged();
	}

	public void ignoreSelected(View v) {
		selectedInvites = ((InviteAdapter) adapter).getSelected();
		for (Invite i : selectedInvites) {
			Invite.ignore(context, i); // class method
			invites.remove(i);
		}
		String pluralizer = "invite";
		if (selectedInvites.size() > 1)
			pluralizer += "s";
		UIUtil.showMessage(context, pluralizer + " ignored.");
		adapter.notifyDataSetChanged();
	}

	public static void updateInvites() {
		if (adapter != null) 
			adapter.notifyDataSetChanged();		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.invites_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == android.R.id.home) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_refresh) {
			new InviteTask().getInvites(context);
			return true;
		} else {
			return false;
		}
	}
}