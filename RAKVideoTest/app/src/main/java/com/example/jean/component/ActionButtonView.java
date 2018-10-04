package com.example.jean.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class ActionButtonView extends ImageButton {
	public ActionButtonView(Context context) {
		super(context);
		init();
	}

	public ActionButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ActionButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		//getDrawable().setAlpha(130);
		setAlpha(130);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				getDrawable().setAlpha(255);
				break;
			case MotionEvent.ACTION_UP:
				getDrawable().setAlpha(130);
				break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void setSelected(boolean selected) {
		getDrawable().setAlpha(selected ? 255 : 130);
		super.setSelected(selected);
	}
}
