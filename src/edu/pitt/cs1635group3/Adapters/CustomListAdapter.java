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

public class CustomListAdapter extends ArrayAdapter<CustomList> {
	// This pulls in the user's lists and inflates them appropriately for the
	// layout.

	private final ArrayList<CustomList> lists;
	private DBHelper db = new DBHelper(getContext());

	public CustomListAdapter(Context context, int textViewResourceId,
			ArrayList<CustomList> lists) {
		super(context, textViewResourceId, lists);
		this.lists = lists;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder; // When convertView is not null, we can reuse it
							// directly, there is
		// no need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.

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
				db.open();

				ArrayList<Item> items = db.getItemsForListByID(o.getID());

				Log.d("GET ITEMS FOR LIST", "List ID " + o.getID() + " has "
						+ items.size() + " children.");

				int numItems = 0, completedItems = 0, unassignedItems = 0;
				String summary = "", pluralizer = " items";
				for (Item i : items) {
					numItems++;
					if (i.isCompleted())
						completedItems++;
					if (i.getAssignee() <= 0)
						unassignedItems++;
				}

				if (numItems == 1)
					pluralizer = " item";
				summary = numItems + pluralizer + " (" + completedItems
						+ " completed, " + unassignedItems + " unassigned)";
				note.setText(summary);

				db.close();

			}
		}

		return v;
	}

	static class ViewHolder {
		TextView title, subtitle;
		Button delete;
	}

}