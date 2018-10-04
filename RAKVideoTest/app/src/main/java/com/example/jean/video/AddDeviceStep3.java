package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.demo.sdk.EasyConfig;
import com.demo.sdk.Scanner;
import com.example.jean.rakvideotest.R;
import com.demo.sdk.Lx520;

import java.net.InetAddress;
import java.util.Map;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceStep3 extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceStep3Back;
    private com.example.jean.component.ProcessingView _addDeviceStep3Process;
    private boolean _processing = false;
    private String _password="";
    private String _ssid="";
    private int _type=0;
    private EasyConfig _easyConfig = null;
    private boolean _success = false;
    private static AddDeviceStep3 _self;
    private WLANAPI mWifiAdmin;//wifi API函数
    private Scanner _scanner;
    private int TIMEOUT=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_step3);
        _self = this;
        mWifiAdmin = new WLANAPI(this);

        _addDeviceStep3Back=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_step3_back);
        _addDeviceStep3Back.setOnClickListener(_addDeviceStep3Back_Click);
        _addDeviceStep3Process=(com.example.jean.component.ProcessingView)findViewById(R.id.add_device_step3_progress);

        Intent intent=getIntent();
        _password=intent.getStringExtra("psk");
        _ssid=intent.getStringExtra("ssid");
        _type=intent.getIntExtra("type",0);
        if (_type==0) {//Easyconfig
            _easyConfig = new EasyConfig();
            _easyConfig.setOnStopListener(new EasyConfig.OnStopListener() {
                @Override
                public void onStop() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _addDeviceStep3Process.setValue(TIMEOUT);
                            _addDeviceStep3Process.invalidate();
                            _processing = false;
                        }
                    });
                }
            });
            _easyConfig.setOnProgressListener(new EasyConfig.OnProgressListener() {
                @Override
                public void onData(int progress, String ip, String mac,String id) {
                    Log.i("progress==>",progress+"");
                    if (ip != null &&mac != null &&id != null) {
                        _success = true;
                        Log.i("ip==>",ip);
                        Log.i("mac==>",mac);
                        Log.i("id==>",id);
                        if (_success) {
                            Intent intent=new Intent();
                            intent.setClass(AddDeviceStep3.this, AddDeviceSuccess.class);
                            intent.putExtra("id",id);
                            intent.putExtra("ip",ip);
                            startActivity(intent);
                        }
                    } else {
                        _addDeviceStep3Process.setValue(progress);
                        _addDeviceStep3Process.postInvalidate();

                        Log.e("==>","_success="+_success+" progress="+progress);
                        if((_success==false)&&(progress>=TIMEOUT)){
//                        _addDeviceStep3Process.startAnimation(
//                                AnimationUtils.loadAnimation(
//                                        AddDeviceStep3.this,
//                                        R.anim.processing_car));

                            Intent intent = new Intent();
                            intent.putExtra("type", 0);
                            intent.setClass(AddDeviceStep3.this, AddDeviceFailed.class);
                            startActivity(intent);
                        }
                    }
                }
            });
            _easyConfig.start(_password);
            _processing = true;
            _addDeviceStep3Process.setColor(
                    getResources().getColor(R.color.dialog_blue));
            new ConfigProcessTask().execute();
        }
        else if (_type==1){//Apconfig
            _deviceId=intent.getStringExtra("device_id");
            _success=false;
            ScanDevice();

            _processing = true;
            _addDeviceStep3Process.setColor(
                    getResources().getColor(R.color.dialog_blue));
            new ConfigProcessTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    protected void onDestroy() {
        _processing=false;
        if(_type==0)
            _easyConfig.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Close();
    }

    /**
     *  Back
     */
    View.OnClickListener _addDeviceStep3Back_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Close();
        }
    };

    void Close(){
        _processing = false;
        if(_type==0)
            _easyConfig.stop();
        else if (_type==1){
            AddDeviceStep2AP addDeviceStep2Ap  = AddDeviceStep2AP.self();
            if (addDeviceStep2Ap != null)
                addDeviceStep2Ap.self().finish();
            AddDeviceStep3AP addDeviceStep3Ap  = AddDeviceStep3AP.self();
            if (addDeviceStep3Ap != null)
                addDeviceStep3Ap.self().finish();
        }
        finish();
    }

    /**
     *  Config Process Task
     */
    int value = 0;
    class ConfigProcessTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            value = 0;
            while (value <= TIMEOUT) {
                if (!_processing)
                    return null;
                _addDeviceStep3Process.setValue(value);
                _addDeviceStep3Process.postInvalidate();

                try {
                    Thread.sleep(600);
                }
                catch (InterruptedException ex) {}
                value += 1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!_processing) {
                return;
            }
            _processing = false;
        }
    }

    /**
     *  Scan Device
     */
    String _deviceId="";//记录模块的id
    void ScanDevice(){
        _scanner = new Scanner(AddDeviceStep3.this);
        _scanner.setOnScanOverListener(new Scanner.OnScanOverListener() {
            @Override
            public void onResult(Map<InetAddress, String> data, InetAddress gatewayAddress) {
                if ((data != null) && (data.size() > 0)) {//扫描到的设备
                    for (Map.Entry<InetAddress, String> entry : data.entrySet()) {
                        String id = entry.getValue();
                        String ip = entry.getKey().getHostAddress();
                        Log.e("id==>", id);
                        Log.e("ip==>", ip);
                        Log.e("_deviceId==>", _deviceId);
                        if (id.equals(_deviceId) && (!_success)) {
                            _processing = false;
                            _success = true;
                            Intent intent = new Intent();
                            intent.setClass(AddDeviceStep3.this, AddDeviceSuccess.class);
                            intent.putExtra("id", id);
                            intent.putExtra("ip", ip);
                            startActivity(intent);
                        }
                    }
                }
                if (_processing) {
                    ScanDevice();
                } else {//配置超时
                    Config_Error(3);
                }
            }
        });
        _scanner.scanAll();
    }

    /**
     *
     * @param type
     * 0:一键配置失败   1:切换网络失败  2:AP配置失败  3:本地发现超时
     */
    void Config_Error(int type)
    {
        Log.e("==>","_success="+_success+" value="+value);
        if((_success==false)&&(value>=TIMEOUT)) {
            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.setClass(AddDeviceStep3.this, AddDeviceFailed.class);
            startActivity(intent);
        }
    }

    /**
     *  Self
     */
    public static AddDeviceStep3 self() {
        return _self;
    }
}


