package com.example.administrator.bluetoothtest.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.administrator.bluetoothtest.alert.thread.AlertThread;

public class RockerView extends SurfaceView implements SurfaceHolder.Callback {
    private Activity activity;
    private SurfaceHolder surfaceHolder;
    private Paint paint = new Paint();
    private Canvas canvas;
    public static float cx = Float.MIN_VALUE;
    public static float cy = Float.MIN_VALUE;
    public static float rx = -1;
    public static float ry = -1;
    public static float Bradius = 350;//摇杆活动范围
    public static float Mradius = 120;//摇杆底盘半径
    public static float Sradius = 70;//摇杆半径
    public static Quadrant quadrant = null;

    private static final int ChangeColorMaxDrawCount = 20;


    public static enum Quadrant {
        First, Second, Third, Fourth, Xlow, XHigh, Ylow, YHigh, Midpoint
    }


    public RockerView(Context context) {
        super(context);
        activity = (Activity) context;
        this.surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        cx = dm.widthPixels / 2;
        cy = dm.heightPixels / 3 * 2;


    }

    public RockerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;
        this.surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        cx = dm.widthPixels / 2;
        cy = dm.heightPixels / 3 * 2;

    }

    public RockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity = (Activity) context;
        this.surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        cx = dm.widthPixels / 2;
        cy = dm.heightPixels / 3 * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Thread() {
            @Override
            public void run() {
                super.run();
//                 获得屏幕参数
                DisplayMetrics dm = getResources().getDisplayMetrics();
                cx = dm.widthPixels / 2;
                cy = dm.heightPixels / 3 * 2;
                RockerActivity.x = cx;
                RockerActivity.y = cy;
                doing();
                while (!activity.isFinishing()) {
                    doing();
                    try {
                        sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();


    }

    private void onStartAnimation() {
        canvas = RockerView.this.surfaceHolder.lockCanvas();
        /**
         * 清空画布
         */
        paint.setColor(Color.WHITE);
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), paint);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    private void doing() {

        canvas = RockerView.this.surfaceHolder.lockCanvas();
        /**
         * 清空画布
         */
        paint.setColor(Color.WHITE);
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), paint);
        /**
         * 绘制数据文本背景墙
         */
        paint.setColor(Color.parseColor("#b8c5ff"));
        canvas.drawRect(0, 0, cx * 2, 1000, paint);
        /**
         * 绘画最外层摇杆底盘
         */
        paint.setColor(Color.parseColor("#fff9ab"));
        canvas.drawCircle(cx, cy, Mradius + 30, paint);
        /**
         * 绘画最内层摇杆底盘
         */

        if (RockerActivity.x == cx && RockerActivity.y == cy) {
            paint.setColor(Color.parseColor("#FFCCCCCC"));
        } else {
            paint.setColor(Color.parseColor("#FFAAAAAA"));
        }
        canvas.drawCircle(cx, cy, Mradius, paint);

        /**
         * 获知摇杆头坐标
         */
        rx = RockerActivity.x;
        ry = RockerActivity.y;

        /**
         * 绘画摇杆柱头和柱尾
         */
        paint.setColor(Color.parseColor("#FF3D3D3D"));
        canvas.drawCircle(cx, cy, 40, paint);
        canvas.drawCircle(RockerActivity.x, RockerActivity.y, 30, paint);
        /**
         * 绘画摇杆柱身
         */
        float strokeWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(strokeWidth + 40);
        canvas.drawLine(rx, ry, cx, cy, paint);
        paint.setStrokeWidth(strokeWidth);
        /**
         * 绘画摇杆头部
         */
        paint.setColor(Color.parseColor("#ff8d8d"));
        canvas.drawCircle(rx, ry, Sradius, paint);
        /**
         * 打印数据文本
         */
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        if (AlertThread.aswitch){

            canvas.drawText("警报系统状态 = " + "开启" , 10, 50, paint);
        }else {
            canvas.drawText("警报系统状态 = " + "关闭" , 10, 50, paint);
        }

        if (RockerActivity.dataText.length() != 0) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(80);
            int i = 0;
            int equalsIndex = -1;
            String measure = null;
            for (String s : RockerActivity.dataText.toString().split("\n")) {
                if ("".equals(s)) continue;
                //获得键值
                equalsIndex = s.indexOf(" = ");
                measure = s.substring(0, equalsIndex);


                if ("火焰".equals(measure)) {
                    RockerActivity.fireDrawCount++;
                    paint.setColor(getTextColor(RockerActivity.fireDrawCount,RockerActivity.fireColor));


                } else if ("雨滴".equals(measure)) {
                    RockerActivity.rainDrawCount++;
                    paint.setColor(getTextColor(RockerActivity.rainDrawCount,RockerActivity.rainColor));


                } else if ("烟雾".equals(measure)) {
                    RockerActivity.smogDrawCount++;
                    paint.setColor(getTextColor(RockerActivity.smogDrawCount,RockerActivity.smogColor));


                } else if ("CO浓度".equals(measure)) {
                    RockerActivity.coDrawCount++;
                    paint.setColor(getTextColor(RockerActivity.coDrawCount,RockerActivity.coColor));



                } else if ("人体".equals(measure)) {
                    RockerActivity.bodyDrawCount++;
                    paint.setColor(getTextColor(RockerActivity.bodyDrawCount,RockerActivity.bodyColor));


                }
                canvas.drawText(s, 200, 150 + i, paint);
                paint.setColor(Color.BLACK);
                i += 100;
            }
        }


        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private int getTextColor(int count ,int color) {

        if (color == Color.BLACK) {
            return Color.BLACK;
        } else {
            if (count / ChangeColorMaxDrawCount % 2 == 0){
                return color;
            }else {
                return Color.BLACK;
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return 获得两点之间的距离
     */
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }

    public static Quadrant getQuadrant(float x, float y) {
        float changeX = x - cx;
        float changeY = y - cy;
        if (changeX == 0 && changeY > 0) {
            quadrant = Quadrant.YHigh;
        } else if (changeX == 0 && changeY < 0) {
            quadrant = Quadrant.Ylow;
        } else if (changeY == 0 && changeX > 0) {
            quadrant = Quadrant.XHigh;
        } else if (changeY == 0 && changeX < 0) {
            quadrant = Quadrant.Xlow;
        } else if (changeX > 0 && changeY > 0) {
            quadrant = Quadrant.First;
        } else if (changeX > 0 && changeY < 0) {
            quadrant = Quadrant.Fourth;
        } else if (changeX < 0 && changeY > 0) {
            quadrant = Quadrant.Second;
        } else if (changeX < 0 && changeY < 0) {
            quadrant = Quadrant.Third;
        } else {
            quadrant = Quadrant.Midpoint;
        }
        return quadrant;
    }
}
