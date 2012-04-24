package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import zebrafish.util.UIUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.ListUsersAdapter;

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

		userID = User.getCurrUser(context);

		listID = getIntent().getExtras().getInt("LISTID");
		users = User.getUsersForList(context, listID);
		Log.i(TAG, ""+users.size()+ " users in this list");

		// String listName = CustomList.getListName(context, listID);

		getSupportActionBar();
		setTitle("Manage List");
		adapter = new ListUsersAdapter(this, R.layout.generic_row, users, listID);
		setListAdapter(adapter);

	}

	public void removeSelected(View v) {
		removeSelected();
	}

	public void removeSelected() {
		selected = ((ListUsersAdapter) adapter).getSelected();

		if (selected.size() == 0) {
			UIUtil.showMessage(context, "No users selected.");
		} else {
			User.removeFromList(context, selected, listID);
			String pluralizer = "User";
			if (selected.size() > 1)
				pluralizer += "s";
			UIUtil.showMessage(context, pluralizer + " removed.");
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.manage_list_users_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == android.R.id.home) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_invite) {
			intent = new Intent(this, InviteActivity.class);
			intent.putExtra("listID", listID);
			startActivity(intent);
			return true;
		} else {
			return false;
		}
	}

}
