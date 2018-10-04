package com.example.intentservicetest;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created by Administrator on 2018/2/10.
 */

public class MyIntentService extends IntentService {
    private static final String TAG = "MyIntentService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }
    public MyIntentService() {
        super(String.valueOf(new Random().nextInt()));
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        for (int i = 0; i < 80; i++)
        Log.i("线程名称", Thread.currentThread().getName());
        System.out.println("线程名称: " + Thread.currentThread().getName());

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
