package com.example.administrator.bluetoothtest.test;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bluetoothtest.mysocket.Task.Task;
import com.example.administrator.bluetoothtest.util.data.Data;
import com.example.administrator.bluetoothtest.util.data.DataAdapter;
import com.example.administrator.bluetoothtest.util.gear.Gear;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RockerActivity extends AppCompatActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ToastCode) {
                Toast.makeText(RockerActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                if (((String) (msg.obj)).equals("连接服务端成功")) {
                    setTitle("摇杆模式");

                }
            } else if (msg.what == ClosetCode) {
                finish();
            } else if (msg.what == DismissCode) {
                if (dialog != null) dialog.dismiss();
            } else if (msg.what == ShowTransmissionCode) {
//                showReceived.setText(showReceived.getText().toString() + (String) msg.obj);
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
    public static double temptrue = -1;
    public static double humidity = -1;
    public static int rain = -1;
    public static int smog = -1;
    public static int fire = -1;
    public static int distance = -1;
    public static int electric = -1;
    public static int CO = -1;
    public static int body = -1;
    public static int fireColor = Color.BLACK;
    public static int smogColor = Color.BLACK;
    public static int coColor = Color.BLACK;
    public static int rainColor = Color.BLACK;
    public static int bodyColor = Color.BLACK;
    public static int fireDrawCount = 0;
    public static int smogDrawCount = 0;
    public static int coDrawCount = 0;
    public static int rainDrawCount = 0;
    public static int bodyDrawCount = 0;
    private Thread alertThread;
    private boolean isAlert = false;
    private PopupWindow popupWindow;
    private List<Data> dataList = new ArrayList<>();
    private boolean isUpdata = false;
    private DataAdapter dataAdapter;
    public static StringBuilder dataText = new StringBuilder();

    public static float x = -1;
    public static float y = -1;
    private boolean isInside = false;
    private StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isRun = true;
//        InitRockerView();
//        connectServerSocket();


    }

//   private void InitRockerView() {
//        final SurfaceView rockerView = new RockerView(this);
//        setContentView(rockerView);
//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);//获取震动器
//        rockerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        if (RockerView.getDistance(RockerView.cx, RockerView.cy, motionEvent.getX(), motionEvent.getY()) <= RockerView.Bradius / 3) {
//                            isInside = true;
//                        }
//                    case MotionEvent.ACTION_MOVE:
//                        if (isInside)
//                        {
//                            if (RockerView.getDistance(RockerView.cx, RockerView.cy, motionEvent.getX(), motionEvent.getY()) <= RockerView.Bradius) {
//
//                                x = motionEvent.getX();
//                                y = motionEvent.getY();
//
//                            } else {
//
//                                if (motionEvent.getY() - RockerView.cy == 0 && motionEvent.getX() - RockerView.cx > 0) {
//                                    x = RockerView.cx + RockerView.Bradius;
//                                    y = RockerView.cy;
//                                    return true;
//                                } else if (motionEvent.getY() - RockerView.cy == 0 && motionEvent.getX() - RockerView.cx < 0) {
//                                    x = RockerView.cx - RockerView.Bradius;
//                                    y = RockerView.cy;
//                                    return true;
//                                } else if (motionEvent.getX() - RockerView.cx == 0 && motionEvent.getY() - RockerView.cy > 0) {
//                                    x = RockerView.cx;
//                                    y = RockerView.cy + RockerView.Bradius;
//                                    return true;
//                                } else if (motionEvent.getX() - RockerView.cx == 0 && motionEvent.getY() - RockerView.cy < 0) {
//                                    x = RockerView.cx;
//                                    y = RockerView.cy - RockerView.Bradius;
//                                    return true;
//                                }
//
//                                double radian = Math.atan((motionEvent.getY() - RockerView.cy) / (motionEvent.getX() - RockerView.cx));
//
//                                if (((RockerView) rockerView).getQuadrant(motionEvent.getX(), motionEvent.getY()) == RockerView.Quadrant.First) {
//                                    x = RockerView.cx + RockerView.Bradius * (float) Math.cos(radian);
//                                    y = RockerView.cy + RockerView.Bradius * (float) Math.sin(radian);
//                                } else if (((RockerView) rockerView).getQuadrant(motionEvent.getX(), motionEvent.getY()) == RockerView.Quadrant.Second) {
//                                    x = RockerView.cx - RockerView.Bradius * (float) Math.cos(radian);
//                                    y = RockerView.cy - RockerView.Bradius * (float) Math.sin(radian);
//                                } else if (((RockerView) rockerView).getQuadrant(motionEvent.getX(), motionEvent.getY()) == RockerView.Quadrant.Third) {
//                                    x = RockerView.cx - RockerView.Bradius * (float) Math.cos(radian);
//                                    y = RockerView.cy - RockerView.Bradius * (float) Math.sin(radian);
//                                } else if (((RockerView) rockerView).getQuadrant(motionEvent.getX(), motionEvent.getY()) == RockerView.Quadrant.Fourth) {
//                                    x = RockerView.cx + RockerView.Bradius * (float) Math.cos(radian);
//                                    y = RockerView.cy + RockerView.Bradius * (float) Math.sin(radian);
//                                }
//                            }
//                        }
//
//                        controlByGear();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        isInside = false;
//                        x = RockerView.cx;
//                        y = RockerView.cy;
//                        controlByGear();
//                        return true;
//                }
//
//
//                return false;
//            }
//        });
//    }
//
//    private void controlByGear() {
//
//        float distance = RockerView.getDistance(x, y, RockerView.cx, RockerView.cy);
//        byte speedByte = -1;
//        if (distance <= RockerView.Bradius / 3 * 2) {
//            gear = Gear.FIRST_GEAR;
//            speedByte = (byte) 2;
//
//        } else if (distance > RockerView.Bradius / 3 * 2) {
//            gear = Gear.SECOND_GEAR;
//            speedByte = (byte) 1;
//        } else {
//            gear = Gear.NONE_GEAR;
//            speedByte = (byte) 3;
//        }
//        Task task = getTaskByXY(speedByte);
//        if (task != null && out != null) {
//            try {
//                Ttask = task;
//
//                out.write(Ttask.getBytes());
//                vibrator.vibrate(30);
//                if (Ttask.orientation.getIsReverse() == 1) {
//                    stringBuilder.append("R ");
//
//                }
//                if (Gear.NONE_GEAR == gear) {//空挡
//                    stringBuilder.append("None");
//
//                } else if (Gear.FIRST_GEAR == gear) {//1挡
//                    stringBuilder.append("First");
//
//                } else if (Gear.SECOND_GEAR == gear) {//2挡
//                    stringBuilder.append("Second");
//
//                }
//                if (Ttask.orientation.getIsStraight() == 0) {
//                    stringBuilder.append("(Left)");
//                } else if (Ttask.orientation.getIsStraight() == 1) {
//                    stringBuilder.append("(Straight)");
//                } else if (Ttask.orientation.getIsStraight() == 2) {
//                    stringBuilder.append("(Right)");
//                }
//                setTitle("摇杆模式 - " + stringBuilder.toString());
//                stringBuilder.setLength(0);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }
//
//    private Task getTaskByXY(byte speedByte) {
//        Task task = new Task(Task.Start_Default, (byte) 1, (byte) 0, (byte) 0, (byte) 0, Task.End_Default);
//
//        RockerView.Quadrant quadrant = RockerView.getQuadrant(x, y);
//        if (quadrant == RockerView.Quadrant.Xlow || quadrant == RockerView.Quadrant.XHigh || quadrant == RockerView.Quadrant.Midpoint) {
//            task.orientation.setIsStraight((byte) 1);//直行
//            task.orientation.setIsReverse((byte) 0);//前进
//            gear = Gear.NONE_GEAR;
//            task.orientation.setSpeed((byte) 3);
//            if (task.equals(Ttask)) {
//                return null;
//            } else {
//                return task;
//            }
//
//        } else if (quadrant == RockerView.Quadrant.Ylow) {
//            task.orientation.setIsStraight((byte) 1);//直行
//            task.orientation.setIsReverse((byte) 0);//前进
//            task.orientation.setSpeed(speedByte);
//            if (task.equals(Ttask)) {
//                return null;
//            } else {
//                return task;
//            }
//        } else if (quadrant == RockerView.Quadrant.YHigh) {
//            task.orientation.setIsStraight((byte) 1);//直行
//            task.orientation.setIsReverse((byte) 1);//后退
//            task.orientation.setSpeed(speedByte);
//            if (task.equals(Ttask)) {
//                return null;
//            } else {
//                return task;
//            }
//        }
//        double radian = Math.abs(Math.atan((y - RockerView.cy) / (x - RockerView.cx)));
//
//        if (quadrant == RockerView.Quadrant.Fourth) {
//            task.orientation.setIsReverse((byte) 0);//前进
//            if (radian >= Math.PI / 3) {
//                task.orientation.setIsStraight((byte) 1);//直行
//            } else {
//                task.orientation.setIsStraight((byte) 2);//右行
//            }
//            task.orientation.setSpeed(speedByte);
//            if (task.equals(Ttask)) {
//                return null;
//            } else {
//                return task;
//            }
//        } else if (quadrant == RockerView.Quadrant.Third) {
//            task.orientation.setIsReverse((byte) 0);//前进
//            if (radian >= Math.PI / 3) {
//                task.orientation.setIsStraight((byte) 1);//直行
//            } else {
//                task.orientation.setIsStraight((byte) 0);//左行
//            }
//            task.orientation.setSpeed(speedByte);
//            if (task.equals(Ttask)) {
//                return null;
//            } else {
//                return task;
//            }
//
//        } else if (quadrant == RockerView.Quadrant.Second) {
//            task.orientation.setIsReverse((byte) 1);//后退
//            if (radian >= Math.PI / 3) {
//                task.orientation.setIsStraight((byte) 1);//直行
//            } else {
//                task.orientation.setIsStraight((byte) 0);//左行
//            }
//            task.orientation.setSpeed(speedByte);
//            if (task.equals(Ttask)) {
//                return null;
//            } else {
//                return task;
//            }
//
//        } else if (quadrant == RockerView.Quadrant.First) {
//            task.orientation.setIsReverse((byte) 1);//后退
//            if (radian >= Math.PI / 3) {
//                task.orientation.setIsStraight((byte) 1);//直行
//            } else {
//                task.orientation.setIsStraight((byte) 2);//右行
//            }
//            task.orientation.setSpeed(speedByte);
//            if (task.equals(Ttask)) {
//                return null;
//            } else {
//                return task;
//            }
//
//
//        }
//        return null;
//
//
//    }
//
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
//                                sleep(10 * 1000);
//
//                                if (getTitle().toString().contains("摇杆模式")) return;
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
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }
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
////                        showReceived.setText(showReceived.getText().toString() + " " + Integer.toHexString(data));
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
//
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
//        if (rain > 20) {
//            rainColor = Color.RED;
////            createNotification("下雨啦");
//        } else {
//            rainColor = Color.BLACK;
//        }
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
//        if (fire > 80) {
//            fireColor = Color.RED;
////            createNotification("前方侦测到有火焰");
//
//        } else {
//            fireColor = Color.BLACK;
//        }
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
//        if (smog > 30) {
//            smogColor = Color.RED;
////            createNotification("前方侦测到有烟雾产生");
//        } else {
//            smogColor = Color.BLACK;
//        }
//        /**
//         * CO浓度
//         */
//        if (bytes[14] < 0) {
//            temp = (byte) (Math.abs(bytes[14]) + Math.pow(2, 8));
//        } else {
//            temp = bytes[14];
//        }
//        CO = (int) ((float) (bytes[13] << 8 | temp) / 4095 * 100);
//        if (CO > 40) {
//            coColor = Color.RED;
////            createNotification("当前地方一氧化碳浓度过高");
//        } else {
//            coColor = Color.BLACK;
//        }
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
//            bodyColor = Color.RED;
////            createNotification("人体感应系统侦测前方有人");
//        } else {
//            people = "没有人";
//            bodyColor = Color.BLACK;
//        }
//        dataList.add(new Data<String>(R.drawable.body, "人体", people));
//        dataText.setLength(0);
//
//
//        dataText.append("\n温度 = " + temptrue + "°C\n湿度 = " + humidity
//                + "%\n火焰 = " + fire + "%\n雨滴 = " + rain + "%\n烟雾 = " + smog
//                + "%\n距离 = " + distance + "cm\n人体 = " + people + "\nCO浓度 = " + CO + "%");
//        if (bodyColor == Color.RED || coColor == Color.RED || fireColor == Color.RED || rainColor == Color.RED || smogColor == Color.RED) {
//            isAlert = true;
//        } else {
//            isAlert = false;
//        }
//        if (isAlert) {
//            alertThread = AlertThread.getInstance(this);
//            if (!alertThread.isAlive()) alertThread.start();
//        } else {
//            AlertThread.isAlert = false;
//        }
//
//
//        //electric = bytes[15] << 8 | bytes[16];
//        handler.obtainMessage(ShowTransmissionCode, dataText.toString()).sendToTarget();
////        createNotification("温度 = " + temptrue + "\n湿度 = " + humidity
////                + "\n火焰 = " + fire + "\n下雨 = " + rain + "\n烟雾 = " + smog + "\n距离 = " + distance + "\n");
//
//    }
//
//    private void createNotification(String contentText) {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent();
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        Notification notification = new NotificationCompat.Builder(this).setContentText(contentText).setSmallIcon(R.drawable.icon).build();
//        notification.when = System.currentTimeMillis();  //设置通知显示的时间
//        notification.defaults = notification.DEFAULT_SOUND;
//        notificationManager.notify(new Random().nextInt(10), notification);
//    }
//
//
//    /***********************************************************************************************************/
//    /**
//     * 初始化菜单并加载
//     * 设置监听事件
//     */
//    /***********************************************************************************************************/
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.rocker_setting, menu);
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
//            case R.id.startAlert:
//                if (!AlertThread.aswitch) {
//                    AlertThread.aswitch = true;
//                    Toast.makeText(this, "开启警报系统", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "警报系统已开启,不需要再次开启", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.closeAlert:
//                if (AlertThread.aswitch) {
//                    AlertThread.aswitch = false;
//                    Toast.makeText(this, "关闭警报系统", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "警报系统已关闭,不需要再次关闭", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//        }
//        return true;
//    }
//
//    private void showDataPopupWindow() {
//
//        View contentView = LayoutInflater.from(this).inflate(R.layout.devices, null);
//        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
//        popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, 1000, true);
//
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.water));
//
//        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
//        TextView textView = (TextView) contentView.findViewById(R.id.closeListWindow);
//        textView.setText("当前侦测数据:");
//
//
//        ListView listView = (ListView) contentView.findViewById(R.id.listView);
//
//        dataAdapter = new DataAdapter(RockerActivity.this, R.layout.data_item, dataList);
//        listView.setAdapter(dataAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Data data = dataList.get(i);
//                Toast.makeText(RockerActivity.this, data.getName() + " = " + data.getValue(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    public class UpdataThread extends Thread {
//
//        @Override
//        public void run() {
//            super.run();
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
//
//    public void dismiss(View v) {
//        popupWindow.dismiss();
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
//            finish();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        AlertThread.isAlert = false;
//        isRun = false;
//        super.onDestroy();
//    }
//
//
}



