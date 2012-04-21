package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import service.CustomListTask;

import zebrafish.util.JSONfunctions;
import zebrafish.util.UIUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.CustomListAdapter;

public class SociaListActivity extends SherlockListActivity { // ListActivity
	private ArrayList<CustomList> lists = null;
	private RelativeLayout parentLayout;
	private static CustomListAdapter adapter;

	private Context context;
	private static final String TAG = "SociaListActivity";
	private static int userID;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaceholder);
		context = this;
		userID = User.getCurrUser(context);
		Log.i(TAG, "User ID fetched: " + userID);
		getSupportActionBar();
		setTitle("Lists");

		lists = CustomList.getAllLists(context, userID); // TODO: for user ID...

		adapter = new CustomListAdapter(this, R.layout.list_row, lists);
		parentLayout = (RelativeLayout) findViewById(R.id.userlists_layout);
		lv = getListView();

		setListAdapter(adapter);
		lv.setClickable(true);
		lv.setTextFilterEnabled(true);

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int pos, long id) {
				final View parentView = v;
				final int position = pos;
				final CustomList userlist = lists.get(position);

				final Button b = (Button) v
						.findViewById(R.id.delete_list_button);
				b.setVisibility(View.VISIBLE);

				b.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						CustomList.deleteListAndChildren(context, userlist);

						UIUtil.showMessage(context, "List deleted.");
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
	
	public static CustomListAdapter getAdapter() {
		// give it to the AsyncTask for refreshing after postExecute
		return adapter;
	} // end getAdapter

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		CustomList list = adapter.getItem(position);

		Intent intent = new Intent(this, InsideListActivity.class);
		intent.putExtra("ListID", list.getID());
		startActivityForResult(intent, 0);
	} // end onListItemClick

	public void createNewList(View v) {

		Intent intent = new Intent(SociaListActivity.this,
				CreateListActivity.class);
		startActivityForResult(intent, 0);

	} // end createNewList

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1 || resultCode == 0) { // force refresh the view
			adapter.notifyDataSetChanged();
		} else if (resultCode == 2) { // coming from ItemActivity, where we have
										// deleted the last item and user wants
										// to remove the list.

			UIUtil.showMessage(context, "Item and list deleted.");
			startActivity(getIntent()); // force refresh
			finish();
		}

	} // end onActivityResult

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home_lists_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == android.R.id.home) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_add) {
			intent = new Intent(this, CreateListActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_refresh) {
			new CustomListTask().refreshLists(context);
			return false;
		} else {
			return false;
		}

	}

}