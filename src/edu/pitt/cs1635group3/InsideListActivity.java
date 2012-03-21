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
import android.widget.AdapterView.OnItemClickListener;


public class InsideListActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	       
	   //ArrayAdapter<Item> adapter = new InteractiveArrayAdapter(this, getItems());
	   
	  
        Log.i("HERE", "Inside onCreate in InsideListActivity");
        setContentView(R.layout.insidelistplaceholder);

    	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

    	//Get the data (see above)
    	JSONObject json =
    		JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/items.json");

    	       try{
    		JSONArray  lists = json.getJSONArray("items");

    	      	       	//Loop the Array
    	        for(int i=0;i < lists.length()-1; i++){						

    	        	HashMap<String, String> map = new HashMap<String, String>();
    	        	JSONObject e = lists.getJSONObject(i);

    	        	map.put("id",  String.valueOf(i));
    	        	map.put("name", e.getString("name"));
    	        	map.put("assignee",e.getString("assignee"));
    	        	
    	        	Log.d("Test", "item name is "+e.getString("name"));
    	        	
    	        	mylist.add(map);
    		}
    	       }catch(JSONException e)        {
    	       	 Log.e("log_tag", "Error parsing data "+e.toString());
    	       }
    	       
        ListAdapter adapter = new SimpleAdapter(this, mylist, R.layout.insidelist_main,
                new String[] { "name", "assignee" },
                new int[] { R.id.item_name, R.id.item_assignee });
/*
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							Item element = (Item) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());

						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));*/
		
		final ListView lv = getListView();

		View header = getLayoutInflater().inflate(R.layout.header, null);
		lv.addHeaderView(header);
		TextView label_header = (TextView) findViewById(R.id.label_header);
		label_header.setText("Viewing List Contents"); //TODO get name of List (implement Parcelable for List)
		

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			  public void onItemClick(AdapterView<?> l, View v, int position, long id) {

			    Object o = lv.getItemAtPosition(position);
			    Log.e("Clicked item in list", "At position" +position);
			    //Log.e("Object clicked", o.toString() + " and name is " +((Item) o).getName());
			    /* write you handling code like...
			    String st = "sdcard/";
			    */
			    //super.onListItemClick(l, v, position, id);
				// Get the item that was clicked
				//Item item = (Item) this.getListAdapter().getItem(position);

		        //Intent intent = new Intent(getBaseContext(), ItemActivity.class);
		        //intent.putExtra("Item", item); // can pass as object because it implements Parcelable
		        //startActivity(intent);
			  }
			});

	   setListAdapter(adapter);
	   
	}
/*
	@Override
	protected void setOnListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Item item = (Item) this.getListAdapter().getItem(position);

        Intent intent = new Intent(getBaseContext(), ItemActivity.class);
        intent.putExtra("Item", item); // can pass as object because it implements Parcelable
        startActivity(intent);

    }*/
    
    private List<Item> getItems() {

		List<Item> list = new ArrayList<Item>();
		JSONObject json = JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/items.json");

    	       try {
    			JSONArray  lists = json.getJSONArray("items");

    	      	Item item;
    	      	String id, name, assigner, assignee, creation_date, notes, creator, completion_date;
    	      	int item_id, quantity;
    	      	boolean completed;

    	        for (int i=0;i < lists.length()-1; i++){						

    	        	JSONObject e = lists.getJSONObject(i);
					id = String.valueOf(i);
					item_id = e.getInt("id");
    	        	name = e.getString("name");
    	        	assigner = e.getString("assigner");
    	        	assignee = e.getString("assignee");
    	        	creation_date = e.getString("creation_date");
    	        	notes = e.getString("notes");
    	        	creator = e.getString("creator");
    	        	completion_date = e.getString("completion_date");
    	        	quantity = e.getInt("quantity");
    	        	completed = e.getBoolean("completed");
    	        	item = new Item(item_id, name, assigner, assignee, creation_date, notes, quantity, creator, completion_date, completed);
    	        	list.add(item);
    	        }
    	       } catch (JSONException e)        {
    	       	 Log.e("log_tag", "Error parsing data "+e.toString());
    	       }
		return list;
	}
    
    
    
}