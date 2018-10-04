package com.leslie.javabase.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.lang.ref.WeakReference;

public abstract class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    protected WeakReference<AppCompatActivity> activityWR;
    protected SurfaceHolder surfaceHolder;
    protected Paint paint = new Paint();
    protected Canvas canvas;
    protected int interval = 100;
    protected Drawable drawable;
    protected boolean isUpdate = true;


    private void initView(Context context) {
        if (context instanceof AppCompatActivity) {
            activityWR = new WeakReference<>((AppCompatActivity) context);
        }
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        this.setOnTouchListener(this);
    }

    public MySurfaceView(Context context) {
        super(context);
        initView(context);

    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Thread() {
            @Override
            public void run() {
                super.run();


                try {
                    while (activityWR.get() != null && !activityWR.get().isFinishing()) {


                        if (drawable != null && isUpdate) {

                            drawing();
                            // sufaceView已经被销毁
                            if (canvas == null) return;
                        }

                        sleep(interval);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }.start();

    }

    /**
     * 停止更新
     */
    public void stopUpdate() {
        isUpdate = false;
    }

    /**
     * 开始更新
     */
    public void startUpdate() {
        isUpdate = true;
    }

    /**
     * 绘画方法
     */
    private final void drawing() {

        canvas = surfaceHolder.lockCanvas();
        if (canvas == null) return;
        clearCanvas();
        drawable.ondraw();
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * 画布涂满白色
     */
    protected void clearCanvas() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getRight(), getBottom(), paint);
    }

    /**
     * 画布涂满传入的颜色
     */
    protected void paintedcanvas(int color) {
        paint.setColor(color);
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), paint);
    }

    /**
     * 绘画逻辑接口
     */
    public interface Drawable {
        public abstract void ondraw();
    }


}
