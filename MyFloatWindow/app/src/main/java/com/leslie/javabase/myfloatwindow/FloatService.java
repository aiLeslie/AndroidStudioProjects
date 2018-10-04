package com.leslie.javabase.myfloatwindow;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class FloatService extends Service {
    private View view;
    private boolean isStarted = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    public FloatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        // 初始化悬浮窗口
        initFloatWindow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 显示悬浮窗口
            showFloatingWindow();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private void initFloatWindow(){
        isStarted = true;
        // 得到系统服务 - 窗口管理者
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 实例化布局参数对象
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 500;
        layoutParams.height = 200;
        layoutParams.x = 300;
        layoutParams.y = 300;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    /**
     * 显示悬浮窗口
     */
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            view = new Button(getApplicationContext());
            Button button = (Button)view;
            button.setText("Floating Window");
            button.setBackgroundColor(Color.BLUE);
            windowManager.addView(button, layoutParams);

            button.setOnTouchListener(new FloatingOnTouchListener());


        }
    }

    /**
     * 悬浮窗口监听器
     */
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 获取移动增量
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    // 布局参数加上移动增量
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    // 更新布局参数
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
//                default:
//                    Toast.makeText(FloatService.this, "(" + event.getRawX() + "," + event.getRawY() + ")", Toast.LENGTH_SHORT).show();
//                    break;
            }
            return false;
        }
    }
}
