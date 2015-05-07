package com.dictionary.model;

import android.content.ContentResolver;
import android.content.ContentValues;

public class DictionaryUtility {

	public synchronized static void insert(ContentResolver resolver, String dictionary_name, String group_name, String source_1, String source_2,
			String dictionary_size, String dictionary_version) {

		ContentValues values = new ContentValues();
		values.put(SQLiteDB.DICTIONARY_NAME, dictionary_name);
		values.put(SQLiteDB.GROUP_NAME, group_name);
		values.put(SQLiteDB.SOURCE_1, source_1);
		values.put(SQLiteDB.SOURCE_2, source_2);
		values.put(SQLiteDB.DICTIONARY_SIZE, dictionary_size);
		values.put(SQLiteDB.DICTIONARY_VERSION, dictionary_version);

		if (null != resolver) {
			resolver.insert(DictionaryContentProvider.CONTENT_URI, values);
		}
	}

	public synchronized static int delete(ContentResolver resolver, String dictionary_name, String group_name) {
		int result = 0;
		try {
			if (null != resolver) {
				String where = SQLiteDB.DICTIONARY_NAME + "=? AND " + SQLiteDB.GROUP_NAME + "=?";

				result = resolver.delete(DictionaryContentProvider.CONTENT_URI, where, new String[] { dictionary_name, group_name });

				return result;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}

	public synchronized static int deleteAll(ContentResolver resolver, String group_name) {
		int result = 0;
		try {
			if (null != resolver) {
				String where = SQLiteDB.GROUP_NAME + "=?";

				result = resolver.delete(DictionaryContentProvider.CONTENT_URI, where, new String[] { group_name });

				return result;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}
}
