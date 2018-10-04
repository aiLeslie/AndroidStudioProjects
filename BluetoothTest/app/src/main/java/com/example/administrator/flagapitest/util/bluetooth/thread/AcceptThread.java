package com.example.administrator.flagapitest.util.bluetooth.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import com.example.administrator.flagapitest.mysocket.socket.SocketMode;
import com.example.administrator.flagapitest.util.bluetooth.thread.message.Message;
import com.example.administrator.flagapitest.util.bluetooth.thread.message.OnMsgListener;

import java.io.IOException;
import java.util.UUID;

import static com.example.administrator.flagapitest.ApplicationUtil.MODE;
import static com.example.administrator.flagapitest.ApplicationUtil.accept;
import static com.example.administrator.flagapitest.ApplicationUtil.accepts;
import static com.example.administrator.flagapitest.ApplicationUtil.bluetoothServerSocket;

public class AcceptThread extends Thread {
    public boolean isListen = true;
    public BluetoothAdapter bluetoothAdapter;
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private SocketListener<String> listener;
    private OnMsgListener<String> msgListener;
    private Message msg;

    public AcceptThread(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public AcceptThread(BluetoothAdapter bluetoothAdapter, SocketListener<String> listener) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.listener = listener;
    }

    public AcceptThread setMsgListener(OnMsgListener<String> msgListener) {
        this.msgListener = msgListener;
        return this;
    }

    public Message getMsg() {
        return msg;
    }

    @Override
    public void run() {
        if (bluetoothServerSocket != null) {
            throw new RuntimeException("server is open");
        }
        //服务器端的bltsocket需要传入uuid和一个独立存在的字符串，以便验证，通常使用包名的形式
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(bluetoothAdapter.getName(), MY_UUID);
            MODE.setmode(SocketMode.SERVER_MODE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (isListen) {
                //注意，当accept()返回BluetoothSocket时，socket已经连接了，因此不应该调用connect方法。
                //这里会线程阻塞，直到有蓝牙设备链接进来才会往下走
                if ((accept = bluetoothServerSocket.accept()) != null) {

                    // 初始化流操作
                    if (msg == null) {
                        msg = new Message(accepts, msgListener);
                    }

                    accepts.add(accept);
                    listener.onSucceed(accept.getRemoteDevice().getName());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void close() {

        try {

            isListen = false; // 停止监听

            if (bluetoothServerSocket != null) {
                bluetoothServerSocket.close(); // 关闭服务器
                bluetoothServerSocket = null;
            }

            if (msg != null) msg.close();
            closeSockets();
            MODE.setmode(SocketMode.NULL_MODE);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭socket资源
     */
    private void closeSockets() {
        try {
            for (BluetoothSocket socket : accepts) {
                socket.close();
            }
            accepts.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
