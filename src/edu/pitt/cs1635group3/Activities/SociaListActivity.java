package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONfunctions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.CustomListAdapter;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.User;

public class SociaListActivity extends SherlockListActivity { // ListActivity
	private int activeListPosition;
	private ArrayList<CustomList> lists = null;
	private ArrayList<User> users = null;
	private DBHelper db;
	private RelativeLayout parentLayout;
	private ArrayAdapter<CustomList> adapter;

	private Context context;
	private static final String TAG = "SociaListActivity";
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaceholder);
		context = this;
		getSupportActionBar();
		setTitle("Lists");

		db = new DBHelper(this);
		db.open();
		lists = db.getAllLists(); // TODO: for user ID...
		users = db.getAllUsers();
		db.close();

		adapter = new CustomListAdapter(this, R.layout.list_row, lists);
		parentLayout = (RelativeLayout) findViewById(R.id.userlists_layout);
		lv = getListView();

		setListAdapter(adapter);
		lv.setClickable(true);
		lv.setTextFilterEnabled(true);

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int pos, long id) {
				final View parentView = v;
				final int position = pos;
				final CustomList userlist = lists.get(position);
				final String listname = userlist.getName();

				Log.i(TAG, "List name is " + listname + " and ID is "
						+ userlist.getID());
				// parentView.getBackground().setColorFilter(Color.parseColor("#323331"),
				// Mode.DARKEN);

				final Button b = (Button) v
						.findViewById(R.id.delete_list_button);
				b.setVisibility(View.VISIBLE);

				b.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						db.open();
						db.deleteListAndChildren(userlist);
						db.close();

						Toast.makeText(getBaseContext(), "List deleted.",
								Toast.LENGTH_SHORT).show();
						b.setVisibility(View.INVISIBLE);
						parentLayout.removeView(parentView);
						adapter.remove(userlist);
						adapter.notifyDataSetChanged();

					}
				});

				return true;
			}
		});

	} // end onCreate

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		CustomList list = adapter.getItem(position);

		Intent intent = new Intent(this, InsideListActivity.class);
		intent.putExtra("ListID", list.getID());
		startActivityForResult(intent, 0);
	}

	public void createNewList(View v) {

		Intent intent = new Intent(SociaListActivity.this,
				CreateListActivity.class);
		startActivityForResult(intent, 0);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1 || resultCode == 0) { // force refresh the view
			adapter.notifyDataSetChanged();
		} else if (resultCode == 2) { // coming from ItemActivity, where we have
										// deleted the last item and user wants
										// to remove the list.

			Toast.makeText(this, "Item and list deleted.", Toast.LENGTH_SHORT)
					.show();
			startActivity(getIntent()); // force refresh
			finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home_lists_menu, menu); // todo - alter the menu
														// (make new)

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_add:
			intent = new Intent(this, CreateListActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_refresh:
			lists = JSONfunctions.getRefreshLists(context); // TODO
			adapter.notifyDataSetChanged();
		default:
			return false;
		}
	}

}