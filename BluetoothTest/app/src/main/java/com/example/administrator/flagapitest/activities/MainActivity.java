package com.example.administrator.flagapitest.activities;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.flagapitest.R;
import com.example.administrator.flagapitest.alert.dialog.DialogFactory;
import com.example.administrator.flagapitest.mvp.presenter.BluetoothPresenter;
import com.example.administrator.flagapitest.mvp.view.BaseActivity;
import com.example.administrator.flagapitest.mvp.view.MyBluetoothView;
import com.example.administrator.flagapitest.mysocket.socket.DataFormat;
import com.example.administrator.flagapitest.picture.PictureResource;
import com.example.administrator.flagapitest.util.bluetooth.BluetoothController;
import com.example.administrator.flagapitest.util.bluetooth.thread.SocketListener;
import com.example.administrator.flagapitest.util.bluetooth.thread.message.OnMsgListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
import static com.example.administrator.flagapitest.ApplicationUtil.MODE;
import static com.example.administrator.flagapitest.ApplicationUtil.RFormat;
import static com.example.administrator.flagapitest.ApplicationUtil.TFormat;
import static com.example.administrator.flagapitest.ApplicationUtil.bluetoothServerSocket;
import static com.example.administrator.flagapitest.ApplicationUtil.client;
import static com.example.administrator.flagapitest.ApplicationUtil.encodeList;
import static com.example.administrator.flagapitest.ApplicationUtil.encodeMode;


public class MainActivity extends BaseActivity<BluetoothPresenter<MainActivity>> implements MyBluetoothView, View.OnClickListener {
    private static final String TAG = "MainActivity";

    //控件类型
    private TextView showDevice; // 显示连接设备名称
    private EditText edit; // 发送编辑文本
    private TextView showReceived; // 显示连接设备名称
    private PopupWindow popupWindow; // 显示设备列表的popupWindow
    private PopupWindow IOpopupWindow; // 显示IO格式设置的popupWindow
    private ProgressDialog dialog; // 进度条控件
    private DialogFactory dFactory = new DialogFactory(this); // 对话框工厂
    private EditText key; // 指令列表的键
    private EditText value; // 指令列表的值

    // 信息类型
    private final int ERROR_MESSAGE = 0;
    private final int MESSAGE_WRITE = 1;//写信息what的值
    private final int MESSAGE_READ = 2;


    private List<String> devicesName = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private List<String> commadList = new ArrayList<>();

    private byte backCount = 0;


    //    蓝牙串口通讯，谷歌给出了一个固定UUID： 00001101-0000-1000-8000-00805F9B34FB，
    // 大多数蓝牙串口设备使用此UUID作为连接用UUID，此UUID在BluetoothDevice的createRfcommSocketToServiceRecord方法中有提到。
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private void openingAnimation() {
        PictureResource pr = new PictureResource(this, R.id.main);
        pr.openingAnimation();

    }


