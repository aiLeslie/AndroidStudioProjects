package com.example.administrator.flagapitest.util.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Set;

/**
 * 蓝牙适配器
 */
public class BluetoothController {
    public BluetoothAdapter adapter;

    public BluetoothController() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 是否支持蓝牙
     * false 表示不支持
     * true 表示支持
     *
     * @return
     */
    public boolean isSupportBluetooth() {

        if (adapter == null) return false;
        else return true;
    }

    /**
     * 判断当前蓝牙状态
     * false 表示蓝牙关闭
     * true 表示蓝牙打开
     *
     * @return
     */
    public boolean getBluetoothAdapterStatus() {
        return adapter.isEnabled();
    }


    /**
     * 打开蓝牙
     *
     * @param activity
     * @param requestCode
     */
    private void turnOnBluetooth(Activity activity, int requestCode) {
        //adapter.enable();不推荐使用该方法
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);

    }

    /**
     * 关闭蓝牙
     */
    public void turnOffBluetooth() {
        adapter.disable();
    }


    /**
     * 判断当前蓝牙状态
     * 如果没有打开就到开蓝牙
     */
    public void checkBluetoothStatus(Activity activity, int requestCode) {
        if (!getBluetoothAdapterStatus()) {
            Toast.makeText(activity, "蓝牙没有开启哦", Toast.LENGTH_SHORT).show();
            turnOnBluetooth(activity, requestCode);
        }
    }

    /**
     * 使蓝牙设备可见
     *
     * @param context
     * @param second
     */
    public void enableVisibility(Context context, int second) {
        if (adapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, second);
            context.startActivity(discoverableIntent);
        }
    }

    /**
     * 查找设备
     * 该方法只是发现蓝牙设备
     * 要配合广播来添加设备
     */
    public void findDevice(final Listener<Void> listener) {
        adapter.startDiscovery();

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(6 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adapter.cancelDiscovery();
                if (listener != null){
                    listener.response(null);
                }

            }
        }.start();
    }

    /**
     * 获取绑定设备
     *
     * @return
     */
    public Set<BluetoothDevice> getBondDevicesSet() {
        return adapter.getBondedDevices();
    }

    public interface Listener<T>{
        void response(T t);
    }


}
