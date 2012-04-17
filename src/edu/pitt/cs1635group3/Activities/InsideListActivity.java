package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONfunctions;
import zebrafish.util.UIUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Adapters.InviteAdapter;
import edu.pitt.cs1635group3.Adapters.ItemAdapter;

public class InsideListActivity extends SherlockListActivity {

	private CustomList list = null;
	private ArrayList<Item> items = null;
	private ArrayAdapter<Item> adapter;
	private RelativeLayout parentLayout;

	private static final String TAG = "InsideListActivity";

	private int totalItems;
	protected Context context;

	protected Button assignButton, completeButton, inviteButton;
	private View buttonsHelper;
	private ListView lv;
	private boolean newItems = false;
	private static int userID;
	private ArrayList<Integer> newItemPKs;

	private CharSequence users[];
	private ArrayList<Item> selected;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insidelist_layout);
		context = this;
		userID = User.getCurrUser(context);
		parentLayout = (RelativeLayout) findViewById(R.id.insidelist_parent); // used for deletions
		newItemPKs = new ArrayList<Integer>(); // used for additions

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		list = CustomList.getListByID(context, extras.getInt("ListID"));
		items = CustomList
				.getItemsForListByID(context, extras.getInt("ListID"));
		totalItems = items.size();

		getSupportActionBar();
		setTitle(list.getName());

		selected = new ArrayList<Item>(); // track the items checked. used for button click events

		assignButton = (Button) findViewById(R.id.assign_button);
		assignButton.setOnClickListener(new Button.OnClickListener() {  
			public void onClick(View v) {
				assign();
			}
		});
		completeButton = (Button) findViewById(R.id.complete_button);
		completeButton.setOnClickListener(new Button.OnClickListener() {  
			public void onClick(View v) {
				complete();
			}
		});
		inviteButton = (Button) findViewById(R.id.invite_button);
		inviteButton.setOnClickListener(new Button.OnClickListener() {  
			public void onClick(View v) {
				invite();
			}
		});
		buttonsHelper = (View) findViewById(R.id.buttons_helper);
		lv = getListView();

		users = User.getUsersForDialog(context, list.getID());

		adapter = new ItemAdapter(this, R.layout.item_row, items, assignButton, completeButton, inviteButton);
		lv.setTextFilterEnabled(true);
		lv.setClickable(true);
		setListAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentView, final View v, int pos,
					long id) {

				Item item = adapter.getItem(pos);

				if (items.size() == 1) {
					item.setPrev(context, item.getID());
					item.setNext(context, item.getID());
				}
					
				if (newItems) {
					list.attachItems(items);
					Log.i(TAG, "Back button pressed");
					int listSize = list.getItems().size();
					totalItems = listSize;
					Log.d(TAG, "listSize = " + listSize);
					list.setLinks(context);
				}
				Intent intent = new Intent(context, ItemActivity.class);
				intent.putExtra("ItemID", item.getID());
				intent.putExtra("pos", pos + 1); // Display item X of Y
				intent.putExtra("totalItems", totalItems);

				startActivityForResult(intent, 1);
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int pos, long id) {
				final View parentView = v;
				final int position = pos;
				final Item item = items.get(position);
				final String itemName = item.getName();

				final Button b = (Button) parentView
						.findViewById(R.id.delete_item_button);
				b.setVisibility(View.VISIBLE);

				b.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						totalItems--;

						int prevPos, nextPos;
						if (position == items.size() - 1)
							nextPos = 0;
						else
							nextPos = position + 1;

						if (position == 0)
							prevPos = items.size() - 1;
						else
							prevPos = position - 1;

						Item nextItem, prevItem;
						nextItem = items.get(nextPos);
						prevItem = items.get(prevPos);

						int next = nextItem.getID();
						int prev = prevItem.getID();

						prevItem.setNext(context, next);
						nextItem.setPrev(context, prev);

						if (totalItems == 1) { // always this issue with one
							// item in list
							item.setNext(context, item.getID());
							item.setPrev(context, item.getID());
						}

						Item.deleteItem(context, item);

						UIUtil.showMessage(context, "Item deleted");
						b.setVisibility(View.GONE);
						parentLayout.removeView(parentView);
						adapter.remove(item);
						adapter.notifyDataSetChanged();

					}
				});

				return true;
			}
		});

	}// end onCreate

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) { // force refresh the view
			startActivity(getIntent());
			finish();
		}
	}

	public void saveRename(String newName) {

		list.updateName(context, newName);
		setTitle(newName);

	}

	public void rename() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Rename List");

		final EditText input = new EditText(this); // Set an EditText view to
		// get user input
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				saveRename(value);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();

	}

	@Override
	public void onBackPressed() {

		if (newItems) {
			list.attachItems(items);
			Log.i(TAG, "Back button pressed");
			int listSize = list.getItems().size();
			Log.d(TAG, "listSize = " + listSize);
			list.setLinks(context);
		}
		Intent in = new Intent();
		setResult(1, in);// Requestcode 1. Tell parent activity to refresh
		// items.

		finish();
		super.onBackPressed();
	}

	public void assignItemsTo(String user) {
		selected = getSelectedItems();
		
		int assignee = User.getUserByName(context, user); // this is weak - Kim

		for (Item item : selected) {
			Log.i(TAG, "Assigning " +item.getName() + " to " +assignee);
			item.assignTo(context, assignee);
		}

		adapter.notifyDataSetChanged();
	}

	public void assign() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Assign To");
		builder.setItems(users, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int pos) {
				assignItemsTo((String) users[pos]);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void uncomplete() {
		// TODO - test to see if all selected items are uncompleted
		selected = getSelectedItems();

		for (Item item : selected) {
			// item.setUnCompleted(context); TODO (easy)
		}

		adapter.notifyDataSetChanged();
	}

	public void complete() {
		selected = getSelectedItems();

		for (Item item : selected) {
			item.setCompleted(context);
		}
		adapter.notifyDataSetChanged();
	}

	public ArrayList<Item> getSelectedItems() {
		// called multiple times because there are many actions that require use
		// of selected items
		selected = ((ItemAdapter) adapter).getSelected();
		return selected;
	}

	public void invite() {

		Intent i = getIntent();
		Bundle extras = i.getExtras();

		Intent intent = new Intent(getBaseContext(), InviteActivity.class);
		intent.putExtra("ListID", extras.getInt("ListID"));
		Log.i(TAG, "ListID: " + extras.getInt("ListID"));
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == 0) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_add) {
			addItem();
			return false;
		} else if (item.getItemId() == R.id.menu_rename) {
			rename();
			return true;
		} else if (item.getItemId() == R.id.menu_manage_users) {
			intent = new Intent(this, ManageListUsersActivity.class);
			intent.putExtra("listID", list.getID());
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_invite) {
			intent = new Intent(this, InviteActivity.class);
			startActivity(intent);
			return true;
		} else {
			return false;
		}
	}

	public void addItem() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("New List Item Name");

		final EditText input = new EditText(this); // Set an EditText view to
		// get user input
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				Item i = new Item();

				i.setName(value);
				i.setCreator(userID);
				i.setParent(list.getID());

				int itemID = JSONfunctions.getItemPK();
				newItemPKs.add(itemID);
				i.setID(context, itemID);

				items.add(i);

				adapter.notifyDataSetChanged();
				newItems = true;
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		// alert.show();
		AlertDialog dialog = alert.create();
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();

	}

}