package edu.pitt.cs1635group3;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CustomList implements Parcelable {
	private String name, creationDate, note;
	private int ID;
	protected ArrayList<Item> listItems;

	public CustomList() {
		this.name = null;
		this.ID = -1;
	}

	public CustomList(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	/*
	 * SETTERS
	 */

	public void setNote(String n) {
		this.note = n;
	}
	
	public void setCreationDate(String s) {
		this.creationDate = s;
	}
	
	public void attachItems(ArrayList<Item> children) {
		this.listItems = children;
	}

	/*
	 * GETTERS
	 */


	public int getID() {
		return ID; 
	}

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}
	
	public ArrayList<Item> getItems() {
		return listItems;
	}
	
	public Item getItemAfter(Item item) {
		//return the item in the list directly proceeding Item item
		int i = listItems.indexOf(item);
		int numItems = listItems.size();
		
		if (i==numItems-1) return listItems.get(0);
		else return listItems.get(i+1);
	}
	
	public Item getItemBefore(Item item) {
		//like previous method
		int i = listItems.indexOf(item);
		
		if (i>0) return listItems.get(i-1); // Thoughts on this - If we're doing this lookup often, better to store item's position as item var?
		else return listItems.get(listItems.size()-1); // otherwise pass the last list item
	}

	public void pullItems() {
		listItems = new ArrayList<Item>();
	
		JSONObject json = JSONfunctions.getJSONfromURL("http://www.zebrafishtec.com/server.php", "getItemsForList");
	
		try {
			JSONArray lists = json.getJSONArray("items");
			
			Item item;
			String itemName, assigner, assignee, creationDate, notes, creator, completionDate;
			int itemID, quantity;
			boolean completed;
	
			for (int i = 0; i < lists.length(); i++) {
	
				JSONArray e = lists.getJSONArray(i);
				//ID = String.valueOf(i);
				itemID = e.getInt(0);
				itemName = e.getString(2);
				creator = e.getString(3);
				creationDate = e.getString(4);
				quantity = e.getInt(5);
				assigner = e.getString(6);
				assignee = e.getString(7);
				notes = e.getString(8);
				completed = e.getBoolean(9);
				completionDate = e.getString(10);
	
				item = new Item(itemID, itemName, assigner, assignee, creationDate, notes, quantity, creator, completionDate, completed);
				item.setParent(this.ID);
				listItems.add(item);
				
				Log.d("List item added", "Name: "+itemName);
	
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
	}


	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(ID);
		out.writeString(name);
		out.writeString(note);
		//out.writeTypedList(listItems);
	}

	public static final Parcelable.Creator<CustomList> CREATOR = new Parcelable.Creator<CustomList>() {
		public CustomList createFromParcel(Parcel in) {
			return new CustomList(in);
		}

		public CustomList[] newArray(int size) {
			return new CustomList[size];
		}
	};

	private CustomList(Parcel in) {
		ID = in.readInt();
		name = in.readString();
		note = in.readString();
		//in.readTypedList(listItems, Item.CREATOR);
		//in.readList(listItems,null);
	}

}
