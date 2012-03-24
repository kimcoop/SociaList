package edu.pitt.cs1635group3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

	CustomList list = null;
	ArrayList<Item> items = null;

	DBHelper db;

	Button assign_button;
	Button complete_button;
	Button invite_button;
	View buttons_helper;
	boolean inviteUp = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insidelist_layout);

		assign_button = (Button) findViewById(R.id.assign_button);
		complete_button = (Button) findViewById(R.id.complete_button);
		invite_button = (Button) findViewById(R.id.invite_button);
		buttons_helper = (View) findViewById(R.id.buttons_helper);

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		// Toast.makeText(this,"InsideListActivity onCreate. List id is "+extras.getInt("List_id"),
		// Toast.LENGTH_LONG).show();

		list = extras.getParcelable("List");
		// Toast.makeText(this,"List "+list.getNote(),
		// Toast.LENGTH_LONG).show();

		list.pullItems(); // pull the list's items from the server;
		items = list.getItems();

		db = new DBHelper(this);
		db.open();
		for (Item el : items) {
			db.insertItem(el);
			Log.i("ITEM INSERTION", "Inserted item with ID " + el.getID());
		}
		db.close();

		ArrayAdapter<Item> adapter = new ItemAdapter(this, R.layout.item_row,
				items);

		final ListView lv = getListView();

		View header = getLayoutInflater().inflate(R.layout.header, null);
		lv.addHeaderView(header);
		TextView label_header = (TextView) findViewById(R.id.label_header);
		label_header.setText("Viewing " + list.getName());

		lv.setTextFilterEnabled(true);
		lv.setClickable(true);
		setListAdapter(adapter);

	}// end onCreate

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Item item = (Item) this.getListAdapter().getItem(position - 1);

		Intent intent = new Intent(getBaseContext(), ItemActivity.class);
		intent.putExtra("Item", item); // can pass as object because it
										// implements Parcelable
		startActivity(intent);
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
		
		db.close();

		((ItemAdapter) getListAdapter()).notifyDataSetChanged();
	}

	public void assignItems(View v) {
		//Grab users from the db. Alert Dialog to display all of them.

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
				Toast.makeText(this,
						"ITEM marked as completed: " + item.getName(),
						Toast.LENGTH_LONG).show();
				db.updateItem(item);
			}
		}
		db.close();
		((ItemAdapter) getListAdapter()).notifyDataSetChanged();
	}

	public void flipButtons(View v) {

		if (inviteUp) {
			invite_button.setVisibility(View.GONE);
			complete_button.setVisibility(View.VISIBLE);
			assign_button.setVisibility(View.VISIBLE);
			buttons_helper.setVisibility(View.VISIBLE);
			inviteUp = false;
		} else {
			invite_button.setVisibility(View.VISIBLE);
			complete_button.setVisibility(View.GONE);
			assign_button.setVisibility(View.GONE);

			assign_button.setVisibility(View.VISIBLE); // remove this for final
														// product (testing now)
			buttons_helper.setVisibility(View.GONE);
			inviteUp = true;
		}
	}

}