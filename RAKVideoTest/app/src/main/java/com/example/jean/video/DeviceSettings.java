package com.example.jean.video;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;
import com.example.jean.component.DeviceEntity;
import com.demo.sdk.Lx520;
import com.nabto.api.RemoteTunnel;

import paintss.common.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class DeviceSettings extends Activity{
    private com.example.jean.component.MainMenuButton _videoSettingsBack;
    private TextView _appVersion;
    private TextView _fwVersion;
    private EditText _videoSettingsPsk;
    private EditText _videoSettingsConfirmPsk;
    private ImageView _videoSettingsShowPsk;
    private ImageView _videoSettingsShowConfirmPsk;
    private Button _videoSettingsBtn;
    private LinearLayout _videoSettingsParameter;
    private EditText _videoSettingsPipe;
    private ImageView _videoSettingsPipeBtn;
    private EditText _videoSettingsFps;
    private EditText _videoSettingsQuality;
    private EditText _videoSettingsGop;
    private Button _videoSettingsParameterBtn;
    private static DeviceSettings _self;

    private String _deviceName="";
    private String _deviceId="";
    private String _deviceIp="";
    private String _devicePsk="";
    private int _voicePort=80;
    private int _fps=20;
    private Dialog _modifyProgressDialog;
    private String _version="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_settings);
        _self = this;
        _modifyProgressDialog = new Dialog(this,R.style.myDialogTheme);
        _videoSettingsBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_settings_back);
        _videoSettingsBack.setOnClickListener(_videoSettingsBack_Click);
        _appVersion=(TextView)findViewById(R.id.video_settings_app_version);
        _fwVersion=(TextView)findViewById(R.id.video_settings_fw_version);
        _videoSettingsPsk=(EditText)findViewById(R.id.video_settings_psk);
        _videoSettingsConfirmPsk=(EditText)findViewById(R.id.video_settings_confirm_psk);
        _videoSettingsShowPsk=(ImageView)findViewById(R.id.video_settings_showpsk);
        _videoSettingsShowPsk.setOnClickListener(_videoSettingsShowPsk_Click);
        _videoSettingsShowConfirmPsk=(ImageView)findViewById(R.id.video_settings_showconfirmpsk);
        _videoSettingsShowConfirmPsk.setOnClickListener(_videoSettingsShowConfirmPsk_Click);
        _videoSettingsBtn=(Button)findViewById(R.id.video_settings_btn);
        _videoSettingsBtn.setOnClickListener(_videoSettingsBtn_Click);
        _videoSettingsParameter=(LinearLayout)findViewById(R.id.video_settings_parameter);
        _videoSettingsPipe=(EditText)findViewById(R.id.video_settings_pipe);
        _videoSettingsPipeBtn=(ImageView)findViewById(R.id.video_settings_pipe_btn);
        _videoSettingsPipeBtn.setOnClickListener(_videoSettingsPipeBtn_Click);
        _videoSettingsFps=(EditText)findViewById(R.id.video_settings_fps);
        _videoSettingsQuality=(EditText)findViewById(R.id.video_settings_quality);
        _videoSettingsGop=(EditText)findViewById(R.id.video_settings_gop);
        _videoSettingsParameterBtn=(Button)findViewById(R.id.video_settings_parameter_btn);
        _videoSettingsParameterBtn.setOnClickListener(_videoSettingsParameterBtn_Click);

        Intent intent = getIntent();
        _deviceName = intent.getStringExtra("devicename");
        _deviceId = intent.getStringExtra("deviceid");
        _deviceIp = intent.getStringExtra("deviceip");
        _devicePsk = intent.getStringExtra("devicepsk");
        _voicePort=intent.getIntExtra("voicport",80);
        if(_deviceIp.equals("127.0.0.1")==false){
            _voicePort=80;
        }
        _version= intent.getStringExtra("version");
        _appVersion.setText(getVersion());
        _fwVersion.setText(_version);
        if(_version.toLowerCase().contains("wifiv")){
            _videoSettingsParameter.setVisibility(View.VISIBLE);
            getParameter();
        }
        else{
            _videoSettingsParameter.setVisibility(View.GONE);
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
    View.OnClickListener _videoSettingsBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     * Get APP Version
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "V"+version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *  Modify Password
     */
    private RemoteTunnel _remoteTunnel=null;
    View.OnClickListener _videoSettingsBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_videoSettingsPsk.getText().toString().equals("")){
                Toast.show(getApplicationContext(), getString(R.string.video_settings_psk_text));
                return;
            }
            if(_videoSettingsConfirmPsk.getText().toString().equals("")){
                Toast.show(getApplicationContext(), getString(R.string.video_settings_new_psk_text));
                return;
            }
            modifyDevicesPasswordIndicator();
            Modify_Password(_voicePort);
        }
    };

    /**
     * Modify Password Indicator
     */
    private void modifyDevicesPasswordIndicator() {
        LayoutInflater getdeviceDialog_inflater =getLayoutInflater();
        View getdeviceDialog_admin=getdeviceDialog_inflater.inflate(R.layout.dialog_indicator, (ViewGroup) findViewById(R.id.dialog_indicator1));
        TextView dialog_indicator_title =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_title);
        TextView dialog_indicator_text =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_text);
        TextView dialog_indicator_line =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_line);
        dialog_indicator_line.setVisibility(View.GONE);
        LinearLayout dialog_indicator_btn =(LinearLayout)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_btn);
        dialog_indicator_btn.setVisibility(View.GONE);
        _modifyProgressDialog.setCanceledOnTouchOutside(true);
        _modifyProgressDialog.setContentView(getdeviceDialog_admin);
        dialog_indicator_title.setText(getApplication().getString(R.string.device_modify_indicator_title));
        dialog_indicator_text.setText(R.string.device_modify_indicator_text);
        _modifyProgressDialog.show();
    }

    /**
     *  Modify Password
     */
   void Modify_Password(int port){
        Lx520 lx520 = new Lx520(_deviceIp+":"+port, _videoSettingsPsk.getText().toString());
        lx520.setOnResultListener(new Lx520.OnResultListener()
        {
            @Override
            public void onResult(Lx520.Response result)
            {
                if (result.type==14) {
                    if (result.statusCode == 200) {
                        DeviceEntity.modifyDevicePasswordById(_self, _deviceId, _videoSettingsConfirmPsk.getText().toString());
                        Toast.show(getApplicationContext(), getString(R.string.video_settings_new_psk_success));
                        if (_remoteTunnel != null){
                            _remoteTunnel.closeTunnels();
                            _remoteTunnel=null;
                        }
                        VideoPlay videoPlay1  = VideoPlay.self();
                        if (videoPlay1 != null)
                            videoPlay1.self().finish();
                        finish();
                    } else {
                        Toast.show(getApplicationContext(), getString(R.string.video_settings_new_psk_error));
                    }
                    if (_modifyProgressDialog != null)
                        _modifyProgressDialog.dismiss();
                }
            }
        });
        lx520.Set_Password(_videoSettingsConfirmPsk.getText().toString());
    }

    /**
     *  Show Password
     */
    private  boolean psk_open=false;
    View.OnClickListener _videoSettingsShowConfirmPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open=!psk_open;
            if(psk_open)
            {
                _videoSettingsConfirmPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _videoSettingsShowConfirmPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _videoSettingsConfirmPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _videoSettingsShowConfirmPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Show Confirm Password
     */
    private  boolean psk_open1=false;
    View.OnClickListener _videoSettingsShowPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open1=!psk_open1;
            if(psk_open1)
            {
                _videoSettingsPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _videoSettingsShowPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _videoSettingsPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _videoSettingsShowPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Choose Pipe
     */
    View.OnClickListener _videoSettingsPipeBtn_Click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            LayoutInflater getdeviceDialog_inflater =getLayoutInflater();
            View getdeviceDialog_admin=getdeviceDialog_inflater.inflate(R.layout.device_paremeter_admin, (ViewGroup) findViewById(R.id.device_parameter_admin1));
            TextView device_parameter_vhd =(TextView)getdeviceDialog_admin.findViewById(R.id.device_parameter_vhd);
            device_parameter_vhd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _videoSettingsPipe.setText(getString(R.string.device_parameter_vhd));
                    _modifyProgressDialog.dismiss();
                }
            });
            TextView device_parameter_hd =(TextView)getdeviceDialog_admin.findViewById(R.id.device_parameter_hd);
            device_parameter_hd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _videoSettingsPipe.setText(getString(R.string.device_parameter_hd));
                    _modifyProgressDialog.dismiss();
                }
            });
            TextView device_parameter_vga =(TextView)getdeviceDialog_admin.findViewById(R.id.device_parameter_vga);
            device_parameter_vga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _videoSettingsPipe.setText(getString(R.string.device_parameter_vga));
                    _modifyProgressDialog.dismiss();
                }
            });
            TextView device_parameter_l =(TextView)getdeviceDialog_admin.findViewById(R.id.device_parameter_l);
            device_parameter_l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _videoSettingsPipe.setText(getString(R.string.device_parameter_l));
                    _modifyProgressDialog.dismiss();
                }
            });

            _modifyProgressDialog.setCanceledOnTouchOutside(true);
            _modifyProgressDialog.setContentView(getdeviceDialog_admin);
            _modifyProgressDialog.show();
        }
    };

    /**
     * Get Parameter
     */
    private Lx520 lx520;
    private void getParameter() {
        lx520 = new Lx520(_deviceIp + ":" + _voicePort, _devicePsk);
        lx520.setOnResultListener(new Lx520.OnResultListener() {
            @Override
            public void onResult(Lx520.Response result) {
                if (result.type == 17) {
                    if (result.statusCode == 200) {
                        String ff = result.body.replace(" ", "");
                        String keyStr = "\"value\":\"";
                        int index = ff.indexOf(keyStr);
                        if (index != -1) {
                            int index2 = ff.indexOf("\"", index + keyStr.length());
                            if (index2 != -1) {
                                String fpsStr = ff.substring(index + keyStr.length(), index2);
                                _fps = Integer.parseInt(fpsStr);
                                _videoSettingsFps.setText(_fps+"");
                            }
                        }
                    }
                    lx520.Get_Quality();
                }

                if (result.type == 27) {
                    if (result.statusCode == 200) {
                        String ff=result.body.replace(" ","");
                        String keyStr="\"value\":\"";
                        int index=ff.indexOf(keyStr);
                        if (index!=-1){
                            int index2=ff.indexOf("\"",index+keyStr.length());
                            if(index2!=-1){
                                String Str=ff.substring(index+keyStr.length(),index2);
                                _videoSettingsQuality.setText(Str);
                            }
                        }
                    }
                    lx520.Get_GOP();
                }

                if (result.type == 29) {
                    if (result.statusCode == 200) {
                        String ff=result.body.replace(" ","");
                        String keyStr="\"value\":\"";
                        int index=ff.indexOf(keyStr);
                        if (index!=-1){
                            int index2=ff.indexOf("\"",index+keyStr.length());
                            if(index2!=-1){
                                String Str=ff.substring(index+keyStr.length(),index2);
                                _videoSettingsGop.setText(Str);
                            }
                        }
                    }
                }
            }
        });
        lx520.Get_Fps();
    }

    /**
     * Set Parameter
     */
    private void setParameter() {
        if(_videoSettingsFps.getText().toString().equals("")){
            Toast.show(DeviceSettings.this,getString(R.string.set_video_fps_empty));
            return;
        }
        if(_videoSettingsQuality.getText().toString().equals("")){
            Toast.show(DeviceSettings.this,getString(R.string.set_video_quality_empty));
            return;
        }
        if(_videoSettingsGop.getText().toString().equals("")){
            Toast.show(DeviceSettings.this,getString(R.string.set_video_gop_empty));
            return;
        }

        lx520 = new Lx520(_deviceIp + ":" + _voicePort, _devicePsk);
        lx520.setOnResultListener(new Lx520.OnResultListener() {
            @Override
            public void onResult(Lx520.Response result) {
                if (result.type == 24) {
                    if (result.statusCode == 200) {
                        lx520.Set_Quality(Integer.parseInt(_videoSettingsQuality.getText().toString()));
                    }
                    else{
                        Toast.show(DeviceSettings.this,getString(R.string.set_video_fps_failed));
                    }
                }
                if (result.type == 26) {
                    if (result.statusCode == 200) {
                        lx520.Set_GOP(Integer.parseInt(_videoSettingsGop.getText().toString()));
                    }
                    else{
                        Toast.show(DeviceSettings.this,getString(R.string.set_video_quality_failed));
                    }
                }
                else if (result.type == 28) {
                    if (result.statusCode == 200) {
                        Toast.show(DeviceSettings.this,getString(R.string.device_parameter_success));
                    }
                    else{
                        Toast.show(DeviceSettings.this,getString(R.string.set_video_gop_failed));
                    }
                }
            }
        });
        lx520.Set_Fps(Integer.parseInt(_videoSettingsFps.getText().toString()));
    }

    /**
     *  Set Parameter
     */
    View.OnClickListener _videoSettingsParameterBtn_Click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setParameter();
        }
    };

    /**
     *  Self
     */
    public static DeviceSettings self() {
        return _self;
    }
}


