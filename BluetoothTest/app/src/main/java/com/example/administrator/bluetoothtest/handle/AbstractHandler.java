package com.example.administrator.bluetoothtest.handle;

import android.os.Handler;
import android.os.Looper;

import com.example.administrator.bluetoothtest.activities.checkup.view.CoordinateActivity;
import com.example.administrator.bluetoothtest.mysocket.Task.Task;
import com.example.administrator.bluetoothtest.mysocket.Task.TaskHandler;

import java.lang.ref.SoftReference;

public abstract class AbstractHandler  implements TaskHandler {
    protected SoftReference<CoordinateActivity> activitySR;
    Handler handler = new Handler(Looper.getMainLooper());

    public AbstractHandler(CoordinateActivity activity) {
        activitySR = new SoftReference<>(activity);
    }

    public void setActivitySR(CoordinateActivity activity) {
        activitySR.clear();
        activitySR = new SoftReference<>(activity);
    }

    @Override
    public void handleTask(Task task) {
        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
