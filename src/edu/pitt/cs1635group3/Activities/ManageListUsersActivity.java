package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import zebrafish.util.UIUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.InviteAdapter;
import edu.pitt.cs1635group3.Adapters.ListUsersAdapter;
import edu.pitt.cs1635group3.Adapters.MyListItemAdapter;

public class ManageListUsersActivity extends SherlockListActivity {

	private Context context;
	private int listID;
	private CustomList list;
	private static String TAG = "ManageListUsersActivity";
	private static int userID;
	private ArrayList<User> users;
	private ArrayAdapter<User> adapter;
	private ArrayList<User> selected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_list_users);
		context = this;

		int uID = User.getCurrUser(context);
		Log.i(TAG, "User ID fetched: " + uID);

		listID = getIntent().getExtras().getInt("listID");
		users = User.getUsersForList(context, listID);
		
		//String listName = CustomList.getListName(context, listID);

		getSupportActionBar();
		setTitle("Manage List");
		adapter = new ListUsersAdapter(this, R.layout.generic_row, users);
		setListAdapter(adapter);

	}
	

	
	public void removeSelected(View v) {
		selected = ((ListUsersAdapter) adapter).getSelected();
		for (User u : selected) {
			//User.removeFromList(context, u, listID);
			users.remove(u);
			selected.remove(u);
		}
		String pluralizer = "User";
		if (selected.size() > 1) pluralizer += "s";
		UIUtil.showMessage(context, pluralizer + " removed.");
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == R.id.menu_add) {
			// TODO: add user item popup
			return false;
		} else if (item.getItemId() == R.id.menu_rename) {
			// rename();
			return true;
		} else if (item.getItemId() == R.id.menu_invite) {
			intent = new Intent(this, InviteActivity.class);
			startActivity(intent);
			return true;
		} else {
			return false;
		}
	}

}
