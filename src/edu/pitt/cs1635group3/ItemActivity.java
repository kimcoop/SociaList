package edu.pitt.cs1635group3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ItemActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
 
        TextView name, quantity, creation_date, creator, assignee, notes;
		name = (TextView) findViewById(R.id.item_name);
		quantity = (TextView) findViewById(R.id.item_quantity);
		creation_date = (TextView) findViewById(R.id.item_creation_date);
		creator = (TextView) findViewById(R.id.item_creator);
		assignee = (TextView) findViewById(R.id.item_assignee);
		notes = (TextView) findViewById(R.id.item_notes);
        
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String item_name, item_creation_date = "", item_creator = "", item_assignee = "", item_notes = "";
		int item_quantity = 1; // by default
		
		item_name = extras.getString("name");
		
        name.setText("This data was pulled from intent: "+item_name);
        quantity.setText("Quantity: "+item_quantity);
        creation_date.setText("Added on: "+item_creation_date);
        creator.setText("Added by: " +item_creator);
        assignee.setText("Assigned to: "+item_assignee);
        notes.setText("Notes: "+item_notes);
	}
	
	
}
