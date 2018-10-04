package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.jean.rakvideotest.R;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceStep01 extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceStep01Back;
    private static AddDeviceStep01 _self;
    private LinearLayout _addDeviceStep01Easy;
    private LinearLayout _addDeviceStep01AP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_step01);
        _self = this;
        _addDeviceStep01Back=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_step01_back);
        _addDeviceStep01Back.setOnClickListener(_addDeviceStep01Back_Click);
        _addDeviceStep01Easy=(LinearLayout)findViewById(R.id.add_device_step01_easy);
        _addDeviceStep01Easy.setOnClickListener(_addDeviceStep01Easy_Click);
        _addDeviceStep01AP=(LinearLayout)findViewById(R.id.add_device_step01_ap);
        _addDeviceStep01AP.setOnClickListener(_addDeviceStep01AP_Click);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    /**
     *  Easy Add
     */
    View.OnClickListener _addDeviceStep01Easy_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(AddDeviceStep01.this,AddDeviceStep1.class);
            startActivity(intent);
        }
    };

    /**
     *  Ap Add
     */
    View.OnClickListener _addDeviceStep01AP_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(AddDeviceStep01.this,AddDeviceStep1AP.class);
            startActivity(intent);
        }
    };
    /**
     *  Back
     */
    View.OnClickListener _addDeviceStep01Back_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    /**
     *  Self
     */
    public static AddDeviceStep01 self() {
        return _self;
    }
}


