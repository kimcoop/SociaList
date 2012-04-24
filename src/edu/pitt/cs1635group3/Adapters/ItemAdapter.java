package edu.pitt.cs1635group3.Adapters;

import java.util.ArrayList;

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
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class ItemAdapter extends ArrayAdapter<Item> {
	// This pulls in items and inflates them appropriately for the layout.

	private final ArrayList<Item> items; // all the items
	private ArrayList<Item> selected; // track the selected items
											// (checkboxes)
	protected Button assignBtn, completeBtn; // properly handle
														// button display

	private static final String TAG = "ItemAdapter";
	private DBHelper db;
	protected Context context;
	private boolean isCompleted;

	public ItemAdapter(Context c, int textViewResourceId,
			ArrayList<Item> items, Button assignButton, Button completeButton) {
		super(c, textViewResourceId, items);
		this.context = c;
		this.items = items;
		this.selected = new ArrayList<Item>();
		this.db = new DBHelper(context);

		assignBtn = assignButton;
		completeBtn = completeButton;
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
			CheckBox cb = (CheckBox) v.findViewById(R.id.check);
			TextView name = (TextView) v.findViewById(R.id.item_name);
			TextView assignee = (TextView) v.findViewById(R.id.item_assignee);
			ImageView b = (ImageView) v.findViewById(R.id.delete_item_button);
			ImageView img = (ImageView) v.findViewById(R.id.chat_bubble);

			String comments = o.getNotes();
			if (comments != null) {
				if (!comments.equals("")) {
					img.setVisibility(View.VISIBLE);
				} else {
					img.setVisibility(View.INVISIBLE);
				}
			}

			if (!o.isSelected()) {
				cb.setChecked(false);
			}

			if (name != null) {
				name.setText(o.getName());

				if (o.isCompleted()) {
					name.setPaintFlags(name.getPaintFlags()
							| Paint.STRIKE_THRU_TEXT_FLAG);
				} else {
					name.setPaintFlags(name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
				}
			}
			if (assignee != null) {

				int itemUser = o.getAssignee();
				String assignment;

				if (itemUser > 0) {
					assignment = User.getUserByID(context, itemUser);
				} else {
					assignment = "Unassigned";
				}

				assignee.setText(assignment);

			}

			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					View row = ((View) buttonView.getParent().getParent());
					if (isChecked) {
						row.setBackgroundResource(R.color.turquoise_bg_light);
						selected.add(o);
					} else {
						row.setBackgroundResource(R.color.transparent);
						selected.remove(o);
					}
					
					if (o.isCompleted()){
						//isCompleted = true;
					} else {
						isCompleted = false;
					}
					handleButtons();
				}
			}); // end onCheckedChangeListener

		} // end if o!= null

		return v;
	}

	public void handleButtons() {

		if (assignBtn != null && completeBtn != null) {

			if (selected.size() == 0) { // button display based on number
										// selected items
				disableButtons();
				isCompleted = true;
				
			} else {
				if(isCompleted){
					completeBtn.setText("Mark Incomplete");
				}else{
					completeBtn.setText("Mark Complete");
				}
				showActionButtons();
			}
		}
	}

	public ArrayList<Item> getSelected() { // return an arrayList of the
											// currently-checked items
		Log.i(TAG, "Returning selected items " + selected.size());
		return selected;
	}

	public void disableButtons() {
		completeBtn.setEnabled(false);
		assignBtn.setEnabled(false);
	}

	public void showActionButtons() {
		completeBtn.setEnabled(true);
		assignBtn.setEnabled(true);
	}
	

	
	public boolean allSelected() {
		return selected.size() == items.size();
	}

	public void selectAll() {
		Log.i(TAG, "selectingAll");
		
		selected = new ArrayList<Item>(); // empty it first
		
		for (Item i : items) {
			Log.i(TAG, "adding inv to selected");
			selected.add(i);
		}
		
		
	} // end selectAll
	
	public void deselectAll() {
		Log.i(TAG, "deselectingAll");
		
		selected = new ArrayList<Item>(); // empty it first
		
		
	} // end deselectAll

}