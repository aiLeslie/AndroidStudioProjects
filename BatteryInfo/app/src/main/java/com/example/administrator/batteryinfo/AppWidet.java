package com.example.administrator.batteryinfo;//package com.example.hello.powerdisplay;
//
//import android.app.AlarmManager;
//import android.app.ApplicationErrorReport;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.appwidget.AppWidgetManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.widget.RemoteViews;
//
///**
// * Created by Hello on 2017/11/24.
// */
//
//public class AppWidet extends AppWidgetProvider {
//    private static int currrentBatteryLevel;//监控电池电量
//    private static int currentBatteryStaus;//监控电池状态
//
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        //启动自动更新电池信息的service
//        context.startService(new Intent(context, updateService.class));
//
//        //为appwidget设置单击事件响应，启动显示电池信息详情的activity
//        Intent startActivityIntent = new Intent(context, BatteryInfoActivity.class);
//        PendingIntent pintent = PendingIntent.getActivity(context, 0, startActivityIntent, 0);
//
//        //设置远程显示view
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.other_layout);
//
//        views.setOnClickPendingIntent(R.id.imageView, pintent);
//        appWidgetManager.updateAppWidget(appWidgetIds, views);
//
//    }
//
//    /**
//     * 自动更新电池信息的service，通过AlarmManger实现实时不间断的发送电池信息
//     */
//    public static class updateService extends Service {
//        Bitmap bmp;//定义机器人图片
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//    }
//
//    /**
//     * 定义一个接受电池信息的broascastReceiver
//     */
//    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //得到电量电池和电池状态
//
//            currentBatteryStaus = intent.getIntExtra("status", 0);
//            currrentBatteryLevel = intent.getIntExtra("level", 0);
//
//        }
//    };
//
//    public void onStart(Intent intent, int startId) {
//
//        super.onStart(intent, startId);
//        //注册接收器
//        registsterReceiver(batteryReceiver, new IntentFilter(intent.ACTION_BATTERY_CHANGED));
//        //定义一个AppWidgetManager
//        AppWidgetManager manger = AppWidgetManager.getInstance(this);
//
//        //定义一个RemoteViews,实现对AppWidget界面控制
//        RemoteViews views = new RemoteViews(getPackageName(), R.layout.other_layout);
//        //定义当正在充电或充满电时，显示充电状态系列图片
//        if (currentBatteryStaus == 2 || currentBatteryStaus == 5) {
//            //根据电量显示图片，代码省略
//
//
//        } else {//未在充电时，显示不在充电状态系列图片
//            //根据电量显示图片，代码省略
//        }
//
//        //设置Appwidget上显示的图片和文字的内容
//        views.setImageViewBitmap(R.id.imageView, bmp);
//        views.setTextViewText(R.id.tv, currrentBatteryLevel + "%");
//        ComponentName thisWidgt = new ComponentName(this,AppWidet.class);
//
//        //使用alarmManager实现每隔一秒发送一次更新提示消息，实现信息动态变化
//        long now = System.currentTimeMillis();
//        long pause = 1000;
//
//        Intent alarmIntent = new Intent();
//        alarmIntent = intent;
//
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarm.set(AlarmManager.RTC_WAKEUP, now + pause, pendingIntent);
//
//        //更新appWidget
//        manager.updateAppWidget(this, views);
//    }
//
//
//}
