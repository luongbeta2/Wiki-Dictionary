package com.dictionary.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.dictionary.activity.GlobalValue;

public class EnglishTestSharedPreferences {

	public static final String APPLICATION_INSTALL_FIRST_TIME = "APPLICATION_INSTALL_FIRST_TIME";
	public static final String PREF_SETTING_USER_ACCESS_TOKEN = "PREF_SETTING_USER_ACCESS_TOKEN";

	public static final String USER = "USER";
	public static final String PASS = "PASS";
	public static final String USER_ID = "USER_ID";
	public static final String FACEBOOK_ID = "FACEBOOK_ID";
	public static final String FACEBOOK_TOKEN = "FACEBOOK_TOKEN";
	public static final String FACEBOOK_NAME = "FACEBOOK_NAME";
	public static final String FACEBOOK_FIRST_NAME = "FACEBOOK_FIRST_NAME";
	public static final String FACEBOOK_LAST_NAME = "FACEBOOK_LAST_NAME";
	public static final String FACEBOOK_BIRTHDAY = "FACEBOOK_BIRTHDAY";
	public static final String FACEBOOK_EMAIL = "FACEBOOK_EMAIL";
	public static final String AVATAR_URL = "AVATAR_URL";
	public static final String USING_FACEBOOK_ACCOUNT = "USING_FACEBOOK_ACCOUNT";
	public static final int CINEMA_ACCOUNT = 0;
	public static final int FACEBOOK_ACCOUNT = 1;
	public static final int TWEETER_ACCOUNT = 2;
	public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public static final String LOG_OUT = "LOG_OUT";
	public static final String NUMBER_OF_QUESTIONS = "NUMBER_OF_QUESTION";
	public static final String NUMBER_IN_PAGE = "NUMBER_IN_PAGE";
	public static final String TIME_TEST = "TIME_TEST";

	public static final String TWEETER_ID = "TWEETER_ID";
	public static final String TWEETER_TOKEN = "TWEETER_TOKEN";
	public static final String TWEETER_NAME = "TWEETER_NAME";
	public static final String TOGGLE_CHECKED_DETAIL = "TOGGLE_CHECKED_DETAIL";

	private Context context;

	public EnglishTestSharedPreferences(Context context) {
		this.context = context;
	}

	public void putNumber_Of_Question_In_A_Testing(int number) {
		putIntValue(NUMBER_OF_QUESTIONS, number);
	}

	public int getNumber_Of_Question_In_A_Testing() {
		return getIntValue(NUMBER_OF_QUESTIONS);
	}

	public void putNumber_Of_A_Page(int number) {
		putIntValue(NUMBER_IN_PAGE, number);
	}

	public int getNumber_In_A_Page() {
		return getIntValue(NUMBER_IN_PAGE);
	}

	public void putTimeTest(String time) {
		putStringValue(TIME_TEST, time);
	}

	public String getTimeTest() {
		return getStringValue(TIME_TEST);
	}

	// ====
	public int getAccountType() {
		return getIntValue(ACCOUNT_TYPE);
	}

	public void putAccountType(int accountType) {
		putIntValue(ACCOUNT_TYPE, accountType);
	}

	// ======================== UTILITY FUNCTIONS ========================

	public void cleanFacebookData() {
		putFacebookId("");
		putFacebookName("");
		putFacebookFirstName("");
		putFacebookLastName("");
		putFacebookToken("");
	}

	public void cleancinemaAccount() {
		putcinemaUser("");
		putcinemaPass("");
	}

	public static String COUNT_START_APP = "COUNT_START_APP";

	// count start app for ad
	public void putCountStartApp(int count) {
		putIntValue(COUNT_START_APP, count);
	}

	public int getCountStartApp() {
		return getIntValue(COUNT_START_APP);
	}

	// end

	public void putAcountLogout(boolean isLogout) {
		putBooleanValue(LOG_OUT, isLogout);
	}

	public boolean getAcountLogout() {
		return getBooleanValue(LOG_OUT);
	}

	public void putcinemaUser(String name) {
		putStringValue(USER, name);
	}

	public String getcinemaName() {
		return getStringValue(USER);
	}

	public void putcinemaPass(String pass) {
		putStringValue(PASS, pass);
	}

	public String getcinemaPass() {
		return getStringValue(PASS);
	}

	public void putUsingFacebookAccountStatus(String usingFacebookAccount) {
		putStringValue(USING_FACEBOOK_ACCOUNT, usingFacebookAccount);
	}

