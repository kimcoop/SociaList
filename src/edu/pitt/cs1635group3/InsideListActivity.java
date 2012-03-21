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


public class InsideListActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	       
	   ArrayAdapter<Item> adapter = new InteractiveArrayAdapter(this, getItems());
	   
	   /*
	   final Button btnAddMore = new Button(this);
	   btnAddMore.setText("Button");
	   exArticlesList = (ExpandableListView) this.findViewById(R.id.art_list_exlist);
	   exArticlesList.addFooterView(btnAddMore);
*/
	   setListAdapter(adapter);
	   
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Item item = (Item) this.getListAdapter().getItem(position);

        Intent intent = new Intent(getBaseContext(), ItemActivity.class);
        intent.putExtra("Item", item); // can pass as object because it implements Parcelable
        startActivity(intent);

    }
    
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