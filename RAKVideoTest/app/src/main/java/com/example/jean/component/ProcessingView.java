package com.example.jean.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.jean.rakvideotest.R;

public class ProcessingView extends View {
	private Paint _paint;
	private int _color;
	private int _textColor;
	private Bitmap _car = BitmapFactory.decodeResource(getResources(), R.drawable.config_device);
	private int _value = 0;
	private String _text;

	public ProcessingView(Context context) {
		super(context);
	}

	public ProcessingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProcessingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setValue(int value) {
		_value = value;
	}

	public void setColor(int color) {
		_color = color;
	}

	public void setText(String text) {
		_text = text;
	}

	public void setTextColor(int color) {
		_textColor = color;
	}

	private int measure(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int result = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED)
			result = 200;

		return result;
	}

	@Override
	protected void onMeasure(int width, int height) {
		int i = Math.min(measure(width), measure(height));
		setMeasuredDimension(i, i);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		_paint = new Paint();
		_paint.setAntiAlias(true);
		_paint.setColor(Color.parseColor("#999999"));
		canvas.drawColor(Color.TRANSPARENT);
		_paint.setStrokeWidth((float)1.0);
		_paint.setStyle(Paint.Style.FILL);

		int width = getWidth();
		int radius = width / 2;
		int center = radius;

		canvas.drawCircle(center, center, radius, _paint);

		_paint.setColor(Color.parseColor("#f2f2f2"));
		canvas.drawCircle(center, center, (int) (radius * .98), _paint);

		_paint.setColor(_color);
		int left = (int)(radius * 0);
		int top = left;
		int right = (int)(radius * 2) + left;
		int bottom = right;
		canvas.drawArc(new RectF(left, top, right, bottom), -90, (int) (_value * 3.6), true, _paint);

		_paint.setColor(Color.parseColor("#f2f2f2"));
		canvas.drawCircle(center, center, (int)(radius * .98), _paint);

//		_paint.setColor(Color.parseColor("#ffffff"));
//		canvas.drawCircle(center, center, (int)(radius * .82), _paint);
//
//		_paint.setStyle(Paint.Style.STROKE);
//		_paint.setColor(Color.parseColor("#eeeeee"));
//		canvas.drawCircle(center, center, radius, _paint);
//		canvas.drawCircle(center, center, (int)(radius * .76), _paint);

		// draw car
		int carWidth = (int)(width * .61);
		left = (width - carWidth) / 2;
		top = (int)(left * .7);

		canvas.drawBitmap(_car, null, new Rect(left, top, carWidth + left, carWidth + top), _paint);

		// draw percent text
		_paint.setStyle(Paint.Style.FILL);
		_paint.setColor(Color.parseColor("#828282"));

		String value = Integer.toString(_value);
		_paint.setTextSize(80);
		float textWidth = _paint.measureText(value);
		float y = (float)(radius * 1.7);
		float x;

		if (_text != null) {
			_paint.setTextSize(40);
			_paint.setColor(_textColor);
			x = (width - _paint.measureText(_text)) / 2;
			canvas.drawText(_text, x, y, _paint);
			return;
		}

		_paint.setTextSize(40);
		float percentWidth = _paint.measureText("%");

		x = (width - textWidth - percentWidth) / 2;

		_paint.setTextSize(80);
		canvas.drawText(value, x, y, _paint);

		_paint.setTextSize(40);
		x = x + textWidth + 5;

		canvas.drawText("%", x, y, _paint);
	}
}
