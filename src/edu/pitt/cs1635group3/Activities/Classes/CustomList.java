package edu.pitt.cs1635group3.Activities.Classes;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


import zebrafish.util.DBHelper;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CustomList implements Parcelable {
	private String name, customID, creationDate, note;
	private int ID, creatorID;
	private int populated;
	protected ArrayList<Item> listItems;
	public boolean NO_PUSH_TO_CLOUD = false;

	private static DBHelper db;

	private static final String TAG = "CUSTOM LIST";

	public CustomList(JSONObject e) {

		try {
			ID = e.getInt("id");
			customID = e.getString("custom_id");
			name = e.getString("name");
			creatorID = e.getInt("creator_id");
			creationDate = e.getString("creation_date");
			note = e.getString("note");
		} catch (JSONException e1) {
			Log.i(TAG, e1.toString());
		}
	}

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

	public void setCustomID(String str) {
		this.customID = str;
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
			Log.i(TAG, "IN ATTACHITEMS(), listItems == null");
			listItems = new ArrayList<Item>(children.size());
		} else {
			Log.i(TAG, "AttachItems(): ListItems was not null. Why?");
		}
		this.listItems = children;
	}

	public void setLinks(Context context) {
		Item itemA, itemB, itemC;
		ArrayList<Item> items = new ArrayList<Item>();
		;
		int listSize = this.getItems().size();
		Log.d("CustomList.java", "listSize = " + listSize);
		for (int i = 0; i < listSize; i++) { // make linked list

			if (listSize - 1 == i) {
				// At the last index
				itemA = this.getItem(i);
				itemB = this.getItem(0);
				// itemC = newList.getItem(1);

				itemA.setNext(itemB.getID());
				// itemB.setPrev(itemA.getID());
				// itemB.setNext(itemC.getID());
				Log.d("CustomList.java", "ItemA's next is"
						+ this.getItem(0).getID());
				Log.d("CustomList.java", "ItemB's prev is" + itemA.getID());

				// db.insertItem(itemB);
			} else if (listSize > 1 && i == 0) {
				// Two or more items in the list, insert the first two
				itemA = this.getItem(i);
				itemB = this.getItem(i + 1);
				itemC = this.getItem(listSize - 1);
				itemA.setNext(itemB.getID());
				itemA.setPrev(itemC.getID());
				itemB.setPrev(itemA.getID());

				Log.d("CustomList.java", "ItemA's next is" + itemB.getID());
				Log.d("CustomList.java", "ItemB's prev is" + itemA.getID());
			} else if (listSize > 1 && i < listSize) {
				// Two or more items in the list
				itemA = this.getItem(i);
				itemB = this.getItem(i + 1);
				itemA.setNext(itemB.getID());
				itemB.setPrev(itemA.getID());

				Log.d("CustomList.java", "ItemA's next is" + itemB.getID());
				Log.d("CustomList.java", "ItemB's prev is" + itemA.getID());
			}

			else {
				// Only one item in the list
				itemA = this.getItem(0);
				itemA.setNext(itemA.getID());
				itemA.setPrev(itemA.getID());
				Log.d("CustomList.java", "ItemA's next is" + itemA.getID());
				Log.d("CustomList.java", "ItemA's prev is" + itemA.getID());
			}
			items.add(itemA);
		}
		Item.insertOrUpdateItems(context, items, NO_PUSH_TO_CLOUD);
	}

	/*
	 * GETTERS
	 */

	public int getID() {
		return ID;
	}

	public String getCustomID() {
		return customID;
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

	public void updateName(Context context, String newName) {
		setName(newName);
		DBHelper db = new DBHelper(context);
		db.open();
		db.updateList(this);
		db.close();

	}

	/*
	 * CLASS METHODS
	 */

	public static void insertOrUpdateLists(Context context,
			ArrayList<CustomList> myCustomLists, boolean pushToCloud) {
		db = new DBHelper(context);
		db.open();

		for (CustomList c : myCustomLists) {
			db.insertOrUpdateList(c, pushToCloud);
		}
		db.close();

	}

	public static CustomList getListByID(Context context, int i) {
		db = new DBHelper(context);
		db.open();
		CustomList c = db.getListByID(i);
		db.close();
		return c;
	}

	public static ArrayList<Item> getItemsForListByID(Context context, int i) {
		db = new DBHelper(context);
		db.open();
		ArrayList<Item> items = db.getItemsForListByID(i);
		db.close();
		return items;
	}

	public static ArrayList<CustomList> getAllLists(Context context, int uID) {
		db = new DBHelper(context);
		db.open();
		ArrayList<CustomList> lists = db.getAllLists(); // todo for uID..
		db.close();
		return lists;

	}

	public static void deleteListAndChildren(Context context,
			CustomList userlist) {
		db = new DBHelper(context);
		db.open();
		db.deleteListAndChildren(userlist);
		db.close();
	}
	
	public static String getListName (Context context, int listID)
	{
		db = new DBHelper(context);
		db.open();
		String s = db.getListName(listID);
		db.close();
		return s;
	}
}