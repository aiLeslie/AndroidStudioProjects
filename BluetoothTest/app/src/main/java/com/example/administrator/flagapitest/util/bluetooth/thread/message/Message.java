package com.example.administrator.flagapitest.util.bluetooth.thread.message;

import android.bluetooth.BluetoothSocket;

import com.example.administrator.flagapitest.mysocket.Task.MsgCounter;
import com.example.administrator.flagapitest.mysocket.util.PrintStyle;
import com.example.administrator.flagapitest.mysocket.util.SysConvert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.example.administrator.flagapitest.ApplicationUtil.RFormat;
import static com.example.administrator.flagapitest.ApplicationUtil.TFormat;
import static com.example.administrator.flagapitest.ApplicationUtil.encodeMode;

public class Message {

    private InputStream in;
    private OutputStream out;
    private ReceiveThread receiveThread;
    private MsgCounter counter;
    private List<BluetoothSocket> sockets;
    private OnMsgListener<String> listener;

    public Message(List<BluetoothSocket> sockets, OnMsgListener<String> listener) {
        this.sockets = sockets;
        counter = new MsgCounter();
        receiveThread = new ReceiveThread();
        receiveThread.start();
        this.listener = listener;
    }

    public void setListener(OnMsgListener<String> listener) {
        this.listener = listener;
    }

    public MsgCounter getCounter() {
        return counter;
    }

    /**
     * 向客户发送数据
     *
     * @param content
     */
    public void sendData(byte[] content) {

        try {
            for (int i = 0; i < sockets.size(); i++) {
                out = sockets.get(i).getOutputStream();
                out.write(content, 0, content.length);

            }

            System.out.println("发送数据 >>>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(int[] content) {

        try {
            for (int i = 0; i < sockets.size(); i++) {
                out = sockets.get(i).getOutputStream();
                for (int j = 0; j < content.length; j++)
                    out.write(content[j]);
                System.out.println("发送数据 >>>");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String content) {

        if (TFormat.isHexFormat()) {
            sendData(SysConvert.parseHex(content));
            return;
        }
        try {
            for (int i = 0; i < sockets.size(); i++) {
                out = sockets.get(i).getOutputStream();
                out.write(content.getBytes(encodeMode));

            }

            System.out.println("发送数据 >>>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收客户发送数据
     */
    private void receiveData(BluetoothSocket socket) {

        if (socket == null) {
            return;
        } else if (!socket.isConnected()) {
            System.out.println(socket.toString() + " socket close!");
            sockets.remove(socket);
            return;
        }

        try {
            // 获得输入流
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            // 输入流中没有数据
            if (in.available() == 0) {
                return;
            }

            PrintStyle.printLine();
            System.out.println("接收数据 <<<");
            System.out.println(socket.toString());

            if (RFormat.isHexFormat()) {
                int value = -1;
                while (in.available() > 0) {
                    value = in.read();
                    if (listener != null)listener.OnMsg(Integer.toHexString(value) + " ");
                    counter.add(value);


                    System.out.print(Integer.toHexString(value));
                    System.out.print(" ");

                    if (in.available() == 0)
                        System.out.println();
                }
            } else if (RFormat.isStringFormat()) {
                byte[] bytes = new byte[in.available()];
                in.read(bytes, 0, bytes.length);
                String string =  new String(bytes, encodeMode);
                System.out.println(string);
                if (listener != null)listener.OnMsg(string + " ");
            }

            PrintStyle.printLine();
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ReceiveThread extends Thread {
        private boolean aswitch = true;

        public boolean getAswitch() {
            return aswitch;
        }

        public void setAswitch(boolean aswitch) {
            this.aswitch = aswitch;
        }

        @Override
        public void run() {
            super.run();
            aswitch = true;
            while (aswitch) {
                System.out.flush();
                try {
                    for (int i = 0; i < sockets.size(); i++) {
                        if (!aswitch)
                            break;
                        receiveData(sockets.get(i));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void close() {
        receiveThread.aswitch = false;
        closeStream();

    }

    /**
     * 关闭流
     */
    private void closeStream() {
        try {
            for (BluetoothSocket socket : sockets) {
                if (socket.isConnected()) continue;
                socket.getOutputStream().close();
                socket.getInputStream().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
