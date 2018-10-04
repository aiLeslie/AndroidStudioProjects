package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceFailed extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceFailedBack;
    private static AddDeviceFailed _self;
    private int type=0;
    private TextView DeviceConnectFailedText;
    private Button DeviceConnectFailedAp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_failed);

        _addDeviceFailedBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_failed_back);
        _addDeviceFailedBack.setOnClickListener(_addDeviceFailedBack_Click);
        DeviceConnectFailedText=(TextView)findViewById(R.id.add_device_failed_text);
        DeviceConnectFailedAp=(Button)findViewById(R.id.add_device_failed_ap);
        DeviceConnectFailedAp.setOnClickListener(_deviceConnectFailedAp_Click);
        Intent intent=getIntent();
        type=intent.getIntExtra("type",0);
        if(type==0){
            DeviceConnectFailedText.setText(getApplication().getString(R.string.add_device_failed));
            DeviceConnectFailedAp.setVisibility(View.VISIBLE);
        }
        else{
            if(type==1)
                DeviceConnectFailedText.setText(getApplication().getString(R.string.add_device_failed1));
            else if(type==2)
                DeviceConnectFailedText.setText(getApplication().getString(R.string.add_device_failed2));
            else if(type==3)
                DeviceConnectFailedText.setText(getApplication().getString(R.string.add_device_failed3));
            DeviceConnectFailedAp.setVisibility(View.INVISIBLE);
        }
        Log.e("type==>", type + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        CloseActivity();
    }
    /**
     *  Back
     */
    View.OnClickListener _addDeviceFailedBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CloseActivity();
        }
    };

    /**
     *  Close Activity
     */
    void CloseActivity(){
        if(type==0){
            AddDeviceStep3 addDeviceStep3  = AddDeviceStep3.self();
            if (addDeviceStep3 != null)
                addDeviceStep3.self().finish();
            finish();
        }
        else{
            AddDeviceStep2AP addDeviceStep2Ap  = AddDeviceStep2AP.self();
            if (addDeviceStep2Ap != null)
                addDeviceStep2Ap.self().finish();
            AddDeviceStep3AP addDeviceStep3Ap  = AddDeviceStep3AP.self();
            if (addDeviceStep3Ap != null)
                addDeviceStep3Ap.self().finish();
            AddDeviceStep3 addDeviceStep3  = AddDeviceStep3.self();
            if (addDeviceStep3 != null)
                addDeviceStep3.self().finish();
            finish();
        }
    }

    /**
     *  Goto AP Config
     */
    View.OnClickListener _deviceConnectFailedAp_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddDeviceStep1 addDeviceStep1  = AddDeviceStep1.self();
            if (addDeviceStep1 != null)
                AddDeviceStep1.self().finish();

            AddDeviceStep2 addDeviceStep2  = AddDeviceStep2.self();
            if (addDeviceStep2 != null)
                addDeviceStep2.self().finish();

            AddDeviceStep3 addDeviceStep3  = AddDeviceStep3.self();
            if (addDeviceStep3 != null)
                addDeviceStep3.self().finish();
            Intent intent=new Intent();
            intent.setClass(AddDeviceFailed.this,AddDeviceStep1AP.class);
            startActivity(intent);
            finish();
        }
    };

    /**
     *  Self
     */
    public static AddDeviceFailed self() {
        return _self;
    }
}


