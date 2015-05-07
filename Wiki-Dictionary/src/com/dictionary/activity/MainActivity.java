package com.dictionary.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dictionary.aard.pro.Application;
import com.dictionary.aard.pro.BlobDescriptorList;
import com.dictionary.aard.pro.R;
import com.dictionary.fragment.BaseListFragment;
import com.dictionary.fragment.BlobDescriptorListFragment;
import com.dictionary.fragment.DictionariesFragment;
import com.dictionary.fragment.LookupFragment;
import com.dictionary.model.DictionaryUtility;
import com.dictionary.object.DictionaryObject;
import com.dictionary.utils.DLog;
import com.shamanland.fonticon.FontIconDrawable;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements ActionBar.TabListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private ViewPager mViewPager;
	private static DLog log = new DLog();
	public Application application;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (Application) getApplication();
		application.installTheme(this);

		setContentView(R.layout.activity_main);

		init(savedInstanceState);

		// Get new dictionary url
		getDictionaryInfo();
	}

	private void init(Bundle savedInstanceState) {
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(mAppSectionsPagerAdapter.getCount());
		mViewPager.setAdapter(mAppSectionsPagerAdapter);

		final String[] subtitles = new String[] { getString(R.string.subtitle_lookup), getString(R.string.subtitle_bookmark),
				getString(R.string.subtitle_history), getString(R.string.subtitle_dictionaries), getString(R.string.subtitle_settings), };

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
				actionBar.setSubtitle(subtitles[position]);
			}
		});

		Drawable[] tabIcons = new Drawable[5];
		tabIcons[0] = FontIconDrawable.inflate(this, R.xml.ic_tab_search);
		tabIcons[1] = FontIconDrawable.inflate(this, R.xml.ic_tab_bookmark);
		tabIcons[2] = FontIconDrawable.inflate(this, R.xml.ic_tab_history);
		tabIcons[3] = FontIconDrawable.inflate(this, R.xml.ic_tab_dictionary);
		tabIcons[4] = FontIconDrawable.inflate(this, R.xml.ic_tab_settings);
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			Tab tab = actionBar.newTab();
			tab.setTabListener(this);
			tab.setIcon(tabIcons[i]);
			actionBar.addTab(tab);
		}

		if (savedInstanceState != null) {
			onRestoreInstanceState(savedInstanceState);
		} else {
			if (application.dictionaries.size() == 0) {
				mViewPager.setCurrentItem(3);
				DictionariesFragment df = (DictionariesFragment) mAppSectionsPagerAdapter.getItem(3);
				df.findDictionaries();
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int currentSection = savedInstanceState.getInt("currentSection");
		mViewPager.setCurrentItem(currentSection);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentSection", mViewPager.getCurrentItem());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		Fragment frag = mAppSectionsPagerAdapter.getItem(tab.getPosition());
		if (frag instanceof BaseListFragment) {
			((BaseListFragment) frag).finishActionMode();
		}
		if (tab.getPosition() == 0) {
			View v = this.getCurrentFocus();
			if (v != null) {
				InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, ArticleCollectionActivity.class);
			intent.setAction("showRandom");
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		int currentItem = mViewPager.getCurrentItem();
		Fragment frag = mAppSectionsPagerAdapter.getItem(currentItem);
		Log.d(TAG, "current tab: " + currentItem);
		if (frag instanceof BlobDescriptorListFragment) {
			BlobDescriptorListFragment bdFrag = (BlobDescriptorListFragment) frag;
			if (bdFrag.isFilterExpanded()) {
				Log.d(TAG, "Filter is expanded");
				bdFrag.collapseFilter();
				return;
			}
		}
		super.onBackPressed();
	}

	public static final class BookmarksFragment extends BlobDescriptorListFragment {
		@Override
		public String getItemClickAction() {
			return "showBookmarks";
		}

		@Override
		public BlobDescriptorList getDescriptorList() {
			Application app = (Application) getActivity().getApplication();
			return app.bookmarks;
		}

		@Override
		public int getEmptyIcon() {
			return R.xml.ic_empty_view_bookmark;
		}

		@Override
		public String getEmptyText() {
			return getString(R.string.main_empty_bookmarks);
		}

		@Override
		public int getDeleteConfirmationItemCountResId() {
			return R.plurals.confirm_delete_bookmark_count;
		}

		@Override
		public String getPreferencesNS() {
			return "bookmarks";
		}
	}

	public static class HistoryFragment extends BlobDescriptorListFragment {
		@Override
		public String getItemClickAction() {
			return "showHistory";
		}

		@Override
		public BlobDescriptorList getDescriptorList() {
			Application app = (Application) getActivity().getApplication();
			return app.history;
		}

		@Override
		public int getEmptyIcon() {
			return R.xml.ic_empty_view_history;
		}

		@Override
		public String getEmptyText() {
			return getString(R.string.main_empty_history);
		}

		@Override
		public int getDeleteConfirmationItemCountResId() {
			return R.plurals.confirm_delete_history_count;
		}

		@Override
		public String getPreferencesNS() {
			return "history";
		}

	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
		private Fragment[] fragments;
		private LookupFragment tabLookup;
		private BlobDescriptorListFragment tabBookmarks;
		private BlobDescriptorListFragment tabHistory;
		private DictionariesFragment tabDictionaries;
//		private SettingsFragment tabSettings;

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			tabLookup = new LookupFragment();
			tabBookmarks = new BookmarksFragment();
			tabHistory = new HistoryFragment();
			tabDictionaries = new DictionariesFragment();
//			tabSettings = new SettingsFragment();
			fragments = new Fragment[] { tabLookup, tabBookmarks, tabHistory, tabDictionaries };
		}

		@Override
		public Fragment getItem(int i) {
			return fragments[i];
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "";
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	// Get link Dictionaries

	public void getDictionaryInfo() {
		// call AsynTask to perform network operation on separate thread
		new HttpAsyncTask().execute(dicResource_luong);
	}

	public boolean GET(String url) {
		InputStream inputStream = null;
		String result = "";
		boolean isQuerySuccess = false;
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				isQuerySuccess = true;
			}

			ContentResolver resolver = getContentResolver();

			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject json = new JSONObject(result);
					JSONArray jsonArray = json.getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						String dictionary_name = jsonObject.getString("name");
						String source_1 = jsonObject.getString("source");
						String source_2 = jsonObject.getString("source_2");
						String dictionary_size = jsonObject.getString("size");
						int dictionary_version = jsonObject.getInt("version");
						String group_name = jsonObject.getString("group_name");

						DictionaryObject item = new DictionaryObject(dictionary_name, source_1, source_2, dictionary_size, dictionary_version,
								group_name);
						log.e(TAG, item.toString());

						DictionaryUtility.delete(resolver, dictionary_name, group_name);
						DictionaryUtility.insert(resolver, dictionary_name, group_name, source_1, source_2, dictionary_size, dictionary_version + "");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return isQuerySuccess;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... urls) {
			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(Boolean result) {
			// Toast.makeText(getBaseContext(), "Received!",
			// Toast.LENGTH_LONG).show();
			log.e(TAG, "load dictionary successfully");
		}
	}
}
