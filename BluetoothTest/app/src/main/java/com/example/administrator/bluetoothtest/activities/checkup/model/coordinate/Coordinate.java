package com.example.administrator.bluetoothtest.activities.checkup.model.coordinate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.administrator.bluetoothtest.activities.checkup.view.CoordinateView;

import java.util.ArrayList;
import java.util.List;

public class Coordinate implements CoordinateView.Object {
    public float cx; // 圆心坐标
    public float cy; // 圆心坐标

    // 用户填入
    private float X_MIN = 0, Y_MIN = 0; // 量最小值
    private float X_MAX = 1, Y_MAX = 1; // 量最大值
    private String X_DESC = "x", Y_DESC = "y"; // 坐标量描述
    private float X_UNIT = 50, Y_UNIT = 50; // 单位量

    // 系统注入
    private float  WIDTH, HEIGHT; // 像素长度和宽度

    // 数据更新
    private float X_SEPARATOR = 50, Y_SEPARATOR = 50; // 坐标间隔
    private float X_SCALE, Y_SCALE; // 单位量与像素的转换


    private List<Point> points = new ArrayList<>(); // 点集合


    public Coordinate() {

    }

    public Coordinate(float cx, float cy) {
        this.cx = cx;
        this.cy = cy;
    }

    public Coordinate updeate() {


        X_SCALE =  WIDTH / (X_MAX - X_MIN);
        Y_SCALE = HEIGHT / (Y_MAX - Y_MIN);
        X_SEPARATOR = X_UNIT * X_SCALE;
        Y_SEPARATOR = Y_UNIT * Y_SCALE;
        return this;
    }


    @Override
    public void drawMyself(Canvas canvas, Paint paint) {
        // 重置画笔
        paint.reset();


        // 会好坐标系
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(cx, cy, 1000, cy, paint);
    }

    /**********GETTER**********/
    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getX_SEPARATOR() {
        return X_SEPARATOR;
    }

    public float getY_SEPARATOR() {
        return Y_SEPARATOR;
    }

    public float getX_UNIT() {
        return X_UNIT;
    }

    public float getY_UNIT() {
        return Y_UNIT;
    }

    public String getX_DESC() {
        return X_DESC;
    }

    public String getY_DESC() {
        return Y_DESC;
    }

    public float getX_MIN() {
        return X_MIN;
    }

    public float getY_MIN() {
        return Y_MIN;
    }

    public float getX_MAX() {
        return X_MAX;
    }

    public float getY_MAX() {
        return Y_MAX;
    }

    public List<Point> getPoints() {
        return points;
    }

    public float getWIDTH() {
        return WIDTH;
    }

    public float getHEIGHT() {
        return HEIGHT;
    }

    public float getX_SCALE() {
        return X_SCALE;
    }

    public float getY_SCALE() {
        return Y_SCALE;
    }

    /**********SETTER**********/
    public Coordinate setCx(float cx) {
        this.cx = cx;
        return this;
    }

    public Coordinate setCy(float cy) {
        this.cy = cy;
        return this;
    }


    public Coordinate setX_DESC(String x_DESC) {
        X_DESC = x_DESC;
        return this;
    }

    public Coordinate setY_DESC(String y_DESC) {
        Y_DESC = y_DESC;
        return this;
    }

    public Coordinate setX_MIN(float x_MIN) {
        X_MIN = x_MIN;
        return this;
    }

    public Coordinate setY_MIN(float y_MIN) {
        Y_MIN = y_MIN;
        return this;
    }

    public Coordinate setX_MAX(float x_MAX) {
        X_MAX = x_MAX;
        return this;
    }

    public Coordinate setY_MAX(float y_MAX) {
        Y_MAX = y_MAX;
        return this;
    }

    public Coordinate setX_UNIT(float x_UNIT) {
        X_UNIT = x_UNIT;
        return this;
    }

    public Coordinate setY_UNIT(float y_UNIT) {
        Y_UNIT = y_UNIT;
        return this;
    }

    public Coordinate setWIDTH(float WIDTH) {
        this.WIDTH = WIDTH;
        return this;
    }

    public Coordinate setHEIGHT(float HEIGHT) {
        this.HEIGHT = HEIGHT;
        return this;
    }

    public Coordinate setPoints(List<Point> points) {
        this.points = points;
        return this;
    }
}
