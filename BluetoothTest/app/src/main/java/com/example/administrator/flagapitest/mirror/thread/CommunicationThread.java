package com.example.administrator.flagapitest.mirror.thread;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Handler;

public class CommunicationThread {
    public static BluetoothSocket client;
    public static InputStream in;
    public static OutputStream out;
    public static Handler handler;

    public CommunicationThread(BluetoothSocket client) {
        this.client = client;
        try {
            in = client.getInputStream();
            out = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommunicationThread(BluetoothSocket client, Handler handler) {

        this.client = client;
        this.handler = handler;
        try {
            in = client.getInputStream();
            out = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
