package com.example.jean.video;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jean.rakvideotest.R;
import com.example.jean.component.DeviceEntity;

import paintss.common.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceRemote extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceRemoteBack;
    private static AddDeviceRemote _self;
    private Button _addDeviceRemote;
    private EditText _addDeviceRemoteId;
    private EditText _addDeviceRemoteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_remote);
        _self = this;
        _addDeviceRemoteBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_remote_back);
        _addDeviceRemoteBack.setOnClickListener(_addDeviceRemoteBack_Click);
        _addDeviceRemoteId=(EditText)findViewById(R.id.add_device_remote_id);
        _addDeviceRemoteName=(EditText)findViewById(R.id.add_device_remote_name);
        _addDeviceRemote=(Button)findViewById(R.id.add_device_remote);
        _addDeviceRemote.setOnClickListener(_addDeviceRemote_Click);
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
     *  Back
     */
    View.OnClickListener _addDeviceRemoteBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Add Remote
     */
    View.OnClickListener _addDeviceRemote_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_addDeviceRemoteId.getText().toString().equals("")){
                Toast.show(_self,getApplication().getString(R.string.add_device_remote_id_error));
                return;
            }
            if(_addDeviceRemoteName.getText().toString().equals("")){
                Toast.show(_self,getApplication().getString(R.string.add_device_remote_name_error));
                return;
            }
            DeviceEntity.saveDevicesById(_self,_addDeviceRemoteId.getText().toString(),_addDeviceRemoteName.getText().toString(),"127.0.0.1");
            Toast.show(_self, getApplication().getString(R.string.add_device_remote_success));
            AddDeviceStep0 addDeviceStep0  = AddDeviceStep0.self();
            if (addDeviceStep0 != null)
                AddDeviceStep0.self().finish();
            finish();
        }
    };

    /**
     *  Self
     */
    public static AddDeviceRemote self() {
        return _self;
    }
}


