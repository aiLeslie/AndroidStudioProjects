package com.xinlan.imageedit.editimage.view;

import com.example.jean.rakvideotest.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class StickerItem
{
	private static final float MIN_SCALE = 0.15f;
	private static final int HELP_BOX_PAD = 25;

	private static final int BUTTON_WIDTH = 25;

	public Bitmap bitmap;
	public Rect srcRect;
	public RectF dstRect;
	private Rect helpToolsRect;
	public RectF deleteRect;
	public RectF rotateRect;

	RectF helpBox;
	public Matrix matrix;
	private float roatetAngle = 0;
	boolean isDrawHelpTool = false;
	private Paint dstPaint = new Paint();
	private Paint paint = new Paint();
	private Paint helpBoxPaint = new Paint();

	private float initWidth;

	private static Bitmap deleteBit;
	private static Bitmap rotateBit;

	private Paint greenPaint = new Paint();
	public RectF detectRotateRect;

	public RectF detectDeleteRect;

	public StickerItem(Context context)
	{

		helpBoxPaint.setColor(Color.BLACK);
		helpBoxPaint.setStyle(Style.STROKE);
		helpBoxPaint.setAntiAlias(true);
		helpBoxPaint.setStrokeWidth(4);

		dstPaint = new Paint();
		dstPaint.setColor(Color.RED);
		dstPaint.setAlpha(120);

		greenPaint = new Paint();
		greenPaint.setColor(Color.GREEN);
		greenPaint.setAlpha(120);

		if (deleteBit == null)
		{
			deleteBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_delete);
		}// end if
		if (rotateBit == null)
		{
			rotateBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker_rotate);
		}// end if
	}

	public void init(Bitmap addBit, View parentView)
	{
		this.bitmap = addBit;
		this.srcRect = new Rect(0, 0, addBit.getWidth(), addBit.getHeight());
		int bitWidth = Math.min(addBit.getWidth(), parentView.getWidth() >> 1);
		int bitHeight = (int) bitWidth * addBit.getHeight() / addBit.getWidth();
		int left = (parentView.getWidth() >> 1) - (bitWidth >> 1);
		int top = (parentView.getHeight() >> 1) - (bitHeight >> 1);
		this.dstRect = new RectF(left, top, left + bitWidth, top + bitHeight);
		this.matrix = new Matrix();
		this.matrix.postTranslate(this.dstRect.left, this.dstRect.top);
		this.matrix.postScale((float) bitWidth / addBit.getWidth(), (float) bitHeight / addBit.getHeight(), this.dstRect.left, this.dstRect.top);
		initWidth = this.dstRect.width();
		// item.matrix.setScale((float)bitWidth/addBit.getWidth(),
		// (float)bitHeight/addBit.getHeight());
		this.isDrawHelpTool = true;
		this.helpBox = new RectF(this.dstRect);
		updateHelpBoxRect();

		helpToolsRect = new Rect(0, 0, deleteBit.getWidth(), deleteBit.getHeight());

		deleteRect = new RectF(helpBox.left - BUTTON_WIDTH, helpBox.top - BUTTON_WIDTH, helpBox.left + BUTTON_WIDTH, helpBox.top + BUTTON_WIDTH);
		rotateRect = new RectF(helpBox.right - BUTTON_WIDTH, helpBox.bottom - BUTTON_WIDTH, helpBox.right + BUTTON_WIDTH, helpBox.bottom + BUTTON_WIDTH);

		detectRotateRect = new RectF(rotateRect);
		detectDeleteRect = new RectF(deleteRect);
	}

	private void updateHelpBoxRect()
	{
		this.helpBox.left -= HELP_BOX_PAD;
		this.helpBox.right += HELP_BOX_PAD;
		this.helpBox.top -= HELP_BOX_PAD;
		this.helpBox.bottom += HELP_BOX_PAD;
	}

	public void updatePos(final float dx, final float dy)
	{
		this.matrix.postTranslate(dx, dy);

		dstRect.offset(dx, dy);
		helpBox.offset(dx, dy);
		deleteRect.offset(dx, dy);
		rotateRect.offset(dx, dy);

		this.detectRotateRect.offset(dx, dy);
		this.detectDeleteRect.offset(dx, dy);
	}

	public void updateRotateAndScale(final float oldx, final float oldy, final float dx, final float dy)
	{
		float c_x = dstRect.centerX();
		float c_y = dstRect.centerY();

		float x = this.detectRotateRect.centerX();
		float y = this.detectRotateRect.centerY();

		// float x = oldx;
		// float y = oldy;

		float n_x = x + dx;
		float n_y = y + dy;

		float xa = x - c_x;
		float ya = y - c_y;

		float xb = n_x - c_x;
		float yb = n_y - c_y;

		float srcLen = (float) Math.sqrt(xa * xa + ya * ya);
		float curLen = (float) Math.sqrt(xb * xb + yb * yb);

		// System.out.println("srcLen--->" + srcLen + "   curLen---->" +
		// curLen);

		float scale = curLen / srcLen;

		float newWidth = dstRect.width() * scale;
		if (newWidth / initWidth < MIN_SCALE)
		{
			return;
		}

		this.matrix.postScale(scale, scale, this.dstRect.centerX(), this.dstRect.centerY());
		// this.matrix.postRotate(5, this.dstRect.centerX(),
		// this.dstRect.centerY());
		scaleRect(this.dstRect, scale);
		helpBox.set(dstRect);
		updateHelpBoxRect();
		rotateRect.offsetTo(helpBox.right - BUTTON_WIDTH, helpBox.bottom - BUTTON_WIDTH);
		deleteRect.offsetTo(helpBox.left - BUTTON_WIDTH, helpBox.top - BUTTON_WIDTH);

		detectRotateRect.offsetTo(helpBox.right - BUTTON_WIDTH, helpBox.bottom - BUTTON_WIDTH);
		detectDeleteRect.offsetTo(helpBox.left - BUTTON_WIDTH, helpBox.top - BUTTON_WIDTH);

		double cos = (xa * xb + ya * yb) / (srcLen * curLen);
		if (cos > 1 || cos < -1)
			return;
		float angle = (float) Math.toDegrees(Math.acos(cos));
		// System.out.println("angle--->" + angle);
		float calMatrix = xa * yb - xb * ya;

		int flag = calMatrix > 0 ? 1 : -1;
		angle = flag * angle;

		// System.out.println("angle--->" + angle);
		roatetAngle += angle;
		this.matrix.postRotate(angle, this.dstRect.centerX(), this.dstRect.centerY());

		rotateRect(this.detectRotateRect, this.dstRect.centerX(), this.dstRect.centerY(), roatetAngle);
		rotateRect(this.detectDeleteRect, this.dstRect.centerX(), this.dstRect.centerY(), roatetAngle);
		// System.out.println("angle----->" + angle + "   " + flag);

		// System.out
		// .println(srcLen + "     " + curLen + "    scale--->" + scale);

	}

	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(this.bitmap, this.matrix, null);
		// canvas.drawRect(this.dstRect, dstPaint);

		if (this.isDrawHelpTool){
			canvas.save();
			canvas.rotate(roatetAngle, helpBox.centerX(), helpBox.centerY());
			canvas.drawRoundRect(helpBox, 10, 10, helpBoxPaint);
			canvas.drawBitmap(deleteBit, helpToolsRect, deleteRect, null);
			canvas.drawBitmap(rotateBit, helpToolsRect, rotateRect, null);
			canvas.restore();

			// canvas.drawRect(deleteRect, dstPaint);
			// canvas.drawRect(rotateRect, dstPaint);
			// canvas.drawRect(detectRotateRect, this.greenPaint);
			// canvas.drawRect(detectDeleteRect, this.greenPaint);
		}// end if

		// detectRotateRect
	}

	Path path = new Path();

	private static void scaleRect(RectF rect, float scale)
	{
		float w = rect.width();
		float h = rect.height();

		float newW = scale * w;
		float newH = scale * h;

		float dx = (newW - w) / 2;
		float dy = (newH - h) / 2;

		rect.left -= dx;
		rect.top -= dy;
		rect.right += dx;
		rect.bottom += dy;
	}


	private static void rotateRect(RectF rect, float center_x, float center_y, float roatetAngle)
	{
		float x = rect.centerX();
		float y = rect.centerY();
		float sinA = (float) Math.sin(Math.toRadians(roatetAngle));
		float cosA = (float) Math.cos(Math.toRadians(roatetAngle));
		float newX = center_x + (x - center_x) * cosA - (y - center_y) * sinA;
		float newY = center_y + (y - center_y) * cosA + (x - center_x) * sinA;

		float dx = newX - x;
		float dy = newY - y;

		rect.offset(dx, dy);

		// float w = rect.width();
		// float h = rect.height();
		// rect.left = newX;
		// rect.top = newY;
		// rect.right = newX + w;
		// rect.bottom = newY + h;
	}
}// end class
