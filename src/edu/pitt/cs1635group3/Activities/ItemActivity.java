package edu.pitt.cs1635group3.Activities;

import zebrafish.util.DBHelper;
import zebrafish.util.UIUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class ItemActivity extends SherlockActivity {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;

	CharSequence[] users;
	int[] correspUserIDs;

	private Item item, prevItem, nextItem;
	private DBHelper db;

	private AlertDialog builder;
	private Context context;
	private static final String TAG = "ITEM ACTIVITY";
	private static int userID;

	private int pos, totalItems;
	private boolean itemUpdated = false;
	private EditText name, notes;
	private View v;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);

		context = this;
		v = this.getCurrentFocus();
		userID = User.getCurrUser(context);
		// Gesture detection
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				/*
				 * if(itemUpdated){ saveItem(v); }
				 */
				return gestureDetector.onTouchEvent(event);
			}
		};
		View swiper = (View) findViewById(R.id.swiper);
		swiper.setOnTouchListener(gestureListener);

		EditText quantity, assignee;
		name = (EditText) findViewById(R.id.item_name);
		quantity = (EditText) findViewById(R.id.item_quantity);
		assignee = (EditText) findViewById(R.id.item_assignee);
		notes = (EditText) findViewById(R.id.item_notes);
		CheckBox itemCompletion = (CheckBox) findViewById(R.id.item_completion);

		// name.addTextChangedListener(mTextEditorWatcher);
		// notes.addTextChangedListener(mTextEditorWatcher);

		name.addTextChangedListener(new CustomTextWatcher(name));
		notes.addTextChangedListener(new CustomTextWatcher(notes));

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
		setupUsersForAssignment();

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
				selectAssignee();
			}
		});

	}

	public void onToggleClicked(View v) {
		// Perform action on clicks

		itemUpdated = !itemUpdated; // Might not actually be true need to see
		// if it changed from the original state
		if (((CheckBox) v).isChecked()) {

			item.setCompleted(context, true);
		} else {
			item.setCompleted(context, false);
		}
	}

	public void assignItemTo(int selectionPos) {

		TextView assignee = (TextView) findViewById(R.id.item_assignee);
		itemUpdated = true;
		assignee.setText(users[selectionPos]);
		item.assignTo(correspUserIDs[selectionPos]);
	}

	public void selectAssignee() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Assign To");
		builder.setItems(users, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pos) {

				assignItemTo(pos);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	@Override
	public void onBackPressed() {

		db.close();
		if (itemUpdated) {
			saveItem(v);
		}
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

	} // end saveItem

	public void deleteWithList() {
		// delete item and parent list
		Intent intent = new Intent(this, SociaListActivity.class);
		builder.dismiss();

		db.open();
		db.deleteItem(item);
		db.deleteListByID(item.getParentID());
		db.close();

		startActivityForResult(intent, 2);
		finish();
	} // end deleteWithList

	public void deleteAndExit() {
		builder.dismiss();

		prevItem.setNext(context, nextItem.getID());
		nextItem.setPrev(context, prevItem.getID());
		Item.deleteItem(context, item);

		UIUtil.showMessage(context, "Item deleted.");

		Intent in = new Intent();
		setResult(1, in);// Requestcode 1. Tell parent activity to refresh
		finish();
	}

	public void deleteItem() {

		if (prevItem.getID() == item.getID()
				|| nextItem.getID() == item.getID()) { // if this is the last
														// item in the list,
														// inform user and give
														// option to delete
														// whole list

			prevItem.setNext(context, nextItem.getID()); // set the previous
															// item's
															// next item
			// to the next
			nextItem.setPrev(context, prevItem.getID());

			builder = new AlertDialog.Builder(this)
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

			prevItem.setNext(context, nextItem.getID()); // set the previous
															// item's
															// next item
			// to the next
			nextItem.setPrev(context, prevItem.getID());

			builder = new AlertDialog.Builder(this)
					.setTitle("Confirm delete item?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									deleteAndExit();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// no delete
								}
							}).create();
			builder.show();

			prevItem.update(context);
			nextItem.update(context);
		} // end else

	}

	public void goToPrev() {

		if (itemUpdated) {
			saveItem(v);
		}
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

		if (itemUpdated) {
			saveItem(v);
		}
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
				if (itemUpdated) {
					saveItem(v);
				}
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
				if (itemUpdated) {
					saveItem(v);
				}
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

	/* PLUS/MINUS BUTTONS FOR ITEM QTY */
	public void plusButtonPressed(View v) {
		itemUpdated = true; // Might not actually be true need to see
							// if it changed from the original state
		EditText t = (EditText) findViewById(R.id.item_quantity);
		String s = t.getText().toString();
		int i = Integer.parseInt(s);
		i++;
		t.setText("" + i);
	}

	public void minusButtonPressed(View v) {
		itemUpdated = true; // Might not actually be true need to see
							// if it changed from the original state
		EditText t = (EditText) findViewById(R.id.item_quantity);
		String s = t.getText().toString();
		int i = Integer.parseInt(s);
		if (i > 0) {
			i--;
			t.setText("" + i);
		}
	}
	
	public void setupUsersForAssignment() {
		// do the initialization for users here

		CharSequence[] tempUsers = User.getUsersForDialog(context, item.getParentID());
		int[] tempCorrespUserIDs = User.getUserIDsForDialog(context, item.getParentID()); // these must go together (hacky, but needed for popup)
		int numUsers = tempUsers.length +1;
		users = new CharSequence[numUsers];
		correspUserIDs = new int[numUsers];
		
		for (int i = 0; i<numUsers-1; i++) {
			users[i] = tempUsers[i];
			correspUserIDs[i] = tempCorrespUserIDs[i];
		}
		
		
		users[numUsers-1] = User.getCurrUsername(context) + " (me)";
		correspUserIDs[numUsers-1] = userID;
		
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
		if (item.getItemId() == android.R.id.home) {
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

	private class CustomTextWatcher implements TextWatcher {
		private EditText mEditText;
		private boolean first;
		private String original;

		public CustomTextWatcher(EditText e) {
			mEditText = e;
			first = true;
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void afterTextChanged(Editable s) {
			if (first) {
				// The first time it loads it actually sets s to be an empty
				// string.
				// And then it changes it to what was in the editText field
				original = mEditText.getText().toString();
				first = false;
				Log.i(TAG, "Dont save item");
				// Log.i(TAG, "if Original is " + original+ " and s is " +
				// s.toString());
			} else if ((!((original.compareTo(s.toString())) == 0)) && !first) {
				// So it is only after the first time we can actually tell if
				// the item
				// has been updated.
				itemUpdated = true;
				Log.i(TAG, "Save item");
				// Log.i(TAG, "else if Original is " + original+ " and s is " +
				// s.toString());
			} else if (!first) {
				// The user changed back to what was originally there.
				itemUpdated = false;
				Log.i(TAG, "Dont save item");
				// Log.i(TAG, "else Original is " + original+ " and s is " +
				// s.toString());
			}
		}
	}

}
