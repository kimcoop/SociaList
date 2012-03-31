package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.DBHelper;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.ItemAdapter;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

public class InsideListActivity extends ListActivity {

	private CustomList list = null;
	private ArrayList<Item> items = null;
	
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

		Intent i = getIntent();
		Bundle extras = i.getExtras();
		db = new DBHelper(this);
		db.open();
		list = db.getListByID(extras.getInt("ListID"));
		items = db.getItemsForListByID(extras.getInt("ListID"));
		totalItems = items.size();
		
		Log.e("LIST ID", " List is " +list.getName());
		db.close();

		ArrayAdapter<Item> adapter = new ItemAdapter(this, R.layout.item_row,
				items, assign_button, complete_button, invite_button, inviteUp);

		final ListView lv = getListView();

		View header = getLayoutInflater().inflate(R.layout.header, null);
		lv.addHeaderView(header);
		TextView label_header = (TextView) findViewById(R.id.label_header);
		label_header.setText("Viewing " + list.getName());

		lv.setTextFilterEnabled(true);
		lv.setClickable(true);
		setListAdapter(adapter);

	}// end onCreate
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){ // force refresh the view
    		startActivity(getIntent()); 
    		finish();       
    	}
    }

	@Override
	public void onBackPressed() {

		db.close();
		Intent in = new Intent();
	    setResult(0,in);//Requestcode 1. Tell parent activity to refresh items.
	    finish();
		super.onBackPressed();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		complete_button.setSelected(false);
		Item item = (Item) this.getListAdapter().getItem(position - 1);
		
		db.open();
		for (Item i : items) {		// this stops the weird jumping when a list item is clicked
			i.setSelected(false);
			db.updateItem(i);
		}
		
		if (items.size() ==1 ) { // if there is only one item in the list, it doesn't link prev and next correctly 
			item.setPrev(item.getID());					// long term TODO, but for now, fix it here
			item.setNext(item.getID());
			db.updateItem(item);
		}
		
		db.close();

		Log.d("PASSING ITEM", "Item " +item.getName()+". ID passing as " + item.getID());

		Intent intent = new Intent(getBaseContext(), ItemActivity.class);
		intent.putExtra("ItemID", item.getID()); 
		intent.putExtra("pos", items.indexOf(item)+1);
		intent.putExtra("totalItems", totalItems);
        startActivityForResult(intent, 1);
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

		((ItemAdapter) getListAdapter()).notifyDataSetChanged();

		db.close();
	}

	public void assignItems(View v) {
		// Grab users from the db. Alert Dialog to display all of them.
		db.open();
		final CharSequence[] users = db.getUsersForDialog();
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
		complete_button.setSelected(false);
		((ItemAdapter) getListAdapter()).notifyDataSetChanged();
	}
	
	public void inviteToList(View v) {

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		Intent intent = new Intent(getBaseContext(), InviteActivity.class);
		intent.putExtra("ListID", extras.getInt("ListID"));
		Log.i("INVITE TO", "ListID: " + extras.getInt("ListID"));
		startActivity(intent);

	}
	

}