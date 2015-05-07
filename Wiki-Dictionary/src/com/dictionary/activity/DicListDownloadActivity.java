package com.dictionary.activity;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.dictionary.aard.pro.R;
import com.dictionary.adapter.DicDownloadAdapter;
import com.dictionary.model.DictionaryContentProvider;
import com.dictionary.model.SQLiteDB;
import com.dictionary.object.DictionaryObject;

public class DicListDownloadActivity extends BaseActivity {

	private final String TAG = "Dic List Activity";
	private ListView listView;
	private DicDownloadAdapter adapter;
	private ArrayList<DictionaryObject> listDic;
	private Cursor c;
	private String[] from;
	private int[] to;
	private String where;
	private ContentResolver contentResolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_dic_list_download);

		init();
		getListIdVideo();
	}

	private void init() {
		listView = (ListView) findViewById(R.id.list_download);
		listDic = new ArrayList<DictionaryObject>();
	}

	private ArrayList<DictionaryObject> getListIdVideo() {
		contentResolver = getContentResolver();
		from = new String[] { SQLiteDB.KEY_ID, SQLiteDB.DICTIONARY_NAME, SQLiteDB.GROUP_NAME, SQLiteDB.DICTIONARY_SIZE, SQLiteDB.SOURCE_1,
				SQLiteDB.SOURCE_2, SQLiteDB.DICTIONARY_VERSION };

		c = contentResolver.query(DictionaryContentProvider.CONTENT_URI, from, "", new String[] {}, null);
		listDic.clear();

		log.e(TAG, "cursor size = " + c.getCount());

		try {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);

				DictionaryObject item = new DictionaryObject(c.getString(c.getColumnIndex(SQLiteDB.DICTIONARY_NAME)), c.getString(c
						.getColumnIndex(SQLiteDB.SOURCE_1)), c.getString(c.getColumnIndex(SQLiteDB.SOURCE_2)), c.getString(c
						.getColumnIndex(SQLiteDB.DICTIONARY_SIZE)), Integer.parseInt(c.getString(c.getColumnIndex(SQLiteDB.DICTIONARY_VERSION))),
						c.getString(c.getColumnIndex(SQLiteDB.GROUP_NAME)));

				listDic.add(item);
			}
		} catch (Exception e) {
			log.e(TAG, "ERROR getListIdVideo: " + e.toString());
		}
		c.close();

		listDic = sortAndAddSections(listDic);
		adapter = new DicDownloadAdapter(this, listDic);
		listView.setAdapter(adapter);

		return listDic;
	}

	private ArrayList<DictionaryObject> sortAndAddSections(ArrayList<DictionaryObject> list) {

		ArrayList<DictionaryObject> tempList = new ArrayList<DictionaryObject>();
		// First we sort the array
//		Collections.sort(list);

		// Loops thorugh the list and add a section before each sectioncell
		// start
		String header = "";
		for (int i = 0; i < list.size(); i++) {
			// If it is the start of a new section we create a new
			// DictionaryObject and add it to our array
			if (!header.equals(list.get(i).getGroupName())) {
				DictionaryObject sectionCell = new DictionaryObject(list.get(i).getGroupName());
				tempList.add(sectionCell);
				header = list.get(i).getGroupName();
			}
			tempList.add(list.get(i));
		}

		return tempList;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
