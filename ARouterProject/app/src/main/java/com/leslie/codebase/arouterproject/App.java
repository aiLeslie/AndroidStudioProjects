package com.leslie.codebase.arouterproject;

import android.app.Application;

import com.leslie.codebase.arouterproject.activities.Main2Activity;
import com.leslie.codebase.arouterproject.activities.MainActivity;
import com.leslie.codebase.router_api.ARouter;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);


    }
}
