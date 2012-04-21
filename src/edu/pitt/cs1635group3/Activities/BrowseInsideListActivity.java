package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;
import java.util.HashMap;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONCustomList;
import zebrafish.util.JSONItem;
import zebrafish.util.JSONfunctions;
import zebrafish.util.UIUtil;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import edu.pitt.cs1635group3.Adapters.BrowseItemAdapter;
import edu.pitt.cs1635group3.Adapters.InviteAdapter;
import edu.pitt.cs1635group3.Adapters.ItemAdapter;

public class BrowseInsideListActivity extends SherlockListActivity {
	
	public ProgressDialog pd;
	private static DBHelper db;
	public boolean PUSH_TO_CLOUD = true;
	private String listName, CID;
	private int newListPK;
	private ArrayList<HashMap<String, String>> mylist;
	
	private CustomList list = null;
	private ArrayList<Item> items = null;
	private ArrayAdapter<Item> adapter;
	private RelativeLayout parentLayout;

	private static final String TAG = "BrowseInsideListActivity";

	private int totalItems;
	protected Context context;

	protected Button addButton;
	private View buttonsHelper;
	private ListView lv;
	private boolean newItems = false;
	private static int userID;
	private ArrayList<Integer> newItemPKs;

	private CharSequence users[];
	private ArrayList<Item> selected;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_insidelist_layout);
		context = this;
		userID = User.getCurrUser(context);
		parentLayout = (RelativeLayout) findViewById(R.id.insidelist_parent); // used for deletions
		newItemPKs = new ArrayList<Integer>(); // used for additions

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		list = CustomList.getListByID(context, extras.getInt("ListID"));
		items = CustomList
				.getItemsForListByID(context, extras.getInt("ListID"));
		totalItems = items.size();

		getSupportActionBar();
		setTitle(list.getName());

		selected = new ArrayList<Item>(); // track the items checked. used for button click events

		addButton = (Button) findViewById(R.id.add_button);
		addButton.setOnClickListener(new Button.OnClickListener() {  
			public void onClick(View v) {
				//assign();
				//addList();
				getNewListName();
			}
		});
		
		buttonsHelper = (View) findViewById(R.id.buttons_helper);
		lv = getListView();

		users = User.getUsersForDialog(context, list.getID());

		adapter = new BrowseItemAdapter(this, R.layout.item_row, items);
		lv.setTextFilterEnabled(true);
		lv.setClickable(true);
		setListAdapter(adapter);
		
		

	}// end onCreate

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) { // force refresh the view
			startActivity(getIntent());
			finish();
		}
	}
	public void getNewListName(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("New List Name");

		final EditText input = new EditText(this); // Set an EditText view to
													// get user input
		input.setText(list.getName());
		
		
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				listName = input.getText().toString();

				
				addList();
				

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

	public void addList(){
		int userID = User.getCurrUser(context);

		CustomList newList = new CustomList();
		newList.setCreator(userID);
		newList.setName(listName);
		
		pd = ProgressDialog.show(context , null ,
				  "Creating new list.." , true,
				  false);
		// get ID better
		db = new DBHelper(this);
		db.open();

		newListPK = JSONCustomList.getListPK(); // get a truly unique ID from
												// server
		newList.setID(newListPK);

		Item newItem;
		Log.v(TAG,"list.size():"+list.getItems().size() );
		for(int i=0; i<items.size(); i++){
			newItem = new Item(items.get(i).getName());
			newItem.setParent(context, newListPK);
			int itemID = JSONItem.getItemPK();
			newItem.setID(context, itemID);
			newList.addItem(newItem);
			Log.v(TAG,"NewItem"+i+": "+items.get(i).getName()+"["+itemID+"]");
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
		pd.dismiss();
		finish();
		

	}
	
	

	@Override
	public void onBackPressed() {

		if (newItems) {
			list.attachItems(items);
			Log.i(TAG, "Back button pressed");
			int listSize = list.getItems().size();
			Log.d(TAG, "listSize = " + listSize);
			list.setLinks(context);
		}
		Intent in = new Intent();
		setResult(1, in);// Requestcode 1. Tell parent activity to refresh
		// items.

		finish();
		super.onBackPressed();
	}

	

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.browse_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == 0) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_add_to_my_list) {
			//addItem();
			getNewListName();
			return false;
		} else {
			return false;
		}
	}



}