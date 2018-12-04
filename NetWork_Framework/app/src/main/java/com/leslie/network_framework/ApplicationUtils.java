package com.leslie.network_framework;

import android.app.Application;
import com.leslie.network_framework.HttpProcessors.OkHttpProcessor;

public class ApplicationUtils extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HttpHelper.init(new OkHttpProcessor());

    }
}
