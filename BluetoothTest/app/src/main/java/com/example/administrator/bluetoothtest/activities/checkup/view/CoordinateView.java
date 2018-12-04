package com.example.administrator.bluetoothtest.activities.checkup.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;


import com.example.administrator.bluetoothtest.activities.checkup.model.coordinate.Coordinate;
import com.example.administrator.bluetoothtest.view.FloatWater;
import com.example.administrator.bluetoothtest.view.MySurfaceView;

import java.util.ArrayList;


public class CoordinateView extends MySurfaceView implements MySurfaceView.Drawable{
    private Coordinate coord = new Coordinate();
    private float ox = 0; // 原点x坐标
    private float oy = 0; // 原点y坐标
    private float tempX = -1; // 用户点击屏幕x坐标
    private float tempY = -1; // 用户点击屏幕y坐标
//    private float oxLBound = -500;
//    private float oxRBound = 500;
//    private float oyTBound = -500;
//    private float oyBBound = 500;
    private boolean firstClick = false;
    private float scale = 0.5f;
    public int DISPLAY_WIDTH;
    public int DISPLAY_HEIGHT;
    private float X_OFFSET = 1;
    public boolean isOffset = false;
    private  FloatWater water;

    private void initFloatWater(){

        water = new FloatWater(-200, 500, DISPLAY_WIDTH + 200);
    }

    public ArrayList<Float> data = new ArrayList<>();

    public CoordinateView(Context context) {
        super(context);

    }

    public CoordinateView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    {
        // 开启绘画功能
        setDrawable(this);
    }

    public Coordinate getCoord() {
        return coord;
    }

    private boolean getSize = false;

