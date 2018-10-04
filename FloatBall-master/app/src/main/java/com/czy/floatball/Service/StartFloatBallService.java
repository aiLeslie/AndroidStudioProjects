package com.czy.floatball.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.czy.floatball.Manager.ViewManager;

public class StartFloatBallService extends Service {

    public StartFloatBallService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        ViewManager manager = ViewManager.getInstance(this);
        manager.showFloatBall();
        super.onCreate();
    }
}
