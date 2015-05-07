package com.dictionary.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.dictionary.aard.pro.R;
import com.dictionary.utils.DLog;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.analytics.Tracker;

public class BaseActivity extends FragmentActivity {

	public DLog log;
	public static String dicResource_luong = "https://raw.githubusercontent.com/luongbeta2/Store_Luong/master/assets/store_dictionary.txt";
	public static String dicResource_goc = "https://github.com/itkach/slob/wiki/Dictionaries";
	public ProgressDialog ringProgressDialog;
	public Drawable oldBackground = null;
	// public int currentColor = 0xFFC74B46;
	// public int currentColor = 0xFFBB0000; // do
	// public int currentColor = 0xFF36ad8a; // xanh
	public int currentColor = 0xFFb18856; // vang
	public ActionBar actionBar;
	public final Handler handler = new Handler();
	public boolean isNetWorkConnection = true;
	public String dictionaryUrl = "https://translate.google.com/m/translate?";
	// public String dictionaryUrl =
	// "http://www.oxforddictionaries.com/definition/english/";
	public final static String COUNTING_TIME_TEST = "COUNTING_TIME_TEST";
	public final static String TYPE_QUESTION = "TYPE_QUESTION";
	public static String GO_PAGE_ACTION = "com.smart_test.go_page_action";
	public static String QUESTION_INDEX = "QUESTION_INDEX";
	public static String IS_REVIEW = "IS_REVIEW";
	public final String ASSETS_PATH = "file:///android_asset/";
	public static final String email = "luongbeta.it@gmail.com";

	public enum TextType {
		AbeatbyKaiRegular, Harrington_normal, Station, VAG_Rounded_Bold, VAG_Rounded_Thin, MYRIADAB, MYRIADAM, MYRIADAS, MYRIADAT, LCD
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.plum_color));
		}

		log = new DLog();
	}

	public void sendTrackerEvent(Tracker tracker, String categoryId, String actionId, String label, long value) {
		tracker.send(MapBuilder.createEvent(categoryId, actionId, label, value).build());
	}

	public void setFont(TextView tv, TextType type) {
		String parth = "";
		switch (type) {
		case AbeatbyKaiRegular:
			parth = "fonts/AbeatbyKaiRegular.otf";
			break;
		case Harrington_normal:
			parth = "fonts/Harrington_normal.ttf";
			break;
		case Station:
			parth = "fonts/Station.ttf";
			break;
		case VAG_Rounded_Bold:
			parth = "fonts/VAG_Rounded_Bold.ttf";
			break;
		case VAG_Rounded_Thin:
			parth = "fonts/VAG_Rounded_Thin.otf";
			break;
		case MYRIADAB:
			parth = "fonts/MYRIADAB.TTF";
			break;
		case MYRIADAM:
			parth = "fonts/MYRIADAM.TTF";
			break;
		case MYRIADAS:
			parth = "fonts/MYRIADAS.TTF";
			break;
		case MYRIADAT:
			parth = "fonts/MYRIADAT.TTF";
			break;
		case LCD:
			parth = "fonts/lcd.ttf";
			break;

		default:
			parth = "fonts/MYRIADAS.TTF";
			break;
		}

		Typeface face = Typeface.createFromAsset(getAssets(), parth);
		tv.setTypeface(face);
	}

	public void showProgressbarWait(Context context) {
		ringProgressDialog = ProgressDialog.show(context, "", "Please wait ...", true);

		ringProgressDialog.setCancelable(true);
	}

	public long convertTimeTest(String timeStr) {
		// format input like "10 minutes"
		String[] timeArray = timeStr.split(" ");
		long timeTest = 0;
		try {
			timeTest = Long.parseLong(timeArray[0]) * 60 * 1000;
		} catch (Exception e) {
			timeTest = 0;
		}
		return timeTest;
	}

	public MediaPlayer player;

	public void playBeep() {
		try {
			if (player == null) {
				player = new MediaPlayer();
			}

			if (player.isPlaying()) {
				player.stop();
				player.release();
				player = new MediaPlayer();
			}

			AssetFileDescriptor descriptor = getAssets().openFd("beepbeep.mp3");
			player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();

			player.prepare();
			player.setVolume(1f, 1f);
			player.setLooping(true);
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String webview_format = "<style>img{display: inline;height: auto;max-width: 100%;}</style>";


}
