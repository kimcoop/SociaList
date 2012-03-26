package edu.pitt.cs1635group3;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class ItemActivity extends Activity {

	private Item item, prevItem, nextItem;
	private DBHelper db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);

		TextView name, quantity, creation_details, assignee, notes;
		name = (EditText) findViewById(R.id.item_name);
		quantity = (EditText) findViewById(R.id.item_quantity);
		creation_details = (TextView) findViewById(R.id.item_creation);
		assignee = (TextView) findViewById(R.id.item_assignee);
		notes = (EditText) findViewById(R.id.item_notes);

		Intent i = getIntent();
		Bundle extras = i.getExtras();
		int itemID = extras.getInt("ItemID");
		
		db = new DBHelper(this);
		db.open();
		
		item = db.getItem(itemID);
		
		Log.d("ITEM RECEIVED", "Item ID = "+itemID);
		
		//prevItem = db.getItem(item.getPrev());
		//nextItem = db.getItem(item.getNext());

		name.setText(item.getName());
		quantity.setText("" + item.getQuantity());

		String creator;
		if (item.getCreator() > 0) 
			creator = db.getUserNameByID(item.getCreator());
		else creator = "";
		creation_details.setText("Added on " + item.getCreationDate() + " by "
				+ creator);

		if (item.getAssignee() > 0) {
			Log.d("ASSIGNEE", item.getAssignee()+"");
			//assignee.setText(db.getUserByID(item.getAssignee()).getName());
		} else {
			assignee.setHint("Click to assign");
		}

		notes.setText(item.getNotes());

		db.close();

		assignee.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				selectAssignee(v);
			}
		});

	}/*
	 * Toast.makeText(this, "next", Toast.LENGTH_LONG).show();
	 */

	public void assignItemTo(String user) {

		int userID = db.getUserByName(user);
		TextView assignee = (TextView) findViewById(R.id.item_assignee);
		assignee.setText(user);
		assignee.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				selectAssignee(v);
			}
		});
		// item.assignTo(userID); -- Don't do this here. Do on save
		db.close();
	}

	public void selectAssignee(View v) {
		db.open();
		final CharSequence[] users = db.getUsersForDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Assign To");
		builder.setItems(users, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pos) {
				assignItemTo((String) users[pos]);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void saveItem(View v) {

		// regather the item details, open the db, use the updateItem method.

		TextView name, quantity, assignee, notes;
		name = (EditText) findViewById(R.id.item_name);
		quantity = (EditText) findViewById(R.id.item_quantity);
		assignee = (TextView) findViewById(R.id.item_assignee);
		notes = (EditText) findViewById(R.id.item_notes);

		db.open();
		item.setName(name.getText().toString().trim());
		item.setQuantity(Integer.parseInt(quantity.getText().toString().trim()));
		
		String rawAssignee = assignee.getText().toString().trim();
		int assigneeID;
		if (rawAssignee != "" && rawAssignee != null) {
			assigneeID = db.getUserByName(rawAssignee);
			item.assignTo(assigneeID);
		}
		
		db.updateItem(item);
		db.close();
		Toast.makeText(this, "Updated item " +item.getName(),
				Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		intent.putExtra("refresh", 1); // tell the InsideListActivity to refresh the list since we've changed it
		finish();

	}

	public void deleteItem(View v) {
		Toast.makeText(this, "TODO; deleteItem method in ItemActivity.java",
				Toast.LENGTH_LONG).show();

		// just gather the item ID, open the db, use the deleteItem method,
		// reset wiring for linked list,
		// close the db.
	}

	public void prevItem(View v) {

		Intent intent = new Intent(this, ItemActivity.class);
		intent.putExtra("Item", prevItem);
		startActivity(intent);
		finish();
	}

	public void nextItem(View v) {

		Intent intent = new Intent(this, ItemActivity.class);
		intent.putExtra("Item", nextItem);
		startActivity(intent);
		finish();
	}

}
