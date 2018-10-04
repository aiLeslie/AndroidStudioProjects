package com.example.networkstatus;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManger.getActiveNetworkInfo();
        NetworkInfo.State state = activeNetworkInfo.getState();
        TextView textView = (TextView) findViewById(R.id.textView);
        if (activeNetworkInfo == null){
            Toast.makeText(this, "这里没有网络信号~", Toast.LENGTH_SHORT).show();
        }
        if(state.equals(NetworkInfo.State.CONNECTED)){

        }else if(state.equals(NetworkInfo.State.CONNECTING)){

        }else if(state.equals(NetworkInfo.State.DISCONNECTED)){

        }else if(state.equals(NetworkInfo.State.DISCONNECTING)){

        }
        textView.setText(state.toString());



    }
}
