package com.example.administrator.bluetoothtest.util.bluetooth.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import com.example.administrator.bluetoothtest.mysocket.socket.SocketMode;
import com.example.administrator.bluetoothtest.util.bluetooth.communication.message.counter.Message;
import com.example.administrator.bluetoothtest.util.bluetooth.communication.message.OnMsgListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static com.example.administrator.bluetoothtest.ApplicationUtil.MODE;
import static com.example.administrator.bluetoothtest.ApplicationUtil.client;

public class ConnectThread extends Thread {
    public BluetoothAdapter bluetoothAdapter;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public String address;
    private SocketListener<String> listener;
    private Message msg;
    private OnMsgListener<String> msgListener;

    public ConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.address = address;
    }

    public ConnectThread(BluetoothAdapter bluetoothAdapter, String address, SocketListener<String> listener) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.address = address;
        this.listener = listener;
    }

    public Message getMsg() {
        return msg;
    }

    public ConnectThread setMsgListener(OnMsgListener<String> msgListener) {
        this.msgListener = msgListener;
        return this;
    }

    @Override

    public void run() {
        try {
            super.run();
            if (client != null) {
                throw new RuntimeException("client is connected");
            }
            client = bluetoothAdapter.getRemoteDevice(address).createRfcommSocketToServiceRecord(MY_UUID);
            client.connect();
            // 初始化流操作
            if (msg == null) {
                msg = new Message(Arrays.asList(new BluetoothSocket[]{client}), msgListener);
            }
           MODE.setmode(SocketMode.CLIENT_MODE);
            listener.onSucceed(client.getRemoteDevice().getName());


        } catch (IOException e) {
            e.printStackTrace();
            listener.onFail(bluetoothAdapter.getRemoteDevice(address).getName() + "的服务器没有开启哦!");
        }
    }

    public void close() {

        try {
            if (msg != null) msg.close();
            if (client != null)client.close();
            client = null;
            MODE.setmode(SocketMode.NULL_MODE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
