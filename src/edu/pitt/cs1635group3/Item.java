package edu.pitt.cs1635group3;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Item implements Parcelable {

	private String name, creationDate, notes, completionDate;
	private int parentID, ID, quantity, assigner, assignee, creator; // use
																		// parentID
																		// to
																		// tie
																		// to
																		// list
	private boolean selected, completed;
	private int nextID, prevID; // Linked-list format. necessary to make "next"
								// and

	// "prev" buttons work on item details screen

	public Item() {
	}

	public Item(HashMap<String, String> item) {
		name = item.get("name");
		assignee = -1;
	}

	public Item(JSONObject e) {

		try {
			ID = e.getInt("id");
			parentID = e.getInt("parent_id");
			name = e.getString("name");
			creator = e.getInt("adder_id");
			creationDate = e.getString("add_date");
			quantity = e.getInt("quantity");
			assignee = e.getInt("assignee_id");
			assigner = e.getInt("assigner_id");
			notes = e.getString("notes");
			prevID = -1;
			nextID = -1;

			int isCompleted = e.getInt("completed");
			completed = (isCompleted == 1 ? true : false);

			completionDate = e.getString("completion_date");

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

	public int getAssigner() {
		return assigner;
	}

	public int getAssignee() {
		return assignee;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public int getCreator() {
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

	public int getNext() {
		return nextID;
	}

	public int getPrev() {
		return prevID;
	}

	/*
	 * SETTERS
	 */

	public void setID(int i) {
		this.ID = i;
	}

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

	public void setAssigner(int i) {
		this.assigner = i;
	}

	public void assignTo(int i) {
		this.assignee = i;
	}

	public void setQuantity(int q) {
		this.quantity = q;
	}

	public void setNotes(String n) {
		this.notes = n;
	}

	public void setCreator(int i) {
		this.creator = i;
	}

	public void setCompleted(boolean b) {
		this.completed = b;
	}

	public void setCompleted(int i) {
		this.completed = (i == 1 ? true : false);
	}

	public void setCompleted() {
		this.completed = true;
	}

	public void setCreationDate(String string) {
		this.creationDate = string;
	}

	public void setCompletionDate(String string) {
		this.completionDate = string;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setSelected(int i) {
		this.selected = (i == 1 ? true : false);
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
		out.writeInt(assigner);
		out.writeInt(assignee);
		out.writeString(creationDate);
		out.writeString(notes);
		out.writeInt(quantity);
		out.writeInt(creator);
		out.writeString(completionDate);
		out.writeString("" + completed);
		out.writeInt(prevID);
		out.writeInt(nextID);

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
		assigner = in.readInt();
		assignee = in.readInt();
		creationDate = in.readString();
		notes = in.readString();
		quantity = in.readInt();
		creator = in.readInt();
		completionDate = in.readString();

		String strCompleted = in.readString();
		if (strCompleted.equals("true"))
			completed = true;
		else
			completed = false;

		prevID = in.readInt();
		nextID = in.readInt();
	}

}
