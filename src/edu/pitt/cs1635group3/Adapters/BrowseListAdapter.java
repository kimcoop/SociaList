package edu.pitt.cs1635group3.Adapters;

import java.util.ArrayList;

import zebrafish.util.DBHelper;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.Item;

public class BrowseListAdapter extends ArrayAdapter<CustomList> {
	// This pulls in the user's lists and inflates them appropriately for the
	// layout.

	private final ArrayList<CustomList> lists;

	public BrowseListAdapter(Context context, int textViewResourceId,
			ArrayList<CustomList> lists) {
		super(context, textViewResourceId, lists);
		this.lists = lists;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		CustomList o = (CustomList) lists.get(position);

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_row, null);
			holder = new ViewHolder();
			holder.title = (TextView) v.findViewById(R.id.element_title);
			holder.subtitle = (TextView) v.findViewById(R.id.element_subtitle);
			// holder.delete = (Button) v.findViewById(R.id.delete_list_button);

			v.setTag(holder);

		} else {
			holder = (ViewHolder) v.getTag(); // get the viewholder back for
												// faster access
		}

		if (o != null) {
			TextView name = (TextView) v.findViewById(R.id.element_title);
			TextView note = (TextView) v.findViewById(R.id.element_subtitle);
			// Button deleter = (Button)
			// v.findViewById(R.id.delete_list_button);

			if (name != null)
				name.setText("" + o.getName());
			if (note != null) { 

				int numItems = 0;
				String summary = "", pluralizer = " items";
				numItems = o.getItems().size();

				if (numItems == 1)
					pluralizer = " item";
				
				summary = numItems + pluralizer;
				note.setText(summary);

			}
		}

		return v;
	}

	static class ViewHolder {
		TextView title, subtitle;
		Button delete;
	}

}