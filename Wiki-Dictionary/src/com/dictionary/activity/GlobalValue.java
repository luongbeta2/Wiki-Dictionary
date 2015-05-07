package com.dictionary.activity;

import com.dictionary.model.EnglishTestSharedPreferences;


public class GlobalValue {
	public static int numberPage; // Total number pages
	public static int pageIndexGoto; // page index go from result page
	public static int questionIndexGoto = 0;
	public static boolean isReview = false;
	public final static String REVIEW = "REVIEW";
	public static String ENGLISH_TEST_PREFERENCES = "ENGLISH_TEST_PREFERENCE";
	public static EnglishTestSharedPreferences sharePreference;
	public static int startAd = 2;
}
