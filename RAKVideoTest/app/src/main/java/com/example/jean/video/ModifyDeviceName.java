package com.example.jean.video;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;
import com.example.jean.component.DeviceEntity;

import paintss.common.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class ModifyDeviceName extends Activity{
    private com.example.jean.component.MainMenuButton _modifyDeviceNameBack;
    private EditText _deviceModifyName;
    private EditText _deviceModifyPsk;
    private ImageView _deviceModifyShowpsk;
    private Button _deviceDeleteBtn;
    private Button _deviceModifyBtn;
    private String _deviceId="";
    private static ModifyDeviceName _self;
    private Dialog _deleteDeviceDialog;
    private EditText _deviceModifyId;
    private Button _deviceCopyIdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_modify);
        _self=this;
        _deleteDeviceDialog = new Dialog(this,R.style.myDialogTheme);
        _modifyDeviceNameBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.device_modify_back);
        _modifyDeviceNameBack.setOnClickListener(_modifyDeviceNameBack_Click);
        _deviceModifyName=(EditText)findViewById(R.id.device_modify_name);
        _deviceModifyPsk=(EditText)findViewById(R.id.device_modify_psk);
        _deviceDeleteBtn=(Button)findViewById(R.id.device_delete_btn);
        _deviceDeleteBtn.setOnClickListener(_deviceDeleteBtn_Click);
        _deviceModifyBtn=(Button)findViewById(R.id.device_modify_btn);
        _deviceModifyBtn.setOnClickListener(_deviceModifyBtn_Click);
        _deviceModifyShowpsk=(ImageView)findViewById(R.id.device_modify_showpsk);
        _deviceModifyShowpsk.setOnClickListener(_deviceModifyShowpsk_Click);
        _deviceModifyId=(EditText)findViewById(R.id.device_modify_id);
        _deviceCopyIdBtn=(Button)findViewById(R.id.device_copy_id_btn);
        _deviceCopyIdBtn.setOnClickListener(_deviceCopyIdBtn_Click);

        Intent intent=getIntent();
        _deviceId=intent.getStringExtra("deviceid");
        _deviceModifyId.setText(_deviceId);
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
    View.OnClickListener _modifyDeviceNameBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Delete Device
     */
    View.OnClickListener _deviceDeleteBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteDevice();
        }
    };

    /**
     * Delete Device Indicator
     */
    private void deleteDevice() {
        LayoutInflater deleteDeviceDialog_inflater =getLayoutInflater();
        View deleteDeviceDialog_admin=deleteDeviceDialog_inflater.inflate(R.layout.dialog_indicator, (ViewGroup) findViewById(R.id.dialog_indicator1));
        TextView dialog_indicator_title =(TextView)deleteDeviceDialog_admin.findViewById(R.id.dialog_indicator_title);
        TextView dialog_indicator_text =(TextView)deleteDeviceDialog_admin.findViewById(R.id.dialog_indicator_text);
        TextView dialog_indicator_cancel =(TextView)deleteDeviceDialog_admin.findViewById(R.id.dialog_indicator_cancel);
        TextView dialog_indicator_ok =(TextView)deleteDeviceDialog_admin.findViewById(R.id.dialog_indicator_ok);
        ProgressBar dialog_indicator_progress =(ProgressBar)deleteDeviceDialog_admin.findViewById(R.id.dialog_indicator_progress);
        dialog_indicator_progress.setVisibility(View.GONE);
        _deleteDeviceDialog.setCanceledOnTouchOutside(true);
        _deleteDeviceDialog.setContentView(deleteDeviceDialog_admin);
        dialog_indicator_title.setText(getApplication().getString(R.string.modify_device_delete_title));
        dialog_indicator_text.setText(R.string.modify_device_delete_text);
        dialog_indicator_ok.setText(R.string.modify_device_delete_ok);
        dialog_indicator_cancel.setText(R.string.modify_device_delete_cancel);
        dialog_indicator_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceEntity.deleteDeviceById(_self, _deviceId);
                _deleteDeviceDialog.dismiss();
                Toast.show(getApplicationContext(), getString(R.string.modify_device_success_delete));
                finish();
            }
        });
        dialog_indicator_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _deleteDeviceDialog.dismiss();
            }
        });
        _deleteDeviceDialog.show();
    }

    /**
     *  Modify Device Name
     */
    View.OnClickListener _deviceModifyBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_deviceModifyName.getText().toString().equals("")){
                Toast.show(getApplicationContext(), getString(R.string.add_device_success_name_error));
                return;
            }
            DeviceEntity.modifyDeviceNameById(_self,_deviceId,_deviceModifyName.getText().toString());
            Toast.show(getApplicationContext(), getString(R.string.modify_device_success_name)+_deviceModifyName.getText().toString());
            finish();
        }
    };

    /**
     *  Show Password
     */
    private  boolean psk_open=false;
    View.OnClickListener _deviceModifyShowpsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open=!psk_open;
            if(psk_open)
            {
                _deviceModifyPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _deviceModifyShowpsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _deviceModifyPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _deviceModifyShowpsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Copy Id
     */
    View.OnClickListener _deviceCopyIdBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            copy(_deviceModifyId.getText().toString(), _self);
            Toast.show(_self,getApplication().getString(R.string.device_modify_copy_text_success));
        }
    };


    private void copy(String content, Context context)
    {
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     *  Self
     */
    public static ModifyDeviceName self() {
        return _self;
    }
}


