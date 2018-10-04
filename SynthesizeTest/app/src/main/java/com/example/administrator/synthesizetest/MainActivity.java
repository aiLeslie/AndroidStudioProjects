package com.example.administrator.synthesizetest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private View contentView = null;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                default:
                    Toast.makeText(MainActivity.this, "received in my broadcast", Toast.LENGTH_SHORT).show();
                case Intent.ACTION_BATTERY_CHANGED:
                    try {
                        if (contentView != null )
                        {




                            TextView TextViewtvBatter = (TextView) contentView.findViewById(R.id.showInfo);
                            if(TextViewtvBatter == null)return;
                            ProgressBar bar = (ProgressBar) contentView.findViewById(R.id.progressBar);
                            TextView textViewSatus = (TextView) contentView.findViewById(R.id.textViewSatus);

                            int status = intent.getIntExtra("status", 0);

                            //得到将康状态
                            int health = intent.getIntExtra("health", 0);

                            //boolean类型
                            //boolean present = intent.getBooleanExtra("level", false);
                            //得到剩余电量
                            int level = intent.getIntExtra("level", 0);
                            //得到电量最大值
                            int scale = intent.getIntExtra("scale", 0);
                            //得到图标ID
                            int icon_small = intent.getIntExtra("icon-small", 0);

                            //得到电池电压
                            int voltage = intent.getIntExtra("voltage", 0);
                            //得到电池温度
                            int temperture = intent.getIntExtra("temperature", 0);
                            //得到电池类型
                            String technology = intent.getStringExtra("technology");
                            //得到电池状态
                            String statusString = "";
                            //电池充电方式
                            int plugged = intent.getIntExtra("plugged", 0);
                            //根据状态id得到状态字符串，代码自自己实现
                            switch (status) {
                                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                                    statusString = "未知状态";
                                    break;
                                case BatteryManager.BATTERY_STATUS_CHARGING:
                                    statusString = "充电状态";
                                    break;
                                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                                    statusString = "放电状态";
                                    break;
                                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                                    statusString = "未冲满";
                                    break;
                                case BatteryManager.BATTERY_STATUS_FULL:
                                    statusString = "充满电";
                                    break;
                            }
                            String healthString = null;

                            switch (health) {
                                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                                    healthString = "未知错误";
                                    break;
                                case BatteryManager.BATTERY_HEALTH_GOOD:
                                    healthString = "状态良好";
                                    break;
                                case BatteryManager.BATTERY_HEALTH_DEAD:
                                    healthString = "电池没有电";
                                    break;
                                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                                    healthString = "电池电压过高";
                                    break;
                                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                                    healthString = "电池过热";
                                    break;
                                default:
                                    healthString = health + "";
                            }
                            String pluggedString = null;

                            switch (plugged) {
                                case BatteryManager.BATTERY_PLUGGED_AC:
                                    pluggedString = "充电器充电";
                                    break;
                                case BatteryManager.BATTERY_PLUGGED_USB:
                                    pluggedString = "USB充电";
                                    break;
                                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                                    pluggedString = "无线充电";
                                    break;

                                default:
                                    pluggedString = plugged + "";
                            }


                            //显示电池信息
                            bar.setProgress(level);//设置进度

                            textViewSatus.setText("剩余电量\n" + level + "%");//设置电量
                            //让进度条和电量文本视图为可见

//                tvBatter.setText("小图标:\t\t\t" + icon_small + "\n"
//                                + "健康值:\t\t\t" + healthString + "\n"
//                                + "电池状态:\t\t" + statusString + "\n"
//                                + "充电方式:\t\t" + pluggedString + "\n"
//                                + "电池电压:\t\t" + voltage + "\n"
//                                + "电池温度:\t\t" + (temperture * 0.1) + "℃\n"
//                                + "电池的类型:\t" + technology + "\n"
//                                + "电池剩余容量:" + level + "%\n"
//                                + "电池的最大值:" + scale + "%\n");
                            TextViewtvBatter.setText("小图标:" + "\t\t" + icon_small + "\n"//设置tvBattery文本
                                    + "健康值:" + "\t\t" + healthString + "\n"
                                    + "电池状态:" + "\t\t" + statusString + "\n"
                                    + "充电方式:" + "\t\t" + pluggedString + "\n"
                                    + "电池电压:" + "\t\t" + voltage + "\n"
                                    + "电池温度:" + "\t\t" + (float) temperture * 0.1 + "℃\n"
                                    + "电池的类型:" + "\t\t" + technology + "\n"
                                    + "电池剩余容量:" + "\t\t" + level + "%\n"
                                    + "电池的最大值:" + "\t\t" + scale + "%\n");

                            //progressDialog.dismiss();

                        }
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = (Button) findViewById(R.id.buttonShowText);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.buttonShowAlertDialog);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.buttonShowStartActivityForResult);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.buttonShowTransmission);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.buttonShowReceiveBroadcast);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonNet);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.buttonShowText:
                intent = new Intent(MainActivity.this, TextActivity.class);
                startActivity(intent);
                break;

            case R.id.buttonShowAlertDialog:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("warning");
                dialog.setMessage("Are you sure you want to exit now?");
                dialog.setPositiveButton("exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                break;

            case R.id.buttonShowStartActivityForResult:
                intent = new Intent("MainActivityAction");
                startActivityForResult(intent, 0);

                break;
            case R.id.buttonShowTransmission:
                contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_start_for_result, null);
                View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
                PopupWindow mPopWindow = new PopupWindow(contentView, 900, 4 * 900 / 3, true);

                //设置各个控件的点击响应
                Button button = (Button) contentView.findViewById(R.id.buttonForResult);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        EditText text = (EditText) contentView.findViewById(R.id.editTextForResult);
                        SharedPreferences.Editor editor = getSharedPreferences("PopupData", MODE_PRIVATE).edit();
                        editor.putString("data", text.getText().toString());

                        editor.apply();
                        Toast.makeText(MainActivity.this, "transmission success", Toast.LENGTH_SHORT).show();
                    }
                });
                //显示PopupWindow

                mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        SharedPreferences sharedPreferences = getSharedPreferences("PopupData", MODE_PRIVATE);
                        String data = sharedPreferences.getString("data", "程序出错了");
                        if (data.equals("") || data == null) data = "你没有输入值哦";
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
                mPopWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
                mPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 100);
                break;

            case R.id.buttonShowReceiveBroadcast:

                contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.battery_info, null);
                rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
                mPopWindow = new PopupWindow(contentView, 900, 4 * 900 / 3, true);

                //设置各个控件的点击响应
//                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
//                progressDialog.setTitle("wait a moment");
//                progressDialog.show();

                mPopWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                intentFilter.addAction("HelloWorld");
                registerReceiver(receiver, intentFilter);
                mPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 100);

                break;
            case R.id.buttonNet:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            break;


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 0) {

                Toast.makeText(this, data.getStringExtra("dataReturn") == null || data.getStringExtra("dataReturn").equals("") ? "你没有输入值哦" : data.getStringExtra("dataReturn"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "你没有输入值哦\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.exitItem){
            System.exit(0);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
