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
public class ItemsDbAdapter {

	public static final String KEY_ITEM_ID = "id";
	public static final String KEY_ITEM_NAME = "name";
	public static final String KEY_ADDER_ID = "adder_id";
	public static final String KEY_ADD_DATE = "add_date";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_ASSIGNED_TO = "assigned_to";
	public static final String KEY_ASSIGNER_ID = "assigner_id";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_COMPLETED = "completed";
	public static final String KEY_COMPLETION_DATE = "completion_date";

	public static final String KEY_LIST_ID = "id";
	public static final String KEY_LIST_NAME = "name";
	public static final String KEY_ORIGINATOR_ID = "originator_id";
	public static final String KEY_CREATION_DATE = "creation_date";

	public static final String KEY_MAP_ITEM_LIST_ID = "id";
	public static final String KEY_MAP_LIST_USER_ID = "id";
	public static final String KEY_MAP_ITEM_ID = "item_id";
	public static final String KEY_MAP_LIST_ID = "list_id";
	public static final String KEY_MAP_USER_ID = "user_id";

	public static final String KEY_USER_ID = "id";
	public static final String KEY_FNAME = "fname";
	public static final String KEY_LNAME = "lname";

	private static final String TAG = "SociaList: ItemsDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String ITEM_CREATE = "create table item (id integer primary key autoincrement, "
			+ "name text not null, adder_id integer not null, "
			+ "add_date date not null, quantity integer not null, "
			+ "assigned_to integer not null, assigner_id not null, "
			+ "notes text not null, completed integer not null, "
			+ "completion_date date not null)";

	private static final String LIST_CREATE = "create table list (id integer primary key autoincrement, "
			+ "name text not null, originator_id integer not null, "
			+ "creation_date date not null)";

	private static final String MAP_ITEM_LIST_CREATE = "create table map_item_list (id integer primary key autoincrement, "
			+ "item_id integer not null, list_id integer not null)";

	private static final String MAP_LIST_USER_CREATE = "create table map_list_user (id integer primary key autoincrement, "
			+ "list_id integer not null, user_id integer not null)";

	private static final String USER_CREATE = "create table user (id integer primary key autoincrement, "
			+ "fname text not null, lname text not null)";

	private static final String DATABASE_NAME = "socialist_db";
	// private static final String DATABASE_TABLE = "notes"; //USED IN COMMENTED
	// OUT METHODS
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public ItemsDbAdapter(Context ctx) {
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
	public ItemsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
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
	 */

	/*
	 * /** Create a new note using the title and body provided. If the note is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param title the title of the note
	 * 
	 * @param body the body of the note
	 * 
	 * @return rowId or -1 if failed
	 *//*
		 * public long createNote(String title, String body) { ContentValues
		 * initialValues = new ContentValues(); initialValues.put(KEY_TITLE,
		 * title); initialValues.put(KEY_BODY, body);
		 * 
		 * return mDb.insert(DATABASE_TABLE, null, initialValues); }
		 * 
		 * /** Delete the note with the given rowId
		 * 
		 * @param rowId id of note to delete
		 * 
		 * @return true if deleted, false otherwise
		 *//*
			 * public boolean deleteNote(long rowId) {
			 * 
			 * return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null)
			 * > 0; }
			 * 
			 * /** Return a Cursor over the list of all notes in the database
			 * 
			 * @return Cursor over all notes
			 *//*
				 * public Cursor fetchAllNotes() {
				 * 
				 * return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,
				 * KEY_TITLE, KEY_BODY}, null, null, null, null, null); }
				 * 
				 * /** Return a Cursor positioned at the note that matches the
				 * given rowId
				 * 
				 * @param rowId id of note to retrieve
				 * 
				 * @return Cursor positioned to matching note, if found
				 * 
				 * @throws SQLException if note could not be found/retrieved
				 *//*
					 * public Cursor fetchNote(long rowId) throws SQLException {
					 * 
					 * Cursor mCursor =
					 * 
					 * mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
					 * KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null,
					 * null, null, null, null); if (mCursor != null) {
					 * mCursor.moveToFirst(); } return mCursor;
					 * 
					 * }
					 * 
					 * /** Update the note using the details provided. The note
					 * to be updated is specified using the rowId, and it is
					 * altered to use the title and body values passed in
					 * 
					 * @param rowId id of note to update
					 * 
					 * @param title value to set note title to
					 * 
					 * @param body value to set note body to
					 * 
					 * @return true if the note was successfully updated, false
					 * otherwise
					 *//*
						 * public boolean updateNote(long rowId, String title,
						 * String body) { ContentValues args = new
						 * ContentValues(); args.put(KEY_TITLE, title);
						 * args.put(KEY_BODY, body);
						 * 
						 * return mDb.update(DATABASE_TABLE, args, KEY_ROWID +
						 * "=" + rowId, null) > 0; }
						 */
}
