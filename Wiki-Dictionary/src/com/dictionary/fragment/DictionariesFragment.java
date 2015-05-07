package com.dictionary.fragment;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dictionary.aard.pro.Application;
import com.dictionary.aard.pro.DictionaryDiscoveryCallback;
import com.dictionary.aard.pro.R;
import com.dictionary.activity.BaseActivity;
import com.dictionary.activity.DicListDownloadActivity;
import com.dictionary.activity.FileSelectActivity;
import com.dictionary.activity.SettingsActivity;
import com.dictionary.adapter.DictionaryListAdapter;
import com.dictionary.model.DictionaryContentProvider;
import com.dictionary.model.SQLiteDB;
import com.shamanland.fonticon.FontIconDrawable;

public class DictionariesFragment extends BaseListFragment {

	private final static String TAG = DictionariesFragment.class.getSimpleName();

	final static int FILE_SELECT_REQUEST = 17;

	private DictionaryListAdapter listAdapter;
	private boolean findDictionariesOnAttach = false;

	private class DiscoveryProgressDialog extends ProgressDialog {

		public DiscoveryProgressDialog(Context context) {
			super(context);
			setIndeterminate(true);
			setCancelable(false);
			setTitle(getString(R.string.dictionaries_please_wait));
			setMessage(getString(R.string.dictionaries_scanning_device));
		}

		@Override
		public void onBackPressed() {
			final Application app = (Application) getActivity().getApplication();
			app.cancelFindDictionaries();
		}
	}

	public int getEmptyIcon() {
		return R.xml.ic_empty_view_dictionary;
	}

	public CharSequence getEmptyText() {
		return Html.fromHtml(getString(R.string.main_empty_dictionaries));
	}

	@Override
	public boolean supportsSelection() {
		return false;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final Application app = (Application) getActivity().getApplication();
		listAdapter = new DictionaryListAdapter(app.dictionaries, getActivity());
		setListAdapter(listAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View result = super.onCreateView(inflater, container, savedInstanceState);
		View extraEmptyView = inflater.inflate(R.layout.dictionaries_empty_view_extra, container, false);
		Button btn = (Button) extraEmptyView.findViewById(R.id.dictionaries_empty_btn_scan);
		btn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_refresh), null, null, null);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				findDictionaries();
			}
		});

		Button getDictionaryBtn = (Button) extraEmptyView.findViewById(R.id.get_dictionary_btn);
		getDictionaryBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_download), null, null, null);
		getDictionaryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoDicResourcePager();
			}
		});
		LinearLayout emptyViewLayout = (LinearLayout) emptyView;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		emptyViewLayout.addView(extraEmptyView, layoutParams);
		return result;
	}

	private void gotoDicResourcePager() {
		try {
			ContentResolver contentResolver = getActivity().getContentResolver();
			String[] from = new String[] { SQLiteDB.KEY_ID, SQLiteDB.DICTIONARY_NAME, SQLiteDB.GROUP_NAME, SQLiteDB.DICTIONARY_SIZE,
					SQLiteDB.SOURCE_1, SQLiteDB.SOURCE_2, SQLiteDB.DICTIONARY_VERSION };

			Cursor c = contentResolver.query(DictionaryContentProvider.CONTENT_URI, from, "", new String[] {}, null);
			if (c.getCount() == 0) {
				openWebviewResource();
			} else {
				getActivity().startActivity(new Intent(getActivity(), DicListDownloadActivity.class));
			}
			c.close();
		} catch (Exception e) {
			openWebviewResource();
		}
	}

	private void openWebviewResource() {
		Uri uriUrl = Uri.parse(BaseActivity.dicResource_goc);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		getActivity().startActivity(launchBrowser);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.dictionaries, menu);
	}

	@Override
	public void onPrepareOptionsMenu(final Menu menu) {
		MenuItem miFindDictionaries = menu.findItem(R.id.action_find_dictionaries);
		
		FragmentActivity activity = getActivity();
		miFindDictionaries.setIcon(FontIconDrawable.inflate(activity, R.xml.ic_actionbar_reload));
		
		MenuItem miAddDictionaries = menu.findItem(R.id.action_add_dictionaries);
		miAddDictionaries.setIcon(FontIconDrawable.inflate(activity, R.xml.ic_actionbar_add));
		
		MenuItem getDictionary = menu.findItem(R.id.action_get_dictionary);
		getDictionary.setIcon(getResources().getDrawable(R.drawable.ic_download));
		
		MenuItem setting = menu.findItem(R.id.action_setting);
		setting.setIcon(getResources().getDrawable(R.drawable.ic_setting));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_find_dictionaries) {
			findDictionaries();
			return true;
		}
		if (item.getItemId() == R.id.action_add_dictionaries) {
			Intent intent = new Intent(getActivity(), FileSelectActivity.class);
			startActivityForResult(intent, FILE_SELECT_REQUEST);
			return true;
		}
		if (item.getItemId() == R.id.action_get_dictionary) {
//			Intent intent = new Intent(getActivity(), DicListDownloadActivity.class);
//			startActivity(intent);
			gotoDicResourcePager();
			return true;
		}
		if (item.getItemId() == R.id.action_setting) {
			Intent intent = new Intent(getActivity(), SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void findDictionaries() {
		Activity activity = getActivity();
		if (activity == null) {
			this.findDictionariesOnAttach = true;
			return;
		}
		this.findDictionariesOnAttach = false;
		final Application app = ((Application) activity.getApplication());
		final ProgressDialog p = new DiscoveryProgressDialog(getActivity());
		app.findDictionaries(new DictionaryDiscoveryCallback() {
			@Override
			public void onDiscoveryFinished() {
				p.dismiss();
			}
		});
		p.show();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (findDictionariesOnAttach) {
			findDictionaries();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != FILE_SELECT_REQUEST) {
			Log.d(TAG, "Unknown request code: " + requestCode);
			return;
		}
		String selectedPath = data == null ? null : data.getStringExtra(FileSelectActivity.KEY_SELECTED_FILE_PATH);
		Log.d(TAG, String.format("req code %s, result code: %s, selected: %s", requestCode, resultCode, selectedPath));
		if (resultCode == Activity.RESULT_OK && selectedPath != null && selectedPath.length() > 0) {
			final Application app = ((Application) getActivity().getApplication());
			boolean alreadyExists = app.addDictionary(new File(selectedPath));
			String toastMessage;
			if (alreadyExists) {
				toastMessage = getString(R.string.msg_dictionary_already_open);
			} else {
				toastMessage = getString(R.string.msg_dictionary_added, selectedPath);
			}

			Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_LONG).show();
		}
	}
}
