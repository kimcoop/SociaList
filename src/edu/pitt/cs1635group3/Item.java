package edu.pitt.cs1635group3;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Item implements Parcelable {

	private String name, assigner, assignee, creationDate, notes, creator,
			completionDate;
	private int parentID, ID, quantity; // use parentID to tie to list
	private boolean selected, completed;
	private int nextID, prevID; // Linked-list format. necessary to make "next" and
							// "prev" buttons work on item details screen

	public Item(int id, String name, String a1, String a2, String c1, String n,
			int q, String c2, String c_date, boolean complete) {
		this.ID = id;
		this.name = name;
		this.assigner = a1;
		this.assignee = a2;
		this.creationDate = c1;
		this.notes = n;
		this.creator = c2;
		this.completionDate = c_date;
		this.quantity = q;
		this.completed = complete;
		selected = false;
	}

	public Item(JSONArray e) {

		try {
			ID = e.getInt(0);
			name = e.getString(2);
			creator = e.getString(3);
			creationDate = e.getString(4);
			quantity = e.getInt(5);
			assigner = e.getString(6);
			assignee = e.getString(7);
			notes = e.getString(8);
			completed = e.getBoolean(9);
			completionDate = e.getString(10);
		} catch (JSONException e1) {
			Log.i("Item parse problem", e1.toString());
		}

	}

	public Item(String name) { // for now, when a user submits a new list item,
								// this is what's called.
		// eventually switch to next method (after getting parent list ID)
		this.name = name;

	}

	public Item(String name, int parent) {
		// this is what we want to use
		this.name = name;
		this.parentID = parent;
	}

	/*
	 * GETTERS
	 */

	public int getID() {
		return ID;
	}

	public int getParentID() {
		return parentID;
	}

	public String getName() {
		return name;
	}

	public String getAssigner() {
		return assigner;
	}

	public String getAssignee() {
		return assignee;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public String getCreator() {
		return creator;
	}

	public String getCompletionDate() {
		return completionDate;
	}

	public String getNotes() {
		return notes;
	}

	public int getQuantity() {
		return quantity;
	}

	public boolean isCompleted() {
		return completed;
	}

	public boolean isSelected() {
		return selected;
	}

	public Item getNext() {
		//Query DB
		
		
		return null;
	}

	public Item getPrev() {
		//Query DB
		
		return null;
	}

	/*
	 * SETTERS
	 */
	public void setNext(int i) {
		this.nextID = i;
	}

	public void setPrev(int i) {
		this.prevID = i;
	}

	public void setParent(int parent) {
		this.parentID = parent;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void assignTo(String name) {
		this.assignee = name;
	}

	public void setQuantity(int q) {
		this.quantity = q;
	}

	public void setNotes(String n) {
		this.notes = n;
	}

	public void setCompleted() {
		this.completed = true;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable Method like Serializable but for mobile -
	 * "way" faster too
	 */

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(ID);
		out.writeString(name);
		out.writeString(assigner);
		out.writeString(assignee);
		out.writeString(creationDate);
		out.writeString(notes);
		out.writeInt(quantity);
		out.writeString(creator);
		out.writeString(completionDate);
		out.writeString("" + completed);

	}

	// Regenerate the object. All Parcelables must have a CREATOR that
	// implements these two methods
	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

	// Constructor that takes a Parcel and gives you an object populated with
	// its values
	private Item(Parcel in) {
		ID = in.readInt();
		name = in.readString();
		assigner = in.readString();
		assignee = in.readString();
		creationDate = in.readString();
		notes = in.readString();
		quantity = in.readInt();
		creator = in.readString();
		completionDate = in.readString();

		String strCompleted = in.readString();
		if (strCompleted.equals("true"))
			completed = true;
		else
			completed = false;
	}

}
