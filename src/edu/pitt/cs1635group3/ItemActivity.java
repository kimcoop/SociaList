package edu.pitt.cs1635group3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

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
		
		Item item = extras.getParcelable("Item"); 
		
        name.setText(item.getName());
        quantity.setText(""+item.getQuantity());
        creation_details.setText("Added on "+item.getCreationDate()+" by " +item.getCreator());
        assignee.setText(item.getAssignee());
        notes.setText(item.getNotes());
	}
	
	
}
