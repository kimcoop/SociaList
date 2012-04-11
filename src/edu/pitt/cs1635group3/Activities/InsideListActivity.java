package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.DBHelper;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.ItemAdapter;
import edu.pitt.cs1635group3.R;

public class InsideListActivity extends SherlockListActivity {

	private CustomList list = null;
	private ArrayList<Item> items = null;
	private ArrayAdapter<Item> adapter;
	private RelativeLayout parentLayout;

	private int totalItems;

	private DBHelper db;

	private Button assign_button, complete_button, invite_button;
	private View buttons_helper;
	private ListView lv;
	private boolean inviteUp = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insidelist_layout);

		assign_button = (Button) findViewById(R.id.assign_button);
		complete_button = (Button) findViewById(R.id.complete_button);
		invite_button = (Button) findViewById(R.id.invite_button);
		buttons_helper = (View) findViewById(R.id.buttons_helper);
		lv = getListView();

		parentLayout = (RelativeLayout) findViewById(R.id.insidelist_parent);

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		db = new DBHelper(this);
		db.open();
		list = db.getListByID(extras.getInt("ListID"));
		items = db.getItemsForListByID(extras.getInt("ListID"));
		totalItems = items.size();
		db.close();

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
					db.open(); // if there is only one item in the list, it
								// doesn't link prev and next correctly
					item.setPrev(item.getID());
					item.setNext(item.getID());
					db.updateItem(item, false);
					db.close();
				}

				Intent intent = new Intent(getBaseContext(), ItemActivity.class);
				intent.putExtra("ItemID", item.getID());

				intent.putExtra("pos", pos+1); // this is used for displaying
												// "Item X of Y" in the header,
												// so leave it as pos
				intent.putExtra("totalItems", totalItems);
				startActivityForResult(intent, 1);
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                    int pos, long id) {
            	final View parentView = v;
            	final int position = pos-1;
    			final Item item = items.get(position);
    			final String itemName = item.getName();
    			
    			Log.i("LONG PRESS", "Item name is " +itemName+ " and ID is " +item.getID() + " and prev is " +item.getPrev() + " and next is " +item.getNext());
    			//parentView.getBackground().setColorFilter(Color.parseColor("#323331"), Mode.DARKEN);
				// parentView.getBackground().setColorFilter(Color.parseColor("#323331"),
				// Mode.DARKEN);

				final Button b = (Button) parentView
						.findViewById(R.id.delete_item_button);
				b.setVisibility(View.VISIBLE);

				b.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						
						totalItems--;
						
						db.open();
						
						int prevPos, nextPos;
						if (position==items.size()-1) nextPos = 0;
						else nextPos = position+1;
						
						if (position==0) prevPos = items.size()-1;
						else prevPos = position-1;
						
						Item nextItem, prevItem;
						nextItem = items.get(nextPos);
						prevItem = items.get(prevPos);
						
						int next = nextItem.getID();
						int prev = prevItem.getID();
						
						prevItem.setNext(next);
						nextItem.setPrev(prev);
						
						
						if (totalItems==1) { // always this issue with one item in list
							item.setNext(item.getID());
							item.setPrev(item.getID());
						}

						db.updateItem(prevItem);
						db.updateItem(nextItem);
						db.deleteItem(item);
						
						Log.i("PREV ITEM", "Prev item has next item " +nextItem.getName());
						Log.i("NEXT ITEM", "NExt item has prev item " +prevItem.getName());
						
						db.close();

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
		db.open();
		list.setName(newName);
		db.updateList(list);
		db.close();
		setTitle(newName); // change the UI
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

		db.open();

		int userID = db.getUserByName(user);

		for (Item item : items) {
			if (item.isSelected()) {
				item.assignTo(userID);
				item.setSelected(false);
				db.updateItem(item);
			}
		}

		adapter.notifyDataSetChanged();

		db.close();
	}

	public void assignItems(View v) {
		// Grab users from the db. Alert Dialog to display all of them.
		db.open();
		final CharSequence[] users = db.getUsersForDialog(); // todo - instead
																// of querying
																// every time,
																// cache in
																// activity
																// onCreate
																// method
		db.close();

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
		db.open();

		for (Item item : items) {
			if (item.isSelected()) {
				item.setCompleted();
				item.setSelected(false);
				db.updateItem(item);
			}
		}
		db.close();
		adapter.notifyDataSetChanged();
		complete_button.setSelected(false);
	}

	public void inviteToList(View v) {

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		Intent intent = new Intent(getBaseContext(), InviteActivity.class);
		intent.putExtra("ListID", extras.getInt("ListID"));
		Log.i("INVITE TO", "ListID: " + extras.getInt("ListID"));
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
		switch (item.getItemId()) {
		case R.id.menu_add:
			//TODO: add list item popup
			return false;
		case R.id.menu_rename:
			rename();
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