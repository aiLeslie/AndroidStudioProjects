package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jean.rakvideotest.R;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceStep2 extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceStep2Back;
    private Button _addDeviceStep2Next;
    private ImageView _addDeviceStep2Img;
    private String _password="";
    private String _device="";
    private static AddDeviceStep2 _self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_step2);
        _self = this;
        _addDeviceStep2Back=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_step2_back);
        _addDeviceStep2Back.setOnClickListener(_addDeviceStep2Back_Click);
        _addDeviceStep2Next=(Button)findViewById(R.id.add_device_step2_next);
        _addDeviceStep2Next.setOnClickListener(_addDeviceStep2Next_Click);
        _addDeviceStep2Img=(ImageView)findViewById(R.id.add_device_step2_img);

        Intent intent=getIntent();
        _password=intent.getStringExtra("psk");
        _device=intent.getStringExtra("device");
        if(_device.equals(getApplication().getString(R.string.add_device_step1_device1))){
            _addDeviceStep2Img.setImageResource(R.drawable.add_image1);
        }else{
            _addDeviceStep2Img.setImageResource(R.drawable.add_image2);
        }
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
     *  Back
     */
    View.OnClickListener _addDeviceStep2Back_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Next
     */
    View.OnClickListener _addDeviceStep2Next_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("psk",_password);
            intent.putExtra("type",0);
            Log.i("_password==>", _password);
            intent.setClass(AddDeviceStep2.this,AddDeviceStep3.class);
            startActivity(intent);
        }
    };

    /**
     *  Self
     */
    public static AddDeviceStep2 self() {
        return _self;
    }
}


