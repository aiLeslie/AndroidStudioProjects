package com.example.sendorderbroadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private LoacalReceiver receiver;
    private LocalBroadcastManager  localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);
        Button button = (Button) findViewById(R.id.buttonSendLocalBroadcast);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("LocalBroadcast");
                intent.putExtra("key","My name is localBroadcast");
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LocalBroadcast");
        receiver = new LoacalReceiver();
        if(localBroadcastManager != null)localBroadcastManager.registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(localBroadcastManager != null && receiver != null)localBroadcastManager.unregisterReceiver(receiver);
    }

    public class LoacalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, intent.getStringExtra("key"), Toast.LENGTH_SHORT).show();

        }
    }
}
