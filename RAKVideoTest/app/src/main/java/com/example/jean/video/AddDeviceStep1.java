package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;
import com.example.jean.component.DeviceEntity;
import com.example.jean.component.NetworkStateReceiver;

import paintss.common.Toast;
import paintss.util.NetworkUtil;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceStep1 extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceStep1Back;
    private EditText _addDeviceStep1Ssid;
    private EditText _addDeviceStep1Psk;
    private ImageView _addDeviceStep1ShowPsk;
    private Button _addDeviceStep1Next;
    private static AddDeviceStep1 _self;
    private int _networkState = 0;
    private String _ssid="";
    private String _password="";
    private TextView _addDeviceStep1Device1;
    private TextView _addDeviceStep1Device2;
    private ImageView _addDeviceStep1DeviceSelect;
    private LinearLayout _addDeviceStep1Devices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_step1);
        _self = this;
        _addDeviceStep1Back=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_step1_back);
        _addDeviceStep1Back.setOnClickListener(_addDeviceStep1Back_Click);
        _addDeviceStep1Ssid=(EditText)findViewById(R.id.add_device_step1_ssid);
        _addDeviceStep1Psk=(EditText)findViewById(R.id.add_device_step1_psk);
        _addDeviceStep1ShowPsk=(ImageView)findViewById(R.id.add_device_step1_showpsk);
        _addDeviceStep1ShowPsk.setOnClickListener(_addDeviceStep1ShowPsk_Click);
        _addDeviceStep1Next=(Button)findViewById(R.id.add_device_step1_next);
        _addDeviceStep1Next.setOnClickListener(_addDeviceStep1Next_Click);
        _addDeviceStep1Device1=(TextView)findViewById(R.id.add_device_step1_device1);
        _addDeviceStep1Device1.setOnClickListener(_addDeviceStep1Device1_Click);
        _addDeviceStep1Device1.setVisibility(View.GONE);
        _addDeviceStep1Device2=(TextView)findViewById(R.id.add_device_step1_device2);
        _addDeviceStep1DeviceSelect=(ImageView)findViewById(R.id.add_device_step1_select);
        _addDeviceStep1Devices=(LinearLayout)findViewById(R.id.add_device_step1_devices);
        _addDeviceStep1Devices.setOnClickListener(_addDeviceStep1Devices_Click);

        _addDeviceStep1Ssid.setText(NetworkUtil.getSsid(this));
        _addDeviceStep1Psk.requestFocus();
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.setOnNetworkChangedListener(new NetworkStateReceiver.OnNetworkChangedListener() {
            @Override
            public void onChanged(int state) {
                _networkState = state;
                switch (state) {
                    case 1:
                        _ssid = NetworkUtil.getSsid(AddDeviceStep1.this);
                        _addDeviceStep1Ssid.setText(_ssid);
                        break;
                    case -1:
                        _addDeviceStep1Ssid.setText(getResources().getString(R.string.add_device_step1_ssid_error));
                        Toast.show(getApplicationContext(), getString(R.string.add_device_step1_ssid_error));
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume(){
        //获取保存的密码
        SharedPreferences p = getSharedPreferences(_addDeviceStep1Ssid.getText().toString(), MODE_PRIVATE);
        String psk=p.getString("psk", "");
        _addDeviceStep1Psk.setText(psk);
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
     *  Select Device
     */
    View.OnClickListener _addDeviceStep1Device1_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_addDeviceStep1Device1.getText().toString().equals(getApplication().getString(R.string.add_device_step1_device1))){
                _addDeviceStep1Device1.setText(getApplication().getString(R.string.add_device_step1_device2));
                _addDeviceStep1Device2.setText(getApplication().getString(R.string.add_device_step1_device1));
            }
            else{
                _addDeviceStep1Device1.setText(getApplication().getString(R.string.add_device_step1_device1));
                _addDeviceStep1Device2.setText(getApplication().getString(R.string.add_device_step1_device2));
            }
            _addDeviceStep1Device1.setVisibility(View.GONE);
            _addDeviceStep1DeviceSelect.setImageResource(R.drawable.up);
        }
    };

    /**
     *  Show Select Device
     */
    View.OnClickListener _addDeviceStep1Devices_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_addDeviceStep1Device1.getVisibility()==View.VISIBLE){
                _addDeviceStep1Device1.setVisibility(View.GONE);
                _addDeviceStep1DeviceSelect.setImageResource(R.drawable.up);
            }else{
                _addDeviceStep1Device1.setVisibility(View.VISIBLE);
                _addDeviceStep1DeviceSelect.setImageResource(R.drawable.down);
            }
        }
    };

    /**
     *  Back
     */
    View.OnClickListener _addDeviceStep1Back_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Show password
     */
    private  boolean psk_open=false;
    View.OnClickListener _addDeviceStep1ShowPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open=!psk_open;
            if(psk_open)
            {
                _addDeviceStep1Psk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _addDeviceStep1ShowPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _addDeviceStep1Psk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _addDeviceStep1ShowPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Next
     */
    View.OnClickListener _addDeviceStep1Next_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _password=_addDeviceStep1Psk.getText().toString();
            if (_networkState == -1) {
                Toast.show(getApplicationContext(), getString(R.string.add_device_step1_net_error));
                return;
            }
            if (!DeviceEntity.checkRouteName(_addDeviceStep1Ssid.getText().toString()) || !DeviceEntity.checkRoutePassword(_password)) {
                Toast.show(getApplicationContext(), getString(R.string.add_device_step1_ssid_error));
                return;
            }
            SharedPreferences.Editor editor = getSharedPreferences(_addDeviceStep1Ssid.getText().toString(), MODE_PRIVATE).edit();
            editor.putString("psk", _password);
            editor.commit();
            Intent intent = new Intent();
            intent.putExtra("psk", _password);
            intent.putExtra("device", _addDeviceStep1Device2.getText().toString());
            intent.setClass(AddDeviceStep1.this, AddDeviceStep2.class);
            startActivity(intent);
        }
    };

    /**
     *  Self
     */
    public static AddDeviceStep1 self() {
        return _self;
    }
}


