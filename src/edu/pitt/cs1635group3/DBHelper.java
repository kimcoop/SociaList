/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package edu.pitt.cs1635group3;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class DBHelper {

	public static final String ITEM_TABLE = "item";
	public static final String LIST_TABLE = "list";
	public static final String USER_TABLE = "user";

	public static final String KEY_ITEM_ID = "id";
	public static final String KEY_PARENT_ID = "parent_id";
	public static final String KEY_ITEM_NAME = "name";
	public static final String KEY_ADDER_ID = "adder_id";
	public static final String KEY_ADD_DATE = "add_date";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_ASSIGNED_TO = "assignee";
	public static final String KEY_ASSIGNER_ID = "assigner_id";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_COMPLETED = "completed";
	public static final String KEY_COMPLETION_DATE = "completion_date";
	public static final String KEY_ITEM_PREV = "prev_id";
	public static final String KEY_ITEM_NEXT = "next_id";

	public static final String KEY_LIST_ID = "id";
	public static final String KEY_LIST_NAME = "name";
	public static final String KEY_CREATOR_ID = "creator_id";
	public static final String KEY_CREATION_DATE = "creation_date";
	public static final String KEY_NOTE = "notes";

	public static final String KEY_MAP_ITEM_LIST_ID = "id";
	public static final String KEY_MAP_LIST_USER_ID = "id";
	public static final String KEY_MAP_ITEM_ID = "item_id";
	public static final String KEY_MAP_LIST_ID = "list_id";
	public static final String KEY_MAP_USER_ID = "user_id";

	public static final String KEY_USER_ID = "id";
	public static final String KEY_USER_FIRST = "first";
	public static final String KEY_USER_LAST = "last";

	private static final String TAG = "SociaList: DbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase db;

	/**
	 * Database creation sql statement
	 */
	private static final String ITEM_CREATE = "create table item (id integer primary key autoincrement, "
			+ "prev_id integer not null, "
			+ "assigner_id not null, "
			+ "name text not null, adder_id integer not null, "
			+ "assignee integer not null, "
			+ "add_date text not null, "
			+ "next_id integer not null, "
			+ "quantity integer not null, "
			+ "completion_date text, "
			+ "notes text, completed integer not null, "
			+ "parent_id integer not null, "
			+"UNIQUE(id) ON CONFLICT IGNORE)";
	

	private static final String LIST_CREATE = "create table list (id integer primary key autoincrement, "
			+ "name text not null, creator_id integer not null, "
			+ "creation_date text not null, UNIQUE(id) ON CONFLICT IGNORE)";

	private static final String MAP_LIST_USER_CREATE = "create table map_list_user (id integer primary key autoincrement, "
			+ "list_id integer not null, user_id integer not null)";

	private static final String USER_CREATE = "create table user (id integer primary key autoincrement, "
			+ "first text not null, last text not null)";

	private static final String DATABASE_NAME = "socialist_db";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public DBHelper(Context ctx) {
		this.mCtx = ctx;
	}

	public boolean abandonShip() {
		return db.delete("list", null, null) > 0;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS item");
			db.execSQL("DROP TABLE IF EXISTS list");
			db.execSQL(ITEM_CREATE);
			db.execSQL(LIST_CREATE);
			db.execSQL(MAP_LIST_USER_CREATE);
			db.execSQL(USER_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			onCreate(db);
		}
	} // end DatabaseHelper inner class

	public DBHelper open() throws SQLException {

		mDbHelper = new DatabaseHelper(mCtx);
		db = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/*
	 * USER METHODS
	 */

	public long insertUser(User u) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USER_ID, u.getID());
		initialValues.put(KEY_USER_FIRST, u.getFirstName());
		initialValues.put(KEY_USER_LAST, u.getLastName());
		Log.i("DB USER", "Inserted user " + u.getName());
		return db.insert(USER_TABLE, null, initialValues);
	}

	public CharSequence[] getUsersForDialog() { // TODO - make this take ListID
												// as a param

		CharSequence[] users;

		String myQuery = "SELECT * FROM user"; // TODO - more detailed query
												// involving list &
												// map_list_user
		Cursor c = db.rawQuery(myQuery, null);

		if (c != null)
			c.moveToFirst();
		users = new CharSequence[c.getCount()]; // allow for number of users
												// returned

		c.moveToFirst();
		int i = 0;
		while (!c.isAfterLast()) {
			users[i] = (CharSequence) c.getString(1) + " " + c.getString(2);
			c.moveToNext();
			i++;
		}

		return users;
	}
	
