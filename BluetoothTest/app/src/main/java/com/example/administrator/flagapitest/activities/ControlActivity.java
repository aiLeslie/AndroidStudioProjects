package com.example.administrator.flagapitest.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.flagapitest.R;
import com.example.administrator.flagapitest.mysocket.Task.Task;
import com.example.administrator.flagapitest.util.data.Data;
import com.example.administrator.flagapitest.util.data.DataAdapter;
import com.example.administrator.flagapitest.util.gear.Gear;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ControlActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, SensorEventListener {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ToastCode) {
                Toast.makeText(ControlActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                if (((String) (msg.obj)).equals("连接服务端成功")) {
                    setTitle("遥控模式");

                }
            } else if (msg.what == ClosetCode) {
                finish();
            } else if (msg.what == DismissCode) {
                if (dialog != null) dialog.dismiss();
            } else if (msg.what == ShowTransmissionCode) {
                showReceived.setText(showReceived.getText().toString() + (String) msg.obj);
            } else if (msg.what == ShowLightCode) {
                TextView textView = (TextView) findViewById(msg.arg1);
                textView.setTextColor(Color.parseColor("#868686"));
                textView = (TextView) findViewById(msg.arg2);
                textView.setTextColor(Color.RED);
            }
        }
    };
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//只有这个uuid可以使用
    private OutputStream out;
    private String address;
    private ProgressDialog dialog;
    private TextView showReceived;
    private SeekBar seekBar;
    private int backCount = 0;
    private int ShowLightCode = 2;
    private int ShowTransmissionCode = 1;
    private int ToastCode = 0;
    private int ClosetCode = -1;
    private int DismissCode = -2;
    private boolean isRun = false;
    private boolean isTracking = false;
    private final int PARK_GEAR = 0;
    private final int REVERSE_GEAR = 2;
    private final int NONE_GEAR = 4;
    private final int FIRST_GEAR = 6;
    private final int SECOND_GEAR = 8;
    private final int Third_GEAR = 10;
    private Gear gear = Gear.PARK_GEAR;
    private int gearProgress = 0;
    private Task Ttask = null;
    private Task Rtask = null;
    private Vibrator vibrator;
    private Sensor accelerometer;
    private int index = 0;
    private byte[] receiveBuffer = null;
    private double temptrue = -1;
    private double humidity = -1;
    private int rain = -1;
    private int smog = -1;
    private int fire = -1;
    private int distance = -1;
    private int electric = -1;
    private int CO = -1;
    private int body = -1;
    private PopupWindow popupWindow;
    private boolean isUpdata = false;
    private DataAdapter dataAdapter;
    private List<Data> dataList = new ArrayList<>();
    private float x, y, z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        isRun = true;


//        initView();
//        connectServerSocket();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

