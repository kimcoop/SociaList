package edu.pitt.cs1635group3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;

public class ItemActivity extends Activity {

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
		//TODO grab this data from server or intent (if possible to do the latter, that'd be ideal)
		String item_name, item_creation_date = "19 March", item_creator = "Kim", item_assignee = "", item_notes = "Good sale on this at Target!";
		int item_quantity = 1; // by default
		
		item_name = extras.getString("name");
		
        name.setText(item_name);
        quantity.setText(""+item_quantity);
        creation_details.setText("Added on "+item_creation_date+" by " +item_creator);
        assignee.setText(item_assignee);
        notes.setText(item_notes);
	}
	
	
}
