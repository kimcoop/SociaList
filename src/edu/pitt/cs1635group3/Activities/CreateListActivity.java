package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.DBHelper;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class CreateListActivity extends ListActivity {

	private ArrayList<HashMap<String, String>> mylist;
	private EditText listNameSpace;
	private String listName;
	
	int tempListID = 40;	//These will change when we start
	int tempItemID = 40;	//Pulling IDs from the MySQL
	int tempCreatorID = 32;	//Database
	
	DBHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editlist);

		listNameSpace = (EditText) findViewById(R.id.edit_list_name);
		//listNameSpace.setText("List name here");

		mylist = new ArrayList<HashMap<String, String>>();

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.list_row, new String[] { "name", "assignee" },
				new int[] { R.id.element_title, R.id.element_subtitle }); // not
																			// proper
																			// use
																			// of
																			// list_row.xml,
																			// but
																			// works

		final ListView lv = getListView();
		setListAdapter(adapter);

	}

	public void addListItem(View v) {

		// Whenever the list gets refreshed, the other layout pieces (List name)
		// get refreshed too.
		// Store the user's inputted list name prior to list refresh. Restore it
		// after the list refresh.
		listName = listNameSpace.getText().toString().trim();

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

				listNameSpace = (EditText) findViewById(R.id.edit_list_name); // redeclare
																				// this
																				// because
																				// onCreate
																				// is
																				// called
																				// again
																				// for
																				// this
																				// refresh
				// Toast.makeText(getBaseContext(),
				// "original list name is "+listName,
				// Toast.LENGTH_SHORT).show();
				listNameSpace.setText(listName); // restore list name

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
			//listNameSpace.setHint("Please enter a list name");
		} else { // allow save

			CustomList newList = new CustomList();
			newList.setCreator(tempCreatorID);					
			newList.setName(listName);
			// TODO: Get the next valid ID from database to assign to list. For
			// now, use ID = 40. (Query the whole table, retrieve last row,
			// increment index).
			newList.setID(tempListID);
			tempListID++;

			Item newItem;
			for (HashMap<String, String> map : mylist) {
				newItem = new Item(map);
				//newItem = new Item();
				//newItem.setName(map.getKey(0));
				Log.i("CreateListActivity", "Hashmap exists");
				newItem.setParent(newList.getID());
				newItem.setID(tempItemID); // FIX. same problem as above
				tempItemID++;
				newList.addItem(newItem);
			}
			db = new DBHelper(this);
			db.open();
			db.insertList(newList);
			Item itemA, itemB, itemC;
			if(newList.getItems() != null){
				int listSize = newList.getItems().size();
				for (int i = 0; i < listSize; i++) { // make linked list
					
					if(listSize-1 == i){
						//At the last index
						itemA = newList.getItem(i);
						itemB = newList.getItem(0);
						//itemC = newList.getItem(1);
						
						itemA.setNext(itemB.getID());
						//itemB.setPrev(itemA.getID());
						//itemB.setNext(itemC.getID());
						Log.d("CreateListActivity", "ItemA's next is" + newList.getItem(0).getID());
						Log.d("CreateListActivity", "ItemB's prev is" + itemA.getID());
						
						//db.insertItem(itemB);
					}
					else if(listSize > 1 && i == 0){
						//Two or more items in the list, insert the first two
						itemA = newList.getItem(i);
						itemB = newList.getItem(i + 1);
						itemC = newList.getItem(listSize-1);
						itemA.setNext(itemB.getID());
						itemA.setPrev(itemC.getID());
						itemB.setPrev(itemA.getID());
						
						Log.d("CreateListActivity", "ItemA's next is" + itemB.getID());
						Log.d("CreateListActivity", "ItemB's prev is" + itemA.getID());
					}
					else if(listSize > 1 && i < listSize){
						//Two or more items in the list
						itemA = newList.getItem(i);
						itemB = newList.getItem(i + 1);
						itemA.setNext(itemB.getID());
						itemB.setPrev(itemA.getID());
						
						Log.d("CreateListActivity", "ItemA's next is" + itemB.getID());
						Log.d("CreateListActivity", "ItemB's prev is" + itemA.getID());
					}
					
					else{
						//Only one item in the list
						itemA = newList.getItem(0);
						itemA.setNext(itemA.getID());
						itemA.setPrev(itemA.getID());
						Log.d("CreateListActivity", "ItemA's next is" + itemA.getID());
						Log.d("CreateListActivity", "ItemA's prev is" + itemA.getID());
					}
					db.insertItem(itemA);
				}
			}
			db.close();
			Toast.makeText(this, "List Created!", Toast.LENGTH_SHORT).show();
			Intent in = new Intent();
		    setResult(1,in);//Requestcode 1. Tell parent activity to refresh items.
		    finish();

		}

	}

	public void deleteList(View v) {
		Toast.makeText(getBaseContext(), "TODO: delete list",
				Toast.LENGTH_SHORT).show();

		// Question - destroy list only for user? Or for every user who shares
		// it?

	}
}