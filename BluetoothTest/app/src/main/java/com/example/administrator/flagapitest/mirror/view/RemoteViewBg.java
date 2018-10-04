package com.example.administrator.flagapitest.mirror.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class RemoteViewBg {
    private Bitmap bitmapBg;

    public RemoteViewBg(Bitmap bitmap) {
        bitmapBg = bitmap;
    }

    //背景的绘图函数
    public void draw(Canvas canvas, Paint paint, Rect src0, Rect dst0) {
        canvas.drawBitmap(bitmapBg, src0, dst0, paint);
    }
}
