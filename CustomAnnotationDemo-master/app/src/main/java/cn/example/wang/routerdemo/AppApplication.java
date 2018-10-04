package cn.example.wang.routerdemo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by WANG on 17/11/21.
 */

public class AppApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        setupRouter();
    }

    private void setupRouter() {
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
    }
}
