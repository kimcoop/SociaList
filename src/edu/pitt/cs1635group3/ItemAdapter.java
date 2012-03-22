package edu.pitt.cs1635group3;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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

public class ItemAdapter extends ArrayAdapter<Item> {
	//This pulls in items and inflates them appropriately for the layout.
	
	
	private final ArrayList<Item> items;

	public ItemAdapter(Context context, int textViewResourceId, ArrayList<Item> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
        if (v == null) {
        	LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_row, null);
        }
        Item o = items.get(position);
        if (o != null) {
                TextView name = (TextView) v.findViewById(R.id.item_name);
                TextView assignee = (TextView) v.findViewById(R.id.item_assignee);
                if (name != null)
                      name.setText(o.getName());
                if(assignee != null)
                	assignee.setText(o.getAssignee());
        }
        
        return v;
	}
}