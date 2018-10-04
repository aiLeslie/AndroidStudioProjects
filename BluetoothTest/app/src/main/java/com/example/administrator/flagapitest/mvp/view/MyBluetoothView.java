package com.example.administrator.flagapitest.mvp.view;

import android.bluetooth.BluetoothDevice;

public interface MyBluetoothView extends MyView {
    BluetoothDevice connectDevice(); // 获得连接的远程设备
    BluetoothDevice scanDevice(); // 获得扫描设备

    // 指令列表中接口方法
//    String getKey();
//    void setKey();
//    String getValue();
//    void setValue();

}
