package edu.pitt.cs1635group3;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;


public class InsideListActivity extends ListActivity {
	
	ArrayList<Item> items = null;
	  
	Button assign_button;
	Button complete_button;
	Button invite_button;
	View buttons_helper;
	boolean inviteUp = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insidelist_layout);
        
		assign_button = (Button) findViewById(R.id.assign_button);
		complete_button = (Button) findViewById(R.id.complete_button);
		invite_button = (Button) findViewById(R.id.invite_button);
		buttons_helper = (View) findViewById(R.id.buttons_helper);
        
        items = getItems();
        ArrayAdapter<Item> adapter = new ItemAdapter(this, R.layout.item_row, items);
		
		final ListView lv = getListView();

		View header = getLayoutInflater().inflate(R.layout.header, null);
		lv.addHeaderView(header);
		TextView label_header = (TextView) findViewById(R.id.label_header);
		label_header.setText("Viewing Dorm Room Checklist"); //TODO get name of List (make List class & implement Parcelable for List)
		
		lv.setTextFilterEnabled(true);
		lv.setClickable(true);
		setListAdapter(adapter); // must go after header and footer are inflated
/*
	    Spinner spinner = (Spinner) findViewById(R.id.sorter);
	    spinner.setVisibility(View.VISIBLE);
	    ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
	            this, R.array.default_sorts, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(spinnerAdapter);
	    
	    class MyOnItemSelectedListener implements OnItemSelectedListener {

	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	          Toast.makeText(parent.getContext(), "The sort selected is " +parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
	        }

	        public void onNothingSelected(AdapterView parent) {
	          // Do nothing.
	        }
	    }*/
		
		
		
	}// end onCreate

    protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Item item = (Item) this.getListAdapter().getItem(position);

        Intent intent = new Intent(getBaseContext(), ItemActivity.class);
        intent.putExtra("Item", item); // can pass as object because it implements Parcelable
        startActivity(intent);
    }
    
    private ArrayList<Item> getItems() { //TODO: convert this to a getItems() method inside List class

    	ArrayList<Item> myList = new ArrayList<Item>();
		JSONObject json = JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/items.json");

    	       try {
    			JSONArray  lists = json.getJSONArray("items");

    	      	Item item;
    	      	String ID, name, assigner, assignee, creationDate, notes, creator, completionDate;
    	      	int itemID, parentID, quantity;
    	      	boolean completed;

    	        for (int i=0;i < lists.length()-1; i++){						

    	        	JSONObject e = lists.getJSONObject(i);
					ID = String.valueOf(i);
					parentID = e.getInt("parent_id");
					itemID = e.getInt("id");
    	        	name = e.getString("name");
    	        	assigner = e.getString("assigner");
    	        	assignee = e.getString("assignee");
    	        	creationDate = e.getString("creation_date");
    	        	notes = e.getString("notes");
    	        	creator = e.getString("creator");
    	        	completionDate = e.getString("completion_date");
    	        	quantity = e.getInt("quantity");
    	        	completed = e.getBoolean("completed");
    	        	item = new Item(itemID, name, assigner, assignee, creationDate, notes, quantity, creator, completionDate, completed);
    	        	item.setParent(parentID);
    	        	myList.add(item);
    	        }
    	       } catch (JSONException e)        {
    	       	 Log.e("log_tag", "Error parsing data "+e.toString());
    	       }
    	       return myList;
	}
    


    public void flipButtons (View v)
    {

    	if (inviteUp)
    	{
    		invite_button.setVisibility(View.GONE);
    		complete_button.setVisibility(View.VISIBLE);
    		assign_button.setVisibility(View.VISIBLE);
    		buttons_helper.setVisibility(View.VISIBLE);
    		inviteUp = false;
    	}
    	else
    	{
    		invite_button.setVisibility(View.VISIBLE);
    		complete_button.setVisibility(View.GONE);
    		assign_button.setVisibility(View.GONE);
    		buttons_helper.setVisibility(View.GONE);
    		inviteUp = true;
    	}
    }
    
    
}