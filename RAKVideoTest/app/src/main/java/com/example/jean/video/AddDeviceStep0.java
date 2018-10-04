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
public class AddDeviceStep0 extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceStep0Back;
    private static AddDeviceStep0 _self;
    private LinearLayout _addDeviceStep0Local;
    private LinearLayout _addDeviceStep0Remote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_step0);
        _self = this;
        _addDeviceStep0Back=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_step0_back);
        _addDeviceStep0Back.setOnClickListener(_addDeviceStep0Back_Click);
        _addDeviceStep0Local=(LinearLayout)findViewById(R.id.add_device_step0_local);
        _addDeviceStep0Local.setOnClickListener(_addDeviceStep0Local_Click);
        _addDeviceStep0Remote=(LinearLayout)findViewById(R.id.add_device_step0_remote);
        _addDeviceStep0Remote.setOnClickListener(_addDeviceStep0Remote_Click);
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
     *  Local Add
     */
    View.OnClickListener _addDeviceStep0Local_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(AddDeviceStep0.this,AddDeviceStep01.class);
            startActivity(intent);
        }
    };

    /**
     *  Remote Add
     */
    View.OnClickListener _addDeviceStep0Remote_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(AddDeviceStep0.this,AddDeviceRemote.class);
            startActivity(intent);
        }
    };
    /**
     *  Back
     */
    View.OnClickListener _addDeviceStep0Back_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    /**
     *  Self
     */
    public static AddDeviceStep0 self() {
        return _self;
    }
}


