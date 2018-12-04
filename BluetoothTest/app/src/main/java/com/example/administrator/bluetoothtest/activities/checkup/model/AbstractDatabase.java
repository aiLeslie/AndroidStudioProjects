package com.example.administrator.bluetoothtest.activities.checkup.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatabase<D>{
    public static byte ADD_DATA = 1;
    public static byte REMOVE_DATA = -1;

    public AbstractDatabase() {
        initBase();
    }

    protected List<D> dataList = new ArrayList<>();
    // 为数据库加载数据
    public abstract void initBase();
    // 更新数据库
    public  AbstractDatabase updataList(D d, byte mode) {
        if (ADD_DATA == mode) {
            dataList.add(d);
        } else if (REMOVE_DATA == mode) {
            dataList.remove(d);
        }
        return this;
    }
    // 提取数据库数据
    public abstract void fetchData(OnFetchListener listener);

    public interface OnFetchListener{
        void onFetch(List data);
    }



}
