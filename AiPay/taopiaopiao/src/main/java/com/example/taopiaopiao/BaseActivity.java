package com.example.taopiaopiao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.ailipaystandard.AiLiPayInterface;

/**
 * Created by Administrator on 2017/12/30.
 */

public class BaseActivity extends Activity implements AiLiPayInterface {
    protected Activity that;
    public BaseActivity() {
        super();
    }

    @Override
    public void setContentView(int layoutId) {
        if(that == null){
            super.setContentView(layoutId);
            return;
        }
        that.setContentView(layoutId);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if(that == null){
            return super.findViewById(id);
        }
        return that.findViewById(id);
    }

    @Override
    public ClassLoader getClassLoader() {

        if(that == null){
            return super.getClassLoader();
        }
        return that.getClassLoader();
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {

        if(that == null){
            return super.getLayoutInflater();
        }
        return that.getLayoutInflater();
    }

    @Override
    public Window getWindow() {

        if(that == null){
            return super.getWindow();
        }
        return that.getWindow();
    }

    @Override
    public WindowManager getWindowManager() {
        if(that == null){
            return super.getWindowManager();
        }
        return that.getWindowManager();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {

    }
    public void attach(Activity activity){

    }
}
