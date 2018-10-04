package com.example.administrator.aipay;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2017/12/30.
 */

public class PluginManage {

    private DexClassLoader dexClassLoader;
    private Resources resources;
    private Context context;
    public String entryAcivityName;
    private static final PluginManage pluginManage = new PluginManage();
    private PluginManage(){

    }
    public Resources getResources() {
        return resources;
    }
    public void loadPath(String path){
        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        dexClassLoader = new DexClassLoader(path,dexOutFile.getAbsolutePath(),null,context.getClassLoader());
        /**
         * 非常重要(软件升级用的到代码)
         */
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(path,  PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        entryAcivityName = packageInfo.activities[0].name;

        /**
         * 实例化Resource
         */
        AssetManager assetManager = null;
        try {
            AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,path);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        resources = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration() );

    }
    public static PluginManage getInstance(){
        return pluginManage;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public String getEntryAcivityName() {
        return entryAcivityName;
    }
    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

}