public ArrayList<User> getAllUsers(){
		
		ArrayList<User> users = null;
		String myQuery = "SELECT * FROM user";// WHERE parent_id = " + ID;
		Cursor c = db.rawQuery(myQuery, null);

		if (c != null){
			Log.i("DBHelper", "In IF and c = " + c.getCount());
			users = new ArrayList<User> (c.getCount());
			c.moveToFirst();
	
		
			while (!c.isAfterLast()) {
				Log.i("DBHelper", "In While loop"); 
				User u = new User(c.getInt(0), c.getString(1), c.getString(2));
				users.add(u);
			    c.moveToNext();
			}
			
		}
		return users;
		
	}

	public String getUserNameByID(int row) {
		String myQuery = "SELECT * FROM user WHERE id = " + row;
		Cursor c = db.rawQuery(myQuery, null);

		if (c != null)
			c.moveToFirst();

		String name = c.getString(1) + " " + c.getString(2);
		c.close();
		return name;

	}

	public User getUserByID(int row) {
		// Log.i("DB USER", "Querying for user ID = " + row);
		String myQuery = "SELECT * FROM user WHERE id = " + row;
		Cursor c = db.rawQuery(myQuery, null);

		if (c != null)
			c.moveToFirst();

		return cursorToUser(c);
	}

	public int getUserByName(String user) {

		String[] pieces = user.split(" ");
		String fname = pieces[0];
		String lname = pieces[1];

		String myQuery = "SELECT * FROM user WHERE first = ? AND last = ?";
		Cursor c = db.rawQuery(myQuery, new String[] { fname, lname });

		if (c != null)
			c.moveToFirst();

		int userID = c.getInt(0);
		c.close();
		return userID;
	}

	private User cursorToUser(Cursor c) {
		// Log.d("DB", "c.getCount() is " +c.getCount());
		User u = new User(c.getInt(0), c.getString(1), c.getString(2));
		c.close();
		return u;
	}

	/*
	 * LIST METHODS
	 */

	public long insertList(CustomList list) {
		Log.i("INSERTING LIST", "Here" + list.getName());
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_LIST_ID, list.getID());
		initialValues.put(KEY_LIST_NAME, list.getName());
		initialValues.put(KEY_CREATOR_ID, list.getCreator());
		initialValues.put(KEY_CREATION_DATE, list.getCreationDate());
		return db.insert(LIST_TABLE, null, initialValues);
	}
	
	public ArrayList<CustomList> getAllLists(){
		
		ArrayList<CustomList> lists = null;
		String myQuery = "SELECT * FROM list";// WHERE parent_id = " + ID;
		Cursor c = db.rawQuery(myQuery, null);

		if (c != null){
			Log.i("DBHelper", "In IF and c = " + c.getCount());
			lists = new ArrayList<CustomList> (c.getCount());
			c.moveToFirst();
	
		
			while (!c.isAfterLast()) {
				Log.i("DBHelper", "In While loop"); 
				CustomList l = new CustomList(c.getInt(0), c.getString(1));
				l.setCreator(c.getInt(2));
				l.setCreationDate(c.getString(3));
				lists.add(l);
			    c.moveToNext();
			}
		}
		return lists;
		
	}
	
	public CustomList getListByID(int i) {
		
		String myQuery = "SELECT * FROM list WHERE id = " + i;
		Cursor c = db.rawQuery(myQuery, null);
		if (c != null) {
			c.moveToFirst();
		}

		CustomList myList = new CustomList(c.getInt(0), c.getString(1));
		myList.setCreator(c.getInt(2));
		myList.setCreationDate(c.getString(3));
		return myList;
	}
	
	public ArrayList<Item> getItemsForListByID(int ID){
		
		ArrayList<Item> items = null;
		String myQuery = "SELECT * FROM item";// WHERE parent_id = " + ID;
		Cursor c = db.rawQuery(myQuery, null);

		if (c != null){
			Log.i("DBHelper", "In IF and c = " + c.getCount());
			items = new ArrayList<Item> (c.getCount());
			c.moveToFirst();
	
		
			while (!c.isAfterLast()) {
				Log.i("DBHelper", "In While loop"); 
				Item i = new Item(c.getString(3), c.getInt(0));
				i.setParent(c.getInt(12));
				i.setCreator(c.getInt(4));
				i.setCreationDate(c.getString(6));
				i.setQuantity(c.getInt(8));
				i.assignTo(c.getInt(5));
				i.setAssigner(c.getInt(2));
				i.setNotes(c.getString(10));
				i.setCompleted(c.getInt(11));
				i.setCompletionDate(c.getString(9));
				i.setPrev(c.getInt(1));
				i.setNext(c.getInt(7));
				items.add(i);
			    c.moveToNext();
			}
		}
		
		
		return items;
		
	}

	/*
	 * ITEM METHODS
	 */

	public long insertItem(Item i) {
		ContentValues initialValues = new ContentValues();

		int isCompleted = 0;
		if (i.isCompleted())
			isCompleted = 1;

		initialValues.put(KEY_ITEM_ID, i.getID());
		initialValues.put(KEY_ITEM_PREV, i.getPrev());
		initialValues.put(KEY_ASSIGNER_ID, i.getAssigner());
		initialValues.put(KEY_ITEM_NAME, i.getName());
		initialValues.put(KEY_ADDER_ID, i.getCreator());
		initialValues.put(KEY_ASSIGNED_TO, i.getAssignee());
		initialValues.put(KEY_ADD_DATE, i.getCreationDate());
		initialValues.put(KEY_ITEM_NEXT, i.getNext());
		initialValues.put(KEY_QUANTITY, i.getQuantity());
		initialValues.put(KEY_COMPLETION_DATE, i.getCompletionDate());
		initialValues.put(KEY_NOTES, i.getNotes());
		initialValues.put(KEY_COMPLETED, isCompleted);
		initialValues.put(KEY_PARENT_ID, i.getParentID());
		
		
		Log.d("LEGIT INSERTED ITEM", "Inserted item name="+i.getName());
		
		return db.insert(ITEM_TABLE, null, initialValues);
		
	}

	public boolean deleteItem(Item i) {
		// NOTE - Items are in a doubly-linked list. Make sure you do this
		// appropriately so the links stayed wrapped around.
		// May need to write a method like deleteItem(prevItem, Item, nextItem)
		// where prevItem and nextItem are just the IDs
		// of prev and next. Then just reset the wiring.

		return db.delete(ITEM_TABLE, KEY_ITEM_ID + "=?",
				new String[] { String.valueOf(i.getID()) }) > 0;
	}

	public Item getItem(int row) {
		Log.i("DB ITEM", "Querying for item ID = " + row);
		String myQuery = "SELECT * FROM item WHERE id = " + row;
		Cursor c = db.rawQuery(myQuery, null);

		if (c != null) {
			c.moveToFirst();
		}

		return cursorToItem(c);
	}

	private Item cursorToItem(Cursor c) {
		Log.d("DB", "c.getCount() is " + c.getCount());
		Item i = new Item(c.getString(2), c.getInt(0));
		i.setParent(c.getInt(1));
		i.setCreator(c.getInt(4));
		i.setCreationDate(c.getString(5));
		i.setQuantity(c.getInt(5));
		i.assignTo(c.getInt(6));
		i.setAssigner(c.getInt(7));
		i.setNotes(c.getString(8));
		i.setCompleted(c.getInt(9));
		i.setCompletionDate(c.getString(10));
		i.setPrev(c.getInt(11));
		i.setNext(c.getInt(12));
		c.close();
		return i;
	}

	public boolean updateItem(Item i) {
		Log.d("BEGINING OF UPDATE", "Item " +i.getName() + " assigned to "+i.getAssignee());
		ContentValues args = new ContentValues();
		args.put(KEY_ITEM_ID, i.getID());
		args.put(KEY_ITEM_NAME, i.getName());
		args.put(KEY_ADDER_ID, i.getCreator());
		args.put(KEY_ADD_DATE, i.getCreationDate());
		args.put(KEY_QUANTITY, i.getQuantity());
		args.put(KEY_ASSIGNED_TO, i.getAssignee());
		args.put(KEY_ASSIGNER_ID, i.getAssigner());
		args.put(KEY_NOTES, i.getNotes());
		args.put(KEY_COMPLETED, i.isCompleted());
		args.put(KEY_COMPLETION_DATE, i.getCompletionDate());

		Log.d("UPDATED ITEM",
				"Item " + i.getName() + " assigned to " + i.getAssignee());
		return db.update(ITEM_TABLE, args, KEY_ITEM_ID + "=?",
				new String[] { String.valueOf(i.getID()) }) > 0;

	}
}
