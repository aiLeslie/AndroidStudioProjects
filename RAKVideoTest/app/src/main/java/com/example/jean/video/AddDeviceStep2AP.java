package com.example.jean.video;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.demo.sdk.Lx520;
import com.example.jean.rakvideotest.R;
import com.example.jean.component.DeviceEntity;
import com.example.jean.component.JsonDecodec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import paintss.common.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceStep2AP extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceStep2APBack;
    private EditText _addDeviceStep2APSsid;
    private EditText _addDeviceStep2APPsk;
    private ImageView _addDeviceStep2APShowPsk;
    private ImageView _addDeviceStep2APGetWifi;
    private Button _addDeviceStep2APNext;
    private static AddDeviceStep2AP _self;
    private int _networkState = 0;
    private String _ssid="";
    private String _password="";
    private String _deviceid="";
    private Dialog _scanProgressDialog;
    private Dialog _deviceListDialog;
    private boolean timeout=false;
    private int TIMEOUT=20;
    private int ScanNum=0;
    private SimpleAdapter ApListItemAdapter;
    private ArrayList<HashMap<String, Object>> ApListItem;
    private ListView ApList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_step2_ap);
        _self = this;
        _scanProgressDialog = new Dialog(this,R.style.myDialogTheme);
        _deviceListDialog = new Dialog(this,R.style.myDialogTheme);
        _addDeviceStep2APBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_step2_ap_back);
        _addDeviceStep2APBack.setOnClickListener(_addDeviceStep2APBack_Click);
        _addDeviceStep2APSsid=(EditText)findViewById(R.id.add_device_step2_ap_ssid);
        _addDeviceStep2APPsk=(EditText)findViewById(R.id.add_device_step2_ap_psk);
        _addDeviceStep2APShowPsk=(ImageView)findViewById(R.id.add_device_step2_ap_showpsk);
        _addDeviceStep2APShowPsk.setOnClickListener(_addDeviceStep2APShowPsk_Click);
        _addDeviceStep2APGetWifi=(ImageView)findViewById(R.id.add_device_step2_ap_getwifi);
        _addDeviceStep2APGetWifi.setOnClickListener(_addDeviceStep2APGetWifi_Click);
        _addDeviceStep2APNext=(Button)findViewById(R.id.add_device_step2_ap_next);
        _addDeviceStep2APNext.setOnClickListener(_addDeviceStep2APNext_Click);

        _addDeviceStep2APPsk.requestFocus();
        Intent intent=getIntent();
        _deviceid=intent.getStringExtra("device_id");
    }

    @Override
    protected void onResume(){
        //获取保存的密码
        SharedPreferences p = getSharedPreferences(_addDeviceStep2APSsid.getText().toString(), MODE_PRIVATE);
        String psk=p.getString("psk", "");
        _addDeviceStep2APPsk.setText(psk);
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
    View.OnClickListener _addDeviceStep2APBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(timer!=null){
                task.cancel();
                timer.cancel();
                task=null;
                timer=null;
            }
            finish();
        }
    };

    /**
     *  Show password
     */
    private  boolean psk_open=false;
    View.OnClickListener _addDeviceStep2APShowPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open=!psk_open;
            if(psk_open)
            {
                _addDeviceStep2APPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _addDeviceStep2APShowPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _addDeviceStep2APPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _addDeviceStep2APShowPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Get WifiList
     */
    private Timer timer=null;
    private TimerTask task=null;
    View.OnClickListener _addDeviceStep2APGetWifi_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JsonDecodec.ssids.clear();
            LayoutInflater getdeviceDialog_inflater =getLayoutInflater();
            View getdeviceDialog_admin=getdeviceDialog_inflater.inflate(R.layout.dialog_indicator, (ViewGroup) findViewById(R.id.dialog_indicator1));
            TextView dialog_indicator_title =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_title);
            TextView dialog_indicator_text =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_text);
            TextView dialog_indicator_line =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_line);
            dialog_indicator_line.setVisibility(View.GONE);
            LinearLayout dialog_indicator_btn =(LinearLayout)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_btn);
            dialog_indicator_btn.setVisibility(View.GONE);
            _scanProgressDialog.setCanceledOnTouchOutside(false);
            _scanProgressDialog.setContentView(getdeviceDialog_admin);
            dialog_indicator_title.setText(getApplication().getString(R.string.add_device_step2_ap_scan_title));
            dialog_indicator_text.setText(R.string.add_device_step2_ap_scan_text);
            _scanProgressDialog.show();

            if(timer==null){
                timer=new Timer();
                task=new TimerTask() {
                    @Override
                    public void run() {
                        timeout=true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(timeout){
                                    if (_scanProgressDialog != null && _scanProgressDialog.isShowing())
                                        _scanProgressDialog.dismiss();
                                    Toast.show(getApplicationContext(), getApplication().getString(R.string.add_device_step2_ap_scan_failed));
                                }
                            }
                        });
                    }
                };
                timer.schedule(task,15000);
            }

            Lx520 lx520 = new Lx520("192.168.100.1"+":"+80, "admin");
            lx520.setOnResultListener(new Lx520.OnResultListener()
            {
                @Override
                public void onResult(final Lx520.Response result)
                {
                    if (result.statusCode == 200)
                    {
                        if(timer!=null){
                            task.cancel();
                            timer.cancel();
                            task=null;
                            timer=null;
                        }
                        if(timeout)
                            return;
                        String endStr="\n";
                        final String keyStr="\"wifimessage\":";
                        final int index=result.body.indexOf(keyStr);
                        if(index!=-1)
                        {
                            new AsyncTask<Void, Void, Void>()
                            {
                                protected Void doInBackground(Void... params)
                                {
                                    String json=result.body.substring(index+keyStr.length());
                                    JsonDecodec.ssids.clear();
                                    JsonDecodec.readJson2List(json);
                                    return null;
                                }
                                @Override
                                protected void onPostExecute(Void result)
                                {
                                    if (_scanProgressDialog != null && _scanProgressDialog.isShowing())
                                        _scanProgressDialog.dismiss();
                                    if(JsonDecodec.ssids.size()>0) {
                                        LayoutInflater getdeviceList_inflater =getLayoutInflater();
                                        View getdeviceList_admin=getdeviceList_inflater.inflate(R.layout.device_list_admin, (ViewGroup) findViewById(R.id.device_list_admin1));
                                        TextView device_list_admin_title =(TextView)getdeviceList_admin.findViewById(R.id.device_list_admin_title);
                                        device_list_admin_title.setText(getApplication().getString(R.string.add_device_step1_ap_admin_title));
                                        ApList=(ListView)getdeviceList_admin.findViewById(R.id.device_list_admin_list);
                                        ApListItem = new ArrayList<HashMap<String, Object>>();
                                        ApListItemAdapter = new SimpleAdapter(
                                                AddDeviceStep2AP.this,ApListItem, R.layout.device_listitem,
                                                new String[]
                                                        { "ssid", "rssi"},
                                                new int[]
                                                        { R.id.item_ssid, R.id.item_rssi});
                                        ApList.setAdapter(ApListItemAdapter);
                                        ApList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                _deviceListDialog.dismiss();
                                                _addDeviceStep2APSsid.setText(ApListItem.get(position).get("ssid").toString());
                                                SharedPreferences p = getSharedPreferences(_addDeviceStep2APSsid.getText().toString(), MODE_PRIVATE);
                                                String psk=p.getString("psk", "");
                                                _addDeviceStep2APPsk.setText(psk);
                                            }
                                        });

                                        for(int n=0;n<JsonDecodec.ssids.size();n++)
                                        {
                                            HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("ssid", JsonDecodec.ssids.get(n).toString());
                                            map.put("rssi", "");
                                            ApListItem.add(map);
                                            ApListItemAdapter.notifyDataSetChanged();
                                        }

                                        _deviceListDialog.setCanceledOnTouchOutside(true);
                                        _deviceListDialog.setContentView(getdeviceList_admin);
                                        _deviceListDialog.show();
                                    }
                                    else{
                                        Toast.show(getApplicationContext(), getApplication().getString(R.string.add_device_step2_ap_scan_failed));
                                    }
                                }
                            }.execute();
                        }
                        else{
                            if (_scanProgressDialog != null && _scanProgressDialog.isShowing())
                                _scanProgressDialog.dismiss();
                        }
                    }
                    else
                    {
                        Log.e("result.body2==>", result.body);
                        if (_scanProgressDialog != null && _scanProgressDialog.isShowing())
                            _scanProgressDialog.dismiss();
                        Toast.show(getApplicationContext(), getApplication().getString(R.string.add_device_step2_ap_scan_failed));
                    }
                }
            });
            lx520.Get_Ssid_List();
        }
    };

    private void Find_Str(String srcStr,String keyStr)
    {
        while(true) {
            int index = srcStr.indexOf(keyStr);
            if (index != -1){
                int index1 = srcStr.indexOf("\r\n", keyStr.length() + index);
                if (index1 != -1) {
                    String res = srcStr.substring(keyStr.length() + index, index1);
                    JsonDecodec.ssids.add(res);
                    srcStr=srcStr.substring(index1);
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
        }
    }


    /**
     *  Next
     */
    View.OnClickListener _addDeviceStep2APNext_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _password=_addDeviceStep2APPsk.getText().toString();
            _ssid=_addDeviceStep2APSsid.getText().toString();
            if (_networkState == -1) {
                Toast.show(getApplicationContext(), getString(R.string.add_device_step1_net_error));
                return;
            }
            if (!DeviceEntity.checkRouteName(_addDeviceStep2APSsid.getText().toString()) || !DeviceEntity.checkRoutePassword(_password)) {
                Toast.show(getApplicationContext(), getString(R.string.add_device_step1_ssid_error));
                return;
            }
            SharedPreferences.Editor editor = getSharedPreferences(_addDeviceStep2APSsid.getText().toString(), MODE_PRIVATE).edit();
            editor.putString("psk", _password);
            editor.commit();

            Lx520 lx520 = new Lx520("192.168.100.1" + ":" + 80, "admin");
            lx520.setOnResultListener(new Lx520.OnResultListener() {
                @Override
                public void onResult(Lx520.Response result) {
                    if (result.statusCode == 200) {
                        Intent intent = new Intent();
                        intent.putExtra("psk", _password);
                        intent.putExtra("type", 1);
                        intent.putExtra("ssid", _ssid);
                        intent.putExtra("device_id", _deviceid);
                        intent.setClass(AddDeviceStep2AP.this, AddDeviceStep3AP.class);
                        startActivity(intent);
                    } else {
                    }
                }
            });
            lx520.joinWifi(_ssid, _password);
        }
    };

    /**
     *  Self
     */
    public static AddDeviceStep2AP self() {
        return _self;
    }
}


