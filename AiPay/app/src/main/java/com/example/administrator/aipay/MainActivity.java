package com.example.administrator.aipay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PluginManage.getInstance().setContext(this);
    }
    public void load(View view ){
        File file = new File(Environment.getExternalStorageDirectory(),"plugin.apk");
        PluginManage.getInstance().loadPath(file.getAbsolutePath());
    }
    public void click(View view ){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,ProxyActivity.class);
        intent.putExtra("className",PluginManage.getInstance().entryAcivityName);
        startActivity(intent);
    }

}
