package com.dictionary.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteDB {

	public static final String TAG = "DATABASE SQLITE";
	public static final String DATABASE_NAME = "CARTOON_DATA";
	public static final String DATABASE_TABLE_NAME = "CARTOON_TABLE";
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_VERSION_KEY = "DATABASE_VERSION_KEY";
	public static final String KEY_ID = "_id";
	public static final String DICTIONARY_NAME = "dictionary_name";
	public static final String GROUP_NAME = "group_name";
	public static final String DICTIONARY_SIZE = "dictionary_size";
	public static final String SOURCE_1 = "source_1";
	public static final String SOURCE_2 = "source_2";
	public static final String DICTIONARY_VERSION = "dictionary_version";

	// create table MYDATABASE_NAME - ID integer primary key
	private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_NAME + " (_id integer primary key autoincrement, "
			+ DICTIONARY_NAME + " text not null, " + GROUP_NAME + " text not null, " + DICTIONARY_SIZE + " text not null, " + SOURCE_1
			+ " text not null," + SOURCE_2 + " text not null," + DICTIONARY_VERSION + " text not null" + ");"; //

	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase db;
	private Context context;

	public SQLiteDB(Context c) {
		this.context = c;
	}

	public static void onCreate(SQLiteDatabase db) {
		Log.w(TAG, DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
		onCreate(db);
	}

	public SQLiteDB openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

		db = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public SQLiteDB open() throws SQLException {
		sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		sqLiteHelper.close();
	}

	// // ---insert data into table---
	// public long insert(String dateTime, String celsiusMin, String celsiusMax,
	// String celsiusCurrent, String description, String cartoonImageUrl,
	// String videoStyle) {
	//
	// ContentValues contentValues = new ContentValues();
	// contentValues.put(SIZE, dateTime);
	// contentValues.put(SOURCE_1, celsiusMin);
	// contentValues.put(GROUP_NAME, videoStyle);
	// contentValues.put(SOURCE_2, celsiusMax);
	// contentValues.put(VERSION, celsiusCurrent);
	//
	// return db.insert(DATABASE_TABLE_NAME, null, contentValues);
	// }

	// ---deletes item---
	public boolean deleteItem(long id) {
		return db.delete(DATABASE_TABLE_NAME, KEY_ID + "=" + id, null) > 0;
	}

	// ---deletes all---
	public int deleteAll() {
		return db.delete(DATABASE_TABLE_NAME, null, null);
	}

	// public Cursor getDataAll() {
	// String[] columns = new String[] { KEY_ID, SIZE,
	// SOURCE_1, SOURCE_2, VERSION};
	// Cursor cursor = db.query(DATABASE_TABLE_NAME, columns, null, null,
	// null, null, null);
	//
	// return cursor;
	// }

	// // ---retrieves a particular title---
	// public Cursor getDataItem(long id) throws SQLException {
	// Cursor mCursor = db.query(true, DATABASE_TABLE_NAME, new String[] {
	// KEY_ID, SIZE, SOURCE_1, SOURCE_2,
	// VERSION, KEY_DESCRIPTION, KEY_IMAGE_URL }, KEY_ID + "="
	// + id, null, null, null, null, null);
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	// return mCursor;
	// }

}