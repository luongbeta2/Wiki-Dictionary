package com.dictionary.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

@SuppressLint("SimpleDateFormat")
public class Utilitys {
	public static final String HOME_ACTION = "com.cinema.app.MainActivity.status";
	public static final String MENU_LEFT_ITEM_NAME = "item_left_name";
	public static final String MENU_RIGHT_ITEM_NAME = "item_right_name";
	public static DLog log = new DLog();

	// public String formatName(String name) {
	//
	// return name;
	// }

	public static String formatName(String name) {
		return name.trim().replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9_]+", "_");
	}

	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	public static Locale getCurrentLocale(Context context) {
		return context.getResources().getConfiguration().locale;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getDigitInString(String str) {
		int length = str.length();
		String result = "";
		for (int i = 0; i < length; i++) {
			Character character = str.charAt(i);
			if (Character.isDigit(character)) {
				result += character;
			}
		}

		return result;
	}

	/**
	 * 
	 * @param dp
	 * @param context
	 * @return
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static boolean isAppInstalled(Context context, String uri) {
		PackageManager pm = context.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	/**
	 * compare two dates
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareTwoDates(Date date1, Date date2) {
		if (date1 != null && date2 != null) {
			int retVal = date1.compareTo(date2);

			if (retVal > 0)
				return 1; // date1 is greatet than date2
			else if (retVal == 0) // both dates r equal
				return 0;

		}
		return -1; // date1 is less than date2
	}

	public static String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String date = dateFormat.format(new Date(System.currentTimeMillis()));
		return date;
	}

	public static String getCurrentDate(String typeFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(typeFormat);
		String date = dateFormat.format(new Date(System.currentTimeMillis()));
		return date;
	}

	public static Date convertStringToData(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = (Date) dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public static boolean hasConnection(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

	public static int getNumberRandom(int numberMax) {
		Random rand = new Random();
		return rand.nextInt(numberMax);
	}

	public static boolean compareStringLength(String strA, String strB) {
		return (strA.length() == strB.length()) ? true : false;
	}

	// Insert '\n' to String
	public static String parseString(String str) {
		String[] strArray = null;
		String result = "";
		if (str.contains("views")) {
			strArray = str.split(" ");

			for (int i = 0; i < strArray.length; i++) {
				if (strArray[i].equals("months") || strArray[i].equals("month") || strArray[i].equals("year") || strArray[i].equals("years")) {
					strArray[i - 2] += "###";
				}
				if (strArray[i].equals("views")) {
					strArray[i - 2] += " - ";
				}
			}
		} else
			return str;

		if (strArray != null)
			for (String item : strArray) {
				result += " " + item;
			}

		return result;
	}

	public static void getAppFromGoogleMarket(Context context, String appId) {
		try {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId)));

		} catch (android.content.ActivityNotFoundException anfe) {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appId)));
		}
	}

	public static boolean appInstalledOrNot(Context context, String uri) {
		PackageManager pm = context.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	// Time line for chat
	public static String getDuration(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int[] steps = { 1, 60, 3600, 3600 * 24 };
		String[] names = { "seconds", "minutes", "hours", "days" };

		try {
			Date d = sdf.parse(date);
			Long stamp = d.getTime() / 1000;
			Long now = System.currentTimeMillis() / 1000;
			Long dif = now - stamp;

			if (dif == 0) {
				return "";
			}

			if (stamp > now)
				return "";

			for (int i = 0; i < steps.length; i++) {
				if (dif < steps[i]) {
					String output = Long.toString(dif / steps[i - 1]) + " " + names[i - 1];
					return output;
				}
			}

			return Long.toString(dif / steps[3]) + " " + names[3];

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static Bitmap takeScreenshot(Activity activity) {
		ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
		decorChild.setDrawingCacheEnabled(true);
		decorChild.buildDrawingCache();
		Bitmap drawingCache = decorChild.getDrawingCache(true);
		Bitmap bitmap = Bitmap.createBitmap(drawingCache);
		decorChild.setDrawingCacheEnabled(false);
		return bitmap;
	}

	/**
	 * Print hash key
	 */
	public static void printHashKey(Context context) {
		try {
			String TAG = "com.sromku.simple.fb.example";
			PackageInfo info = context.getPackageManager().getPackageInfo(TAG, PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				Log.d(TAG, "keyHash: " + keyHash);
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	/**
	 * Update language
	 * 
	 * @param code
	 *            The language code. Like: en, cz, iw, ...
	 */
	public static void updateLanguage(Context context, String code) {
		Locale locale = new Locale(code);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
	}

	/**
	 * Build alert dialog with properties and data
	 * 
	 * @param pairs
	 * @return {@link AlertDialog}
	 */
	public static AlertDialog buildProfileResultDialog(Activity activity, Pair<String, String>... pairs) {
		StringBuilder stringBuilder = new StringBuilder();

		for (Pair<String, String> pair : pairs) {
			stringBuilder.append(String.format("<h3>%s</h3>", pair.first));
			stringBuilder.append(String.valueOf(pair.second));
			stringBuilder.append("<br><br>");
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(Html.fromHtml(stringBuilder.toString())).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog alertDialog = builder.create();
		return alertDialog;
	}

	public static String toHtml(Object object) {
		StringBuilder stringBuilder = new StringBuilder(256);
		try {
			for (Field field : object.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object val = field.get(object);
				stringBuilder.append("<b>");
				stringBuilder.append(field.getName().substring(1, field.getName().length()));
				stringBuilder.append(": ");
				stringBuilder.append("</b>");
				stringBuilder.append(val);
				stringBuilder.append("<br>");
			}
		} catch (Exception e) {
			// Do nothing
		}
		return stringBuilder.toString();
	}

	public static byte[] getSamleVideo(Context context, String assetFile) {
		try {
			InputStream inputStream = context.getAssets().open(assetFile);
			return getBytes(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getBytes(InputStream input) throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}

	public static String formatTimeDate(long milliSeconds) {
		// SimpleDateFormat dateFormater = new
		// SimpleDateFormat("yyyy-MM-dd hh:mm aa");
		SimpleDateFormat dateFormater = new SimpleDateFormat("MMMM yyyy dd");
		// SimpleDateFormat dateFormater = new
		// SimpleDateFormat("MMMM yyyy dd,  hh:mm aa");
		Date date = new Date(milliSeconds);
		return dateFormater.format(date);
	}

	public static long formatTimeDate(String time) {
		long timeMillis = 0;
		try {
			timeMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS").parse(time).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeMillis;
	}

	public static double roundOneDigit(double number) {
		double result = 0;
		try {
			DecimalFormat df = new DecimalFormat("#.0");
			result = Double.parseDouble(df.format(number));
		} catch (Exception e) {
			DecimalFormat df = new DecimalFormat("#,0");
			result = Double.parseDouble(df.format(number));
		}
		return result;
	}

	public static int getNumberAfterDot(double d) {
		String numString = d + "";
		return Integer.parseInt(numString.substring(numString.indexOf('.') + 1));
	}

	// Method for get files from Assets Folder and sub folder
	public static String[] listAssetFiles(Context context, String path) {
		String[] list;
		try {
			list = context.getAssets().list(path);
			if (list.length > 0) {
				log.e("listAssetFiles", path + " length = " + list.length);
				return list;
			}
		} catch (IOException e) {
		}
		return null;
	}

	// updated: "2013-11-27T08:53:17.000Z",
	// String input = "Mon, 14 May 2012 13:56:38 GMT";

	// @SuppressLint("SimpleDateFormat")
	// public static String parseDate(String strTime) {
	// DateFormat inputDF = new SimpleDateFormat("EEE, d MMM yyyy H:m:s z");
	// DateFormat outputDF = new SimpleDateFormat("d MMM yyyy");
	//
	// String input = "Mon, 14 May 2012 13:56:38 GMT";
	// Date date = inputDF.parse(input);
	// String output = outputDF.format(date);
	//
	// System.out.println(output);
	// try {
	// date = inputDF.parse(input);
	// output = outputDF.format(date);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// return output;
	// }

}
