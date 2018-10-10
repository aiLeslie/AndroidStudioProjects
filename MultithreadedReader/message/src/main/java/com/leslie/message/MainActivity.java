package com.leslie.message;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.pm.PackageInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leslie.message.msgutil.Msg;
import com.leslie.message.msgutil.MsgClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermission()) {
            sendSMS();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},0);
            }
        }



    }

    private void sendSMS() {
        MsgClient client = new MsgClient(this);

        Msg msg = new Msg.Builder().content("hello").phoneNum("13672980012").build();

        client.sendMsg(msg);
    }

    private boolean checkPermission() {
       return ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }
}
