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
	private int ID, creatorID;
	private int populated;
	protected ArrayList<Item> listItems;

	public CustomList() {
		listItems = new ArrayList<Item>();
	}

	public CustomList(int ID, String name) {
		this.ID = ID;
		this.name = name;
		this.populated = 0;
		listItems = new ArrayList<Item>();
	}

	/*
	 * SETTERS
	 */

	public void setName(String n) {
		this.name = n;
	}

	public void setID(int i) {
		this.ID = i;
	}

	public void setNote(String n) {
		this.note = n;
	}

	public void setPopulated(int t) {
		this.populated = t;
	}

	public void setCreationDate(String s) {
		this.creationDate = s;
	}

	public void setCreator(int i) {
		this.creatorID = i;
	}

	public void deleteItem(Item i) {
		this.listItems.remove(i);
	}

	public void addItem(Item i) {
		this.listItems.add(i);
	}

	public void attachItems(ArrayList<Item> children) {
		if (listItems == null) {
			Log.i("IN ATTACHITEMS()", "listItems == null");
			listItems = new ArrayList<Item>(children.size());
		}
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

	public String getCreationDate() {
		return creationDate;
	}

	public int getCreator() {
		return creatorID;
	}

	public ArrayList<Item> getItems() {
		// if(listItems.size() == 0){
		// return null;
		// }
		// else{
		return listItems;
		// }
	}

	public Item getItem(int i) {

		if (listItems != null && i < listItems.size()) {
			Log.d("CustomList", "Returning list item " + listItems.get(i));
			return listItems.get(i);
		} else {
			Log.d("CustomList", "No Item at index = " + i);
			return null;
		}
	}

	public int isPopulated() {
		return this.populated;
	}

	public Item getLastItem() {
		return listItems.get(listItems.size() - 1);
	}

	public Item getItemAfter(Item item) {
		// return the item in the list directly proceeding Item item
		int i = listItems.indexOf(item);
		int numItems = listItems.size();

		if (i == numItems - 1)
			return listItems.get(0);
		else
			return listItems.get(i + 1);
	}

	public Item getItemBefore(Item item) {
		// like previous method
		int i = listItems.indexOf(item);

		if (i > 0)
			return listItems.get(i - 1);
		else
			return listItems.get(listItems.size() - 1); // otherwise pass the
														// last list item
	}

	public void pullItems() {

		this.populated = 1;

		listItems = new ArrayList<Item>();

		JSONObject json = JSONfunctions.getJSONfromURL("getItemsForList", ""
				+ this.ID); // must pass the list ID as a String

		try {
			JSONArray lists = json.getJSONArray("items");

			JSONObject e1, e2;
			Item item1, item2;

			for (int i = 0; i < lists.length(); i++) {

				if (i == 0) { // do the items two at a time in order to set
								// prev
								// and next for each
					e1 = lists.getJSONObject(i);
					item1 = new Item(e1);
					item1.setParent(this.ID);

					e2 = lists.getJSONObject(i + 1);
					item2 = new Item(e2);
					item2.setParent(this.ID);

					item2.setPrev(item1.getID());
					item1.setNext(item2.getID());
					listItems.add(item1);
					listItems.add(item2);
					Log.i("LINKING", "Item name " + item2.getName()
							+ " has previous item " + item1.getName() + " ID "
							+ item1.getID());

					i += 1;

				} else {
					e1 = lists.getJSONObject(i);
					item1 = new Item(e1);
					item1.setParent(this.ID);

					Item prev = listItems.get(i - 1);

					prev.setNext(item1.getID());
					item1.setPrev(prev.getID());

					Log.i("LINKING", "Item name " + item1.getName()
							+ " has previous item " + prev.getName() + " ID "
							+ prev.getID());
					listItems.add(item1);

				}
			}

			listItems.get(0).setPrev(
					listItems.get(listItems.size() - 1).getID()); // "Loop around":

			listItems.get(listItems.size() - 1).setNext(
					listItems.get(0).getID()); // and

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
		out.writeInt(populated);
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
		populated = in.readInt();
	}

}