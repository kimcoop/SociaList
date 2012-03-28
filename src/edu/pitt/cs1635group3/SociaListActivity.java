package edu.pitt.cs1635group3;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SociaListActivity extends ListActivity {
	/** Called when the activity is first created. */
	ArrayList<CustomList> lists = null;
	ArrayList<User> users = null;
	ArrayList<Item> items = null;
	int activeListPosition;
	DBHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaceholder);

		db = new DBHelper(this);
		db.open();
		lists = db.getAllLists();
		users = db.getAllUsers(); 
		db.close();

		ArrayAdapter<CustomList> adapter = new CustomListAdapter(this,
				R.layout.list_row, lists);

		final ListView lv = getListView();

		View header = getLayoutInflater().inflate(R.layout.header, null);
		lv.addHeaderView(header);
		TextView label_header = (TextView) findViewById(R.id.label_header);
		label_header.setText("My Lists");
		
		setListAdapter(adapter);
		lv.setClickable(true);
		lv.setTextFilterEnabled(true);

	} // end onCreate
	

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		db.close();
		CustomList list = (CustomList) this.getListAdapter().getItem(
				position - 1); // not sure why the -1 is needed but it is

		activeListPosition = position - 1; // for intent

		Intent intent = new Intent(this, InsideListActivity.class);
		intent.putExtra("ListID", list.getID());
		startActivityForResult(intent, 0);
	}

	public void createNewList(View v) {

		Intent intent = new Intent(SociaListActivity.this,
				CreateListActivity.class);
		startActivityForResult(intent, 1);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0) {
			if (resultCode == Activity.RESULT_OK) {
				CustomList updatedList = data.getParcelableExtra("list");
				Log.d("ACTIVITY RETURN", "updatedList is populated? "
						+ updatedList.isPopulated());
				lists.set(activeListPosition, updatedList);
			}
		}
		if(resultCode==1){ // force refresh the view
    		startActivity(getIntent()); finish();       
    	}

	}

}