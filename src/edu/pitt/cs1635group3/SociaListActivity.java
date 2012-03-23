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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SociaListActivity extends ListActivity {
	/** Called when the activity is first created. */
	ArrayList<CustomList> lists = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaceholder);

		lists = getLists();
		ArrayAdapter<CustomList> adapter = new CustomListAdapter(this,
				R.layout.list_row, lists);

		final ListView lv = getListView();

		View header = getLayoutInflater().inflate(R.layout.header, null);
		View footer = getLayoutInflater().inflate(R.layout.footer, null);
		lv.addHeaderView(header);
		lv.addFooterView(footer);

		setListAdapter(adapter);
		lv.setClickable(true);
		lv.setTextFilterEnabled(true);

	} // end onCreate

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		CustomList list = (CustomList) this.getListAdapter().getItem(position-1); // not sure why the -1 is needed but it is
		
		Intent intent = new Intent(this, InsideListActivity.class);
		intent.putExtra("List", list);
		startActivity(intent);
	}

	public void createNewList(View v) {

		Intent intent = new Intent(SociaListActivity.this,
				CreateListActivity.class);
		startActivity(intent);

	}

	public ArrayList<CustomList> getLists() {

		ArrayList<CustomList> myCustomLists = new ArrayList<CustomList>();

		JSONObject json = JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/server.php", "getLists");

		try {
			JSONArray myLists = json.getJSONArray("lists");
			
			CustomList newList;
			String listName, listCreation, listNote;
			int listID;

			// Loop the Array
			for (int i = 0; i < myLists.length(); i++) {

				JSONArray e = myLists.getJSONArray(i);
				// id = String.valueOf(i);
				listID = e.getInt(0);
				listName = e.getString(1);
				listCreation = e.getString(3);
				listNote = e.getString(4);

				newList = new CustomList(listID, listName);
				newList.setCreationDate(listCreation);
				newList.setNote(listNote); // don't read in the list's items here. do it once a list is actually clicked (more efficient and also avoids problems with parcelable item passing)
				myCustomLists.add(newList);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return myCustomLists;
	} // end getLists()

}