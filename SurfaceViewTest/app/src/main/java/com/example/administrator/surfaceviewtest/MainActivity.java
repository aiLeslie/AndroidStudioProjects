package com.example.administrator.surfaceviewtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Runnable update;
    private boolean isDestroy = false;

    @Override
    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setContentView(R.layout.activity_main);
        SurfaceView surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);//从内容视图找出surfaceView对象并引用
        SurfaceHolder holder = surfaceView.getHolder();//通过surfaceView实例得到SurfaceHold的实例
        holder.addCallback(new SurfaceHolder.Callback() {//SurfaceHold的实例添加回调
            @Override
            public void surfaceCreated(SurfaceHolder holder) {//surfaceView新建时回调的方法
                update = new DrawRunable(holder);
                new Thread(update).start();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {//surfaceView改变大小

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {//surfaceView销毁时回调该方法

            }
        });

        final Button button = (Button) this.findViewById(R.id.button);
        button.setText("stop");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawRunable) update).reverseState();
                if (((DrawRunable) update).getRunState()){
                    button.setText("stop");
                } else {
                    button.setText("start");
                }
            }
        });


    }

    public class DrawRunable implements Runnable {
        private SurfaceHolder holder;
        private Boolean refresh = true;

        private boolean getRunState(){
            return refresh;
        }


        public DrawRunable(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {

            Random random = new Random();//创建随机器
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);//获得图片资源
            while (!isDestroy)
            {

                while (refresh) {//如果refresh == ture是进入循环
                    Canvas canvas = holder.lockCanvas();//holder实例锁住canvas并得到他的实例
                    Paint paint = new Paint();
                    canvas.drawPaint(paint);//相当于清空画布

                    /**
                     * 显示坐标
                     */
                    int x, y;
                    x = random.nextInt(1000);
                    y = random.nextInt(1500);
                    paint.setColor(Color.RED);
                    paint.setTextSize(50);
                    canvas.drawText("(" + x + "," + y + ")", x, y, paint);

                    paint.setColor(Color.BLUE);
                    Path path = new Path();
                    path.moveTo(100,100);
                    path.lineTo(200,100);
                    path.lineTo(200,200);
                    path.lineTo(100,200);
//                    path.close();
                    canvas.drawPath(path,paint);


                    path.offset(100,100);
                    canvas.drawPath(path,paint);



                    RectF oval = new RectF( x, y, x + 300, y + 300);
                    canvas.drawArc(oval,-90,120,false,paint);


//               canvas.drawLine(0,0,1000,1000,paint);


                    holder.unlockCanvasAndPost(canvas);//holder实例解锁canvas并得提交画布的数据
                    try {
                        Thread.sleep(1000);//线程睡眠1秒继续重新画

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);//线程睡眠1秒继续重新画

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

        public void reverseState() {
            refresh = !refresh;
        }
    }

    public class Timer extends Thread {//为DrawRunable线程进行终止(跳出while循环)
        private Runnable runnable;
        private long millis;

        public Timer(Runnable runnable, long millis) {//传入要中断rawRunable线程,和延时时间进来行延时操作
            this.millis = millis;
            this.runnable = runnable;

        }

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((DrawRunable) runnable).reverseState();
        }
    }


}
