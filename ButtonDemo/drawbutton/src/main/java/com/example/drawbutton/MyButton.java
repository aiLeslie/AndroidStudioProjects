package com.example.drawbutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/21.
 */

public class MyButton extends android.support.v7.widget.AppCompatButton {
    private Paint paint = new Paint();

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        //设置字体属性
        paint.setColor(Color.parseColor("#FF006AFF"));
        paint.setTextSize(60);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        Toast.makeText(getContext(), "invoke <invalidate> method", Toast.LENGTH_SHORT).show();
        super.invalidate(l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Toast.makeText(getContext(), "invoke <onDraw> method", Toast.LENGTH_SHORT).show();
        super.onDraw(canvas);

        //在canvas上添加字体,并设置字体属性
        canvas.drawText("my draw", 800, 60, paint);

        //在canvas上添加图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }
}
