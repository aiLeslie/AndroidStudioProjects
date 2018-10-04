package com.example.administrator.networkreciverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private NetWorkChangeReceiver netWorkChangeReceiver;
    private int count = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new  IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver((netWorkChangeReceiver = new NetWorkChangeReceiver()), filter);
    }
    public  class NetWorkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();

            count++;
            if(count != 0){
                Toast.makeText(MainActivity.this, "network changs ~ 你的网络发生变化 ~", Toast.LENGTH_SHORT).show();
                ((TextView)findViewById(R.id.textView)).setText("network changs\n ~ 你的网络发生变化 ~\n" + "count = " +count  );
            }
            if(info.isAvailable() && info != null){
                ((TextView)findViewById(R.id.textView)).setText(((TextView)findViewById(R.id.textView)).getText() + "\nnetwork is available");

            }else{
                ((TextView)findViewById(R.id.textView)).setText(((TextView)findViewById(R.id.textView)).getText() + "\nnetwork is unavailable");
            }

        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(netWorkChangeReceiver);
        super.onDestroy();
    }
}
