package edu.pitt.cs1635group3;

import java.util.Locale;

import android.app.Activity;
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
		
		DBHelper db = new DBHelper(this);
		db.open();
		
		prevItem = db.getItem(item.getPrev());
		nextItem = db.getItem(item.getNext());

		name.setText(item.getName());
		quantity.setText("" + item.getQuantity());
		creation_details.setText("Added on " + item.getCreationDate() + " by "
				+ item.getCreator());
		
		
		String assignedTo = (item.getAssignee() > 0? db.getUserByID(item.getAssignee()).getName() : "");
		
		assignee.setText(assignedTo); // set to name rather than userID, if the item has been assigned
		notes.setText(item.getNotes());
		
		db.close();
	}/*
	 * Toast.makeText(this, "next", Toast.LENGTH_LONG).show();
	 */

	public void saveItem(View v) {
		Toast.makeText(this, "TODO; saveItem method in ItemActivity.java",
				Toast.LENGTH_LONG).show();
		
		// just gather the item details, open the db, use the updateItem method, close the db.
		
	}

	public void deleteItem(View v) {
		Toast.makeText(this, "TODO; deleteItem method in ItemActivity.java",
				Toast.LENGTH_LONG).show();
		
		// just gather the item ID, open the db, use the deleteItem method, close the db.
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
