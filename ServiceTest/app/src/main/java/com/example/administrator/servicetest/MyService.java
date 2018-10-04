package com.example.administrator.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";
    private MyBinder binder;

    public class MyBinder extends Binder {
        int progress;

        public void startDownload() {
            Log.i(TAG, "startDownload: executed");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        progress = i;
                    }
                    MyService.this.stopSelf();
                }
            }).start();


        }

        public int getProgress() {
            Log.i(TAG, "getProgress: executed");
            return progress;
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, TAG + "onCreate: ");
        binder = new MyBinder();

        /**
         * 使用前台任务
         */
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        Notification notification = new NotificationCompat.Builder(this).setContentTitle("Progress").setContentIntent(pendingIntent).setContentText(binder.getProgress() + "").build();
        startForeground(1,notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, TAG + "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, TAG + "onDestroy: ");
    }


}
