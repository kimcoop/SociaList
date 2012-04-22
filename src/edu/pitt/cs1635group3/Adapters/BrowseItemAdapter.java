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

public class BrowseItemAdapter extends ArrayAdapter<Item> {
	// This pulls in items and inflates them appropriately for the layout.

	private final ArrayList<Item> items;	// all the items
	protected Button addBtn, completeBtn, inviteBtn; // properly handle button display

	private static final String TAG = "BrowseItemAdapter";
	private DBHelper db;
	protected Context context;

	public BrowseItemAdapter(Context c, int textViewResourceId,
			ArrayList<Item> items) {
		super(c, textViewResourceId, items);
		this.context = c;
		this.items = items;
		this.db = new DBHelper(context);
		
	}
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.browse_item_row, null);
		}

		final Item o = items.get(position);

		if (o != null) {
			TextView name = (TextView) v.findViewById(R.id.item_name);
			//TextView assignee = (TextView) v.findViewById(R.id.item_assignee);
			//CheckBox cb = (CheckBox) v.findViewById(R.id.check);
			//Button b = (Button) v.findViewById(R.id.delete_item_button);
			//ImageView img = (ImageView) v.findViewById(R.id.chat_bubble);

			//String comments = o.getNotes();
			//cb.setVisibility(View.GONE);
			//assignee.setVisibility(View.GONE);
			

			if (name != null) {
				name.setText(o.getName());
					
			}
			

			

		} // end if o!= null
		
		return v;
	}
	
	

}