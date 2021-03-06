package edu.pitt.cs1635group3.Activities.Classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import zebrafish.util.DBHelper;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Item implements Parcelable {

	private static DBHelper db;
	private static final String TAG = "ITEM";

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

	public static Item templatize(JSONObject e) {
		
		Item i = new Item();
		
		try {
			i.ID = e.getInt("id");
			i.name = e.getString("name");

		} catch (JSONException e1) {
			Log.i("Item templatize parse problem", e1.toString());
		}
		
		return i;
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

	public void update(Context context) { // General method used for any updates

		db = new DBHelper(context);
		db.open();
		db.updateItem(this);
		db.close();
	}

	public void setID(Context context, int i) {
		this.ID = i;
		update(context);
	}

	public void setNext(Context context, int i) {
		this.nextID = i;
		update(context);
	}

	public void setPrev(Context context, int i) {
		this.prevID = i;
		update(context);
	}

	public void setParent(Context context, int parent) {
		this.parentID = parent;
		update(context);
	}

	public void setName(Context context, String name) {
		this.name = name;
		update(context);
	}

	public void setAssigner(Context context, int i) {
		this.assigner = i;
		update(context);
	}

	public void assignTo(Context context, int i) {
		this.assignee = i;
		update(context);
	}

	public void setQuantity(Context context, int q) {
		this.quantity = q;
		update(context);
	}

	public void setNotes(Context context, String n) {
		this.notes = n;
		update(context);
	}

	public void setCreator(Context context, int i) {
		this.creator = i;
		update(context);
	}

	public void setCompleted(Context context, boolean b) {
		this.completed = b;
		update(context);
	}

	public void setCompleted(Context context, int i) {
		this.completed = (i == 1 ? true : false);
		update(context);
	}

	public void setCompleted(Context context) {
		this.completed = true;
		update(context);
	}
	public void setUnCompleted(Context context) {
		this.completed = false;
		update(context);
	}

	public void setCreationDate(Context context, String string) {
		this.creationDate = string;
		update(context);
	}

	public void setCompletionDate(Context context, String string) {
		this.completionDate = string;
		update(context);
	}

	public void setSelected(Context context, boolean selected) {
		this.selected = selected;
		update(context);
	}

	public void setSelected(Context context, int i) {
		this.selected = (i == 1 ? true : false);
		update(context);
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

	/*
	 * THESE METHODS ARE DEPRECATED USE ABOVE METHODS
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
	 * CLASS METHODS
	 */

	public static void insertOrUpdateItems(Context context,
			ArrayList<Item> items, boolean pushToCloud) {
		db = new DBHelper(context);
		db.open();

		for (Item item : items) {
			db.insertOrUpdateItem(item, pushToCloud);
		}
		db.close();

	}

	public static void deleteItem(Context context, Item item) {
		db = new DBHelper(context);
		db.open();
		db.deleteItem(item);
		db.close();
	}

	public static ArrayList<Item> getAllItemsForUser(Context context) {
		int uid = User.getCurrUser(context);
		db = new DBHelper(context);
		db.open();
		ArrayList<Item> items = db.getAllItemsByUserID(uid);
		db.close();
		return items;
	}

}
