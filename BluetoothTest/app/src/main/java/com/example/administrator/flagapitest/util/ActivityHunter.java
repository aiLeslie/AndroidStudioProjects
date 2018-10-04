package com.example.administrator.flagapitest.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.List;

public class ActivityHunter {
    public static void startUpApplication(Context mContext, String pkg, String cls) {

        //这些代码是启动另外的一个应用程序的主Activity，当然也可以启动任意一个Activity
        ComponentName componetName = new ComponentName(
                //这个是另外一个应用程序的包名
                pkg,
                //这个参数是要启动的Activity
                cls);

        try {
            Intent intent = new Intent();
            intent.setComponent(componetName);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext, "没有找到应用程序，或者是做其他的操作！", Toast.LENGTH_LONG).show();

        }

    }

    /**
     * <功能描述> 启动应用程序
     *
     * @return void [返回类型说明]
     */
    public static void startUpApplication(Context mContext, String pkg) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            // 获取指定包名的应用程序的PackageInfo实例
            packageInfo = packageManager.getPackageInfo(pkg, 0);
        } catch (PackageManager.NameNotFoundException e) {
            // 未找到指定包名的应用程序
            e.printStackTrace();
            // 提示没有GPS Test Plus应用
            Toast.makeText(mContext, "没有找到应用程序，或者是做其他的操作！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (packageInfo != null) {
            // 已安装应用
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageInfo.packageName);
            List<ResolveInfo> apps = packageManager.queryIntentActivities(
                    resolveIntent, 0);
            ResolveInfo ri = null;
            try {
                ri = apps.iterator().next();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (ri != null) {
                // 获取应用程序对应的启动Activity类名
                String className = ri.activityInfo.name;
                // 启动应用程序对应的Activity
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName componentName = new ComponentName(pkg, className);
                intent.setComponent(componentName);
                mContext.startActivity(intent);
            }
        }
    }
}
