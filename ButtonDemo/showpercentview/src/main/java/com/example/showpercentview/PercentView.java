package com.example.showpercentview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/21.
 */

public class PercentView extends View {
    private ArrayList<PercentRectangle> rectList = new ArrayList<>();
    private int rightMargin = 0;
    private Paint paint = new Paint();
    private Rect rect = new Rect();

    public PercentView(Context context) {
        super(context);
    }

    public PercentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);//背景为灰色
//        paint.setColor(Color.RED);
//        rect.top = getTop();
//        rect.bottom = getBottom();
//        rect.left = getLeft();
//        rect.right = getRight();
//        canvas.drawRect(rect,paint);


        //setPercent(Color.RED, 40);
        new PercentRectangle(10, Color.BLUE);
        new PercentRectangle(20, Color.RED);
        new PercentRectangle(30, Color.YELLOW);
        new PercentRectangle(40, Color.GREEN);
        setPercent(Color.RED, 100);


        for (PercentRectangle rectangle : rectList) {
            drawRectangleOnCanvas(rectangle.left, rectangle.right, rectangle.color, canvas);
        }


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }


    private void drawRectangleOnCanvas(int left, int right, int color, Canvas convas) {
        rect.top = getTop();
        rect.bottom = getBottom();
        rect.left = left;
        rect.right = right;
        paint.setColor(color);
        convas.drawRect(rect, paint);


    }

    private class PercentRectangle {

        public int percent;
        public int color;
        public int left;
        public int right;

        public PercentRectangle(int percent, int color) {
            if (percent < 0 || percent > 100) return;
            for (PercentRectangle rectangle : rectList) {//使其rectList不能添加颜色相同的percentRectangle对象
                if (rectangle.color == color) return;
            }
            if (rightMargin >= getRight()) return;
            this.left = rightMargin;
            this.right = this.left + percent * getRight() / 100;
            rightMargin = right;
            this.color = color;
            this.percent = percent;
            rectList.add(this);
        }


    }

    public void setPercent(final int color, final int percent) {
        int index = -1;
        int change = 0;
        PercentRectangle percentRectangle = null;
        for (int i = 0; i < rectList.size(); i++) {
            percentRectangle = rectList.get(i);
            if (percentRectangle.color == color) {
                index = i;
                change = (percent - percentRectangle.percent) / 100 * getRight();
                percentRectangle.percent = percent;
                percentRectangle.right += change;
            }
            if (index != -1 && i > index) {
                rectList.get(i).left += change;
                rectList.get(i).right += change;
            }
        }

    }

}
