package com.leslie.contextpro;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * getApplication() 和 getApplication() 获得是同一个Application对象
         * getBaseContext() 获得ContextImpl对象
         */

        Context myApp = getApplication();
        Log.d(TAG, "getApplication is " + myApp);

        myApp = getApplicationContext();
        Log.d(TAG, "getApplicationContext is " + myApp);

        myApp = getBaseContext();
        Log.d(TAG, "getBaseContext is " + myApp);

        Log.d(TAG, "onCreate: Looper.getMainLooper() = " + Looper.getMainLooper());
        ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                Log.d(TAG, "onCreate: Looper.myLooper() = " + Looper.myLooper());

                Looper.loop();
            }
        });

//        Handler mhandler = new Handler();
//        Handler mainHandler = new Handler();

    }
}
