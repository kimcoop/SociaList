package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import service.InviteTask;

import zebrafish.util.UIUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.CheckBox;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.InviteAdapter;

public class PendingInvitesActivity extends SherlockListActivity { // ListActivity
	private static ArrayList<Invite> invites;
	private static InviteAdapter adapter;
	private static ArrayList<Invite> selectedInvites;

	private static Context context;
	private static final String TAG = "PendingInvitesActivity";
	private static int userID;
	private static ListView lv;

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
		selectedInvites = adapter.getSelected();
		Invite.accept(context, selectedInvites); // run async
		for (Invite inv : selectedInvites) {
			invites.remove(inv);
		}
		String pluralizer = "Invite";
		if (selectedInvites.size() > 1)
			pluralizer += "s";
		adapter.notifyDataSetChanged();
	}

	public void ignoreSelected(View v) {
		selectedInvites = adapter.getSelected();
		for (Invite inv : selectedInvites) {
			inv.ignore(context);
			invites.remove(inv);
		}
		String pluralizer = "Invite";
		if (selectedInvites.size() > 1)
			pluralizer += "s";
		adapter.notifyDataSetChanged();
	}
	
	public static InviteAdapter getAdapter() { // pass to the async task so it will be updated on postExecute
		return adapter;
	}
	
	public static void selectAll() {
		for (int i=0; i < lv.getChildCount(); i++) {
			LinearLayout itemLayout = (LinearLayout) lv.getChildAt(i);
			CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.element_checkbox);
			cb.setChecked(true);
		}
		
		adapter.selectAll();
	}
	
	public static void deselectAll() {
		for (int i=0; i < lv.getChildCount(); i++) {
			LinearLayout itemLayout = (LinearLayout) lv.getChildAt(i);
			CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.element_checkbox);
			cb.setChecked(false);
		}
		
		adapter.deselectAll();
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
			// this ends up calling refresh for the adapter
			return true;
		} else if (item.getItemId() == R.id.menu_select_all) {
			
			if (invites.size() > 0) {
			
				if (adapter.allSelected()) deselectAll();
				else selectAll();
			
			}
			
			return true;
		} else {
			return false;
		}
	}
}