    @Override
    protected BluetoothPresenter createPresenter() {
//        Bpresenter = new BluetoothPresenter<MainActivity>();
//        return Bpresenter;
        return new BluetoothPresenter<MainActivity>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 让手机屏幕保持直立模式

        initView(); // 初始化控件

        presenter.fetch();

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
            if (!presenter.getController().getBluetoothAdapterStatus()) {
                Toast.makeText(this, "请先打开蓝牙再使用该功能", Toast.LENGTH_SHORT).show();
                presenter.getController().checkBluetoothStatus(this, 0);
                return;
            }


            /**
             * 发现蓝牙设备
             */

            if (view.getId() == R.id.buttonDiscovery) {
                final ProgressDialog dialog = dFactory.createProgressDialog("Scanning", "In search of", false);

                presenter.getController().findDevice(new BluetoothController.Listener<Void>() {

                    @Override
                    public void response(Void aVoid) {
                        dialog.dismiss();
                    }
                });


            }/**
             * 查看配对设备
             */

            else if (view.getId() == R.id.buttonWatchBondDevices) {
                List<BluetoothDevice> devices = presenter.getBondedDevices();
                devicesName.clear();
                for (BluetoothDevice b : devices) {
                    devicesName.add(b.getName() + " && " + b.getAddress());
                }
                showPopupWindow();
            }

            /**
             * 发送信息
             */
            else if (view.getId() == R.id.buttonSend) {

                if (MODE.isNull()) {
                    Toast.makeText(this, "请先连接蓝牙设备!", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**************************************服务端发送方式**************************************/
                presenter.write(edit.getText().toString());
                /**************************************客户端发送方式**************************************/


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
        // 蓝牙没打开
        if (!presenter.getController().getBluetoothAdapterStatus() && item.getItemId() != R.id.turnOnBluetooth) {
            Toast.makeText(this, "蓝牙没有开启哦", Toast.LENGTH_SHORT).show();
            return true;
        }
        Intent intent;

        switch (item.getItemId()) {

            /******************打开蓝牙******************/
            case R.id.turnOnBluetooth:
                if (!presenter.getController().getBluetoothAdapterStatus()) {
                    presenter.getController().checkBluetoothStatus(this, 0);
                } else {
                    Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
                }

                break;


            /******************设置可见******************/
            case R.id.setVisibility:
                presenter.getController().enableVisibility(this, 12);

                break;

            /******************打开服务器******************/
            case R.id.ServerListen:
                if (bluetoothServerSocket == null) {
                    Toast.makeText(this, "服务器现在开始监听", Toast.LENGTH_SHORT).show();
                    setTitle("服务端模式");
                    showDevice.setText("未连接");
                    presenter.openServer(new SocketListener<String>() {
                        @Override
                        public void onSucceed(final String s) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, s + "成功连接进入你的服务器", Toast.LENGTH_SHORT).show();
                                    showDevice.setText(s);
                                }
                            });
                        }

                        @Override
                        public void onFail(String s) {

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }, new OnMsgListener<String>() {
                        @Override
                        public void OnMsg(final String msg) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showReceived.setText(showReceived.getText().toString() + msg);
                                }
                            });
                        }
                    });

                } else {
                    Toast.makeText(this, "服务器已开启,不用重复打开", Toast.LENGTH_SHORT).show();
                    return true;
                }


                break;
            /******************停止服务器******************/
            case R.id.ServerStopListen:
                if (bluetoothServerSocket != null) {
                    Toast.makeText(this, "服务器现在关闭", Toast.LENGTH_SHORT).show();
                    presenter.closeServer();
                    setTitle(R.string.app_name);
                    showDevice.setText("未连接");
                } else {
                    Toast.makeText(this, "服务器都没有开启,不需要关闭哟", Toast.LENGTH_SHORT).show();
                }

                break;
            /******************断开连接******************/
            case R.id.disconnect:
                if (client != null) {
                    Toast.makeText(this, "正在与服务器断开连接", Toast.LENGTH_SHORT).show();
                    presenter.disconnect();
                    setTitle(R.string.app_name);
                    showDevice.setText("未连接");

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

        }
        return true;
    }

    @Override
    public void showLoading() {
        openingAnimation();
    }

    @Override
    public void showInfos(List infos) {

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

    private void showModeList(final String address) {
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
                    showDevice.setText("未连接");
                    presenter.connectServer(address, new SocketListener<String>() {
                        @Override
                        public void onSucceed(final String s) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setTitle("客户端模式");
                                    Toast.makeText(MainActivity.this, "成功进入" + s + "的服务器", Toast.LENGTH_SHORT).show();
                                    showDevice.setText(s);
                                }
                            });

                        }

                        @Override
                        public void onFail(String s) {

                        }
                    }, new OnMsgListener<String>() {
                        @Override
                        public void OnMsg(final String msg) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showReceived.setText(showReceived.getText().toString() + msg);
                                }
                            });
                        }
                    });
                } else if ("摇杆模式".equals(mode)) {
                    Intent intent = new Intent(MainActivity.this, RockerActivity.class);
                    intent.putExtra("Address", address);
                    startActivity(intent);
                } else if ("遥控模式".equals(mode)) {
                    Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                    intent.putExtra("Address", address);
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
                if ("添加操控命令".equals(item)) {
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
                if ("添加操控命令".equals(item)) {
                    popupWindow.dismiss();
                    showSetCommandTable(null, null);
                    return;
                } else if (showDevice.getText().toString().equals("未连接")) {
                    Toast.makeText(MainActivity.this, "请先连接蓝牙设备!", Toast.LENGTH_SHORT).show();
                    return;
                }


                String sendData = item.substring(item.indexOf(" - ") + " - ".length());

                /**************************************服务端发送方式**************************************/
                presenter.write(sendData);
                /**************************************客户端发送方式**************************************/

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
        if (arrayAdapter == null) {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, devicesName);
        } else {
            arrayAdapter.notifyDataSetChanged();
        }

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
                showDevice.setText("未连接");
                String address = arrayAdapter.getItem(i).substring(arrayAdapter.getItem(i).indexOf("&&") + 3);
//                if (client != null && address.equals(client.getRemoteDevice().getAddress())) {
//                    Toast.makeText(MainActivity.this, "你已和该服务器已经是连接状态", Toast.LENGTH_SHORT).show();
//                    return;
//                } else { // 开始连接服务器
                    presenter.connectServer(address, new SocketListener<String>() {
                        @Override
                        public void onSucceed(final String s) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setTitle("客户端模式");
                                    Toast.makeText(MainActivity.this, "成功进入" + s + "的服务器", Toast.LENGTH_SHORT).show();
                                    showDevice.setText(s);
                                }
                            });
                        }

                        @Override
                        public void onFail(String s) {

                        }
                    }, new OnMsgListener<String>() {
                        @Override
                        public void OnMsg(final String msg) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showReceived.setText(showReceived.getText().toString() + msg);
                                }
                            });
                        }
                    });
//                }
                popupWindow.dismiss();

//                showModeList();

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
        presenter.closeServer();
        presenter.disconnect();
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

                // 模型层添加扫描设备
                presenter.addScanResult(scanDevice);


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
        if (RFormat.isStringFormat()) {
            button = (RadioButton) contentView.findViewById(R.id.receivedString);
            button.setChecked(true);
        } else {
            button = (RadioButton) contentView.findViewById(R.id.receivedHex);
            button.setChecked(true);
        }

        if (TFormat.isStringFormat()) {
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
                        RFormat.setFormat(DataFormat.STRING_FORMAT);
                        Toast.makeText(MainActivity.this, "你选择接收数据是字符串IO格式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.receivedHex:
                        RFormat.setFormat(DataFormat.HEX_FORMAT);
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
                        TFormat.setFormat(DataFormat.STRING_FORMAT);
                        Toast.makeText(MainActivity.this, "你选择发送数据是字符串IO格式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sendHex:
                        TFormat.setFormat(DataFormat.HEX_FORMAT);
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
        if (++backCount == 1) {
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
            presenter.getController().turnOffBluetooth();
            super.onBackPressed();
        }
    }


    @Override
    public BluetoothDevice connectDevice() {
        return null;
    }

    @Override
    public BluetoothDevice scanDevice() {
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                // 如果不允许打开蓝牙
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // 关闭程序
                    Toast.makeText(this, "即将关闭程序", Toast.LENGTH_SHORT).show();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(2000);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }
                break;
        }
    }


}