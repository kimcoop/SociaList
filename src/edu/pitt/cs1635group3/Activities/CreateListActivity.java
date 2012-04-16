package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;
import java.util.HashMap;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONfunctions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class CreateListActivity extends SherlockListActivity {

	private ArrayList<HashMap<String, String>> mylist;
	private EditText listNameSpace, CIDSpace;
	private String listName, CID;
	public boolean PUSH_TO_CLOUD = true;

	private int newListPK;
	private ArrayList<Integer> newItemPKs; // track the new slices of the web
											// servers we allocate (in case of
											// cancel)

	private static Context context;
	private static String TAG = "CreateListActivity";

	private static DBHelper db;
	private static int userID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editlist);
		context = this;

		int uID = User.getCurrUser(context);
		Log.i(TAG, "User ID fetched: " + uID);

		getSupportActionBar();
		setTitle("New List");

		listNameSpace = (EditText) findViewById(R.id.edit_list_name);
		CIDSpace = (EditText) findViewById(R.id.edit_list_CID);

		mylist = new ArrayList<HashMap<String, String>>();
		newListPK = -1;
		newItemPKs = new ArrayList<Integer>();

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.list_row, new String[] { "name", "assignee" },
				new int[] { R.id.element_title, R.id.element_subtitle });

		setListAdapter(adapter);

	}

	public void addListItem() {

		/*
		 * ROB - when you're working on this, may want to consider moving this
		 * function into the List class? Only since it's called at least twice
		 * in our app (creation of new list, editing list). I don't think it
		 * would be hard, just pass the Activity context as a param to the
		 * method in the class.
		 */

		// Whenever the list gets refreshed, the other layout pieces (List name)
		// get refreshed too.
		// Store the user's inputted list name prior to list refresh. Restore it
		// after the list refresh.
		listName = listNameSpace.getText().toString().trim();
		CID = CIDSpace.getText().toString().trim();

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("New List Item Name");

		final EditText input = new EditText(this); // Set an EditText view to
													// get user input
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();

				HashMap<String, String> newItem = new HashMap<String, String>();
				newItem.put("name", value);
				newItem.put("assignee", "");
				mylist.add(newItem); // append the new item to our list of items
										// (can be saved when saveList() is
										// called)
				((SimpleAdapter) getListView().getAdapter())
						.notifyDataSetChanged();
				setContentView(R.layout.editlist);

				listNameSpace = (EditText) findViewById(R.id.edit_list_name);
				listNameSpace.setText(listName); // restore list name
				CIDSpace = (EditText) findViewById(R.id.edit_list_CID);
				CIDSpace.setText(CID); // restore list name

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

	public void saveList(View v) {

		listName = listNameSpace.getText().toString();
		if (listName.equals("") || listName.equals("Please enter a list name")
				|| listName.equals("List name here")) {
			// invalid name. don't allow save.
			listNameSpace.setBackgroundColor(Color.parseColor("red"));

			CID = CIDSpace.getText().toString();

		} else { // allow save
			int userID = User.getCurrUser(context);

			CustomList newList = new CustomList();
			newList.setCreator(userID);
			newList.setCustomID(CID);
			newList.setName(listName);

			// get ID better
			db = new DBHelper(this);
			db.open();

			newListPK = JSONfunctions.getListPK(); // get a truly unique ID from
													// server
			newList.setID(newListPK);

			Item newItem;
			for (HashMap<String, String> map : mylist) {
				newItem = new Item(map);
				newItem.setParent(context, newListPK);

				int itemID = JSONfunctions.getItemPK();
				newItemPKs.add(itemID);
				newItem.setID(context, itemID);
				newList.addItem(newItem);
			}

			db.insertList(newList, PUSH_TO_CLOUD);
			db.close();

			Item itemA, itemB, itemC;
			if (newList.getItems() != null) {
				int listSize = newList.getItems().size();
				for (int i = 0; i < listSize; i++) { // make linked list

					if (listSize - 1 == i) {
						// At the last index
						itemA = newList.getItem(i);
						itemB = newList.getItem(0);
						itemA.setNext(itemB.getID());

					} else if (listSize > 1 && i == 0) {
						// Two or more items in the list, insert the first two
						itemA = newList.getItem(i);
						itemB = newList.getItem(i + 1);
						itemC = newList.getItem(listSize - 1);
						itemA.setNext(itemB.getID());
						itemA.setPrev(itemC.getID());
						itemB.setPrev(itemA.getID());

					} else if (listSize > 1 && i < listSize) {
						// Two or more items in the list
						itemA = newList.getItem(i);
						itemB = newList.getItem(i + 1);
						itemA.setNext(itemB.getID());
						itemB.setPrev(itemA.getID());
					}

					else {
						// Only one item in the list
						itemA = newList.getItem(0);
						itemA.setNext(itemA.getID());
						itemA.setPrev(itemA.getID());
					}
					Item.insertOrUpdateItems(context, newList.getItems(),
							PUSH_TO_CLOUD);
				}
			}
			Toast.makeText(this, "List Created!", Toast.LENGTH_SHORT).show();
			Intent in = new Intent();
			setResult(1, in);// Requestcode 1. Tell parent activity to refresh
								// items.
			finish();

		}

	}

	public void explainCID(View v) {

		new AlertDialog.Builder(this)
				.setTitle("Custom List IDs")
				.setMessage(getString(R.string.CID_explain))
				.setPositiveButton("Got it",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).show();
	}

	public void cancelNewList(View v) {

		// if this button is clicked, then the list wasn't saved to the user's
		// device;
		// all that happened was we got space on the web server for the list and
		// its items.

		JSONfunctions.deleteList(newListPK);

		for (int i : newItemPKs) { // delete the items as well
			JSONfunctions.deleteItem(i);
		}

		finish();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.new_list_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == R.id.menu_add) {
			addListItem();
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