package edu.pitt.cs1635group3.Activities;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.cs1635group3.DBHelper;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.JSONfunctions;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.User;
import edu.pitt.cs1635group3.R.anim;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ItemActivity extends Activity {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	private Item item, prevItem, nextItem;
	private DBHelper db;

	private int pos, totalItems;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);

		// Gesture detection
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
		View swiper = (View) findViewById(R.id.swiper);
		swiper.setOnTouchListener(gestureListener);

		TextView name, quantity, creation_details, assignee, notes;
		name = (EditText) findViewById(R.id.item_name);
		quantity = (EditText) findViewById(R.id.item_quantity);
		creation_details = (TextView) findViewById(R.id.item_creation);
		assignee = (TextView) findViewById(R.id.item_assignee);
		notes = (EditText) findViewById(R.id.item_notes);

		ToggleButton itemCompletion = (ToggleButton) findViewById(R.id.item_completion);

		Intent i = getIntent();
		Bundle extras = i.getExtras();
		int itemID = extras.getInt("ItemID");

		pos = extras.getInt("pos");
		totalItems = extras.getInt("totalItems");

		TextView nav = (TextView) findViewById(R.id.label_header);
		nav.setText("Item " + pos + " of " + totalItems);

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

		String creator;
		if (item.getCreator() > 0)
			creator = db.getUserNameByID(item.getCreator());
		else
			creator = "";
		creation_details.setText("Added on " + item.getCreationDate() + " by "
				+ creator);

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
		if (((ToggleButton) v).isChecked()) {
			item.setCompleted(true);
		} else {
			item.setCompleted(false);
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
		db.open();
		final CharSequence[] users = db.getUsersForDialog();
		db.close();

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
		db.open();
		item.setName(name.getText().toString().trim());
		item.setQuantity(Integer.parseInt(quantity.getText().toString().trim()));
		item.setNotes(notes.getText().toString().trim());

		String rawAssignee = assignee.getText().toString().trim();

		int assigneeID;
		if (rawAssignee != "" && rawAssignee != null && !rawAssignee.isEmpty()) {
			assigneeID = db.getUserByName(rawAssignee);
			item.assignTo(assigneeID);
		}

		db.updateItem(item);
		db.close();

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

		prevItem.setNext(nextItem.getID());
		nextItem.setPrev(prevItem.getID());
		
		db.open();
		db.updateItem(prevItem);
		db.updateItem(nextItem);
		db.deleteItem(item);
		db.close();
		Toast.makeText(this, "Item deleted.", Toast.LENGTH_SHORT).show();

		Intent in = new Intent();
		setResult(1, in);// Requestcode 1. Tell parent activity to refresh
		finish();
	}

	public void deleteItem(View v) {

		prevItem.setNext(nextItem.getID()); // set the previous item's next item
											// to the next
		nextItem.setPrev(prevItem.getID());

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
			db.open();
			db.updateItem(prevItem);
			db.updateItem(nextItem);
			db.close();
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

}
