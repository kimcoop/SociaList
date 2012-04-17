package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;
import java.util.HashMap;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONfunctions;
import zebrafish.util.UIUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.CustomListAdapter;
import edu.pitt.cs1635group3.Adapters.InviteAdapter;

public class PendingInvitesActivity extends SherlockListActivity { // ListActivity
	private ArrayList<Invite> invites;
	private ArrayAdapter<Invite> adapter;
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
		Log.i(TAG, "User ID fetched: " + userID);
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
		}
		UIUtil.showMessage(context, "Invites accepted.");
	}
	
	public void ignoreSelected(View v) {
		selectedInvites = ((InviteAdapter) adapter).getSelected();
		for (Invite i : selectedInvites) {
			Invite.ignore(context, i); // class method
		}
		UIUtil.showMessage(context, "Invites ignored.");
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
		if (item.getItemId() == R.id.menu_add) {
			// addListItem();
			return false;
		} else if (item.getItemId() == R.id.menu_invite) {
			intent = new Intent(this, InviteActivity.class);
			startActivity(intent);
			return true;
		} else {
			return false;
		}
	}
}