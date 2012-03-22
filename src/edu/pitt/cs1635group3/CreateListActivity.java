package edu.pitt.cs1635group3;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class CreateListActivity extends Activity {


	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.editlist);
	 
	        EditText listName = (EditText) findViewById(R.id.list_name);
	        listName.setText("List name here");

		
	}
	    
	    public void addListItem(View v) {
	    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

	    	alert.setTitle("New list item name");

	    	// Set an EditText view to get user input 
	    	final EditText input = new EditText(this);
	    	alert.setView(input);

	    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {
	    		String value = input.getText().toString();
				Toast.makeText(getBaseContext(), "You added list item: "+value, Toast.LENGTH_LONG).show(); 
	    	  // Do something with value!
	    	  }
	    	});

	    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	  public void onClick(DialogInterface dialog, int whichButton) {
	    	    // Canceled.
	    	  }
	    	});

	    	alert.show();
	    }

	    public void saveList(View v) {
			Toast.makeText(getBaseContext(), "TODO: save list", Toast.LENGTH_SHORT).show(); 

	    }
	    
	    public void deleteList(View v) {
			Toast.makeText(getBaseContext(), "TODO: delete list", Toast.LENGTH_SHORT).show(); 

	    }
}
