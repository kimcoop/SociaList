package edu.pitt.cs1635group3.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.User;

public class ManageListUsersActivity extends SherlockActivity {

	private Context context;
	private int listID;
	private CustomList list;
	private static String TAG = "ManageListUsersActivity";
	private static int userID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_list_users);
		context = this;

		int uID = User.getCurrUser(context);
		Log.i(TAG, "User ID fetched: " +uID);	

		listID = getIntent().getExtras().getInt("listID");

		getSupportActionBar();
		setTitle("List: " + listID);/*
									 * 
									 * ListAdapter adapter = new
									 * SimpleAdapter(this, mylist,
									 * R.layout.list_row, new String[] { "name",
									 * "assignee" }, new int[] {
									 * R.id.element_title, R.id.element_subtitle
									 * });
									 * 
									 * setListAdapter(adapter);
									 */

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
