package com.example.administrator.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initView();
    }

    private void initView() {
        Button button = (Button) findViewById(R.id.buttonStart);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonClose);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);//借助intent的实例构建出pendingIntent实例
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentTitle("This is a content title").setContentText("This is a content text").setWhen(System.currentTimeMillis()).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round)).setSmallIcon(R.mipmap.ic_launcher_round).setContentIntent(pendingIntent);
                builder.setAutoCancel(true);//设置当点击后自动取消通知
                /**
                 * 设置震动频率
                 */
                long[] timeSeparator = new long[20];
                for (int i = 0; i < timeSeparator.length; i++) {
                    if (i % 2 != 0) {//奇数下标 震动时长时长
                        if (i == 1) {
                            timeSeparator[i] = 1000;
                            continue;
                        }
                        timeSeparator[i] = timeSeparator[i - 2] - 100;

                    } else {
                        if (i == 0)timeSeparator[i] = 0;
                        timeSeparator[i] = 200;//复数下标 静止时长
                    }

                }


//                builder.setVibrate(new long[]{0,1000,1000,1000});
//                builder.setLights(Color.GREEN, 1000, 1000);

//                /**
//                 * 设置通知默认状态
//                 */
//                builder.setDefaults(NotificationCompat.DEFAULT_ALL);
                builder.setVibrate(timeSeparator);
                Notification notification = builder.build();
                notificationManager.notify(0, notification);
                break;
            case R.id.buttonClose:
                notificationManager.cancel(0);
                break;
        }
    }
}
