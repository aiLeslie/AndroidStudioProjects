package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.jean.rakvideotest.R;
import com.example.jean.component.DeviceEntity;
import com.demo.sdk.Lx520;

import paintss.common.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceSuccess extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceSuccessBack;
    private EditText _addDeviceSuccessName;
    private EditText _addDeviceSuccessPsk;
    private EditText _addDeviceSuccessConfirmPsk;
    private ImageView _addDeviceSuccessShowPsk;
    private ImageView _addDeviceSuccessShowConfirmPsk;
    private Button _addDeviceSuccessBtn;
    private String _deviceId="";
    private String _deviceIp="";
    private static AddDeviceSuccess _self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_success);
        _self=this;
        _addDeviceSuccessBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_success_back);
        _addDeviceSuccessBack.setOnClickListener(_addDeviceSuccessBack_Click);
        _addDeviceSuccessName=(EditText)findViewById(R.id.add_device_success_name);
        _addDeviceSuccessPsk=(EditText)findViewById(R.id.add_device_success_psk);
        _addDeviceSuccessConfirmPsk=(EditText)findViewById(R.id.add_device_success_confirm_psk);
        _addDeviceSuccessShowPsk=(ImageView)findViewById(R.id.add_device_success_showpsk);
        _addDeviceSuccessShowPsk.setOnClickListener(_addDeviceSuccessShowPsk_Click);
        _addDeviceSuccessShowConfirmPsk=(ImageView)findViewById(R.id.add_device_success_showconfirmpsk);
        _addDeviceSuccessShowConfirmPsk.setOnClickListener(_addDeviceSuccessShowConfirmPsk_Click);
        _addDeviceSuccessBtn=(Button)findViewById(R.id.add_device_success_btn);
        _addDeviceSuccessBtn.setOnClickListener(_addDeviceSuccessBtn_Click);
        Intent intent = getIntent();
        _deviceId=intent.getStringExtra("id");
        _deviceIp=intent.getStringExtra("ip");
    }


    /**
     *  Show password
     */
    private  boolean psk_open=false;
    View.OnClickListener _addDeviceSuccessShowPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open=!psk_open;
            if(psk_open)
            {
                _addDeviceSuccessPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _addDeviceSuccessShowPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _addDeviceSuccessPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _addDeviceSuccessShowPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Show confirm password
     */
    private  boolean psk_open1=false;
    View.OnClickListener _addDeviceSuccessShowConfirmPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open1=!psk_open1;
            if(psk_open1)
            {
                _addDeviceSuccessConfirmPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _addDeviceSuccessShowConfirmPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _addDeviceSuccessConfirmPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _addDeviceSuccessShowConfirmPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Add Device Success
     */

    View.OnClickListener _addDeviceSuccessBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_addDeviceSuccessName.getText().toString().equals("")){
                Toast.show(getApplicationContext(), getString(R.string.add_device_success_name_error));
                return;
            }
            //Save Device
            DeviceEntity.saveDevicesById(_self, _deviceId, _addDeviceSuccessName.getText().toString(), _deviceIp);
            Toast.show(getApplicationContext(), getString(R.string.add_device_success_show));
            CloseActivity();
            finish();

//            if(_addDeviceSuccessPsk.getText().toString().equals("")){
//                Toast.show(getApplicationContext(), getString(R.string.add_device_success_psk_error));
//                return;
//            }
//            if(_addDeviceSuccessConfirmPsk.getText().toString().equals("")){
//                Toast.show(getApplicationContext(), getString(R.string.add_device_success_confirm_error));
//                return;
//            }
//            if(_addDeviceSuccessConfirmPsk.getText().toString().equals(_addDeviceSuccessPsk.getText().toString())==false){
//                Toast.show(getApplicationContext(), getString(R.string.add_device_success_psk_different));
//                return;
//            }

//            Lx520 lx520 = new Lx520(_deviceIp+":"+80, _addDeviceSuccessPsk.getText().toString());
//            lx520.setOnResultListener(new Lx520.OnResultListener()
//            {
//                @Override
//                public void onResult(Lx520.Response result)
//                {
//                    if (result.type==13) {
//                        if (result.statusCode == 200) {
//                            String password=DeviceEntity.Find_Str(result.body, DeviceEntity._passwordKey);
//                            if(password.equals(_addDeviceSuccessPsk.getText().toString())){
//                                //Save Device
//                                DeviceEntity.saveDevicesById(_self, _deviceId, _addDeviceSuccessName.getText().toString(), _deviceIp);
//                                Toast.show(getApplicationContext(), getString(R.string.add_device_success_show));
//                                CloseActivity();
//                                finish();
//                            }else {
//                                Toast.show(getApplicationContext(), getString(R.string.add_device_failed_show));
//                            }
//                        } else {
//                            Toast.show(getApplicationContext(), getString(R.string.add_device_failed_show));
//                        }
//                    }
//                }
//            });
//            lx520.Get_Password();
        }
    };


    /**
     *  Back
     */
    View.OnClickListener _addDeviceSuccessBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CloseActivity();
            finish();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        CloseActivity();
        finish();
    }

    /**
     *  Close Activity
     */
    void CloseActivity(){
        AddDeviceStep0 addDeviceStep0  = AddDeviceStep0.self();
        if (addDeviceStep0 != null)
            AddDeviceStep0.self().finish();

        AddDeviceStep01 addDeviceStep01  = AddDeviceStep01.self();
        if (addDeviceStep01 != null)
            AddDeviceStep01.self().finish();

        AddDeviceStep1 addDeviceStep1  = AddDeviceStep1.self();
        if (addDeviceStep1 != null)
            AddDeviceStep1.self().finish();

        AddDeviceStep2 addDeviceStep2  = AddDeviceStep2.self();
        if (addDeviceStep2 != null)
            addDeviceStep2.self().finish();

        AddDeviceStep1AP addDeviceStep1Ap  = AddDeviceStep1AP.self();
        if (addDeviceStep1Ap != null)
            addDeviceStep1Ap.self().finish();

        AddDeviceStep2AP addDeviceStep2Ap  = AddDeviceStep2AP.self();
        if (addDeviceStep2Ap != null)
            addDeviceStep2Ap.self().finish();

        AddDeviceStep3AP addDeviceStep3Ap  = AddDeviceStep3AP.self();
        if (addDeviceStep3Ap != null)
            addDeviceStep3Ap.self().finish();

        AddDeviceStep3 addDeviceStep3  = AddDeviceStep3.self();
        if (addDeviceStep3 != null)
            addDeviceStep3.self().finish();
    }

    /**
     *  Self
     */
    public static AddDeviceSuccess self() {
        return _self;
    }
}


