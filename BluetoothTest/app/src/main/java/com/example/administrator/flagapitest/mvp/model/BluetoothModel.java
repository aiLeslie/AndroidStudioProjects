package com.example.administrator.flagapitest.mvp.model;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BluetoothModel implements MyModel {
    // 编码格式
    public final List<String> encodeList = Arrays.asList(new String[]{"ASCII", "GBK", "ISO-8859-1", "UTF-8", "UTF-16", "GB2312", "Big5"});
    // 绑定设备列表
    private List<BluetoothDevice> bondedDevices = new ArrayList<>();
    //  扫描结果列表
    private List<BluetoothDevice> ScanResults = new ArrayList<>();

    public List<BluetoothDevice> getBondedDevices() {
        return bondedDevices;
    }

    public List<BluetoothDevice> getScanResults() {
        return ScanResults;
    }

    /**
     * 更新已绑定蓝牙设备列表
     * @param set
     */
    public void updateBondedDevices(Set<BluetoothDevice> set) {
        bondedDevices.clear();
        bondedDevices.addAll(set);
    }

    /**
     * 添加扫描设备
     * @param device
     */
    public void addScanResult(BluetoothDevice device) {
        if (!ScanResults.contains(device)) {
            ScanResults.add(device);
        }
    }

    @Override
    public void loadData(OnLoadListener listener) {

    }
}
