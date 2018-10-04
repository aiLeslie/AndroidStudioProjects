package com.example.administrator.flagapitest;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.flagapitest.activities.ControlActivity;
import com.example.administrator.flagapitest.activities.RockerActivity;
import com.example.administrator.flagapitest.picture.PictureResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
import static com.example.administrator.flagapitest.ApplicationUtil.accept;
import static com.example.administrator.flagapitest.ApplicationUtil.accepts;
import static com.example.administrator.flagapitest.ApplicationUtil.client;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.obj == null) {
                    return;
                }
                if (msg.what == MESSAGE_WRITE) {
                    edit.setText("");
                    try {
                        if (TFormat == Format.String)
                            Toast.makeText(MainActivity.this, new String((byte[]) (msg.obj), encodeMode) + "\n发送成功", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(MainActivity.this, Integer.valueOf(new String((byte[]) (msg.obj))).toString() + "\n发送成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == MESSAGE_READ) {
                    try {
                        if (msg.arg1 == 1 && msg.arg2 == 1) {//字符串格式
                            showReceived.setText(showReceived.getText().toString() + " " + new String((byte[]) (msg.obj), encodeMode));
//                                Toast.makeText(MainActivity.this, new String((byte[]) (msg.obj), encodeMode) + "\n接收成功", Toast.LENGTH_SHORT).show();
                        } else {//十六进制格式
                            showReceived.setText(showReceived.getText().toString() + " " + Integer.toHexString((int) msg.obj));
//                                Toast.makeText(MainActivity.this, Integer.toHexString((int) msg.obj) + "\n接收成功", Toast.LENGTH_SHORT).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else if (msg.what == ERROR_MESSAGE) {
                    Toast.makeText(MainActivity.this, "抛出异常 - " + (String) msg.obj, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                showReceived.setText(showReceived.getText().toString() + " " + e.getMessage());
            }
        }
    };
    final String model = android.os.Build.MODEL;// 获取手机型号
    final String version = android.os.Build.VERSION.RELEASE; // 获取手机版本

    //控件类型
    private TextView showDevice;//显示连接设备名称
    private EditText edit;//显示连接设备名称
    private TextView showReceived;//显示连接设备名称
    private PopupWindow popupWindow;//显示设备列表的popupWindow
    private PopupWindow IOpopupWindow;//显示IO格式设置的popupWindow
    private ProgressDialog dialog;//进度条控件
    private ImageView animation;

    //描述蓝牙设备的类型
    private ArrayAdapter<String> arrayAdapter;//蓝牙设备数组适配器
    private Set<BluetoothDevice> devices;//蓝牙设备Set
    private BluetoothDevice device;//需要连接蓝牙设备
    private BluetoothAdapter bluetoothAdapter;//蓝牙设配器
    private List<String> devicesName;//蓝牙设备名称

    //连接蓝牙设备的类型
    private BluetoothServerSocket bluetoothServerSocket;//服务端


    //信息类型
    private final int ERROR_MESSAGE = 0;
    private final int MESSAGE_WRITE = 1;//写信息what的值
    private final int MESSAGE_READ = 2;

    //线程类型
    private Thread acceptThread;
    private Thread connectThread;
    //线程状态类型
    public boolean isRun;
    public boolean isListen;
    //选择类型
    private MODE mode = MODE.Client;

    private Format RFormat = Format.Hex;
    private Format TFormat = Format.Hex;

    private char backCount = 0;
    private int byteData = -1;
    private int readIndex = 0;
    private int commandCount = 0;
    private List<String> commadList = new ArrayList<>();
    private EditText key;
    private EditText value;


    private List<String> encodeList = Arrays.asList(new String[]{"ASCII", "GBK", "ISO-8859-1", "UTF-8", "UTF-16", "GB2312", "Big5"});
    private String encodeMode = "GBK";


    //    蓝牙串口通讯，谷歌给出了一个固定UUID： 00001101-0000-1000-8000-00805F9B34FB，
    // 大多数蓝牙串口设备使用此UUID作为连接用UUID，此UUID在BluetoothDevice的createRfcommSocketToServiceRecord方法中有提到。
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//只有这个uuid可以使用
    private final UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final UUID LANAccessUsingPPPServiceClass_UUID = UUID.fromString("00001102-0000-1000-8000-00805F9B34FB");

    private void openingAnimation() {
        PictureResource pr = new PictureResource(this, R.id.main);
        pr.openingAnimation();
//        getSupportActionBar().hide();
//        LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
//        animation = new ImageView(this);
//        animation.setImageResource(R.drawable.superbluetooth);
//        animation.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        layout.addView(animation, new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    sleep(1000 * 1);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
//                            layout.removeView(animation);
//                            getSupportActionBar().show();
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();

        isRun = true;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 让手机屏幕保持直立模式
        openingAnimation();
        initView();
        initBluetooth();

    }

    /******************************蓝牙初始化******************************/
    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {//判断是否支持蓝牙
            Toast.makeText(this, Build.DEVICE + "不支持蓝牙", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        if (!bluetoothAdapter.isEnabled()) {//弹出对话框提示用户是后打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, 0);
            //不做提示，直接打开，不建议用下面的方法，有的手机会有问题。
            // mBluetoothAdapter.enable();
        }
        //获取本机蓝牙名称
        String name = bluetoothAdapter.getName();
        //获取本机蓝牙地址
        String address = bluetoothAdapter.getAddress();
        Log.d(TAG, "bluetooth name =" + name + " address =" + address);
        //获取已配对蓝牙设备
        devices = bluetoothAdapter.getBondedDevices();
        Log.d(TAG, "bonded device size =" + devices.size());
        for (BluetoothDevice bonddevice : devices) {
            Log.d(TAG, "bonded device name =" + bonddevice.getName() + " address" + bonddevice.getAddress());
        }

        devicesName = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, devicesName);


    }

    /**
     * 初始化控件对象
     * 设置监听事件
     */
    private void initView() {
        showDevice = (TextView) findViewById(R.id.ShowDevice);
        showReceived = (TextView) findViewById(R.id.textView);
        edit = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.buttonDiscovery);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonSend);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonClear);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonWatchBondDevices);
        button.setOnClickListener(this);
    }


    /**
     * 实现OnClickListener接口
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        try {
            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "请先打开蓝牙再使用该功能", Toast.LENGTH_SHORT).show();
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enabler, 0);
                return;
            }


            /**
             * 发现蓝牙设备
             */

            if (view.getId() == R.id.buttonDiscovery) {
                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Scanning");
                dialog.setMessage("In search of");
                dialog.setCancelable(false);
                dialog.show();

                bluetoothAdapter.startDiscovery();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(6 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        bluetoothAdapter.cancelDiscovery();
                        dialog.dismiss();
                    }
                }.start();

            }/**
             * 查看配对设备
             */

            else if (view.getId() == R.id.buttonWatchBondDevices) {
                devicesName.clear();
                devices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice b : devices) {
                    if (devicesName.contains(b.getName() + " && " + b.getAddress()))
                        continue;
                    devicesName.add(b.getName() + " && " + b.getAddress());
                }
                showPopupWindow();
            }

            /**
             * 发送信息
             */
            else if (view.getId() == R.id.buttonSend) {

                if (showDevice.getText().toString().equals("未连接")) {
                    Toast.makeText(this, "请先连接蓝牙设备!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Send Message");
                dialog.setCancelable(true);
                dialog.show();
                /**************************************服务端发送方式**************************************/
                if (mode == MODE.Accept) {
                    try {
                        if (TFormat == Format.String) {
                            write(accept, edit.getText().toString().getBytes(encodeMode));
                        } else {
                            writeHex(accept, edit.getText().toString(), MainActivity.this);
                            edit.setText("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        dialog.dismiss();

                    }
                }
                /**************************************客户端发送方式**************************************/
                else {
                    try {
                        if (TFormat == Format.String) {
                            write(client, edit.getText().toString().getBytes(encodeMode));
                        } else {
                            writeHex(client, edit.getText().toString(), MainActivity.this);
                            edit.setText("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        dialog.dismiss();

                    }
                }


                /**
                 * 清除输入区文字
                 */
            } else if (view.getId() == R.id.buttonClear) {
                edit.setText("");

            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /***********************************************************************************************************/
    /**
     * 初始化菜单并加载
     * 设置监听事件
     */
    /***********************************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_item, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        //******************先打开蓝牙在使用以下功能******************/
        if (!bluetoothAdapter.isEnabled() && item.getItemId() != R.id.turnOnBluetooth) {
            Toast.makeText(this, "请先打开蓝牙再使用该功能", Toast.LENGTH_SHORT).show();
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, 0);
            return true;
        }
        switch (item.getItemId()) {

            /******************打开蓝牙******************/
            case R.id.turnOnBluetooth:
                if (bluetoothAdapter.isEnabled()) {
                    Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enabler, 0);
                break;


            /******************设置可见******************/
            case R.id.setVisibility:
                if (bluetoothAdapter.isEnabled()) {
                    if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                        startActivity(discoverableIntent);
                    }
                } else {
                    Toast.makeText(this, "蓝牙没有开启哦", Toast.LENGTH_SHORT).show();
                }
                break;

            /******************打开服务器******************/
            case R.id.ServerListen:
                if (acceptThread == null) {
                    Toast.makeText(this, "服务器现在开始监听", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "服务器已开启,不用重复打开", Toast.LENGTH_SHORT).show();
                    return true;
                }
                acceptThread = new AcceptThread();
                isListen = true;
                acceptThread.start();

                break;
            /******************停止服务器******************/
            case R.id.ServerStopListen:
                if (isListen == true) {
                    Toast.makeText(this, "服务器现在关闭", Toast.LENGTH_SHORT).show();
                    setTitle(R.string.app_name);
                    isListen = false;
                    mode = MODE.Client;
                    acceptThread = null;
                    if (!showDevice.getText().toString().equals("未连接")) showDevice.setText("未连接");
                } else {
                    Toast.makeText(this, "服务器都没有开启,不需要关闭哟", Toast.LENGTH_SHORT).show();
                }

                break;
            /******************断开连接******************/
            case R.id.disconnect:
                if (MODE.Client == mode && client != null && client.isConnected()) {
                    setTitle(R.string.app_name);
                    Toast.makeText(this, "正在与服务器断开连接", Toast.LENGTH_SHORT).show();
                    if (!showDevice.getText().toString().equals("未连接")) showDevice.setText("未连接");
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    client = null;
                } else {
                    Toast.makeText(this, "只能在客户端在连接状态才能使用该选项功能", Toast.LENGTH_SHORT).show();
                }

                break;
            /******************清空接收区******************/
            case R.id.clearReceived:
                showReceived.setText("");
                break;


            /******************设定IO模式******************/
            case R.id.setFormat:
                setIOFormat();
                break;
            /******************设定字符编码******************/
            case R.id.setEncodeMode:
                showEncodeList();
                break;
            /******************打开命令列表******************/
            case R.id.openCommandTable:
                showCommandTable();
                break;
//            /******************遥控模式******************/
//            case R.id.controlMode:
//                if (showDevice.getText().toString().equals("未连接")) {
//                    Toast.makeText(this, "请先连接蓝牙设备,再进入该模式", Toast.LENGTH_SHORT).show();
//                    return true;
//                } else if (mode == MODE.Accept) {
//                    Toast.makeText(this, "服务端不能进入该模式", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//                //                showControlPopupWindow();
//                intent = new Intent(MainActivity.this, ControlActivity.class);
//                intent.putExtra("Address", client.getRemoteDevice().getAddress());
//
//                clients.clear();
//                try {
//                    //                    client.getOutputStream().close();
//                    //                    client.getInputStream().close();
//                    client.close();
//                    client = null;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                startActivityForResult(intent, 1);
//                setTitle(R.string.app_name);
//                Toast.makeText(this, "即将重新连接服务器", Toast.LENGTH_SHORT).show();
//                showDevice.setText("未连接");
//
//                /******************遥控模式******************/
//            case R.id.rockerMode:
//                if (showDevice.getText().toString().equals("未连接")) {
//                    Toast.makeText(this, "请先连接蓝牙设备,再进入该模式", Toast.LENGTH_SHORT).show();
//                    return true;
//                } else if (mode == MODE.Accept) {
//                    Toast.makeText(this, "服务端不能进入该模式", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//                //                showControlPopupWindow();
//                intent = new Intent(MainActivity.this, RockerActivity.class);
//                intent.putExtra("Address", client.getRemoteDevice().getAddress());
//
//                clients.clear();
//                try {
//                    //                    client.getOutputStream().close();
//                    //                    client.getInputStream().close();
//                    client.close();
//                    client = null;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                startActivityForResult(intent, 1);
//                setTitle(R.string.app_name);
//                Toast.makeText(this, "即将重新连接服务器", Toast.LENGTH_SHORT).show();
//                showDevice.setText("未连接");
//
//                break;

        }
        return true;
    }

    /************************************************************************************************************/
    /**
     * 服务端
     * android 蓝牙之间可以通过SDP协议建立连接进行通信，通信方式类似于平常使用socket。
     * 首先创建BluetoothServerSocket ，BluetoothAdapter中提供了两种创建BluetoothServerSocket 方式，
     * 为创建安全的RFCOMM Bluetooth socket，该连接是安全的需要进行配对。
     * 而通过listenUsingInsecureRfcommWithServiceRecord创建的RFCOMM Bluetooth socket是不安全的，
     * 连接时不需要进行配对。
     * 其中的uuid需要服务器端和客户端进行统一。
     */
    /************************************************************************************************************/

    private class AcceptThread extends Thread {
        public void run() {
            mode = MODE.Accept;
            isListen = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTitle("服务端模式");
                }
            });

            //服务器端的bltsocket需要传入uuid和一个独立存在的字符串，以便验证，通常使用包名的形式
            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(bluetoothAdapter.getName(), MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (isListen) {
                try {
                    //注意，当accept()返回BluetoothSocket时，socket已经连接了，因此不应该调用connect方法。
                    //这里会线程阻塞，直到有蓝牙设备链接进来才会往下走
                    accept = bluetoothServerSocket.accept();
                    accepts.add(accept);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, accept.getRemoteDevice().getName() + "成功连入你的服务器", Toast.LENGTH_SHORT).show();
                            showDevice.setText(accept.getRemoteDevice().getName());
                        }
                    });


                    new ReceiveDataThread(accept).start();


                } catch (IOException e) {
                    e.printStackTrace();
                    handler.obtainMessage(0, e.getMessage());


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.obtainMessage(0, e.getMessage());
                } finally {


                }
            }
            try {
                mode = MODE.Client;
                if (bluetoothServerSocket != null) bluetoothServerSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                handler.obtainMessage(0, e1.getMessage());
            }
        }
    }


    /*****************************************************************
     * 客户端
     *****************************************************************/
    private class ConnectThread extends Thread {
        private BluetoothDevice needConnectdevide;

        public ConnectThread(BluetoothDevice needConnectdevide) {
            this.needConnectdevide = needConnectdevide;
        }


        public void run() {
            super.run();

            try {

                client = needConnectdevide.createRfcommSocketToServiceRecord(MY_UUID);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!getTitle().toString().equals("客户端模式")) {//如果过了7秒标题不是客户端模式,证明申请链接线程堵塞,连接失败
                            if (dialog != null) dialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }

                    }
                }.start();
                if (client != null) {
                    client.connect();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) dialog.dismiss();
                        if (client != null) {
                            setTitle("客户端模式");
                            Toast.makeText(MainActivity.this, "连接服务器成功", Toast.LENGTH_SHORT).show();
                            showDevice.setText(client.getRemoteDevice().getName());
                            new ReceiveDataThread(client).start();
                        } else {
                            Toast.makeText(MainActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /************************************************************************************************************/

    /***********************************************************************************************************/
    /**
     * 数据传输
     * 客户端与服务端连接成功后都会调用connected(mmSocket, mmDevice)，创建一个ConnectedThread线程（）。
     * 该线程主要用来接收和发送数据。客户端和服务端处理方式一样。该线程通过socket获得输入输出流。
     * private  InputStream mmInStream = socket.getInputStream();
     * private  OutputStream mmOutStream =socket.getOutputStream();
     */
    /***********************************************************************************************************/
    /**
     * 接发数据线程
     */
    private class ReceiveDataThread extends Thread {
        private BluetoothSocket socket;

        public ReceiveDataThread(BluetoothSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            while (isRun && socket.isConnected()) {
                if (RFormat == Format.String) read(socket);
                else readHex(socket);
            }
        }
    }

    private void write(BluetoothSocket socket, byte[] buffer) {
        if (socket == null) return;
        OutputStream out = null;
        try {

            out = socket.getOutputStream();
            if (out == null) return;

            out.write(buffer);
            // 分享发送的信息到Activity
            //handler.obtainMessage(MESSAGE_WRITE, buffer).sendToTarget();

        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
            handler.obtainMessage(0, e.getMessage());
        } catch (Exception e) {
            handler.obtainMessage(0, e.getMessage());


        }
    }

    public static void writeHex(BluetoothSocket socket, String data, Context context) {
        if (socket == null) return;

        OutputStream out = null;
        try {
            data = data.trim();
            out = socket.getOutputStream();
            if (out == null) return;
            if (data.length() > 0 && data.length() <= 2) {
                out.write(Integer.valueOf(data, 16));
                if (context != null)
                    Toast.makeText(context, Integer.valueOf(data, 16) + "\n发送成功", Toast.LENGTH_SHORT).show();
            } else if (data.length() > 2) {
                int start = 0, end = -1;
                String temp = new String();
                while (true) {
                    end = data.indexOf(" ", start + 1);
                    if (end != -1) {
                        temp = data.substring(start, end).trim();
                        if (!temp.equals("")) out.write(Integer.valueOf(temp, 16));

                    } else {
                        temp = data.substring(start, data.length()).trim();
                        if (!temp.equals("")) out.write(Integer.valueOf(temp, 16));
                        break;
                    }
                    start = ++end;
                }

            }

            //            if (context != null)Toast.makeText(context, "成功发送一帧数据", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] read(BluetoothSocket socket) {

        if (socket == null) return null;
        InputStream in = null;
        try {
            in = socket.getInputStream();
            if (in == null) return null;
            if (in.available() <= 0) return null;
            byte[] bytes = new byte[in.available()];
            in.read(bytes);

            // 分享发送的信息到Activity
            handler.obtainMessage(MESSAGE_READ, 1, 1, bytes).sendToTarget();
            return bytes;
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
            handler.obtainMessage(0, e.getMessage());
        } catch (Exception e) {
            handler.obtainMessage(0, e.getMessage());
        }
        return null;
    }

    private void readHex(BluetoothSocket socket) {
        if (socket == null) return;
        InputStream in = null;
        try {
            in = socket.getInputStream();
            if (in == null) return;
            byteData = in.read();
            if (byteData == -1) return;

            // 分享发送的信息到Activity
            handler.obtainMessage(MESSAGE_READ, 0, 0, byteData).sendToTarget();

        } catch (IOException e) {
            handler.obtainMessage(0, e.getMessage());
        } catch (Exception e) {
            handler.obtainMessage(0, e.getMessage());
        }

    }


    /***
     * 显示操控页面(popupWindow)
     */
    private void showControlPopupWindow() {
        try {

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_control, null);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        Button button = (Button) contentView.findViewById(R.id.buttonGo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    client.getOutputStream().write(8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        button = (Button) contentView.findViewById(R.id.buttonLeft);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    client.getOutputStream().write(4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        button = (Button) contentView.findViewById(R.id.buttonRight);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    client.getOutputStream().write(6);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        button = (Button) contentView.findViewById(R.id.buttonBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    client.getOutputStream().write(2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*****************************************************************
     * 显示编码列表(PopupWindow)
     *****************************************************************/

    private void showEncodeList() {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.devices, null);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 1000, true);

        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        TextView textView = (TextView) contentView.findViewById(R.id.closeListWindow);
        textView.setText("当前字符编码是 " + encodeMode);

        ListView listView = (ListView) contentView.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, encodeList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                encodeMode = encodeList.get(i);
                Toast.makeText(MainActivity.this, "你选择" + encodeList.get(i) + "字符编码", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });
    }

    /*****************************************************************
     * 显示编码列表(PopupWindow)
     *****************************************************************/

    private void showModeList() {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.devices, null);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 1000, true);

        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        TextView textView = (TextView) contentView.findViewById(R.id.closeListWindow);
        textView.setText("请选择要操纵的模式");

        ListView listView = (ListView) contentView.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{
                "控制台模式", "摇杆模式", "遥控模式"
        }));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mode = (String) ((ListView) adapterView).getAdapter().getItem(i);
                if ("控制台模式".equals(mode)) {
                    setTitle(R.string.app_name);
                    showDevice.setText("未连接");
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setMessage("Connect ServerSocket");
                    dialog.setCancelable(false);
                    dialog.show();
                    connectThread = new ConnectThread(device);
                    connectThread.start();
                } else if ("摇杆模式".equals(mode)) {
                    Intent intent = new Intent(MainActivity.this, RockerActivity.class);
                    intent.putExtra("Address", device.getAddress());
                    startActivity(intent);
                } else if ("遥控模式".equals(mode)) {
                    Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                    intent.putExtra("Address", device.getAddress());
                    startActivity(intent);
                }

                popupWindow.dismiss();
            }
        });
    }

    /*****************************************************************
     * 显示命令列表(PopupWindow)
     *****************************************************************/

    private void showCommandTable() {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.devices, null);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 1000, true);

        popupWindow.showAtLocation(rootView, Gravity.TOP, 0, 230);
        TextView textView = (TextView) contentView.findViewById(R.id.closeListWindow);
        textView.setText("请选择要操控的命令");

        ListView listView = (ListView) contentView.findViewById(R.id.listView);
        SharedPreferences commandTable = getSharedPreferences("CommandTable", MODE_PRIVATE);
        commandCount = commandTable.getAll().size();

        commadList.clear();

        for (Map.Entry<String, ?> entry : commandTable.getAll().entrySet()) {
            commadList.add(entry.getKey() + " - " + entry.getValue());
        }
        commadList.add("添加操控命令");

        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, commadList));
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupWindow.dismiss();
                String item = commadList.get(i);
                if (commandCount == 0 || "添加操控命令".equals(item)) {
                    popupWindow.dismiss();
                    showSetCommandTable(null, null);
                    return true;
                }

                showSetCommandTable(item.substring(0, item.indexOf(" - ")), item.substring((item.indexOf(" - ")) + " - ".length()));
                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = commadList.get(i);
                if (commandCount == 0 || "添加操控命令".equals(item)) {
                    popupWindow.dismiss();
                    showSetCommandTable(null, null);
                    return;
                } else if (showDevice.getText().toString().equals("未连接")) {
                    Toast.makeText(MainActivity.this, "请先连接蓝牙设备!", Toast.LENGTH_SHORT).show();
                    return;
                }


                String sendData = item.substring(item.indexOf(" - ") + " - ".length());
                /**************************************服务端发送方式**************************************/
                if (mode == MODE.Accept) {
                    try {
                        if (TFormat == Format.String) {
                            write(accept, sendData.getBytes(encodeMode));
                        } else {
                            writeHex(accept, sendData, MainActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                /**************************************客户端发送方式**************************************/
                else {
                    try {
                        if (TFormat == Format.String) {
                            write(client, sendData.getBytes(encodeMode));
                        } else {
                            writeHex(client, sendData, MainActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    /*****************************************************************
     * 显示设置命令列表(PopupWindow)
     *****************************************************************/

    private void showSetCommandTable(String skey, String svalue) {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.command_set, null);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 1000, true);

        popupWindow.showAtLocation(rootView, Gravity.TOP, 0, 230);

        key = (EditText) contentView.findViewById(R.id.editCommandKey);
        if (skey != null) key.setText(skey);

        value = (EditText) contentView.findViewById(R.id.editCommandValue);
        if (svalue != null) value.setText(svalue);

        Button button = (Button) contentView.findViewById(R.id.buttonOK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences("CommandTable", MODE_PRIVATE).edit().putString(key.getText().toString(), value.getText().toString()).apply();
                popupWindow.dismiss();
                showCommandTable();
            }
        });

        button = (Button) contentView.findViewById(R.id.buttonCancel);
        if (skey != null && svalue != null) {
            button.setText("delete");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Button) view).getText().toString().equals("delete")) {
                    getSharedPreferences("CommandTable", MODE_PRIVATE).edit().remove(key.getText().toString()).apply();
                }
                popupWindow.dismiss();
                showCommandTable();
            }
        });

    }

    /*****************************************************************
     * 显示蓝牙设备列表(PopupWindow)
     *****************************************************************/

    private void showPopupWindow() {
        //没有蓝牙设备就不显示
        if (devicesName.size() == 0) {
            Toast.makeText(this, "没有发现蓝牙设备", Toast.LENGTH_SHORT).show();
            return;
        }
        arrayAdapter.notifyDataSetChanged();
        /******************初始化listView******************/
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.devices, null);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 1000, true);

        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        ListView listView = (ListView) contentView.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //如果是服务端就不连接
                if (mode == MODE.Accept) {
                    Toast.makeText(MainActivity.this, "当前为服务端模式,不能连接设备", Toast.LENGTH_SHORT).show();
                    return;
                }
                String address = arrayAdapter.getItem(i).substring(arrayAdapter.getItem(i).indexOf("&&") + 3);
                device = bluetoothAdapter.getRemoteDevice(address);
                if (client != null && address.equals(client.getRemoteDevice().getAddress()) && !showDevice.getText().toString().equals("未连接")) {
                    Toast.makeText(MainActivity.this, "你已和该服务器已经是连接状态", Toast.LENGTH_SHORT).show();
                    return;
                }

                popupWindow.dismiss();

                showModeList();


            }
        });
    }

    /******************************activity生命周期函数******************************/

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBluetoothReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBluetoothReceiver);
    }

    @Override
    protected void onDestroy() {
        try {
            isRun = false;
            isListen = false;


            for (BluetoothSocket b : accepts) {
                b.close();
            }
            accepts.clear();

        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        bluetoothAdapter.disable();


        super.onDestroy();
    }

    /*****************************************************************
     * 声明蓝牙广播接收器
     *****************************************************************/
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "mBluetoothReceiver action =" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//每扫描到一个设备，系统都会发送此广播。
                //获取蓝牙设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.d(TAG, "name = " + scanDevice.getName() + "address = " + scanDevice.getAddress());
                Toast.makeText(MainActivity.this, "扫描设备  name = " + scanDevice.getName() + "\naddress = " + scanDevice.getAddress(), Toast.LENGTH_SHORT).show();

                if (!devicesName.contains(scanDevice.getName() + " && " + scanDevice.getAddress()))
                    devicesName.add(scanDevice.getName() + " && " + scanDevice.getAddress());


            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(MainActivity.this, "开始扫描", Toast.LENGTH_SHORT).show();
                devicesName.clear();


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                Toast.makeText(MainActivity.this, "结束扫描", Toast.LENGTH_SHORT).show();
                showPopupWindow();

            } else if (ACTION_SCAN_MODE_CHANGED.equals(action)) {
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    Toast.makeText(context, "蓝牙设备可见", Toast.LENGTH_SHORT).show();
                } else if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE) {
                    Toast.makeText(context, "蓝牙设备不可见", Toast.LENGTH_SHORT).show();
                }
            }
        }

    };

    /*****************************************************************
     * 显示设置IO格式的页面(PopupWindow)
     *****************************************************************/
    private void setIOFormat() {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.io_option, null);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        IOpopupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 400, true);
        IOpopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        CompoundButton button = null;
        if (RFormat == Format.String) {
            button = (RadioButton) contentView.findViewById(R.id.receivedString);
            button.setChecked(true);
        } else {
            button = (RadioButton) contentView.findViewById(R.id.receivedHex);
            button.setChecked(true);
        }

        if (TFormat == Format.String) {
            button = (RadioButton) contentView.findViewById(R.id.sendString);
            button.setChecked(true);
        } else {
            button = (RadioButton) contentView.findViewById(R.id.sendHex);
            button.setChecked(true);
        }
        RadioGroup radioGroup = (RadioGroup) contentView.findViewById(R.id.receivedGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.receivedString:
                        RFormat = Format.String;
                        Toast.makeText(MainActivity.this, "你选择接收数据是字符串IO格式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.receivedHex:
                        RFormat = Format.Hex;
                        Toast.makeText(MainActivity.this, "你选择接收数据是十六进制IO格式", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        radioGroup = (RadioGroup) contentView.findViewById(R.id.sendGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.sendString:
                        TFormat = Format.String;
                        Toast.makeText(MainActivity.this, "你选择发送数据是字符串IO格式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sendHex:
                        TFormat = Format.Hex;
                        Toast.makeText(MainActivity.this, "你选择发送数据是十六进制IO格式", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    /**
     * 点击textView关闭popupWindow
     *
     * @param v
     */

    public void dismiss(View v) {
        if (v.getId() == R.id.closeListWindow && popupWindow != null) popupWindow.dismiss();
        else if (v.getId() == R.id.closeIOWindow && IOpopupWindow != null) IOpopupWindow.dismiss();
    }

    public void clearMessage(View v) {
        if (v.getId() == R.id.receivingArea) showReceived.setText("");

    }

    /*****************************************************************
     * 重写父类onBackPressed()
     * 实现按多一次退出功能
     *****************************************************************/
    @Override
    public void onBackPressed() {
        backCount++;
        if (backCount == 1) {
            Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(2000);
                        backCount = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (backCount == 2) {
            bluetoothAdapter.disable();
            super.onBackPressed();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1) {
//            new ConnectThread(bluetoothAdapter.getRemoteDevice(data.getStringExtra("Address"))).start();
//        }
//    }

    /*****************************************************************
     * 枚举类型 (模式 和 IO格式)
     *****************************************************************/
    public static enum MODE {
        //客户端模式
        Client,
        //服务器模式
        Accept
    }

    public static enum Format {
        //字符串格式
        String,
        //十六进制
        Hex,
    }

}