package com.example.administrator.handledemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    Handler handler = new MyHeandler();
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int date = calendar.get(Calendar.DATE);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WorkThread().start();
//                while(true) {//时间不能显示
//                    calendar = Calendar.getInstance();
//                    year = calendar.get(Calendar.YEAR);
//                    month = calendar.get(Calendar.MONTH);
//                    date = calendar.get(Calendar.DATE);
//                    hour = calendar.get(Calendar.HOUR_OF_DAY);
//                    minute = calendar.get(Calendar.MINUTE);
//                    second = calendar.get(Calendar.SECOND);
//
//                    time = String.format("%d年%d月%d日\n%d时%d分%d秒", year, month, date, hour, minute, second);
//                    textView.setText(time);
//                }

            }
        });
    }


    class MyHeandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            textView.setText((String) msg.obj);

        }
    }

    class WorkThread extends Thread {
        @Override
        public void run() {
            try {
//                this.sleep(1000 * 3);
//            Thread.sleep(3000);

                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH) + 1;
                    date = calendar.get(Calendar.DATE);
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minute = calendar.get(Calendar.MINUTE);
                    second = calendar.get(Calendar.SECOND);

                    //time = String.format("%d年 %d月%d日\n%d时%d分\n%d秒", year, month, date, hour, minute, second);
                time = String.format("%d年 %d月%d日\n%d时%d分", year, month, date, hour, minute);

                    Message mes = handler.obtainMessage();
                    mes.obj = time;
                    handler.sendMessage(mes);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}