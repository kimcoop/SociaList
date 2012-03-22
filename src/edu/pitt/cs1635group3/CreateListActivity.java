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

public class CreateListActivity extends Activity {


	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.editlist);
	 
	        EditText listName = (EditText) findViewById(R.id.list_name);
			Toast.makeText(getBaseContext(), "Inside CreateListActivity", Toast.LENGTH_SHORT).show(); 

		
	}

}
