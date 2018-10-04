package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import paintss.common.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class DeviceUart extends Activity{
    private com.example.jean.component.MainMenuButton _videoUartBack;
    private EditText _videoRecvData;
    private EditText _videoSendData;
    private Button _videoSendBtn;
    private Button _videoClearBtn;
    private TextView _videoRecvNum;
    private TextView _videoSendNum;
    private static DeviceUart _self;
    private String _deviceIp="";
    private int _sendPort=80;

    private SocketRunnable _socketRunnable;
    private SocketReceiveRunnable _socketReceiveRunnable;
    private Socket _socket;
    private DataOutputStream dataStream;

    private DatagramSocket udp_socket;
    private UDPSocketReceiveRunnable _udpSocketReceiveRunnable;

    private long _sendNum=0;
    private long _recvNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_uart);
        _self = this;

        _videoUartBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_uart_back);
        _videoUartBack.setOnClickListener(_videoUartBack_Click);
        _videoRecvData=(EditText)findViewById(R.id.video_uart_recv_data);
        _videoSendData=(EditText)findViewById(R.id.video_uart_send_data);
        _videoSendBtn=(Button)findViewById(R.id.video_uart_send_btn);
        _videoSendBtn.setOnClickListener(_videoSendBtn_Click);
        _videoClearBtn=(Button)findViewById(R.id.video_uart_clear_btn);
        _videoClearBtn.setOnClickListener(_videoClearBtn_Click);
        _videoSendBtn.setEnabled(false);
        _videoRecvNum=(TextView)findViewById(R.id.video_uart_recv_num);
        _videoSendNum=(TextView)findViewById(R.id.video_uart_send_num);
        _sendNum=0;
        _recvNum=0;
        _videoRecvNum.setText(getString(R.string.video_uart_recv_num)+_recvNum);
        _videoSendNum.setText(getString(R.string.video_uart_send_num)+_sendNum);

        Intent intent = getIntent();
        _deviceIp = intent.getStringExtra("deviceip");
        _sendPort=intent.getIntExtra("sendport",80);
        Log.e("_sendPort==>",_sendPort+"");
        if((_deviceIp==null)||(_deviceIp.equals(""))){
            Toast.show(this,getString(R.string.video_uart_ip_error));
            return;
        }
        if(_sendPort==1008){//图传模块
            _videoSendBtn.setEnabled(true);
            try {
                udp_socket=new DatagramSocket(25000);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            if(_udpSocketReceiveRunnable == null){
                _udpSocketReceiveRunnable = new UDPSocketReceiveRunnable();
                new Thread(_udpSocketReceiveRunnable).start();
            }
        }
        else{//520模块
            if(_socketRunnable == null){
                _socketRunnable = new SocketRunnable();
                new Thread(_socketRunnable).start();
            }

            if(_socketReceiveRunnable == null){
                _socketReceiveRunnable = new SocketReceiveRunnable();
                new Thread(_socketReceiveRunnable).start();
            }
        }
    }

    private void Close(){
        if(_socketRunnable != null){
            _socketRunnable.stop();
            _socketRunnable=null;
        }
        if(_socketReceiveRunnable != null){
            _socketReceiveRunnable.stop();
            _socketReceiveRunnable=null;
        }

        if(_udpSocketReceiveRunnable != null){
            _udpSocketReceiveRunnable.stop();
            _udpSocketReceiveRunnable=null;
        }
        if (udp_socket!=null){
            udp_socket.close();
            udp_socket=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Close();
    }

    @Override
    public void onBackPressed() {
        Close();
        finish();
    }
    /**
     *  Back
     */
    View.OnClickListener _videoUartBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Close();
            finish();
        }
    };


    /**
     *  Send Data
     */
    private boolean sendOk=false;
    View.OnClickListener _videoSendBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int sendLen=_videoSendData.length();
            final byte[] data = new byte[sendLen+2];//发送数据必须以0x01 0x55开头
            data[0] = 0x01;
            data[1] = 0x55;
            byte[] sendData=_videoSendData.getText().toString().getBytes();
            System.arraycopy(sendData,0,data,2,sendLen);
            sendOk=false;

            new AsyncTask<Void, Void, Void>()
            {
                protected Void doInBackground(Void... params)
                {
                    if(_sendPort==1008){
                        try {
                            InetAddress serverAddress = InetAddress.getByName(_deviceIp);
                            DatagramPacket sendPackage = new DatagramPacket(data , data.length , serverAddress , _sendPort);
                            udp_socket.send(sendPackage);
                            sendOk=true;
                        } catch (IOException e) {
                        }
                    }
                    else{
                        if (!_socket.isClosed()) {
                            try {
                                dataStream.write(data);
                                sendOk=true;
                            } catch (IOException e) {
                            }
                        }
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void result)
                {
                    if(sendOk){
                        _sendNum+=sendLen;
                        _videoSendNum.setText(getString(R.string.video_uart_send_num)+_sendNum);
                    }
                    else{
                        Toast.show(DeviceUart.this,getString(R.string.video_uart_connect_failed));
                    }
                }
            }.execute();
        }
    };


    /**
     *  Clear Data
     */

    View.OnClickListener _videoClearBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _videoRecvData.setText("");
            _videoSendData.setText("");
            _sendNum=0;
            _recvNum=0;
            _videoRecvNum.setText(getString(R.string.video_uart_recv_num)+_recvNum);
            _videoSendNum.setText(getString(R.string.video_uart_send_num)+_sendNum);
        }
    };

    /**
     * TCP Connect Socket and Keep-Alive
     */
    class SocketRunnable implements Runnable {
        private boolean _stop = false;

        public void stop() {
            _stop = true;
        }

        @Override
        public void run() {
            try {
                _socket = new Socket(_deviceIp, _sendPort);
                _socket.setKeepAlive(true);

                dataStream =
                        new DataOutputStream(_socket.getOutputStream());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _videoSendBtn.setEnabled(true);
                        Toast.show(DeviceUart.this,getString(R.string.video_uart_connect_success));
                    }
                });

                byte[] message = {0x01,0x55,0x00};//发送数据必须以0x01 0x55开头

                for (; ; ) {
                    if (_stop) {
                        _socket.close();
                        dataStream.close();

                        // dispose socket receive message thread
                        if (_socketReceiveRunnable != null) {
                            _socketReceiveRunnable.stop();
                        }
                        break;
                    }

                    if (!_socket.isClosed()) {
                        try {
                            dataStream.write(message);
                        } catch (IOException e) {
                            _socket.close();
                            dataStream.close();
                            // dispose socket receive message thread
                            if (_socketReceiveRunnable != null) {
                                _socketReceiveRunnable.stop();
                            }
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.show(DeviceUart.this,getString(R.string.video_uart_connect_failed));
                            }
                        });

                        break;
                    }
                    Thread.sleep(10000);//保活：隔10s发送一次0 (因为模块超过20s没检测到有数据收发则会断开连接)
                }
            } catch (IOException | InterruptedException ignored) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.show(DeviceUart.this,getString(R.string.video_uart_connect_failed));
                    }
                });

                ignored.printStackTrace();
            }
        }
    }

    /**
     *  TCP Receive Data
     */
    class SocketReceiveRunnable implements Runnable {
        private boolean _stop = false;

        public void stop() {
            _stop = true;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[4096];
            try {
                for (; ; ) {
                    if (_stop) {
                        break;
                    }

                    int size = 0;
                    if (_socket != null &&
                            _socket.isConnected() &&
                            !_socket.isInputShutdown()) {
                        size = _socket.getInputStream().read(buffer);
                    }

                    if (size > 2) {
                        final String str=new String(buffer,2,size-2);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _videoRecvData.append(str);
                                _recvNum+=str.length();
                                _videoRecvNum.setText(getString(R.string.video_uart_recv_num)+_recvNum);
                            }
                        });
                    }

                    Thread.sleep(5);
                }
            } catch (IOException | InterruptedException ignored) { }
        }
    }


    /**
     *  UDP Receive Data
     */
    class UDPSocketReceiveRunnable implements Runnable {
        private boolean _stop = false;

        public void stop() {
            _stop = true;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[4096];
            try {
                for (; ; ) {
                    if (_stop) {
                        break;
                    }

                    int size = 0;
                    if (udp_socket != null) {
                        DatagramPacket recvPackage = new DatagramPacket(buffer , buffer.length);
                        udp_socket.receive(recvPackage);
                        size = recvPackage.getLength();
                    }

                    if (size > 2) {
                        final String str=new String(buffer,2,size-2);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _videoRecvData.append(str);
                                _recvNum+=str.length();
                                _videoRecvNum.setText(getString(R.string.video_uart_recv_num)+_recvNum);
                            }
                        });
                    }

                    Thread.sleep(5);
                }
            } catch (IOException | InterruptedException ignored) {
                if(udp_socket!=null){
                    udp_socket.close();
                }
                 Log.e("ERROR==>","");
            }
        }
    }

    /**
     *  Self
     */
    public static DeviceUart self() {
        return _self;
    }
}


