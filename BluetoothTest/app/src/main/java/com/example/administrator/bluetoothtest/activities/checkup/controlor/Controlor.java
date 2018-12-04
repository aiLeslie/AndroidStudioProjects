package com.example.administrator.bluetoothtest.activities.checkup.controlor;

import android.support.v7.app.AppCompatActivity;

import com.example.administrator.bluetoothtest.activities.checkup.model.AbstractDatabase;
import com.example.administrator.bluetoothtest.activities.checkup.model.Database;

import java.lang.ref.WeakReference;


public class Controlor {
    private WeakReference<AppCompatActivity> activityWR;
    private Database database = Database.getInstance();

    /**
     * 构造方法
     * @param activity
     */
    public Controlor(AppCompatActivity activity) {
        this.activityWR = new WeakReference<AppCompatActivity>(activity);

    }

    /**
     * 加载数据
     * @param listener
     */
    public void loadData(AbstractDatabase.OnFetchListener listener) {
        database.fetchData(listener);
    }



}
