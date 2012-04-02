package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.DBHelper;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.JSONfunctions;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.User;
import edu.pitt.cs1635group3.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 5000;

	DBHelper db;
	ArrayList<CustomList> lists = null;
	ArrayList<User> users = null;
	ArrayList<Item> items = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					/*
					 * int waited = 0; while(_active && (waited < _splashTime))
					 * { sleep(100); if(_active) { waited += 100; } }
					 */

					if (lists == null) {
						lists = getLists();
					}
					if (users == null) {
						users = getUsers();
					}

				} finally {
					finish();
					startActivity(new Intent(
							"edu.pitt.cs1635group3.HomeActivity"));
					stop();
				}
			}
		};
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}

	public ArrayList<User> getUsers() {
		// pull in users from the server. do this only once

		ArrayList<User> sharedUsers = new ArrayList<User>();

		JSONObject json = JSONfunctions.getJSONfromURL("getUsers");

		try {
			JSONArray myUsers = json.getJSONArray("users");

			User u;

			DBHelper db = new DBHelper(this);
			db.open();

			for (int i = 0; i < myUsers.length(); i++) {
				JSONArray e = myUsers.getJSONArray(i);
				u = new User(e.getInt(0), e.getString(1), e.getString(2), e.getString(3));
				db.insertOrUpdateUser(u);
				sharedUsers.add(u);
			}

			db.close();
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing user data " + e.toString());
		}

		return sharedUsers;

	}

	public ArrayList<CustomList> getLists() {
		Log.i("SocialListActivity", "Getting lists from server");
		ArrayList<CustomList> myCustomLists = new ArrayList<CustomList>();

		JSONObject json = JSONfunctions.getJSONfromURL("getLists");

		try {
			JSONArray myLists = json.getJSONArray("lists");

			CustomList list;
			JSONObject e1;

			DBHelper db = new DBHelper(this);
			db.open();
			// Loop the Array
			for (int i = 0; i < myLists.length(); i++) {
				e1 = myLists.getJSONObject(i);
				list = new CustomList(e1);
				
				Log.v("JSON LIST", "Name: " +list.getName()+", CID: " +list.getCustomID());
				
				myCustomLists.add(list);
				db.insertOrUpdateList(list);

				list.pullItems(); // pull the list's items from the server
				items = list.getItems();
				for (Item el : items) {
					db.insertOrUpdateItem(el);
				}

			}
			db.close();

		} catch (JSONException e) {
			Log.e("SPLASH SCR. ACTIVITY", "Error parsing data " + e.toString());
		}
		return myCustomLists;
	} // end getLists()
}