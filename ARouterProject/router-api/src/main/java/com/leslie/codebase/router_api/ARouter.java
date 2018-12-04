package com.leslie.codebase.router_api;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import dalvik.system.DexFile;

public class ARouter {
    private static final String TAG = "ARouter";
    public static final String AROUTER_SUFFIX = "$$Router";
    private static Application mContext;
    private static HashMap<String, Class<? extends Activity>> hashMap = new HashMap<>();


    public static void init(final Application app) {
        mContext = app;

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    // 获得当前的apk文件
                    ApplicationInfo appInfo = app.getPackageManager().getApplicationInfo(app.getPackageName(), 0);
                    // instant run
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String[] apkPaths = appInfo.splitSourceDirs;
                        for (String apkPath : apkPaths) {
                            DexFile dexFile = new DexFile(apkPath);
                            Enumeration<String> entries = dexFile.entries();
                            String className = null;
                            while (entries.hasMoreElements()) {
                                // 获得全限定名
                                className = entries.nextElement();

                                if (className.endsWith(AROUTER_SUFFIX)) {
                                    Connectable connectable = (Connectable) (Class.forName(className).newInstance());
                                    connectable.load(hashMap);
                                    Log.i(TAG, "put router: " + className);
                                }

                            }
                        }
                    }


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    public static boolean register(String address, Class<? extends Activity> classType) {
        if (hashMap.containsKey(address)) {
            return false;
        }
        hashMap.put(address, classType);
        return true;
    }


    public static boolean start(String address) {
        if (hashMap.containsKey(address)) {
            Class<? extends Activity> aClass = hashMap.get(address);
            Intent intent = new Intent(mContext, aClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return true;

        } else {
            return false;
        }
    }
}
