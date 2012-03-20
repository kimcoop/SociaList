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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class InsideList extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	       
	   ArrayAdapter<Item> adapter = new InteractiveArrayAdapter(this, getItems());
		setListAdapter(adapter); /* May be helpful later -- onclicklistener for items
		
		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);
				Toast.makeText(getBaseContext(), "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_SHORT).show(); 

                Intent intent = new Intent(SociaListActivity.this, InsideList.class);
                intent.putExtra("list_id", o.get("id"));	// Pass the id of the clicked list
                startActivity(intent);
                finish();
				
			}
		});*/
        
    }
    
    private List<Item> getItems() {

		List<Item> list = new ArrayList<Item>();
		JSONObject json = JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/items.json");

    	       try {
    			JSONArray  lists = json.getJSONArray("items");

    	      	Item item;
    	      	String id, name, assignee;
    	        for (int i=0;i < lists.length()-1; i++){						

    	        	JSONObject e = lists.getJSONObject(i);
					id = String.valueOf(i);
    	        	name = e.getString("name");
    	        	assignee = e.getString("assignee");
    	        	item = new Item(name, assignee);
    	        	list.add(item);
    	        }
    	       } catch (JSONException e)        {
    	       	 Log.e("log_tag", "Error parsing data "+e.toString());
    	       }
		return list;
	}
    
    
    
}