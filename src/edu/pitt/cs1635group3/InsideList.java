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
        
        /*
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
    	        	String assignee = e.getString("assignee").trim();
    	        	
    	        	if (assignee != "") map.put("assignee", "Assigned to: " +  e.getString("assignee"));
    	        	mylist.add(map);
    		}
    	       }catch(JSONException e)        {
    	       	 Log.e("log_tag", "Error parsing data "+e.toString());
    	       }
    	       /*
        ListAdapter adapter = new SimpleAdapter(this, mylist, R.layout.main,
                new String[] { "name", "assignee" },
                new int[] { R.id.item_title, R.id.item_subtitle });
*/
    	       
    	       ArrayAdapter<Item> adapter = new InteractiveArrayAdapter(this,
    					getItem());
    			setListAdapter(adapter); /*
		
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

	private List<Item> getItem() {
		List<Item> list = new ArrayList<Item>();
		list.add(get("XBOX", "Jim"));
		list.add(get("Fridge", "Brendan"));
		list.add(get("TV", "Rob"));
		list.add(get("Beer", "Kim"));
		list.add(get("More beer"));
		// Initially select one of the items
		list.get(1).setSelected(true);
		return list;
	}

	private Item get(String s) {
		return new Item(s, "");
	}
	
	private Item get(String s, String a) {
		return new Item(s, a);
	}
    
    
    
    
}