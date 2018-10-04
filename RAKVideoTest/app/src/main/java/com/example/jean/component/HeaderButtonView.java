package com.example.jean.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class HeaderButtonView extends ImageButton {
	private Bitmap _normalImage;
	private Bitmap _downImage;

	public HeaderButtonView(Context context) {
		super(context);
		init();
	}

	public HeaderButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HeaderButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {

	}

	@Override
	 public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				getDrawable().setAlpha(125);
				break;
			case MotionEvent.ACTION_UP:
				getDrawable().setAlpha(255);
				break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void onMeasure(int width, int height) {
		int i = Math.min(measure(width), measure(height));
		setMeasuredDimension(i, i);
	}

	private int measure(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int result = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED)
			result = 200;

		return result;
	}
}
