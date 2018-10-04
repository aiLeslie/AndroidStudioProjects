package com.example.ailipaystandard;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/12/30.
 */

public interface AiLiPayInterface {
    public void onCreate(Bundle savedInstanceState);
    public void onStart();
    public void onResume();
    public void onPause();
    public void onStop();
    public void onDestory();
    public void onSaveInstanceState(Bundle outState);
    public boolean onTouchEvent(MotionEvent event);
    public void onBackPressed();
    /**
     * 需要支付宝注入给淘票票上下文
     */
    public void attach(Activity activity);
}
