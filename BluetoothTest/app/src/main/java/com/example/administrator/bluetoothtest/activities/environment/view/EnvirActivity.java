package com.example.administrator.bluetoothtest.activities.environment.view;

import android.os.Bundle;
import android.os.Vibrator;

import com.example.administrator.bluetoothtest.R;
import com.example.administrator.bluetoothtest.activities.BluetoothActivity;
import com.example.administrator.bluetoothtest.activities.environment.presenter.EnvirPresenter;
import com.example.administrator.bluetoothtest.handle.EnvirHandle;
import com.example.administrator.bluetoothtest.mysocket.Task.TaskHandler;
import com.example.administrator.bluetoothtest.util.bluetooth.communication.message.counter.Message;

import java.util.List;

import butterknife.ButterKnife;

public class EnvirActivity extends BluetoothActivity<EnvirPresenter> implements EnvirView {

    @Override
    protected EnvirPresenter createPresenter() {
        return new EnvirPresenter(null);
    }

    @Override
    protected TaskHandler initMode(Message msg) {
        return new EnvirHandle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envir);
        ButterKnife.bind(this);


        presenter.handle();


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showInfos(List infos) {

    }

    @Override
    public void warning() {
        vibrate(true, new long[]{100, 100});
    }

    @Override
    public void stopWarning() {
        cancelVibrate();
    }
    /**
     * 开启手机震动
     * @param repeat 是否重复
     * @param milliseconds 静止和震动时长
     */
    private void vibrate(boolean repeat, long... milliseconds) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            if (!repeat) {
                vibrator.vibrate(milliseconds, -1);
            } else {
                vibrator.vibrate(milliseconds, 0);
            }
        }
    }

    /**
     * 取消手机震动
     */
    private void cancelVibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelVibrate();
    }
}
