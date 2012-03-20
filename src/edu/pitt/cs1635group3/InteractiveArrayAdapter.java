package edu.pitt.cs1635group3;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class InteractiveArrayAdapter extends ArrayAdapter<Item> {

	private final List<Item> list;
	private final Activity context;

	public InteractiveArrayAdapter(Activity context, List<Item> list) {
		super(context, R.layout.insidelistplaceholder, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
		protected TextView subtext;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.insidelistplaceholder, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.label);
			
			viewHolder.text.setClickable(true);
			viewHolder.subtext = (TextView) view.findViewById(R.id.sublabel);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							Item element = (Item) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());

						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).getName());
		holder.subtext.setText(list.get(position).getAssignee());
		holder.checkbox.setChecked(list.get(position).isSelected());
		return view;
	}
}