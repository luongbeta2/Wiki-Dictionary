package com.dictionary.activity;

import java.io.InputStream;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dictionary.aard.pro.Application;
import com.dictionary.aard.pro.R;
import com.dictionary.adapter.SettingsListAdapter;

public class SettingsActivity extends BaseActivity {

	private final static String TAG = SettingsActivity.class.getSimpleName();

	private SettingsListAdapter listAdapter;
	private AlertDialog clearCacheConfirmationDialog;
	private Activity activity;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_setting);

		activity = this;

		Button sendFeedbackBtn = (Button) findViewById(R.id.send_btn);
		sendFeedbackBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_send_now), null, null, null);
		sendFeedbackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendEmail(activity, "Hi my friend!", "(from wiki dictionary application) \n");
			}
		});

		listView = (ListView) findViewById(R.id.listview);
		listAdapter = new SettingsListAdapter(this);
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == SettingsListAdapter.POS_CLEAR_CACHE) {
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setMessage(R.string.confirm_clear_cached_content)
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									WebView webView = new WebView(activity);
									webView.clearCache(true);
								}
							}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									// User cancelled the dialog
								}
							});
					clearCacheConfirmationDialog = builder.create();
					clearCacheConfirmationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialogInterface) {
							clearCacheConfirmationDialog = null;
						}
					});
					clearCacheConfirmationDialog.show();
					return;
				}

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != SettingsListAdapter.CSS_SELECT_REQUEST) {
			Log.d(TAG, String.format("Unknown request code: %d", requestCode));
			return;
		}
		Uri dataUri = data == null ? null : data.getData();
		Log.d(TAG, String.format("req code %s, result code: %s, data: %s", requestCode, resultCode, dataUri));
		if (resultCode == Activity.RESULT_OK && dataUri != null) {
			String path = dataUri.getPath().toLowerCase();
			if (!(path.endsWith(".css") || path.endsWith(".txt"))) {
				Log.d(TAG, "Doesn't appear to be a css: " + dataUri);
				Toast.makeText(activity, R.string.msg_file_not_css, Toast.LENGTH_LONG).show();
				return;
			}
			try {
				InputStream is = activity.getContentResolver().openInputStream(dataUri);
				Application app = (Application) activity.getApplication();
				String userCss = app.readTextFile(is, 256 * 1024);
				List<String> pathSegments = dataUri.getPathSegments();
				String fileName = pathSegments.get(pathSegments.size() - 1);
				Log.d(TAG, fileName);
				Log.d(TAG, userCss);
				int lastIndexOfDot = fileName.lastIndexOf(".");
				if (lastIndexOfDot > -1) {
					fileName = fileName.substring(0, lastIndexOfDot);
				}
				if (fileName.length() == 0) {
					fileName = "???";
				}
				final SharedPreferences prefs = activity.getSharedPreferences("userStyles", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(fileName, userCss);
				boolean saved = editor.commit();
				if (!saved) {
					Toast.makeText(activity, R.string.msg_failed_to_store_user_style, Toast.LENGTH_LONG).show();
				}
			} catch (Application.FileTooBigException e) {
				Log.d(TAG, "File is too big: " + dataUri);
				Toast.makeText(activity, R.string.msg_file_too_big, Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Log.d(TAG, "Failed to load: " + dataUri, e);
				Toast.makeText(activity, R.string.msg_failed_to_read_file, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (clearCacheConfirmationDialog != null) {
			clearCacheConfirmationDialog.dismiss();
		}
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

	private void sendEmail(Context context, String subject, String body) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:"));
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { BaseActivity.email });
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, body);

		context.startActivity(Intent.createChooser(intent, "E-mail"));
	}
}