//    /**
//     * 未实现效果,继续努力
//     */
//
//    private void initRockerView() {
//        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
//        RemoteSurfaceView remoteSurfaceView = new RemoteSurfaceView(this);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        remoteSurfaceView.setLayoutParams(params);
//        relativeLayout.addView(remoteSurfaceView);
//    }
//
//    /**
//     * 初始化view
//     */
//    private void initView() {
//
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 让手机屏幕保持直立模式
//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);//获取震动器
//        /**
//         * 获取加速度传感器
//         */
//        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//
//        Button button = (Button) findViewById(R.id.buttonLeft);
//        button.setOnTouchListener(this);
//        button = (Button) findViewById(R.id.buttonRight);
//        button.setOnTouchListener(this);
//        button = (Button) findViewById(R.id.buttonBrake);
//        button.setOnClickListener(this);
//        showReceived = (TextView) findViewById(R.id.textView);
//        TextView textView = (TextView) findViewById(R.id.p);
//        textView.setTextColor(Color.RED);
//        seekBar = (SeekBar) findViewById(R.id.seekbar);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                isTracking = true;
//
//                new GearLightThread().start();
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                gearAt(seekBar.getProgress());
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                seekBar.setProgress(gearProgress);
//                if (out == null) {
//                    return;
//                }
//                Ttask = new Task(Start_Default, (byte) 1, getIsReverseByte(), (byte) 1, getSpeedByte(), End_Default);
//                try {
//                    out.write(Ttask.getBytes());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                isTracking = false;
//            }
//        });
//
//    }
//
//    /**
//     * 设置点击事件
//     *
//     * @param view
//     */
//    @Override
//    public void onClick(View view) {
//
//        if (client == null || out == null) {
//            Toast.makeText(this, "请等待连接服务端", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            switch (view.getId()) {
//                case R.id.buttonBrake:
//                    if (gear == Gear.PARK_GEAR) {
//                        return;
//                    }
//                    vibrator.vibrate(100);
//                    Gear temp = gear;
//                    gear = Gear.NONE_GEAR;
//                    handler.obtainMessage(ShowLightCode, findIdByGear(temp), findIdByGear(gear)).sendToTarget();
//                    gearProgress = NONE_GEAR;
//                    seekBar.setProgress(NONE_GEAR);
//                    Ttask = new Task(Start_Default, (byte) 1, getIsReverseByte(), (byte) 1, getSpeedByte(), End_Default);
//                    out.write(Ttask.getBytes());
//                    break;
//
////                case R.id.button:
////                    break;
//            }
//        } catch (Exception e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    /**
//     * 设置触摸事件
//     *
//     * @param view
//     * @param motionEvent
//     * @return
//     */
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (client == null) {
//            Toast.makeText(this, "请等待连接服务端", Toast.LENGTH_SHORT).show();
//        }
//        if (gear == Gear.PARK_GEAR) {
//            return true;
//        }
//        try {
//            if (view.getId() == R.id.buttonLeft) {
//
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            super.run();
//                            vibrator.vibrate(50);
//
//                            Ttask = new Task(Start_Default, (byte) 0x01, getIsReverseByte(), (byte) 0x00, getSpeedByte(), End_Default);
//                            try {
//                                out.write(Ttask.getBytes());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            super.run();
//
//                            vibrator.vibrate(50);
//
//                            Ttask = new Task(Start_Default, (byte) 0x01, getIsReverseByte(), (byte) 0x01, getSpeedByte(), End_Default);
//                            try {
//                                out.write(Ttask.getBytes());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                }
//
//            } else if (view.getId() == R.id.buttonRight) {
//
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            super.run();
//                            vibrator.vibrate(50);
//
//                            Ttask = new Task(Start_Default, (byte) 0x01, getIsReverseByte(), (byte) 0x02, getSpeedByte(), End_Default);
//                            try {
//                                out.write(Ttask.getBytes());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            super.run();
//
//                            vibrator.vibrate(50);
//
//                            Ttask = new Task(Start_Default, (byte) 0x01, getIsReverseByte(), (byte) 0x01, getSpeedByte(), End_Default);
//                            try {
//                                out.write(Ttask.getBytes());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//
//                }
//
//            }
//        } catch (Exception e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        return true;
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        x = sensorEvent.values[0];
//        y = sensorEvent.values[1];
//        z = sensorEvent.values[2];
////        showReceived.setText("x = " + x + "\ny = " + y +"\nz = " + z );
//        if (Math.abs(x) + Math.abs(y) + Math.abs(z) > 32) {
//            if (popupWindow != null && popupWindow.isShowing())
//                popupWindow.dismiss();
//            else {
//                showDataPopupWindow();
//            }
//
//        }
//    }
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
//    /***********************************************************************************************************/
//    /**
//     * 初始化菜单并加载
//     * 设置监听事件
//     */
//    /***********************************************************************************************************/
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.control_setting, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.showData:
//                if (!isUpdata) {
//                    showDataPopupWindow();
//                    isUpdata = true;
//                    new UpdataThread().start();
//                }
//                break;
//        }
//        return true;
//    }
///******************************服务器连接******************************/
//    /**
//     * 需要要连接蓝牙设备ip地址
//     * 通过createRfcommSocketToServiceRecord得到bluetoothsocket实例
//     * bluetoothsocket进行连接
//     * 通过bluetoothsocket与服务器通讯
//     */
//
//    private void connectServerSocket() {
//        address = getIntent().getStringExtra("Address");
//
//        //再次连接服务端线程
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            super.run();
//                            try {
//                                sleep(10000);
//
//                                if (getTitle().toString().equals("遥控模式")) return;
//
//                                handler.obtainMessage(ToastCode, "连接服务端失败").sendToTarget();
//                                handler.obtainMessage(ToastCode, "即将退出当前页面").sendToTarget();
//
//                                sleep(3000);
//                                handler.obtainMessage(ClosetCode).sendToTarget();
//
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//
//                    client = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address).createRfcommSocketToServiceRecord(MY_UUID);
//                    client.connect();
//                    out = client.getOutputStream();
//                    handler.obtainMessage(DismissCode).sendToTarget();
//                    handler.obtainMessage(ToastCode, "连接服务端成功").sendToTarget();
//                    new ReceiveDataThread(client).start();
//
////                    Intent intent = new Intent();
////                    intent.putExtra("Address", address);
////                    setResult(RESULT_OK, intent);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }
//
//
///******************************数据传输******************************/
//
//    /**
//     * 读取服务器数据
//     */
//
//    private class ReceiveDataThread extends Thread {
//        private BluetoothSocket socket;
//
//        public ReceiveDataThread(BluetoothSocket socket) {
//            this.socket = socket;
//        }
//
//        @Override
//        public void run() {
//            super.run();
//            new HandleRTask().start();
//            while (isRun) {
//                readHex(socket);
//            }
//        }
//    }
//
//    /**
//     * 读取服务端数据
//     *
//     * @param socket
//     */
//    private void readHex(BluetoothSocket socket) {
//        if (socket == null || !socket.isConnected()) return;
//        InputStream in = null;
//        try {
//            in = socket.getInputStream();
//            final int data = in.read();
//            if (data == -1) return;
//            else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showReceived.setText(showReceived.getText().toString() + " " + Integer.toHexString(data));
//                        if ((byte) data != Start_Default && index == 0) {//返回码
//                            return;
//
//                        } else if (index == 1) {
//                            receiveBuffer = new byte[data];
//                            receiveBuffer[0] = Start_Default;
//                            receiveBuffer[1] = (byte) data;
//                        } else if (index > 1 && index < receiveBuffer.length) {
//                            receiveBuffer[index] = (byte) data;
//                        }
//                        index++;
//                    }
//                });
//            }
//        } catch (IOException e) {
//            handler.obtainMessage(0, e.getMessage()).sendToTarget();
//            isRun = false;
//        } catch (Exception e) {
//            handler.obtainMessage(0, e.getMessage()).sendToTarget();
//
//        }
//
//    }
//
//    /**
//     * 处理任务线程
//     */
//
//    private class HandleRTask extends Thread {
//        @Override
//        public void run() {
//            super.run();
//            while (isRun) {
//
//                if (receiveBuffer != null && index == receiveBuffer.length) {
//                    Rtask = new Task(receiveBuffer);
//                    if (Task.communicationIsSuccess(Rtask)) {
////                        handler.obtainMessage(ToastCode, "通讯成功").sendToTarget();
//                        parseTransmissionData(Rtask.getBytes());
//
//                    } else {
//                        handler.obtainMessage(ToastCode, "通讯失败").sendToTarget();
//                    }
//                    index = 0;
//                }
//            }
//
//        }
//
//    }
//
//    private void parseTransmissionData(byte[] bytes) {
//        int temp = -1;
//        /**
//         * 温度
//         */
//        temptrue = bytes[3] + bytes[4] * 0.01;
//
//        /**
//         * 湿度
//         */
//        humidity = bytes[5] + bytes[6] * 0.01;
//
//
//        /**
//         * 雨量
//         */
//        if (bytes[8] < 0) {
//            temp = (byte) (Math.abs(bytes[8]) + Math.pow(2, 8));
//        } else {
//            temp = bytes[8];
//        }
//        //  rain = (int) ((1 - (float)((bytes[7] << 8 | bytes[8]) / 4095) * 100));
//        rain = (int) (100 - (float) (bytes[7] << 8 | temp) / 4095 * 100);
//
//        /**
//         * 火焰
//         */
//        if (bytes[10] < 0) {
//            temp = (byte) (Math.abs(bytes[10]) + Math.pow(2, 8));
//        } else {
//            temp = bytes[10];
//        }
////        fire = ( int) ((1 - (bytes[9] << 8 | bytes[10]) / (float)4095.0) * 100);
//        fire = (int) (100 - (float) (bytes[9] << 8 | temp) / 4095 * 100);
//
//        /**
//         * 烟雾
//         */
//        if (bytes[12] < 0) {
//            temp = (byte) (Math.abs(bytes[12]) + Math.pow(2, 8));
//        } else {
//            temp = bytes[12];
//        }
//        smog = (int) ((float) (bytes[11] << 8 | temp) / 4095 * 100);
//        /**
//         * CO浓度
//         */
//        if (bytes[14] < 0) {
//            temp = (byte) (Math.abs(bytes[14]) + Math.pow(2, 8));
//        } else {
//            temp = bytes[14];
//        }
//        CO = (int) ((float) (bytes[13] << 8 | temp) / 4095 * 100);
//
//        /**
//         * 距离
//         */
//        if (bytes[15] < 0) {
//            distance = (int) (Math.abs(bytes[15]) + Math.pow(2, 8));
//        } else {
//            distance = bytes[15];
//        }
//        /**
//         * 人体
//         */
//        if (bytes[16] < 0) {
//            body = (int) (Math.abs(bytes[16]) + Math.pow(2, 8));
//        } else {
//            body = bytes[16];
//        }
//        dataList.clear();
//        dataList.add(new Data<String>(R.drawable.temperture, "温度", temptrue + "°C"));
//        dataList.add(new Data<String>(R.drawable.humidity, "湿度", humidity + "%"));
//        dataList.add(new Data<String>(R.drawable.rain, "雨滴", rain + "%"));
//        dataList.add(new Data<String>(R.drawable.smog, "烟雾", smog + "%"));
//        dataList.add(new Data<String>(R.drawable.fire, "火焰", fire + "%"));
//        dataList.add(new Data<String>(R.drawable.distance, "距离", distance + "cm"));
//        dataList.add(new Data<String>(R.drawable.co, "CO浓度", CO + "%"));
//        String people = null;
//        if (body == 0) {
//            people = "有人";
//        } else {
//            people = "没有人";
//        }
//        dataList.add(new Data<String>(R.drawable.body, "人体", people));
//
//
//        //electric = bytes[15] << 8 | bytes[16];
//        handler.obtainMessage(ShowTransmissionCode, "\n温度 = " + temptrue + "°C\n湿度 = " + humidity
//                + "%\n火焰 = " + fire + "%\n雨滴 = " + rain + "%\n烟雾 = " + smog
//                + "%\n距离 = " + distance +   "cm\n人体 = " + people +"\nCO浓度 = " + CO + "%\n").sendToTarget();
////        createNotification("温度 = " + temptrue + "\n湿度 = " + humidity
////                + "\n火焰 = " + fire + "\n下雨 = " + rain + "\n烟雾 = " + smog + "\n距离 = " + distance + "\n");
//
//    }
//
//
//    private void showDataPopupWindow() {
//        View contentView = LayoutInflater.from(ControlActivity.this).inflate(R.layout.devices, null);
//        View rootView = LayoutInflater.from(ControlActivity.this).inflate(R.layout.activity_main, null);
//        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 1000, true);
//
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.water));
//
//        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
//        TextView textView = (TextView) contentView.findViewById(R.id.closeListWindow);
//        textView.setText("当前侦测数据:");
//
//        ListView listView = (ListView) contentView.findViewById(R.id.listView);
//        dataAdapter = new DataAdapter(ControlActivity.this, R.layout.data_item, dataList);
//        listView.setAdapter(dataAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Data data = dataList.get(i);
//                Toast.makeText(ControlActivity.this, data.getName() + " = " + data.getValue(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public class UpdataThread extends Thread {
//
//        @Override
//        public void run() {
//            super.run();
//
//            try {
//                isUpdata = true;
//                while (isUpdata) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            dataAdapter.notifyDataSetChanged();
//                        }
//                    });
//                    sleep(1000);
//
//
//                    if (popupWindow != null && !popupWindow.isShowing()) {
//                        isUpdata = false;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /******************************档位数据处理******************************/
//    /**
//     * 根据进度得到档位
//     * 如果该进度的档位与上一次档位不同震动手机
//     *
//     * @param progress
//     */
//    private void gearAt(int progress) {
//
//        switch (progress) {
//            case PARK_GEAR:
//                if (gear != Gear.PARK_GEAR) vibrator.vibrate(100);
//                gear = Gear.PARK_GEAR;
//                gearProgress = PARK_GEAR;
//                break;
//
//            case REVERSE_GEAR:
//                if (gear != Gear.REVERSE_GEAR) vibrator.vibrate(100);
//                gear = Gear.REVERSE_GEAR;
//                gearProgress = REVERSE_GEAR;
//                break;
//
//            case NONE_GEAR:
//                if (gear != Gear.NONE_GEAR) vibrator.vibrate(100);
//                gear = Gear.NONE_GEAR;
//                gearProgress = NONE_GEAR;
//                break;
//
//            case FIRST_GEAR:
//                if (gear != Gear.FIRST_GEAR) vibrator.vibrate(100);
//                gear = Gear.FIRST_GEAR;
//                gearProgress = FIRST_GEAR;
//                break;
//
//            case SECOND_GEAR:
//                if (gear != Gear.SECOND_GEAR) vibrator.vibrate(100);
//                gear = Gear.SECOND_GEAR;
//                gearProgress = SECOND_GEAR;
//                break;
//            case Third_GEAR:
//                if (gear != Gear.Third_GEAR) vibrator.vibrate(100);
//                gear = Gear.Third_GEAR;
//                gearProgress = Third_GEAR;
//                break;
//
//        }
//    }
//
//    /**
//     * 根据档位找到显示该档位的textView的Id
//     *
//     * @param g
//     * @return
//     */
//    private int findIdByGear(Gear g) {
//        if (Gear.PARK_GEAR == g) {//泊车
//            return R.id.p;
//        } else if (Gear.REVERSE_GEAR == g) {//倒车
//            return R.id.r;
//
//        } else if (Gear.NONE_GEAR == g) {//空挡
//            return R.id.n;
//
//        } else if (Gear.FIRST_GEAR == g) {//1挡
//            return R.id.f;
//
//        } else if (Gear.SECOND_GEAR == g) {//2挡
//            return R.id.s;
//
//        } else if (Gear.Third_GEAR == g) {//3挡
//            return R.id.t;
//
//        } else {
//            return 0;
//        }
//    }
//
//    /**
//     * 根据档位显示亮灯线程
//     */
//    private class GearLightThread extends Thread {
//        private Gear previousGear = gear;
//
//        @Override
//        public void run() {
//            super.run();
//            while (isTracking) {
//                if (gear != previousGear) {
//                    Message message = handler.obtainMessage(ShowLightCode);
//                    message.arg1 = findIdByGear(previousGear);
//                    message.arg2 = findIdByGear(gear);
//                    message.sendToTarget();
//                    previousGear = gear;
//                }
//
//                try {
//                    sleep(1000 / 50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        }
//    }
//
//    /**
//     * 根据档位返回表示速度的字节
//     *
//     * @return
//     */
//    private byte getSpeedByte() {
//        /**
//         * 速率
//         * 1 - 全速
//         * 2 - 半速
//         * 3 - 停止
//         */
//        if (Gear.PARK_GEAR == gear) {//泊车
//            return (byte) 0x03;
//        } else if (Gear.REVERSE_GEAR == gear) {//倒车
//            return (byte) 0x02;
//
//        } else if (Gear.NONE_GEAR == gear) {//空挡
//            return (byte) 0x03;
//
//        } else if (Gear.FIRST_GEAR == gear) {//1挡
//            return (byte) 0x02;
//
//        } else if (Gear.SECOND_GEAR == gear) {//2挡
//            return (byte) 0x01;
//
//        } else if (Gear.Third_GEAR == gear) {//3挡
//            return (byte) 0x01;
//
//        } else {
//            return 0x03;
//        }
//    }
//
//    /**
//     * 根据档位返回表示方向的字节
//     *
//     * @return
//     */
//    private byte getIsReverseByte() {
//        /**
//         * 速率
//         * 0- 前进
//         * 1 - 后退
//         */
//        if (Gear.REVERSE_GEAR == gear) {//泊车
//            return (byte) 0x01;
//        } else {
//            return (byte) 0x00;
//        }
//
//    }
//
//
//    /**
//     * 显示进度条对话框
//     */
//    private void showDialog() {
//        dialog = new ProgressDialog(ControlActivity.this);
//        dialog.setTitle("Connecting");
//        dialog.setMessage("wait a moment");
//        dialog.setCancelable(false);
//        dialog.show();
//        Intent intent = getIntent();
//    }
//
//    /**
//     * 创建通知
//     *
//     * @param text
//     */
//    private void createNotification(String text) {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent();
//        intent.setClass(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);//借助intent的实例构建出pendingIntent实例
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentTitle("This is a content title").setContentText(text).setWhen(System.currentTimeMillis()).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round)).setSmallIcon(R.mipmap.ic_launcher_round).setContentIntent(pendingIntent);
//        builder.setAutoCancel(true);//设置当点击后自动取消通知
//        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
//        Notification notification = builder.build();
//        notificationManager.notify(0, notification);
//
//
//    }
//
//    /**
//     * 将popupWindow消失
//     *
//     * @param v
//     */
//    public void dismiss(View v) {
//        popupWindow.dismiss();
//    }
//
//    /**
//     * 清空接收区
//     *
//     * @param v
//     */
//    public void clearMessage(View v) {
//        if (v.getId() == R.id.receivePalce) showReceived.setText("");
////        new SendFileThread().start();
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorManager.unregisterListener(this, accelerometer);
//
//    }
//
//    /*****************************************************************
//     * 重写父类onBackPressed()
//     * 实现按多一次退出功能
//     *****************************************************************/
//    @Override
//    public void onBackPressed() {
//        backCount++;
//        if (backCount == 1) {
//            Toast.makeText(this, "再按一次返回键退出遥控模式", Toast.LENGTH_SHORT).show();
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        sleep(2000);
//                        backCount = 0;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//        } else if (backCount == 2) {
//
//
//            new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        super.run();
//                        isRun = false;
//                        client.getOutputStream().close();
//                        client.getInputStream().close();
//                        client.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }.start();
//
//            finish();
//        }
//    }
//
//    /**
//     * 将本活动用到的资源进行释放
//     */
//    @Override
//    protected void onDestroy() {
//        isRun = false;
//
//
//        super.onDestroy();
//
//    }
//
//    private class SendFileThread extends Thread {
//        @Override
//        public void run() {
//            super.run();
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(intent, 1);
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 1:
//                Uri uri = data.getData();
//                File file = new File(String.valueOf(uri));
//                Toast.makeText(this, file.getName(), Toast.LENGTH_SHORT).show();
//                FileInputStream in;
//                try {
//                    in = new FileInputStream(file);
//                    byte[] bytes = new byte[in.available()];
//                    in.read(bytes);
//                    Ttask = new Task((byte) 12, bytes);
//                    out.write(Ttask.getBytes());
//                    Toast.makeText(this, "发送文件成功", Toast.LENGTH_SHORT).show();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                break;
//        }
//    }
}

