package edu.pitt.cs1635group3.Adapters;

import java.util.ArrayList;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Invite;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;

import zebrafish.util.DBHelper;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class InviteAdapter extends ArrayAdapter<Invite> {

	private final ArrayList<Invite> invites;
	private final ArrayList<Invite> selected;
	private final static String TAG = "InviteAdapter";
	private DBHelper db;

	public InviteAdapter(Context context, int textViewResourceId,
			ArrayList<Invite> invites) {
		super(context, textViewResourceId, invites);
		this.invites = invites;
		this.selected = new ArrayList<Invite>(); // populated on any checkbox action (removed when unchecked)
		this.db = new DBHelper(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		final Invite o = (Invite) invites.get(position);

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.generic_row, null);
			holder = new ViewHolder();
			holder.element_title = (TextView) v
					.findViewById(R.id.element_title);
			holder.element_subtitle = (TextView) v
					.findViewById(R.id.element_subtitle);
			holder.element_checkbox = (CheckBox) v
					.findViewById(R.id.element_checkbox);
			
			holder.element_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
						if (isChecked) selected.add(o);
						else selected.remove(o);
					}
				});

			v.setTag(holder);

		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (o != null) {
			TextView title = (TextView) v.findViewById(R.id.element_title);
			TextView subtitle = (TextView) v
					.findViewById(R.id.element_subtitle);
			CheckBox cb = (CheckBox) v.findViewById(R.id.element_checkbox);

			if (title != null) {
				title.setText(o.listID + " is the list ID");
			}

			if (subtitle != null) {
				subtitle.setText(o.inviteDate + " is the date");
			}

			if (cb != null) {
				cb.setChecked(false); // by default, items are not selected
			}
		} // end if (o != null)

		return v;
	}

	static class ViewHolder {
		TextView element_title, element_subtitle;
		CheckBox element_checkbox;
		
	}
	
	public ArrayList<Invite> getSelected() { // return an arrayList of the currently-checked items
		Log.i(TAG, "Returning selected items " +selected.size());
		return selected;
		
	}

}