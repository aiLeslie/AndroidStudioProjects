package com.example.administrator.battetymyimplements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn_battery;
    private TextView tvBatter;
    private ProgressBar bar;
    private TextView textViewSatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //得到布局中的所有的对象
        findView();
        bar.setVisibility(View.GONE);
        textViewSatus.setVisibility(View.GONE);


        //设置对象的监听器
        setListener();
    }
    View.OnClickListener listener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button:
                    //注册广播接收器
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(Intent.ACTION_BATTERY_CHANGED);
                    registerReceiver(mBroadcastReceiver,filter);
                    break;
            }
        }
    };
    //声明广播接受着对象
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                //得到电池状态

//                BatteryManager.BATTERY_STATUS_CHARGING//充电状态
//                BatteryManager.BATTERY_STATUS_DISCHARGING//放电状态
//                BatteryManager.BATTERY_STATUS_NOT_CHARGING//没充满
//                BatteryManager.BATTERY_STATUS_UNKNOWN//未知状态
//                BatteryManager.BATTERY_STATUS_FULL//充满电
                int status = intent.getIntExtra("status", 0);

                //得到将康状态
                int health = intent.getIntExtra("health", 0);

                //boolean类型
                boolean present = intent.getBooleanExtra("level", false);
                //得到剩余电量
                int level = intent.getIntExtra("level", 0);
                //得到电量最大值
                int scale = intent.getIntExtra("scale", 0);
                //得到图标ID
                int icon_small = intent.getIntExtra("icon-small", 0);

                //得到电池电压
                int voltage = intent.getIntExtra("voltage", 0);
                //得到电池温度
                int temperture = intent.getIntExtra("temperture", 0);
                //得到电池类型
                String technology = intent.getStringExtra("technology");
                //得到电池状态
                String statusString = "";
                //电池充电方式
                int plugged = intent.getIntExtra("plugged", 0);
                //根据状态id得到状态字符串，代码自自己实现
                switch (status){
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

                switch (health)
                {
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
                        healthString =  "电池过热";
                        break;
                    default:
                        healthString = health + "";
                }
                String pluggedString = null;

                switch (plugged)
                {
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
                bar.setProgress(level);

                textViewSatus.setText("剩余电量\n" + level + "%");
                bar.setVisibility(View.VISIBLE);
                textViewSatus.setVisibility(View.VISIBLE);
//                tvBatter.setText("小图标:\t\t\t" + icon_small + "\n"
//                                + "健康值:\t\t\t" + healthString + "\n"
//                                + "电池状态:\t\t" + statusString + "\n"
//                                + "充电方式:\t\t" + pluggedString + "\n"
//                                + "电池电压:\t\t" + voltage + "\n"
//                                + "电池温度:\t\t" + (temperture * 0.1) + "℃\n"
//                                + "电池的类型:\t" + technology + "\n"
//                                + "电池剩余容量:" + level + "%\n"
//                                + "电池的最大值:" + scale + "%\n");
                tvBatter.setText("小图标:" + "\t\t" + icon_small + "\n"
                        + "健康值:" + "\t\t"  + healthString + "\n"
                        + "电池状态:" + "\t\t" + statusString + "\n"
                        + "充电方式:" + "\t\t" + pluggedString + "\n"
                        + "电池电压:" + "\t\t" + voltage + "\n"
                        + "电池温度:" + "\t\t" + (float)temperture * 0.1 + "℃\n"
                        + "电池的类型:" + "\t\t" + technology + "\n"
                        + "电池剩余容量:" + "\t\t" + level + "%\n"
                        + "电池的最大值:" + "\t\t" + scale + "%\n");

            }
        }
    };


    private void setListener() {
        btn_battery.setOnClickListener(listener);
    }

    private void findView() {
        btn_battery = (Button)findViewById(R.id.button);
        tvBatter = (TextView)findViewById(R.id.textView);
        bar = (ProgressBar)findViewById(R.id.progressBar);
        textViewSatus = (TextView)findViewById(R.id.textViewSatus);
    }




}
