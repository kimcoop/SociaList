package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import zebrafish.util.DBHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.ItemAdapter;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.User;

public class InsideListActivity extends SherlockListActivity {

	private CustomList list = null;
	private ArrayList<Item> items = null;
	private ArrayAdapter<Item> adapter;
	private RelativeLayout parentLayout;

	private static final String TAG = "InsideListActivity";

	private int totalItems;

	private DBHelper db;
	protected Context context;

	private Button assign_button, complete_button, invite_button;
	private View buttons_helper;
	private ListView lv;
	private boolean inviteUp = true;

	CharSequence users[];
	protected ArrayList<Item> selectedItems;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insidelist_layout);
		context = this;

		selectedItems = new ArrayList<Item>(); // track the items checked (not
												// used yet)

		assign_button = (Button) findViewById(R.id.assign_button);
		complete_button = (Button) findViewById(R.id.complete_button);
		invite_button = (Button) findViewById(R.id.invite_button);
		buttons_helper = (View) findViewById(R.id.buttons_helper);
		lv = getListView();

		parentLayout = (RelativeLayout) findViewById(R.id.insidelist_parent);

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		list = CustomList.getListByID(context, extras.getInt("ListID"));
		items = CustomList
				.getItemsForListByID(context, extras.getInt("ListID"));
		totalItems = items.size();

		users = User.getUsersForDialog(context, list.getID());

		adapter = new ItemAdapter(this, R.layout.item_row, items,
				assign_button, complete_button, invite_button, inviteUp);

		getSupportActionBar();
		setTitle(list.getName());

		lv.setTextFilterEnabled(true);
		lv.setClickable(true);
		setListAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				complete_button.setSelected(false);
				Item item = items.get(pos);

				if (items.size() == 1) {
					item.setPrev(context, item.getID());
					item.setNext(context, item.getID());
				}

				Intent intent = new Intent(context, ItemActivity.class);
				intent.putExtra("ItemID", item.getID());

				intent.putExtra("pos", pos + 1); // this is used for displaying
													// "Item X of Y" in the
													// header,
													// so leave it as pos
				intent.putExtra("totalItems", totalItems);
				startActivityForResult(intent, 1);
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int pos, long id) {
				final View parentView = v;
				final int position = pos;
				final Item item = items.get(position);
				final String itemName = item.getName();

				// Log.i(TAG, "Item name is " +itemName+ " and ID is "
				// +item.getID() + " and prev is " +item.getPrev() +
				// " and next is " +item.getNext());

				final Button b = (Button) parentView
						.findViewById(R.id.delete_item_button);
				b.setVisibility(View.VISIBLE);

				b.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						totalItems--;

						db.open();

						int prevPos, nextPos;
						if (position == items.size() - 1)
							nextPos = 0;
						else
							nextPos = position + 1;

						if (position == 0)
							prevPos = items.size() - 1;
						else
							prevPos = position - 1;

						Item nextItem, prevItem;
						nextItem = items.get(nextPos);
						prevItem = items.get(prevPos);

						int next = nextItem.getID();
						int prev = prevItem.getID();

						prevItem.setNext(context, next);
						nextItem.setPrev(context, prev);

						if (totalItems == 1) { // always this issue with one
												// item in list
							item.setNext(context, item.getID());
							item.setPrev(context, item.getID());
						}

						db.deleteItem(item); // todo - can we do this inside
												// Item.java?
						db.close();

						Log.i(TAG,
								"Prev item has next item " + nextItem.getName());
						Log.i(TAG,
								"NExt item has prev item " + prevItem.getName());

						Toast.makeText(getBaseContext(),
								"Item " + itemName + " deleted.",
								Toast.LENGTH_SHORT).show();
						b.setVisibility(View.GONE);
						parentLayout.removeView(parentView);
						adapter.remove(item);
						adapter.notifyDataSetChanged();

					}
				});

				return true;
			}
		});

	}// end onCreate

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) { // force refresh the view
			startActivity(getIntent());
			finish();
		}
	}

	public void saveRename(String newName) {

		list.updateName(context, newName);
		setTitle(newName);

	}

	public void rename() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Rename List");

		final EditText input = new EditText(this); // Set an EditText view to
													// get user input
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				saveRename(value);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();

	}

	@Override
	public void onBackPressed() {

		Intent in = new Intent();
		setResult(0, in);// Requestcode 1. Tell parent activity to refresh
							// items.
		finish();
		super.onBackPressed();
	}

	public void assignItemsTo(String user) {

		int userID = User.getUserByName(context, user);

		for (Item item : items) {
			if (item.isSelected()) {
				item.assignTo(context, userID);
				item.setSelected(context, false);
			}
		}

		adapter.notifyDataSetChanged();
	}

	public void assignItems(View v) {
		// Grab users from the db. Alert Dialog to display all of them.

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Assign To");
		builder.setItems(users, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pos) {
				assignItemsTo((String) users[pos]);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void uncompleteItems(View v) {

	}

	public void completeItems(View v) {

		for (Item item : items) {
			if (item.isSelected()) {
				item.setCompleted(context);
				item.setSelected(context, false);
			}
		}
		adapter.notifyDataSetChanged();
		complete_button.setSelected(false);
	}

	public void inviteToList(View v) {

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		Intent intent = new Intent(getBaseContext(), InviteActivity.class);
		intent.putExtra("ListID", extras.getInt("ListID"));
		Log.i(TAG, "ListID: " + extras.getInt("ListID"));
		startActivity(intent);

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
		if (item.getItemId() == 0) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_add) {
			// TODO: add list item popup
			return false;
		} else if (item.getItemId() == R.id.menu_rename) {
			rename();
			return true;
		} else if (item.getItemId() == R.id.menu_manage_users) {
			intent = new Intent(this, ManageListUsersActivity.class);
			intent.putExtra("listID", list.getID());
			startActivity(intent);
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