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
		item = extras.getParcelable("Item");

		db = new DBHelper(this);
		db.open();

		prevItem = db.getItem(item.getPrev());
		nextItem = db.getItem(item.getNext());

		name.setText(item.getName());
		quantity.setText("" + item.getQuantity());
		
		String creator = db.getUserNameByID(item.getCreator());
		creation_details.setText("Added on " + item.getCreationDate() + " by "
				+ creator);

		if (item.getAssignee() > 0) {
			assignee.setText(db.getUserByID(item.getAssignee()).getName());
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
		//item.assignTo(userID); -- Don't do this here. Do on save
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
		Toast.makeText(this, "TODO; saveItem method in ItemActivity.java",
				Toast.LENGTH_LONG).show();

		// just gather the item details, open the db, use the updateItem method,
		// close the db.

	}

	public void deleteItem(View v) {
		Toast.makeText(this, "TODO; deleteItem method in ItemActivity.java",
				Toast.LENGTH_LONG).show();

		// just gather the item ID, open the db, use the deleteItem method, reset wiring for linked list,
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