	public String getUsingFacebookAccountStatus() {
		return getStringValue(USING_FACEBOOK_ACCOUNT);
	}

	public void putFacebookId(String strFacebookId) {
		putStringValue(FACEBOOK_ID, strFacebookId);
	}

	public String getFacebookId() {
		return getStringValue(FACEBOOK_ID);
	}

	public void putFacebookToken(String strFacebookToken) {
		putStringValue(FACEBOOK_TOKEN, strFacebookToken);
	}

	public String getFacebookToken() {
		return getStringValue(FACEBOOK_TOKEN);
	}

	public void putFacebookName(String strName) {
		putStringValue(FACEBOOK_NAME, strName);
	}

	public String getFacebookName() {
		return getStringValue(FACEBOOK_NAME);
	}

	public void putFacebookFirstName(String strFirstName) {
		putStringValue(FACEBOOK_FIRST_NAME, strFirstName);
	}

	public String getFacebookFirstName() {
		return getStringValue(FACEBOOK_FIRST_NAME);
	}

	public void putFacebookBirthday(String strBirtday) {
		putStringValue(FACEBOOK_BIRTHDAY, formatFrenchDate(strBirtday));
	}

	public String getFacebookBirthday() {
		return getStringValue(FACEBOOK_BIRTHDAY);
	}

	public void putFacebookLastName(String strLastName) {
		putStringValue(FACEBOOK_LAST_NAME, strLastName);
	}

	public String getFacebookLastName() {
		return getStringValue(FACEBOOK_LAST_NAME);
	}

	public void putFacebookEmail(String strEmail) {
		putStringValue(FACEBOOK_EMAIL, strEmail);
	}

	public String getFacebookEmail() {
		return getStringValue(FACEBOOK_EMAIL);
	}

	/** Tweeter **/

	public void putTweeterId(String strTweeterId) {
		putStringValue(TWEETER_ID, strTweeterId);
	}

	public String getTweeterId() {
		return getStringValue(TWEETER_ID);
	}

	public void putTweeterToken(String strTweeterToken) {
		putStringValue(TWEETER_TOKEN, strTweeterToken);
	}

	public String getTweeterToken() {
		return getStringValue(TWEETER_TOKEN);
	}

	public void putTweeterName(String strName) {
		putStringValue(TWEETER_NAME, strName);
	}

	public String getTweeterName() {
		return getStringValue(TWEETER_NAME);
	}

	public void putToggleDetailIsChecked(boolean isChecked) {
		putBooleanValue(TOGGLE_CHECKED_DETAIL, isChecked);
	}

	public boolean getToggleDetailIsChecked() {
		return getBooleanValue(TOGGLE_CHECKED_DETAIL);
	}

	// ======================== CORE FUNCTIONS ========================

	/**
	 * Save a long integer to SharedPreferences
	 * 
	 * @param key
	 * @param n
	 */
	public void putLongValue(String key, long n) {
		// SmartLog.log(TAG, "Set long integer value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, n);
		editor.commit();
	}

	/**
	 * Read a long integer to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public long getLongValue(String key) {
		// SmartLog.log(TAG, "Get long integer value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		return pref.getLong(key, 0);
	}

	/**
	 * Save an integer to SharedPreferences
	 * 
	 * @param key
	 * @param n
	 */
	public void putIntValue(String key, int n) {
		// SmartLog.log(TAG, "Set integer value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, n);
		editor.commit();
	}

	/**
	 * Read an integer to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public int getIntValue(String key) {
		// SmartLog.log(TAG, "Get integer value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		return pref.getInt(key, 0);
	}

	/**
	 * Save an string to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putStringValue(String key, String s) {
		// SmartLog.log(TAG, "Set string value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, s);
		editor.commit();
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public String getStringValue(String key) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		return pref.getString(key, "");
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getStringValue(String key, String defaultValue) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		return pref.getString(key, defaultValue);
	}

	/**
	 * Save an boolean to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putBooleanValue(String key, Boolean b) {
		// SmartLog.log(TAG, "Set boolean value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	/**
	 * Read an boolean to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key) {
		// SmartLog.log(TAG, "Get boolean value");
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	/**
	 * Save an float to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putFloatValue(String key, float f) {
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putFloat(key, f);
		editor.commit();
	}

	/**
	 * Read an float to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public float getFloatValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.ENGLISH_TEST_PREFERENCES, 0);
		return pref.getFloat(key, 0.0f);
	}

	public static String formatFrenchDate(String dateTime) {
		String[] date = dateTime.split("/");

		return date[2] + "-" + date[1] + "-" + date[0];
	}
}
