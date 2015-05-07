package com.dictionary.aard.pro;

/**
 * Created by itkach on 9/24/14.
 */
public interface LookupListener {
	public void onLookupStarted(String query);

	public void onLookupFinished(String query);

	public void onLookupCanceled(String query);
}
