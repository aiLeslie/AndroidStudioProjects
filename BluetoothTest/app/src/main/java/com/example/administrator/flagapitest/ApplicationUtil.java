package com.example.administrator.flagapitest;

import android.app.Application;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.example.administrator.flagapitest.mysocket.socket.DataFormat;
import com.example.administrator.flagapitest.mysocket.socket.SocketMode;
import com.example.administrator.flagapitest.util.bluetooth.thread.AcceptThread;
import com.example.administrator.flagapitest.util.bluetooth.thread.ConnectThread;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ApplicationUtil extends Application {
    public static String encodeMode = "GBK";// 可选编码列表
    public static List<String> encodeList = Arrays.asList(new String[]{"ASCII", "GBK", "ISO-8859-1", "UTF-8", "UTF-16", "GB2312", "Big5"});
    public static DataFormat TFormat = new DataFormat(DataFormat.HEX_FORMAT);// 发送数据格式
    public static DataFormat RFormat = new DataFormat(DataFormat.HEX_FORMAT); // 接收数据格式
    public static SocketMode MODE = new SocketMode(); // 用户选择的模式
    public static BluetoothSocket client; // 客户端socket
    public static BluetoothServerSocket bluetoothServerSocket;// 服务端
    public static List<BluetoothSocket> accepts = new LinkedList<>();// 服务端socket列表
    public static BluetoothSocket accept; // 服务端socket
    public static AcceptThread acceptThread; // 服务器线程
    public static ConnectThread connectThread; // 服务端线程

}
