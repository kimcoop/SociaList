package edu.pitt.cs1635group3;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 5000;

	DBHelper db;
	ArrayList<CustomList> lists = null;
	ArrayList<User> users = null;
	ArrayList<Item> items = null;
	
	public TextView splash_tv;
	Handler mHandler;
	public String listName;
	public String itemName;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		splash_tv = (TextView) findViewById(R.id.splash_text_view);
		splash_tv.setText("test");
		mHandler = new Handler();
        mHandler.post(mUpdate);


		// thread for displaying the SplashScreen
		/*Thread splashTread = new Thread() {
			
		};
		splashTread.start();*/
	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}*/
	private Runnable mUpdate = new Runnable() {
		public void run() {
			try {
				/*
				 * int waited = 0; while(_active && (waited < _splashTime))
				 * { sleep(100); if(_active) { waited += 100; } }
				 */

				if (lists == null) {
					runOnUiThread(new Runnable() {
		                public void run() {
		                	splash_tv.setText("Loading: Lists!");  
		                }
		            });
					lists = getLists();
				}
				if (users == null) {
					runOnUiThread(new Runnable() {
		                public void run() {
		                	splash_tv.setText("Loading: Users!");
		                }
		            });
					users = getUsers();
				}

			} finally {
				finish();
				startActivity(new Intent(
						"edu.pitt.cs1635group3.SociaListActivity"));
				//stop();
			}
		}
		};


	public ArrayList<User> getUsers() {
		// pull in users from the server. do this only once

		ArrayList<User> sharedUsers = new ArrayList<User>();

		JSONObject json = JSONfunctions.getJSONfromURL(
				"http://www.zebrafishtec.com/server.php", "getUsers");

		try {
			JSONArray myUsers = json.getJSONArray("users");

			User u;

			DBHelper db = new DBHelper(this);
			db.open();

			for (int i = 0; i < myUsers.length(); i++) {
				JSONArray e = myUsers.getJSONArray(i);
				u = new User(e.getInt(0), e.getString(1), e.getString(2));
				db.insertUser(u);
				sharedUsers.add(u);
			}

			db.close();
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing user data " + e.toString());
		}

		return sharedUsers;

	}

	public ArrayList<CustomList> getLists() {
		Log.i("SocialListActivity", "inside getLists");
		ArrayList<CustomList> myCustomLists = new ArrayList<CustomList>();

		JSONObject json = JSONfunctions.getJSONfromURL(
				"http://www.zebrafishtec.com/server.php", "getLists");

		try {
			Log.i("SocialListActivity", "inside Try");
			JSONArray myLists = json.getJSONArray("lists");

			CustomList list;
			String listName, listCreation, listNote;
			int listID;

			DBHelper db = new DBHelper(this);
			db.open();
			// Loop the Array
			for (int i = 0; i < myLists.length(); i++) {
				JSONArray e = myLists.getJSONArray(i);
				listID = e.getInt(0);
				listName = e.getString(1);
				this.listName = listName;
				runOnUiThread(new Runnable() {
	                public void run() {
	                	splash_tv.setText("Loading: List - ");// + this.listName);
	                }
	            });
				
				listCreation = e.getString(3);
				listNote = e.getString(4);
				list = new CustomList(listID, listName);
				list.setCreationDate(listCreation);
				list.setNote(listNote);
				myCustomLists.add(list);
				db.insertList(list, true); // true = from server (acts as a flag)
				
				list.pullItems(); // pull the list's items from the server
				items = list.getItems();
				for (Item el : items) {
					this.itemName = el.getName();
					runOnUiThread(new Runnable() {
		                public void run() {
		                	splash_tv.setText("Loading: Grabbing ");// + this.listName + "'s item: " + itemName);
		                }
		            });
					db.insertItem(el, true);// true = from server (acts as a flag)
				}
			}
			db.close();

		} catch (JSONException e) {
			Log.e("SPLASH SCR. ACTIVITY", "Error parsing data " + e.toString());
		}
		return myCustomLists;
	} // end getLists()
}