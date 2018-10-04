package com.example.administrator.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyService.MyBinder binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.MyBinder) service;
            binder.startDownload();
            Toast.makeText(MainActivity.this, binder.getProgress() + "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.buttonStartSevice);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonDestroyService);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonBindSevice);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonUnbindService);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.butonGetProgress);
        button.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, MyService.class);
        switch (v.getId()) {
            case R.id.buttonStartSevice:
                startService(intent);
                Toast.makeText(this, "start Service", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonDestroyService:
                stopService(intent);
                Toast.makeText(this, "destroy Service", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonBindSevice:
                bindService(intent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.buttonUnbindService:
                if (connection == null){
                    Toast.makeText(this, "活动没有与任务绑定,不需要解绑!", Toast.LENGTH_SHORT).show();
                    return;
                }
                unbindService(connection);
                break;
            case R.id.butonGetProgress:
                if (binder != null){
                    Toast.makeText(this, binder.getProgress() + "", Toast.LENGTH_SHORT).show();
                    Log.i("Progress", "getProgress" + binder.getProgress());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,MyService.class));
        unbindService(connection);
        super.onDestroy();
    }
}
