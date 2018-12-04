package com.example.administrator.bluetoothtest.view;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class FloatWater {
    private Paint paint = new Paint();
    private final int backgroundColor = Color.parseColor("#c1d0ff");
    private final int foregroundColor = Color.parseColor("#88a4ff");
    private float cx, cy; // 坐标
    private int RIGHT_BOUND = 1000; // 右边边界
    private int LEFT_BOUND = 0; // 左边边界
    private float position; // 控制点
    private boolean isRight = true;
    private int increment = 50; // 每次绘制x坐标偏移量
    private int swimHigh = 400; // 动态贝塞尔曲线高度
    private Rect seafoot;
    private int depth = 200;
    private Random random = new Random();

    public FloatWater(float cx, float cy, int rightBound) {
        // 开始点坐标赋值
        this.cx = cx;
        this.cy = cy;
        // 控制点位置
        position = cx;

        // 初始化边界
        LEFT_BOUND = (int) cx;
        if (cx < rightBound) {
            RIGHT_BOUND = rightBound;
        } else {
            RIGHT_BOUND = (int) cx + 500;
        }

        seafoot = new Rect((int) cx, (int) cy, RIGHT_BOUND, (int) cy + depth);

    }

    public void floatAnimation(Canvas canvas) {
        // 边界处理
        if (position < LEFT_BOUND) {
            isRight = true;
            position = LEFT_BOUND + increment;
        } else if (position > RIGHT_BOUND) {
            isRight = false;
            position = RIGHT_BOUND - increment;
        }
        paint.setAntiAlias(true); // 消除锯齿
        // 绘画背景水纹
        paint.setColor(backgroundColor);
        canvas.drawPath(Bezier.getBezierPath(LEFT_BOUND, cy, position, cy - swimHigh, RIGHT_BOUND, cy), paint);
        // 绘画前景水纹
        paint.setColor(foregroundColor);
        canvas.drawPath(Bezier.getBezierPath(LEFT_BOUND, cy, RIGHT_BOUND - position, cy - swimHigh, RIGHT_BOUND, cy), paint);
        // 绘画海底
        paint.setColor(foregroundColor);
        canvas.drawRect(seafoot, paint);

        // 偏移控制点
        if (isRight) {
            position += increment;
        } else {
            position -= increment;
        }

    }

}
