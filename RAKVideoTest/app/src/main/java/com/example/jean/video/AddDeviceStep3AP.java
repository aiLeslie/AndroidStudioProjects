package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jean.rakvideotest.R;

import paintss.common.Toast;

/**
 * Created by Jean on 2016/3/25.
 */
public class AddDeviceStep3AP extends Activity{
    private WLANAPI mWifiAdmin;//wifi API函数
    private static AddDeviceStep3AP _self;
    private String _deviceid="";
    private String _ssid="";
    private com.example.jean.component.MainMenuButton _addDeviceStep3APBack;
    private Button _addDeviceStep3APNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_step3_ap);
        _self = this;

        _addDeviceStep3APBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_step3_ap_back);
        _addDeviceStep3APBack.setOnClickListener(_addDeviceStep3APBack_Click);
        _addDeviceStep3APNext=(Button)findViewById(R.id.add_device_step3_ap_next);
        _addDeviceStep3APNext.setOnClickListener(_addDeviceStep3APNext_Click);

        Intent intent=getIntent();
        _deviceid=intent.getStringExtra("device_id");
        _ssid=intent.getStringExtra("ssid");
    }

    /**
     *  Back
     */
    View.OnClickListener _addDeviceStep3APBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddDeviceStep2AP addDeviceStep2Ap  = AddDeviceStep2AP.self();
            if (addDeviceStep2Ap != null)
                addDeviceStep2Ap.self().finish();
            finish();
        }
    };

    /**
     * Next
     */
    View.OnClickListener _addDeviceStep3APNext_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mWifiAdmin = new WLANAPI(getApplicationContext());
            if(mWifiAdmin.getSSID().toString().contains(_ssid)){
                Intent intent = new Intent();
                intent.putExtra("type",1);
                intent.putExtra("device_id", _deviceid);
                intent.setClass(AddDeviceStep3AP.this, AddDeviceStep3.class);
                startActivity(intent);
            }
            else{
                Toast.show(getApplicationContext(), getString(R.string.add_device_step3_ap_error));
            }
        }
    };

    /**
     *  Self
     */
    public static AddDeviceStep3AP self() {
        return _self;
    }

}
