package com.dictionary.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.dictionary.utils.DLog;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.analytics.Tracker;

public class BaseFragment extends Fragment {

	private ProgressDialog mProgressDialog;
	public DLog log = new DLog();
	public final String ASSETS_PATH = "file:///android_asset/";
	public final String EXERCISE_FOLDER = "exercise";
	public final String GRAMMAR_FOLDER = "grammar";

	protected void showDialog() {
		if (mProgressDialog == null) {
			setProgressDialog();
		}
		mProgressDialog.show();
	}

	protected void hideDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	private void setProgressDialog() {
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle("Thinking...");
		mProgressDialog.setMessage("Doing the action...");
	}

	public void sendTrackerEvent(Tracker tracker, String categoryId, String actionId, String label, long value) {
		tracker.send(MapBuilder.createEvent(categoryId, actionId, label, value).build());
	}
}
