package com.dictionary.utils;

import com.dictionary.aard.pro.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

//<com.wvr.widget.TextProgressBar
//android:id="@+id/progressBarWithText"
//style="@android:style/Widget.ProgressBar.Horizontal"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:max="100"
//android:maxHeight="30dp"
//android:minHeight="30dp"
//android:progress="70"
//android:progressDrawable="@drawable/progress_bar_horizontal_2"
//components:textSize="18dp" 
//components:textColor="@android:color/black" 
//components:text="Loading 70%" />

public class TextProgressBar extends ProgressBar {

	private String text = "";
	private int textColor = getResources().getColor(R.color.app_text);
	private float textSize = 15;

	public TextProgressBar(Context context) {
		super(context);
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// create an instance of class Paint, set color and font size
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);
		// In order to show text in a middle, we need to know its size
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		// Now we store font size in bounds variable and can calculate it's
		// position
		int x = getWidth() / 2 - bounds.centerX();
		int y = getHeight() / 2 - bounds.centerY();
		// drawing text with appropriate color and size in the center
		canvas.drawText(text, x, y, textPaint);
	}

	public String getText() {
		return text;
	}

	public synchronized void setText(String text) {
		if (text != null) {
			this.text = text;
		} else {
			this.text = "";
		}
		postInvalidate();
	}

	public int getTextColor() {
		return textColor;
	}

	public synchronized void setTextColor(int textColor) {
		this.textColor = textColor;
		postInvalidate();
	}

	public float getTextSize() {
		return textSize;
	}

	public synchronized void setTextSize(float textSize) {
		this.textSize = textSize;
		postInvalidate();
	}

}