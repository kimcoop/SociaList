package edu.pitt.cs1635group3.Activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.CustomListAdapter;
import edu.pitt.cs1635group3.DBHelper;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.User;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;

import android.app.Activity;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SociaListActivity extends ListActivity { // ListActivity
	int activeListPosition;
	private ArrayList<CustomList> lists = null;
	private ArrayList<User> users = null;
	private DBHelper db;
	private RelativeLayout parentLayout;
	ArrayAdapter<CustomList> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaceholder);

		db = new DBHelper(this);
		db.open();
		lists = db.getAllLists(); // TODO: for user ID...
		users = db.getAllUsers();
		db.close();

		adapter = new CustomListAdapter(this, R.layout.list_row, lists);
		parentLayout = (RelativeLayout) findViewById(R.id.userlists_layout);
		final ListView lv = getListView();

		View header = getLayoutInflater().inflate(R.layout.header, null);
		lv.addHeaderView(header);
		TextView label_header = (TextView) findViewById(R.id.label_header);
		label_header.setText("My Lists");

		setListAdapter(adapter);
		lv.setClickable(true);
		lv.setTextFilterEnabled(true);

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                    int pos, long id) {
            	final View parentView = v;
            	final int position = pos-1;
    			final CustomList userlist = lists.get(position);
    			final String listname = userlist.getName();
    			
    			Log.i("LIST LONG PRESS", "List name is " +listname+ " and ID is " +userlist.getID());
    			//parentView.getBackground().setColorFilter(Color.parseColor("#323331"), Mode.DARKEN);

    			final Button b = (Button) v.findViewById(R.id.delete_list_button);
    			b.setVisibility(View.VISIBLE);
    			
    			b.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						db.open();
						db.deleteListAndChildren(userlist);
						db.close();
						
		            	Toast.makeText(getBaseContext(), "List " + listname + " deleted.", Toast.LENGTH_SHORT).show();
		            	b.setVisibility(View.INVISIBLE);
		            	parentLayout.removeView(parentView);
		            	adapter.remove(userlist);
		            	adapter.notifyDataSetChanged();
					
					}
    	        }); 
            	
                return true;
            }
        }); 
		
		

	} // end onCreate

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		CustomList list = adapter.getItem(position - 1);

		Intent intent = new Intent(this, InsideListActivity.class);
		intent.putExtra("ListID", list.getID());
		startActivityForResult(intent, 0);
	}

	public void createNewList(View v) {

		Intent intent = new Intent(SociaListActivity.this,
				CreateListActivity.class);
		startActivityForResult(intent, 0);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1 || resultCode == 0) { // force refresh the view
			startActivity(getIntent());
			finish();
		} else if (resultCode == 2) { // coming from ItemActivity, where we have
										// deleted the last item and user wants
										// to remove the list.

			Toast.makeText(this, "Item and list deleted.", Toast.LENGTH_SHORT)
					.show();
			startActivity(getIntent()); // force refresh
			finish();
		}

	}

}