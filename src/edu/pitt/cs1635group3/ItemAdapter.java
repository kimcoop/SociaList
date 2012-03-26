package edu.pitt.cs1635group3;

import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ItemAdapter extends ArrayAdapter<Item> {
	// This pulls in items and inflates them appropriately for the layout.

	private final ArrayList<Item> items;
	Button assign_button;
	Button complete_button;
	Button invite_button;
	boolean inviteUp;
	int checkedItems = 0;

	DBHelper db;

	public ItemAdapter(Context context, int textViewResourceId,
			ArrayList<Item> items, Button assign_button,
			Button complete_button, Button invite_button, boolean inviteUp) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.assign_button = assign_button;
		this.complete_button = complete_button;
		this.invite_button = invite_button;
		this.inviteUp = inviteUp;

		db = new DBHelper(getContext());
		db.open();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_row, null);
		}

		Item o = items.get(position);
		CheckBox cb;

		if (o != null) {
			TextView name = (TextView) v.findViewById(R.id.item_name);
			TextView assignee = (TextView) v.findViewById(R.id.item_assignee);
			cb = (CheckBox) v.findViewById(R.id.check);
			// TextView snippit = (TextView) v.findViewById(R.id.item_snippit);

			if (!o.isSelected()) cb.setChecked(false); // This solves the jumping problem [facepalm]
			
			if (name != null) {
				name.setText(o.getName());

				if (o.isCompleted()) {
					name.setPaintFlags(name.getPaintFlags()
							| Paint.STRIKE_THRU_TEXT_FLAG);
				}

			}

			if (assignee != null) {

				int userID = o.getAssignee();
				String assignment = (userID > 0 ? db.getUserByID(userID)
						.getName() : "Unassigned");
				assignee.setText(assignment);

			}

			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {

					if (isChecked) {
						checkedItems++;
					} else {
						checkedItems--;
					}
					if (checkedItems == 0) {
						invite_button.setVisibility(View.VISIBLE);
						complete_button.setVisibility(View.GONE);
						assign_button.setVisibility(View.GONE);

					} else if (checkedItems > 0) {
						invite_button.setVisibility(View.GONE);
						complete_button.setVisibility(View.VISIBLE);
						assign_button.setVisibility(View.VISIBLE);

					}
					// items.get(pos)
					Item activeItem = items.get(position);
					Log.d("ACTIVE ITEM", "After cb click, active item is "
							+ activeItem.getName());

					// activeItem = db.getItem(activeItem.getID());
					activeItem.setSelected(isChecked);
					db.updateItem(activeItem); // the item needs to be marked as
												// selected in the db so
												// InsideListActivity can
												// identify it as needing to be
												// acted upon for assign or
												// selected

				}
			}); // end onCheckedChangeListener

			/*
			 * String note = o.getNotes(); if (snippit != null && note != null)
			 * { snippit.setVisibility(View.VISIBLE); snippit.setText(note); }
			 */// TODO - in item_row.xml, use this field for note snippit

		}
		return v;
	}

	public void flipButtons() {

		if (inviteUp) {
			invite_button.setVisibility(View.GONE);
			complete_button.setVisibility(View.VISIBLE);
			assign_button.setVisibility(View.VISIBLE);
			inviteUp = false;
		} else {
			invite_button.setVisibility(View.VISIBLE);
			complete_button.setVisibility(View.GONE);
			assign_button.setVisibility(View.GONE);

			// assign_button.setVisibility(View.VISIBLE); // remove this for
			// final
			// product (testing now)
			inviteUp = true;
		}
	}

}