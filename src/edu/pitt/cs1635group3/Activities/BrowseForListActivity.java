package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import service.CustomListTask;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONCustomList;
import zebrafish.util.JSONfunctions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Adapters.BrowseListAdapter;
import edu.pitt.cs1635group3.Adapters.CustomListAdapter;
import edu.pitt.cs1635group3.R;

public class BrowseForListActivity extends SherlockListActivity 
{
	protected static Context context;
	protected int userID;
	private static final String TAG = "BrowseForListActivity";
	
	private static BrowseListAdapter adapter;
	private static ListView lv;
	private static ArrayList<CustomList> lists = null;
	private static EditText listID_Text;
	private String idStr;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_listplaceholder);
		context = this;
		getSupportActionBar();
		setTitle("Browse for a List");
		lists = new ArrayList<CustomList>();
		
		adapter = new BrowseListAdapter(this, R.layout.list_row, lists);
		lv = getListView();
		setListAdapter(adapter);
		lv.setClickable(true);
		lv.setTextFilterEnabled(true);
	}
	
	public void searchList(View v){
		listID_Text = (EditText) findViewById(R.id.list_id);
		idStr = listID_Text.getText().toString().trim();
		lists.clear();
		
		if (!idStr.equals("")){
			
			new CustomListTask().browse(context, idStr);
			
		} else{
			Toast t = Toast.makeText(context, "Search text cannot be empty.", Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 80);
			t.show();
		}
		
	}
	
	public static void display(ArrayList<CustomList> results) {
		lists = results;
		
		if (lists.size()>0){
			//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			//imm.hideSoftInputFromWindow(listID_Text.getWindowToken(), 0);
			adapter = new BrowseListAdapter(context, R.layout.list_row, lists);
			lv.setAdapter(adapter);
		} else{
			Toast t = Toast.makeText(context, "No lists found.", Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 80);
			t.show();
		}
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		CustomList list = adapter.getItem(position);

		Intent intent = new Intent(this, BrowseInsideListActivity.class);
		intent.putExtra("ListID", list.getID());
		intent.putExtra("ListCID", idStr);
		startActivityForResult(intent, 0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.blank_menu, menu);

		return true;
	}
	
}
