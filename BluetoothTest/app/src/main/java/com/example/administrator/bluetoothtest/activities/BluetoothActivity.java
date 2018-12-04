package com.example.administrator.bluetoothtest.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.administrator.bluetoothtest.ApplicationUtil;
import com.example.administrator.bluetoothtest.mvp.presenter.Presenter;
import com.example.administrator.bluetoothtest.mvp.view.BaseActivity;
import com.example.administrator.bluetoothtest.mysocket.Task.TaskHandler;
import com.example.administrator.bluetoothtest.util.bluetooth.communication.message.counter.Message;

public abstract class BluetoothActivity<P extends Presenter> extends BaseActivity<P> {
    protected abstract TaskHandler initMode(Message msg);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ApplicationUtil.MODE.isNull()) return;

        TaskHandler taskHandler;

        if (ApplicationUtil.MODE.isClient()) {

            taskHandler = initMode(ApplicationUtil.connectThread.getMsg());

            if (taskHandler != null) {
                ApplicationUtil.connectThread.getMsg().getCounter().setHandler(taskHandler);
            }


        }else if (ApplicationUtil.MODE.isServer()){

            taskHandler = initMode(ApplicationUtil.acceptThread.getMsg());

            if (taskHandler != null) {
                ApplicationUtil.acceptThread.getMsg().getCounter().setHandler(taskHandler);
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
