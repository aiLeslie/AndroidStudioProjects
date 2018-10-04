package com.example.administrator.robredpacket;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   public void start(View v){
       Intent intent = new Intent();
       intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
       startActivity(intent);
   }
}
