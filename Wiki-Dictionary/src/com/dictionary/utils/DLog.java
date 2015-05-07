package com.dictionary.utils;

import android.util.Log;

public class DLog {
	private final boolean SHOW_LOG = true;
	private final String DEBUG = "DEBUG";

	public DLog() {
	}

	public void e(String msg) {
		if (SHOW_LOG) {
			Log.e(DEBUG, msg);
		}
	}

	public void e(String tag, String msg) {
		if (SHOW_LOG) {
			Log.e(tag, msg);
		}
	}

	public void d(String tag, String msg) {
		if (SHOW_LOG) {
			Log.d(tag, msg);
		}
	}

	public void v(String tag, String msg) {
		if (SHOW_LOG) {
			Log.v(tag, msg);
		}
	}

	public void w(String tag, String msg) {
		if (SHOW_LOG) {
			Log.w(tag, msg);
		}
	}

	public void i(String tag, String msg) {
		if (SHOW_LOG) {
			Log.i(tag, msg);
		}
	}

	public void i(String msg) {
		if (SHOW_LOG) {
			Log.i(DEBUG, msg);
		}
	}

	public void error(String msg) {
		if (SHOW_LOG) {
			Log.e(DEBUG, "ERROR: " + msg);
		}
	}

	public void error(String tag, String msg) {
		if (SHOW_LOG) {
			Log.e(tag, "ERROR: " + msg);
		}
	}
}
