package com.leslie.javabase.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.leslie.javabase.view.R;

/**
 * 这个是自定义的TextView.
 * 至少需要重载构造方法和onDraw方法
 * 对于自定义的View如果没有自己独特的属性，可以直接在xml文件中使用就可以了
 * 如果含有自己独特的属性，那么就需要在构造函数中获取属性文件attrs.xml中自定义属性的名称
 * 并根据需要设定默认值，放在在xml文件中没有定义。
 * 如果使用自定义属性，那么在应用xml文件中需要加上新的schemas，
 * 比如这里是xmlns:my="http://schemas.android.com/apk/res/demo.view.my"
 * 其中xmlns后的“my”是自定义的属性的前缀，res后的是我们自定义View所在的包
 * @author Administrator
 *
 */
public class LeslieText extends View{
    Paint mPaint; //画笔,包含了画几何图形、文本等的样式和颜色信息
    public LeslieText(Context context) {
        super(context);

    }

    public LeslieText(Context context, AttributeSet attrs){
        super(context, attrs);
        mPaint = new Paint();
        //TypedArray是一个用来存放由context.obtainStyledAttributes获得的属性的数组
        //在使用完成后，一定要调用recycle方法
        //属性的名称是styleable中的名称+“_”+属性名称
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        int textColor = array.getColor(R.styleable.MyView_textColor, 0XFF00FF00); //提供默认值，放置未指定
        float textSize = array.getDimension(R.styleable.MyView_textSize, 36);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);

        array.recycle(); //一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //Canvas中含有很多画图的接口，利用这些接口，我们可以画出我们想要的图形
        //mPaint = new Paint();
        //mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL); //设置填充
        canvas.drawRect(10, 10, 100, 100, mPaint); //绘制矩形

        mPaint.setColor(Color.BLUE);
        canvas.drawText("我是被画出来的", 10, 120, mPaint);
    }


}
