package edu.pitt.cs1635group3.Adapters;

import java.util.ArrayList;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;

import zebrafish.util.DBHelper;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListItemAdapter extends ArrayAdapter<Item> {
	// This pulls in items and inflates them appropriately for the layout.

	private final ArrayList<Item> items;	// all the items
	private final ArrayList<Item> selected; // track the selected items (checkboxes)
	protected Button assignBtn, completeBtn, inviteBtn; // properly handle button display

	private static final String TAG = "ItemAdapter";
	private DBHelper db;
	protected Context context;

	public MyListItemAdapter(Context c, int textViewResourceId,
			ArrayList<Item> items, Button assignButton, Button completeButton,
			Button inviteButton) {
		super(c, textViewResourceId, items);
		this.context = c;
		this.items = items;
		this.selected = new ArrayList<Item>();
		this.db = new DBHelper(context);
		
		assignBtn  = assignButton;
		completeBtn = completeButton;
		inviteBtn = inviteButton;
	}
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_row, null);
		}

		final Item o = items.get(position);

		if (o != null) {
			TextView name = (TextView) v.findViewById(R.id.item_name);
			TextView assignee = (TextView) v.findViewById(R.id.item_assignee);
			CheckBox cb = (CheckBox) v.findViewById(R.id.check);
			Button b = (Button) v.findViewById(R.id.delete_item_button);
			ImageView img = (ImageView) v.findViewById(R.id.chat_bubble);

			String comments = o.getNotes();
			if (comments != null) {
				if (!comments.equals("")) {
					img.setVisibility(View.VISIBLE);
				}
			}
			
			if (!o.isSelected()) {
				cb.setChecked(false);
			}

			if (name != null) {
				name.setText(o.getName());
				
				if (cb != null && o.isCompleted()) {
					name.setPaintFlags(name.getPaintFlags()
							| Paint.STRIKE_THRU_TEXT_FLAG);
				}
			}
			if (assignee != null) {

			//	int itemUser = o.getAssignee();
				int itemList = o.getParentID();
				db.open();
			//	String assignment = (itemUser > 0 ? db.getUserByID(itemUser)
			//			.getName() : "Unassigned");
				String listName = db.getListName(itemList);
				db.close();
			//	assignee.setText(assignment);
				assignee.setText(listName);

			}

			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						selected.add(o);
						Log.i(TAG, "Adding " +o.getName() + " to selected: " +selected.size());
					} else {
						selected.remove(o);
						Log.i(TAG, "Removing " +o.getName() + " from selected: " +selected.size() );
					}
					handleButtons();
				}
			}); // end onCheckedChangeListener

		} // end if o!= null
		
		return v;
	}
	
	public void handleButtons() {

		if (assignBtn != null && completeBtn != null & inviteBtn != null) {
		
			if (selected.size() == 0) { // button display based on number selected items
				showInvite();
			} else {
				showActionButtons();
			}
		}
	}
	
	public ArrayList<Item> getSelected() { // return an arrayList of the currently-checked items
		Log.i(TAG, "Returning selected items " +selected.size());
		return selected;
	}

	public void showInvite() {
		inviteBtn.setVisibility(View.VISIBLE);
		completeBtn.setVisibility(View.GONE);
		assignBtn.setVisibility(View.GONE);
	}
	
	public void showActionButtons() {
		inviteBtn.setVisibility(View.GONE);
		completeBtn.setVisibility(View.VISIBLE);
		assignBtn.setVisibility(View.VISIBLE);
	}

}