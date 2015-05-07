package com.dictionary.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DictionaryContentProvider extends ContentProvider {

	public static final String PROVIDER_NAME = "com.dictionary.aard.pro";
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/cartoon");

	private static final int CARTOON_ALL = 1;
	private static final int CARTOON_SINGLE = 2;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "cartoon", CARTOON_ALL);
		uriMatcher.addURI(PROVIDER_NAME, "cartoon/#", CARTOON_SINGLE);
	}

	private SQLiteHelper dbHelper;

	@Override
	public boolean onCreate() {
		dbHelper = new SQLiteHelper(getContext());
		return false;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case CARTOON_ALL:
			// do nothing
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		long id = db.insert(SQLiteDB.DATABASE_TABLE_NAME, null, values);
		getContext().getContentResolver().notifyChange(uri, null);

		return Uri.parse(CONTENT_URI + "/" + id);
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case CARTOON_ALL:
			return "vnd.android.cursor.dir/vnd.luongbeta.cartoon";
		case CARTOON_SINGLE:
			return "vnd.android.cursor.item/vnd.luongbeta.cartoon";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(SQLiteDB.DATABASE_TABLE_NAME);

		switch (uriMatcher.match(uri)) {
		case CARTOON_ALL:
			// do nothing
			break;
		case CARTOON_SINGLE:
			String id = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(SQLiteDB.KEY_ID + "=" + id);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		return cursor;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case CARTOON_ALL:
			// do nothing
			break;
		case CARTOON_SINGLE:
			String id = uri.getPathSegments().get(1);
			selection = SQLiteDB.KEY_ID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		int updateCount = db.update(SQLiteDB.DATABASE_TABLE_NAME, values,
				selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}

	@Override
	public synchronized int delete(Uri uri, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case CARTOON_ALL:
			// do nothing
			break;
		case CARTOON_SINGLE:
			String id = uri.getPathSegments().get(1);
			selection = SQLiteDB.KEY_ID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		int deleteCount = db.delete(SQLiteDB.DATABASE_TABLE_NAME, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

}
