package com.example.administrator.bluetoothtest.mirror.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.administrator.bluetoothtest.R;


public class RemoteSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private float scale = this.getResources().getDisplayMetrics().density;
    private Thread th;
    private SurfaceHolder sfh;
    private Canvas canvas;
    private Paint paint;
    private boolean flag;

    private int bigCircleX = 0;
    private int bigCircleY = 0;
    private int bigCircleR = 0;
    //摇杆的X,Y坐标以及摇杆的半径
    private float smallCircleX = 0;
    private float smallCircleY = 0;
    private float smallCircleR = 0;


    private Bitmap bitmap;
    private RemoteViewBg remoteViewBg;

    public RemoteSurfaceView(Context context) {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

    }

    public void surfaceCreated(SurfaceHolder holder) {
        int width = getWidth();
        int height = getHeight();
        bigCircleX = width / 2;
        bigCircleY = height / 2;
        bigCircleR = width / 4;
        smallCircleX = width / 2;
        smallCircleY = height / 2;
        smallCircleR = width / 8;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.area_bg);
        remoteViewBg = new RemoteViewBg(bitmap);
        th = new Thread(this);
        flag = true;
        th.start();


    }

    /***
     * 得到两点之间的弧度
     */
    public float getRad(float px1, float py1, float px2, float py2) {
        float x = px2 - px1;

        float y = py1 - py2;
        //斜边的长
        float z = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        float cosAngle = x / z;
        float rad = (float) Math.acos(cosAngle);

        if (py2 < py1) {
            rad = -rad;
        }
        return rad;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            // 在范围外触摸
            if (Math.sqrt(Math.pow((bigCircleX - (int) event.getX()), 2) + Math.pow((bigCircleY - (int) event.getY()), 2)) >= bigCircleR) {

                double tempRad = getRad(bigCircleX, bigCircleY, event.getX(), event.getY());

                getXY(bigCircleX, bigCircleY, bigCircleR, tempRad);
            } else {//范围内触摸
                smallCircleX = (int) event.getX();
                smallCircleY = (int) event.getY();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            smallCircleX = bigCircleX;
            smallCircleY = bigCircleY;

        }
        return true;
    }


    public void getXY(float x, float y, float R, double rad) {
        //获取圆周运动的X坐标
        smallCircleX = (float) (R * Math.cos(rad)) + x;
        //获取圆周运动的Y坐标
        smallCircleY = (float) (R * Math.sin(rad)) + y;

    }

    public void draw() {
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.WHITE);

            // 指定图片绘制区域(左上角的四分之一)
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            // 指定图片在屏幕上显示的区域
            Rect dst = new Rect(bigCircleX - bigCircleR, bigCircleY - bigCircleR, bigCircleX + bigCircleR, bigCircleY + bigCircleR);
            // 绘制图片
            remoteViewBg.draw(canvas, paint, src, dst);
            paint.setColor(0x70ff0000);
            //绘制摇杆
            canvas.drawCircle(smallCircleX, smallCircleY, smallCircleR, paint);
        } catch (Exception e) {

        } finally {
            try {
                if (canvas != null)
                    sfh.unlockCanvasAndPost(canvas);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void run() {

        while (flag) {
            draw();
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;

    }

}
