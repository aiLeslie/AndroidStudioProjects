package com.example.administrator.bluetoothtest.mvp.model;

import java.util.List;

public interface MyModel {
    void loadData(OnLoadListener listener);
    // 设计一个内部回调接口
    interface OnLoadListener{
        void onComplete(List datas);
    }

}
