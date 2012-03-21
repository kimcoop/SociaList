package edu.pitt.cs1635group3;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SociaListActivity extends ListActivity {
    /** Called when the activity is first created. */
	
		
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listplaceholder);

    	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

    	//Get the data (see above)
    	JSONObject json =
    		JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/index.json");

    	       try{
    		JSONArray  lists = json.getJSONArray("lists");

    	      	       	//Loop the Array
    	        for(int i=0;i < lists.length()-1; i++){						

    	        	HashMap<String, String> map = new HashMap<String, String>();
    	        	JSONObject e = lists.getJSONObject(i);

    	        	map.put("id",  String.valueOf(i));
    	        	map.put("name", e.getString("name"));
    	        	map.put("updated", "Last updated: " +  e.getString("datetime"));
    	        	mylist.add(map);
    		}
    	       }catch(JSONException e)        {
    	       	 Log.e("log_tag", "Error parsing data "+e.toString());
    	       }
    	       
        ListAdapter adapter = new SimpleAdapter(this, mylist , R.layout.main,
                new String[] { "name", "updated" },
                new int[] { R.id.item_title, R.id.item_subtitle });

		
		final ListView lv = getListView();
		
		View header = getLayoutInflater().inflate(R.layout.header, null);
		View footer = getLayoutInflater().inflate(R.layout.footer, null);
		lv.addHeaderView(header);
		lv.addFooterView(footer);
		
		Button newListButton = (Button) findViewById(R.id.new_list_button);
		         newListButton.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		                 // Perform action on click
		            	 Log.e("You clicked!", "STUB: create new list");
		             }
		         });
		
		
		setListAdapter(adapter);
		
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);
				//Toast.makeText(getBaseContext(), "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_SHORT).show(); 

                Intent intent = new Intent(SociaListActivity.this, InsideListActivity.class);
                intent.putExtra("list_id", o.get("id"));	// Pass the id of the clicked list -- TODO use this id to grab proper data (currently grabs generic data). Talk to Kim about this.
                startActivity(intent);
				
			}
		});
        
    } //end onCreate
    
    
}