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
public class DBItemAdapater {

	/*
	 * private String name, assigner, assignee, creationDate, notes, creator,
	 * completionDate; private int parentID, ID, quantity; // use parentID to
	 * tie to list private boolean selected, completed;
	 */

	public static final String ITEM_TABLE = "item";
	public static final String LIST_TABLE = "list";

	public static final String KEY_ITEM_ID = "id";
	public static final String KEY_ITEM_NAME = "name";
	public static final String KEY_ADDER_ID = "adder_id";
	public static final String KEY_ADD_DATE = "add_date";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_ASSIGNED_TO = "assignee";
	public static final String KEY_ASSIGNER_ID = "assigner_id";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_COMPLETED = "completed";
	public static final String KEY_COMPLETION_DATE = "completion_date";
	/*
	 * private String name, creationDate, note; private int ID;
	 */

	public static final String KEY_LIST_ID = "id";
	public static final String KEY_LIST_NAME = "name";
	public static final String KEY_ORIGINATOR_ID = "originator_id";
	public static final String KEY_CREATION_DATE = "creation_date";
	public static final String KEY_NOTE = "notes";

	public static final String KEY_MAP_ITEM_LIST_ID = "id";
	public static final String KEY_MAP_LIST_USER_ID = "id";
	public static final String KEY_MAP_ITEM_ID = "item_id";
	public static final String KEY_MAP_LIST_ID = "list_id";
	public static final String KEY_MAP_USER_ID = "user_id";

	public static final String KEY_USER_ID = "id";
	public static final String KEY_FNAME = "fname";
	public static final String KEY_LNAME = "lname";

	private static final String TAG = "SociaList: DbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase db;

	/**
	 * Database creation sql statement
	 */
	private static final String ITEM_CREATE = "create table item (id integer primary key autoincrement, "
			+ "name text not null, adder_id integer not null, "
			+ "add_date date not null, quantity integer not null, "
			+ "assignee integer not null, assigner_id not null, "
			+ "notes text not null, completed integer not null, "
			+ "completion_date date not null)";

	private static final String LIST_CREATE = "create table list (id integer primary key autoincrement, "
			+ "name text not null, creator_id integer not null, "
			+ "creation_date date not null)";

	private static final String MAP_ITEM_LIST_CREATE = "create table map_item_list (id integer primary key autoincrement, "
			+ "item_id integer not null, list_id integer not null)";

	private static final String MAP_LIST_USER_CREATE = "create table map_list_user (id integer primary key autoincrement, "
			+ "list_id integer not null, user_id integer not null)";

	private static final String USER_CREATE = "create table user (id integer primary key autoincrement, "
			+ "fname text not null, lname text not null)";

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
	public DBItemAdapater(Context ctx) {
		this.mCtx = ctx;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(ITEM_CREATE);
			db.execSQL(LIST_CREATE);
			db.execSQL(MAP_ITEM_LIST_CREATE);
			db.execSQL(MAP_LIST_USER_CREATE);
			db.execSQL(USER_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public DBItemAdapater open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		db = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * The following are methods from the original file. These are left in to
	 * show examples on how to manipulate data in the tables. -Rob
	 * 
	 * P.S. This all comes from here:
	 * http://developer.android.com/resources/tutorials/notepad/index.html
	 * 
	 * 
	 * 
	 */

	public long insertList(CustomList list) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_LIST_ID, list.getID());
		initialValues.put(KEY_LIST_NAME, list.getName());
		initialValues.put(KEY_ORIGINATOR_ID, list.getCreator());
		initialValues.put(KEY_CREATION_DATE, list.getCreationDate());
		return db.insert(LIST_TABLE, null, initialValues);
	}

	public long insertItem(Item i) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ITEM_ID, i.getID());
		initialValues.put(KEY_ITEM_NAME, i.getName());
		initialValues.put(KEY_ADDER_ID, i.getCreator());
		initialValues.put(KEY_ADD_DATE, i.getCreationDate());
		initialValues.put(KEY_QUANTITY, i.getQuantity());
		initialValues.put(KEY_ASSIGNED_TO, i.getAssignee());
		initialValues.put(KEY_ASSIGNER_ID, i.getAssigner());
		initialValues.put(KEY_NOTES, i.getNotes());
		initialValues.put(KEY_COMPLETED, i.isCompleted());
		initialValues.put(KEY_COMPLETION_DATE, i.getCompletionDate());
		return db.insert(ITEM_TABLE, null, initialValues);
	}

	public boolean deleteItem(Item i) {
		return db.delete(ITEM_TABLE, KEY_ITEM_ID + "=?",
				new String[] { String.valueOf(i.getID()) }) > 0;
	}

	/*
	 * 
	 * // ---retrieves all the titles--- public Cursor getAllTitles() { return
	 * db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_ISBN, KEY_TITLE,
	 * KEY_PUBLISHER }, null, null, null, null, null); }
	 * 
	 * // ---retrieves a particular title---
	 * 
	 * public Cursor getItem(long rowId) throws SQLException { Cursor mCursor =
	 * db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_ISBN,
	 * KEY_TITLE, KEY_PUBLISHER }, KEY_ROWID + "=" + rowId, null, null, null,
	 * null, null); if (mCursor != null) { mCursor.moveToFirst(); } return
	 * mCursor; }
	 */

	public Cursor getItem(int row) {
		Cursor mCursor = db.query(true, ITEM_TABLE, new String[] { 
				KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ADDER_ID, KEY_ADD_DATE, KEY_QUANTITY, KEY_ASSIGNED_TO, KEY_ASSIGNER_ID,
				KEY_NOTES, KEY_COMPLETED, KEY_COMPLETION_DATE}, 
				KEY_ITEM_ID + "=" + row,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateItem(Item i) {
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
		return db.update(ITEM_TABLE, args, KEY_ITEM_ID + "=?",
				new String[] { String.valueOf(i.getID()) }) > 0;
	}
}