    /**
     * 初始化尺寸
     */
    private void size() {
        getSize = true;

        DISPLAY_WIDTH = getWidth();
        DISPLAY_HEIGHT = getHeight();

        // 初始化边界
//        oxLBound = 0;
//        oxRBound = DISPLAY_WIDTH;
//        oyTBound = 0;
//        oyBBound = 0;


        coord.setCx(DISPLAY_WIDTH / 8).setCy(DISPLAY_HEIGHT - DISPLAY_HEIGHT / 8).setWIDTH(DISPLAY_WIDTH - 2 * DISPLAY_WIDTH / 8).setHEIGHT(DISPLAY_HEIGHT - 2 * DISPLAY_HEIGHT / 8).updeate();


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 放大缩小视图
//                if (isDoubleClick()) {
//                    reverseScale();
//                }

                // 获取按下的坐标
                tempX = event.getX();
                tempY = event.getY();

                isOffset = false;
                if (isDoubleClick()) {
                    isOffset = true;
                }


                break;
            case MotionEvent.ACTION_MOVE:
                // 检查边界
//                checkBound();
                // 获得当前按下的坐标增加量加上原先的原点坐标(更新原点坐标)
//                ox += event.getX() - tempX;
//                oy += event.getY() - tempY;
                X_OFFSET -= event.getX() - tempX;
//                oy += event.getY() - tempY;
                // 更新当前按下的坐标
                tempX = event.getX();
                tempY = event.getY();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    /**
     * 检测边界
     */
//    public void checkBound() {
//        if (ox < oxLBound) {
//            ox = oxLBound;
//        } else if (ox > oxRBound) {
//            ox = oxRBound;
//        }
//        if (oy < oyTBound) {
//            oy = oyTBound;
//        } else if (oy > oyBBound) {
//            oy = oyBBound;
//        }
//    }


    /**
     * 回归原点(0,0)
     */
    private void resetOrigin() {
        ox = 0;
        oy = 0;
    }

    /**
     * 检测是否双击屏幕
     *
     * @return
     */
    private boolean isDoubleClick() {
        if (!firstClick) {
            // 点击一次
            firstClick = true;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                        firstClick = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
            return false;
        } else {
            // 快速点击两次
            firstClick = false;
            return true;

        }
    }

    /**
     * 平移画布
     */
    public void translateCanvas() {
        canvas.translate(ox, oy);
    }

    /**
     * 伸缩画布
     */
    public void telescopicCanvas() {
        canvas.scale(scale, scale);
    }

    @Override
    public void ondraw() {
        // 获得该控件尺寸
        if (!getSize) size();

        // 平移画布
//        coordinate.translateCanvas();

        // 伸缩画布
//        coordinate.telescopicCanvas();

        // 绘画布局
        drawing();

        // 检查边界


        // 停止更新surfaceView
//        mapView.stopUpdate();

    }

    public void drawing() {

        // 清空画布
        clearCanvas();

        // 绘画Y轴参考线
        for (int i = 0; i < (coord.getY_MAX() - coord.getY_MIN()) / coord.getY_UNIT() + 1; i++) {
            paint.reset();
            paint.setColor(Color.parseColor("#bcbcbc"));
            paint.setStrokeWidth(5);
            canvas.drawLine(coord.getCx(), coord.getCy() - (i) * coord.getY_SEPARATOR(), coord.getCx() + coord.getWIDTH(), coord.getCy() - (i) * coord.getY_SEPARATOR(), paint);
        }

        for (int i = 0; i < data.size(); i++) {

            // 绘画X轴
            paint.reset();
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            // 带位移动画
            canvas.drawText(i + "", coord.getCx() + i * coord.getX_SEPARATOR() - X_OFFSET, coord.getCy() + coord.getHEIGHT() / 10, paint);

            // 绘画点
            paint.reset();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(8);
            canvas.drawPoint(coord.getCx() + coord.getX_SEPARATOR() * i - X_OFFSET, coord.getCy() - (data.get(i) - coord.getY_MIN()) * coord.getY_SCALE(), paint);
            if (i > 0) {
                canvas.drawLine(coord.getCx() + coord.getX_SEPARATOR() * (i - 1) - X_OFFSET, coord.getCy() - (data.get(i - 1) - coord.getY_MIN()) * coord.getY_SCALE(), coord.getCx() + coord.getX_SEPARATOR() * i - X_OFFSET, coord.getCy() - (data.get(i) - coord.getY_MIN()) * coord.getY_SCALE(), paint);
            }

            // 如果这是后面的数据就停止动画
            if (i == data.size() - 1 && coord.getX_SEPARATOR() * (i - 1) - X_OFFSET <= coord.getCx() + coord.getWIDTH()) {
                isOffset = false;
            }


        }
        // 清除图像X坐标小于原点坐标部分
        paint.reset();
        paint.setColor(Color.WHITE);
//        paint.setColor(Color.parseColor("#b6adff"));
        canvas.drawRect(0, 0, coord.getCx(), DISPLAY_HEIGHT, paint);
        canvas.drawRect(coord.getCx() + coord.getWIDTH(), 0, coord.getCx() + DISPLAY_WIDTH, DISPLAY_HEIGHT, paint);

        // 绘画X值和Y值描述
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        canvas.drawText(coord.getY_DESC(), coord.getWIDTH() / 6, coord.getHEIGHT() / 10, paint);
        canvas.drawText(coord.getX_DESC(), DISPLAY_WIDTH - coord.getWIDTH() / 10, DISPLAY_HEIGHT - coord.getHEIGHT() / 10, paint);


        // 绘画Y轴值
        for (int i = 0; i < (coord.getY_MAX() - coord.getY_MIN()) / coord.getY_UNIT() + 1; i++) {

            paint.reset();
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            canvas.drawText((int) (coord.getY_MIN() + i * coord.getY_UNIT()) + "", coord.getCx() - coord.getWIDTH() / 10, coord.getCy() - (i) * coord.getY_SEPARATOR(), paint);

//            paint.reset();
//            paint.setColor(Color.parseColor("#bcbcbc"));
//            paint.setStrokeWidth(5);
//            canvas.drawLine(coord.getCx(), coord.getCy() - (i) * coord.getY_SEPARATOR(), coord.getCx() + coord.getWIDTH(), coord.getCy() - (i) * coord.getY_SEPARATOR(), paint);
        }
        // 绘画X轴和点

        if (isOffset) {
            X_OFFSET += 5;
        }


    }

    @Override
    protected void clearCanvas() {
        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.parseColor("#b6adff"));
        paint.setColor(Color.WHITE);
        canvas.drawRect(this.getLeft(), 0, this.getRight(), this.getBottom(), paint);
    }

    public void update(int[] data) {
        isOffset= true;
        for (float d : data) {
            this.data.add(d);
        }

    }

    public void clear() {
        data.clear();

    }

    public interface Object {
        public void drawMyself(Canvas canvas, Paint paint);
    }

    public interface DataCollection {
        public void update(float[] data);
        public void clear();
    }
}
