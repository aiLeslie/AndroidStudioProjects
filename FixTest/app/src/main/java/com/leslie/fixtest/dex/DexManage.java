package com.leslie.fixtest.dex;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import dalvik.system.DexFile;

public class DexManage {
    private static final String TAG = "DexManage";
    private Context mContext;

    public DexManage(Context mContext) {
        this.mContext = mContext;
    }

    public void loadDex(File dexFilePath) {
        File opFile = new File(mContext.getCacheDir(), dexFilePath.getName());
        if (opFile.exists()) {
            opFile.delete();
        }

        try {
            // 加载dex
            DexFile dexFile = DexFile.loadDex(dexFilePath.getAbsolutePath(), opFile.getAbsolutePath(), Context.MODE_PRIVATE);
            Enumeration<String> entries = dexFile.entries();
            // 遍历dex里面的class
            while (entries.hasMoreElements()) {
                String className = entries.nextElement();
                // 修复好的class
                Class aClass = dexFile.loadClass(className, mContext.getClassLoader());

                Log.i(TAG, "loadDex: className = " + className);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
