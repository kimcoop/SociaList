package edu.pitt.cs1635group3;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class CreateListActivity extends ListActivity {

	ArrayList<HashMap<String, String>> mylist;
		
@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.editlist);
	 
	        EditText listName = (EditText) findViewById(R.id.list_name);
	        listName.setText("List name here");

	    	mylist = new ArrayList<HashMap<String, String>>();

	    	//Get the data (see above)
	    	JSONObject json = JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/items.json");

	    	       try{
	    		JSONArray  lists = json.getJSONArray("items");

	    	      	       	//Loop the Array
	    	        for(int i=0;i < lists.length()-1; i++){						

	    	        	HashMap<String, String> map = new HashMap<String, String>();
	    	        	JSONObject e = lists.getJSONObject(i);

	    	        	map.put("id",  String.valueOf(i));
	    	        	map.put("name", e.getString("name"));
	    	        	map.put("assignee", e.getString("assignee"));
	    	        	mylist.add(map);
	    		}
	    	       }catch(JSONException e)        {
	    	       	 Log.e("log_tag", "Error parsing data "+e.toString());
	    	       }
	    	       
	        ListAdapter adapter = new SimpleAdapter(this, mylist, R.layout.main,
	                new String[] { "name", "assignee" },
	                new int[] { R.id.item_title, R.id.item_subtitle });

			
			final ListView lv = getListView();
			setListAdapter(adapter);

		
	}
	    
	    public void addListItem(View v) {
	    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

	    	alert.setTitle("New List Item Name");

	    	// Set an EditText view to get user input 
	    	final EditText input = new EditText(this);
	    	alert.setView(input);

	    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    	@SuppressWarnings("unchecked")
			public void onClick(DialogInterface dialog, int whichButton) {
	    		String value = input.getText().toString();
				Toast.makeText(getBaseContext(), "You added list item: "+value, Toast.LENGTH_LONG).show(); 
	    	  // Do something with value!
				

	        	HashMap<String, String> newItem = new HashMap<String, String>();
				newItem.put("name", value);
				newItem.put("assignee", "test");
			    mylist.add(newItem);
			    ((SimpleAdapter) getListView().getAdapter()).notifyDataSetChanged();
			    setContentView(R.layout.editlist);
				
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
