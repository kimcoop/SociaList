package edu.pitt.cs1635group3.Adapters;

import java.util.ArrayList;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.CustomList;
import edu.pitt.cs1635group3.Activities.Classes.User;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;

import zebrafish.util.DBHelper;
import zebrafish.util.DateUtil;
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

public class ListUsersAdapter extends ArrayAdapter<User> {

	private final ArrayList<User> users;
	private final ArrayList<User> selected;
	private final static String TAG = "ListUserAdapter";
	private DBHelper db;
	private static Context context;

	public ListUsersAdapter(Context context, int textViewResourceId,
			ArrayList<User> users) {
		super(context, textViewResourceId, users);
		this.users = users;
		this.context = context;
		this.selected = new ArrayList<User>(); // populated on any checkbox action (removed when unchecked)
		this.db = new DBHelper(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		final User o = (User) users.get(position);

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
				title.setText(o.getName());
			}

			if (subtitle != null) {
				//long longDate = Long.parseLong(o.getUserDate());
				//subtitle.setText(DateUtil.formatDate(longDate, context)); TODO
				subtitle.setText("6 items");
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
	
	public ArrayList<User> getSelected() { // return an arrayList of the currently-checked items
		Log.i(TAG, "Returning selected items " +selected.size());
		return selected;
		
	}

}