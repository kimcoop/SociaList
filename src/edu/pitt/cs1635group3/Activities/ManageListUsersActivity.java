package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.R;

public class ManageListUsersActivity extends SherlockActivity {
	
	private Context context;
	private int listID;
	private CustomList list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_list_users);
		context = this;
		
		listID = getIntent().getExtras().getInt("listID");

		getSupportActionBar();
		setTitle("List: "+listID);/*

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.list_row, new String[] { "name", "assignee" },
				new int[] { R.id.element_title, R.id.element_subtitle });

		setListAdapter(adapter);*/

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
		switch (item.getItemId()) {
		case R.id.menu_add:
			//TODO: add user item popup
			return false;
		case R.id.menu_rename:
			//rename();
			return true;
		case R.id.menu_invite:
			intent = new Intent(this, InviteActivity.class);
			startActivity(intent);
			return true;

		default:
			return false;
		}
	}

}
