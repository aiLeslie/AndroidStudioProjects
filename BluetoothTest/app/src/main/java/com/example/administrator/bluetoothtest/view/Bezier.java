package com.example.administrator.bluetoothtest.view;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.Random;

public class Bezier {
    public static Path path = new Path();
    private Paint paint = new Paint();
    private float cx, cy; // 坐标
    private int RIGHT_BOUND = 1000; // 右边边界
    private int LEFT_BOUND = 0; // 左边边界
    private int count = 0; // 调用getStreamPath的次数
    private int cycle = 0;
    private int increment = 50; // 每次绘制x坐标偏移量
    private int swimHigh = 100; // 动态贝塞尔曲线高度
    private int swimWidth = 1000; // 动态贝塞尔曲线周期的长度
    private Path streamPath;
    private Random random = new Random();

    public Bezier(float cx, float cy) {
        this.cx = cx;
        this.cy = cy;

    }

    public Path getStreamPath() {
        if (streamPath == null) {
            streamPath = new Path();
            cycle = RIGHT_BOUND / swimWidth;
            for (int i = 0; i < cycle + 2; i++) {
                // 加入路径
                streamPath.addPath(getBezierPath(cx + swimWidth * i, cy, cx + swimWidth * i + swimWidth / 4, cy + swimHigh, cx + swimWidth * i + swimWidth / 4 * 3, cy - swimHigh, cx + swimWidth * i + swimWidth, cy));
            }
            streamPath.offset(-swimWidth, 0);
            return streamPath;

        }

        /**
         * 随机升降贝塞尔曲线
         */
        if (random.nextBoolean()) {
            streamPath.offset(increment, +random.nextInt(50));
        } else {
            streamPath.offset(increment, -random.nextInt(50));
        }
        /**
         * 偏移了一个周期复位
         */
        if (increment * ++count >= swimWidth) {
            streamPath.offset(-swimWidth, 0);
            count = 0;
            return streamPath;
        }



        return streamPath;
    }

    public Path getSinStreamPath(Canvas canvas, Paint paint) {

        // 抗锯齿
        paint.setAntiAlias(true);

        if (streamPath == null) {
            streamPath = new Path();
            Path path = new Path();
            cycle = RIGHT_BOUND / swimWidth;

            for (int i = 0; i < cycle + 2; i++) {
                // 绘画向上的半圆
                path.reset();
                path.setFillType(Path.FillType.EVEN_ODD);
                RectF rectF = new RectF(cx + swimWidth * i, cy - swimHigh, cx + swimWidth * i + swimWidth / 2, cy);
                path.addArc(rectF, 180, 180);
                streamPath.addPath(path);

                // 绘画向下的半圆
                path.reset();
                path.setFillType(Path.FillType.WINDING);
                rectF = new RectF(cx + swimWidth * i + swimWidth / 2, cy - swimHigh, cx + swimWidth * i + swimWidth , cy);
                path.addArc(rectF, 0, 180);
                streamPath.addPath(path);
            }
            streamPath.offset(-swimWidth, 0);
            return streamPath;

        }

        if (random.nextBoolean()) {
            streamPath.offset(increment, +random.nextInt(10));
        } else {
            streamPath.offset(increment, -random.nextInt(10));
        }
        /**
         * 偏移了一个周期复位
         */
        if (increment * ++count >= swimWidth) {
            streamPath.offset(-swimWidth, 0);
            count = 0;
            return streamPath;
        }



        return streamPath;
    }

    /**
     * 获得一帧的动态贝塞尔曲线的路径
     *
     * @return
     */
    public Path getPath() {
        // 如果原点坐标超过右边边界
        if (cx >= RIGHT_BOUND) {
            // 原点坐标被赋值左边边界
            cx = LEFT_BOUND;
        }

        // 创建左边的贝塞尔曲线的路径
        Path left = new Path();


        float x = cx; // x 为绘画完成到坐标
        do {
            // 加入路径
            left.addPath(getBezierPath(x, cy, x - swimWidth, cy - swimHigh, x - 2 * swimWidth, cy + swimHigh, x - 3 * swimWidth, cy));

            x -= 3 * swimWidth;
        } while (x > LEFT_BOUND); // x坐标没画到左边边界继续画
        // 创建右边的贝塞尔曲线的路径
        Path right = new Path();
        x = cx;
        do {
            // 加入路径
            right.addPath(getBezierPath(x, cy, x + swimWidth, cy + swimHigh, x + 2 * swimWidth, cy - swimHigh, x + 3 * swimWidth, cy));
            x += 3 * swimWidth;
        } while (x < RIGHT_BOUND); // x坐标没画到右边边界继续画

        right.addPath(left); // 左边路加入右边路径


        cx += increment; // x坐标偏移

        return right;
    }

    /**
     * 获取一个控制点的贝塞尔曲线路径
     *
     * @param xStart
     * @param ySart
     * @param cpx
     * @param cpy
     * @param xEnd
     * @param yEnd
     * @return
     */
    public static Path getBezierPath(float xStart, float ySart, float cpx, float cpy, float xEnd, float yEnd) {
        path.reset();
        path.moveTo(xStart, ySart); // 设置Path的起点
        path.quadTo(cpx, cpy, xEnd, yEnd); // 设置贝塞尔曲线的控制点坐标和终点坐标
        return path;

    }

    /**
     * 获取两个控制点的贝塞尔曲线路径
     *
     * @param xStart
     * @param ySart
     * @param cpx1
     * @param cpy1
     * @param cpx2
     * @param cpy2
     * @param xEnd
     * @param yEnd
     * @return
     */

    public static Path getBezierPath(float xStart, float ySart, float cpx1, float cpy1, float cpx2, float cpy2, float xEnd, float yEnd) {
        path.reset();
        path.moveTo(xStart, ySart); // 设置Path的起点
        path.cubicTo(cpx1, cpy1, cpx2, cpy2, xEnd, yEnd); // 设置贝塞尔曲线的控制点坐标和终点坐标
        return path;

    }
}
