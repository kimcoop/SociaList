package edu.pitt.cs1635group3.Activities;

import zebrafish.util.DBHelper;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.User;

public class ItemActivity extends SherlockActivity {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	private Item item, prevItem, nextItem;
	private DBHelper db;

	private Context context;
	private static final String TAG = "ITEM ACTIVITY";
	private static int userID;

	private int pos, totalItems;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);

		context = this;
		int uID = User.getCurrUser(context);
		Log.i(TAG, "User ID fetched: " +uID);	

		// Gesture detection
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
		View swiper = (View) findViewById(R.id.swiper);
		swiper.setOnTouchListener(gestureListener);

		EditText name, quantity, notes, assignee;
		name = (EditText) findViewById(R.id.item_name);
		quantity = (EditText) findViewById(R.id.item_quantity);
		assignee = (EditText) findViewById(R.id.item_assignee);
		notes = (EditText) findViewById(R.id.item_notes);

		CheckBox itemCompletion = (CheckBox) findViewById(R.id.item_completion);

		Intent i = getIntent();
		Bundle extras = i.getExtras();
		int itemID = extras.getInt("ItemID");

		pos = extras.getInt("pos");
		totalItems = extras.getInt("totalItems");

		getSupportActionBar();
		setTitle("Item " + pos + " of " + totalItems);

		db = new DBHelper(this);
		db.open();

		item = db.getItem(itemID);

		int prevID, nextID;
		prevID = item.getPrev();
		nextID = item.getNext();

		if (prevID > 0)
			prevItem = db.getItem(prevID);
		else
			prevItem = item;
		if (nextID > 0)
			nextItem = db.getItem(nextID);
		else
			nextItem = item;

		itemCompletion.setChecked(item.isCompleted());

		name.setText(item.getName());
		quantity.setText("" + item.getQuantity());

		if (item.getAssignee() > 0) {
			assignee.setText(db.getUserByID(item.getAssignee()).getName());
		} else {
			assignee.setHint("Click to assign");
		}

		notes.setText(item.getNotes());

		db.close();

		assignee.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				selectAssignee(v);
			}
		});

	}

	public void onToggleClicked(View v) {
		// Perform action on clicks
		if (((CheckBox) v).isChecked()) {
			item.setCompleted(context, true);
		} else {
			item.setCompleted(context, false);
		}
	}

	public void assignItemTo(String user) {

		TextView assignee = (TextView) findViewById(R.id.item_assignee);
		assignee.setText(user);
		assignee.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				selectAssignee(v);
			}
		});
		// item.assignTo(userID); -- Don't do this here. Do on save
	}

	public void selectAssignee(View v) {
		final CharSequence[] users = User.getUsersForDialog(context,
				item.getParentID());

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Assign To");
		builder.setItems(users, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pos) {
				assignItemTo((String) users[pos]);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onBackPressed() {

		db.close();
		Intent in = new Intent();
		setResult(1, in);// Requestcode 1. Tell parent activity to refresh
							// items.
		finish();
		super.onBackPressed();
	}

	public void saveItem(View v) {

		// regather the item details, open the db, use the updateItem method.

		TextView name, quantity, assignee, notes;
		name = (EditText) findViewById(R.id.item_name);
		quantity = (EditText) findViewById(R.id.item_quantity);
		assignee = (TextView) findViewById(R.id.item_assignee);
		notes = (EditText) findViewById(R.id.item_notes);

		// toggle is handled onClick for item completion altering
		item.setName(context, name.getText().toString().trim());
		item.setQuantity(context,
				Integer.parseInt(quantity.getText().toString().trim()));
		item.setNotes(context, notes.getText().toString().trim());
		item.setAssigner(User.getCurrUser(context));

		String rawAssignee = assignee.getText().toString().trim();

		int assigneeID;
		if (rawAssignee != "") {
			assigneeID = User.getUserByName(context, rawAssignee);
			Log.i(TAG, "Assignee ID is " + assigneeID);
			// TODO item.assignTo(context, assigneeID);
		} else {
			Log.e(TAG, "Cannot assign item to null user");
		}

		Toast.makeText(this, "Item updated.", Toast.LENGTH_SHORT).show();

	}

	public void deleteWithList() {
		// delete item and parent list
		Intent intent = new Intent(this, SociaListActivity.class);

		db.open();
		db.deleteItem(item);
		db.deleteListByID(item.getParentID());
		db.close();

		startActivityForResult(intent, 2);
		finish();

	}

	public void deleteAndExit() {

		prevItem.setNext(context, nextItem.getID());
		nextItem.setPrev(context, prevItem.getID());

		Toast.makeText(this, "Item deleted.", Toast.LENGTH_SHORT).show();

		Intent in = new Intent();
		setResult(1, in);// Requestcode 1. Tell parent activity to refresh
		finish();
	}

	public void deleteItem() {

		prevItem.setNext(context, nextItem.getID()); // set the previous item's
														// next item
		// to the next
		nextItem.setPrev(context, prevItem.getID());

		if (prevItem.getID() == item.getID()
				|| nextItem.getID() == item.getID()) { // if this is the last
														// item in the list,
														// inform user and give
														// option to delete
														// whole list

			AlertDialog builder = new AlertDialog.Builder(this)
					// builder.setIcon(R.drawable.alert_dialog_icon)
					.setTitle("Deleting final list item. Also delete the list?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									deleteWithList();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									deleteAndExit();
								}
							}).create();
			builder.show();

		} else {
			prevItem.update(context);
			nextItem.update(context);
			deleteAndExit();
		}

	}

	public void goToPrev() {

		Intent intent = new Intent(this, ItemActivity.class);
		intent.putExtra("ItemID", prevItem.getID());

		int prevPos = (pos == 1 ? totalItems : pos - 1);
		intent.putExtra("pos", prevPos);
		intent.putExtra("totalItems", totalItems);
		startActivity(intent);
		finish();
	}

	public void prevItem(View v) {
		// calling like this because it's an onclick but we also use it for
		// swipe
		goToPrev();
	}

	public void goToNext() {
		Intent intent = new Intent(this, ItemActivity.class);
		intent.putExtra("ItemID", nextItem.getID());

		int nextPos = (pos == totalItems ? 1 : pos + 1);
		intent.putExtra("pos", nextPos);
		intent.putExtra("totalItems", totalItems);
		startActivity(intent);
		finish();
	}

	public void nextItem(View v) {
		goToNext();
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Intent intent = new Intent(getBaseContext(), ItemActivity.class);

			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
				return false;
			}

			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				intent.putExtra("ItemID", item.getNext());
				int prevPos = (pos == 1 ? totalItems : pos - 1);
				intent.putExtra("pos", prevPos);
				intent.putExtra("totalItems", totalItems);
				startActivity(intent);
				finish();
				ItemActivity.this.overridePendingTransition(
						R.anim.slide_in_right, R.anim.slide_out_left);
				// right to left swipe
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				intent.putExtra("ItemID", item.getPrev());
				int nextPos = (pos == totalItems ? 1 : pos + 1);
				intent.putExtra("pos", nextPos);
				intent.putExtra("totalItems", totalItems);
				startActivity(intent);
				finish();
				ItemActivity.this.overridePendingTransition(
						R.anim.slide_in_left, R.anim.slide_out_right);
			}

			return false;
		}

		// It is necessary to return true from onDown for the onFling event to
		// register
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
	}
	
	/*PLUS/MINUS BUTTONS FOR ITEM QTY*/
	public void plusButtonPressed (View v)
	{
		EditText t = (EditText) findViewById(R.id.item_quantity);
		String s = t.getText().toString();
		int i = Integer.parseInt(s);
		i++;
		t.setText(""+i);
	}

	public void minusButtonPressed (View v)
	{
		EditText t = (EditText) findViewById(R.id.item_quantity);
		String s = t.getText().toString();
		int i = Integer.parseInt(s);
		if (i > 0)
		{
			i--;
			t.setText(""+i);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.item_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == 0) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_delete) {
			deleteItem();
			return true;
		} else {
			return false;
		}
	}

}
