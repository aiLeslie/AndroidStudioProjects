package com.example.administrator.bluetoothtest.activities.main.presenter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.bluetoothtest.activities.main.model.BluetoothModel;
import com.example.administrator.bluetoothtest.mvp.presenter.Presenter;
import com.example.administrator.bluetoothtest.mvp.view.MyView;
import com.example.administrator.bluetoothtest.util.bluetooth.BluetoothController;
import com.example.administrator.bluetoothtest.util.bluetooth.communication.AcceptThread;
import com.example.administrator.bluetoothtest.util.bluetooth.communication.ConnectThread;
import com.example.administrator.bluetoothtest.util.bluetooth.communication.SocketListener;
import com.example.administrator.bluetoothtest.util.bluetooth.communication.message.OnMsgListener;

import java.util.List;
import java.util.UUID;

import static com.example.administrator.bluetoothtest.ApplicationUtil.MODE;
import static com.example.administrator.bluetoothtest.ApplicationUtil.acceptThread;
import static com.example.administrator.bluetoothtest.ApplicationUtil.bluetoothServerSocket;
import static com.example.administrator.bluetoothtest.ApplicationUtil.client;
import static com.example.administrator.bluetoothtest.ApplicationUtil.connectThread;

public class BluetoothPresenter<T extends MyView> extends Presenter<T> {
    private static final String TAG = "BluetoothPresenter";

    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothController controller = new BluetoothController(); // 蓝牙操控者

    private BluetoothModel mModel; // 模型层引用

    public <M extends BluetoothModel>BluetoothPresenter(M model) {
        super(model);
        if (model != null) {
            mModel = (M) modelLayer;
        }

    } // 实例化模型层

    public BluetoothController getController() {
        return controller;
    } // 获得蓝牙操控者对象


    /******************************蓝牙初始化******************************/
    public void initBluetooth() {

        controller.checkBluetoothStatus((AppCompatActivity) mViewWf.get(), 0);

        /********************************************************
         * 打印蓝牙信息
         *********************************************************/
        //获取本机蓝牙名称
        String name = controller.adapter.getName();
        //获取本机蓝牙地址
        String address = controller.adapter.getAddress();
        Log.d(TAG, "bluetooth name =" + name + " address =" + address);

        //获取已配对蓝牙设备
        mModel.updateBondedDevices(controller.getBondDevicesSet());

        Log.d(TAG, "bonded device size =" + mModel.getBondedDevices().size());
        for (BluetoothDevice bonddevice : mModel.getBondedDevices()) {
            Log.d(TAG, "bonded device name =" + bonddevice.getName() + " address" + bonddevice.getAddress());
        }
    }
    /******************************模型层接口******************************/
    /**
     * 更新模型层数据
     * 更新已绑定蓝牙设备列表
     */
    private void updateBondedDevices() {
        mModel.updateBondedDevices(controller.adapter.getBondedDevices());
    }

    public List<BluetoothDevice> getBondedDevices() {
        updateBondedDevices();
        return mModel.getBondedDevices();
    }

    /**
     * 更新模型层数据
     * 添加扫描设备
     *
     * @param device
     */
    public void addScanResult(BluetoothDevice device) {
        mModel.addScanResult(device);
    }

    /******************************Socket接口******************************/
    public boolean openServer(SocketListener<String> listener, OnMsgListener<String> msgListener) {
        // 如果服务器没有关闭
        if (bluetoothServerSocket != null) {
            return false;

        } else if (client != null) { // 当前连接着服务器
            // 断开服务器连接
            connectThread.close();

        }

        acceptThread = new AcceptThread(controller.adapter, listener).setMsgListener(msgListener);
        acceptThread.start();
        return true;
    }

    public boolean closeServer() {
        // 如果服务器没有关闭
        if (bluetoothServerSocket == null) {
            return false;
        } else {
            acceptThread.close();
        }

        return true;
    }

    public boolean connectServer(String address, SocketListener<String> listener, OnMsgListener<String> msgListener) {
        // 如果服务器没有关闭
        if (client != null) {
            connectThread.close();

        } else if (bluetoothServerSocket != null) {// 当前开启服务器
            acceptThread.close();
        }

        connectThread = new ConnectThread(getController().adapter, address, listener).setMsgListener(msgListener);
        connectThread.start();

        return true;


    }

    public boolean disconnect() {
        // 如果服务器没有关闭
        if (client == null) {
            return false;
        } else {
            connectThread.close();
        }

        return true;
    }

    /******************************Socket通讯******************************/

    public void write(String data) {
        if (MODE.isNull()) {
            return;
        } else if (MODE.isServer()) {
            acceptThread.getMsg().sendData(data);
        } else if (MODE.isClient()) {
            connectThread.getMsg().sendData(data);
        }
    }


}
