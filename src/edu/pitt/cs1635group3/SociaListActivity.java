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

		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

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
		CustomList list = (CustomList) this.getListAdapter().getItem(position);

		Intent intent = new Intent(getBaseContext(), InsideListActivity.class);
		// intent.putExtra("Item", item); // can pass as object because it
		// implements Parcelable - TODO
		startActivity(intent);
	}

	public void createNewList(View v) {

		Intent intent = new Intent(SociaListActivity.this,
				CreateListActivity.class);
		startActivity(intent);

	}

	public ArrayList<CustomList> getLists() {

		ArrayList<CustomList> myCustomLists = new ArrayList<CustomList>();

		JSONObject json = JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/index.json");

		try {
			JSONArray myLists = json.getJSONArray("lists");

			// Loop the Array
			for (int i = 0; i < myLists.length() - 1; i++) {

				JSONObject e = myLists.getJSONObject(i);
				// id = String.valueOf(i);
				int ID = e.getInt("id");
				String name = e.getString("name");
				String lastUpdated = e.getString("lastUpdated");

				CustomList newList = new CustomList(ID, name);
				newList.setLastUpdated(lastUpdated);
				myCustomLists.add(newList);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return myCustomLists;
	} // end getLists()

}