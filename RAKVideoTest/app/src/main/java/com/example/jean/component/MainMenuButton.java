package com.example.jean.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainMenuButton extends ImageView {

	public MainMenuButton(Context context) {
		super(context);
		init();
	}

	public MainMenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MainMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setAlpha(180);
		setClickable(true);
		setAdjustViewBounds(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				setAlpha(255);
				break;
			default:
				setAlpha(180);
				break;
		}

		return super.onTouchEvent(event);
	}
}
