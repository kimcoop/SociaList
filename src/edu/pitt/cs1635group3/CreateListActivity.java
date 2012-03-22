package edu.pitt.cs1635group3;


import android.app.Activity;
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
			Toast.makeText(getBaseContext(), "TODO: add list item", Toast.LENGTH_SHORT).show(); 

	    }

	    public void saveList(View v) {
			Toast.makeText(getBaseContext(), "TODO: save list", Toast.LENGTH_SHORT).show(); 

	    }
	    
	    public void deleteList(View v) {
			Toast.makeText(getBaseContext(), "TODO: delete list", Toast.LENGTH_SHORT).show(); 

	    }
}
