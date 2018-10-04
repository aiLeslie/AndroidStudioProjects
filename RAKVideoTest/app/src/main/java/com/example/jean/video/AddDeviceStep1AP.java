package com.example.jean.video;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.sdk.Scanner;
import com.example.jean.rakvideotest.R;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jean on 2016/1/12.
 */
public class AddDeviceStep1AP extends Activity{
    private com.example.jean.component.MainMenuButton _addDeviceStep1APBack;
    private Button _addDeviceStep1APNext;
    private ImageView _addDeviceStep1APImg;
    private String _password="";
    private String _device="";
    private static AddDeviceStep1AP _self;
    private TextView _addDeviceStep1APDevice1;
    private TextView _addDeviceStep1APDevice2;
    private ImageView _addDeviceStep1APDeviceSelect;
    private LinearLayout _addDeviceStep1APDevices;
    private Dialog _deviceListDialog;
    private boolean timeout=false;
    private int TIMEOUT=20;
    private int ScanNum=0;
    private String ssidKey="LTH_";
    private ArrayList<String> ssids = new ArrayList<String>();
    private ArrayList<String> rssis = new ArrayList<String>();
    private WLANAPI mWifiAdmin;//wifi API函数
    private List<ScanResult> list;
    private ScanResult mScanResult;
    private SimpleAdapter ApListItemAdapter;
    private ArrayList<HashMap<String, Object>> ApListItem;
    private ListView ApList;
    private Dialog _scanProgressDialog;
    private Scanner _scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_step1_ap);
        _self = this;
        mWifiAdmin = new WLANAPI(this);
        _scanProgressDialog = new Dialog(this,R.style.myDialogTheme);
        _deviceListDialog = new Dialog(this,R.style.myDialogTheme);

        _addDeviceStep1APBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.add_device_step1_ap_back);
        _addDeviceStep1APBack.setOnClickListener(_addDeviceStep1APBack_Click);
        _addDeviceStep1APNext=(Button)findViewById(R.id.add_device_step1_ap_next);
        _addDeviceStep1APNext.setOnClickListener(_addDeviceStep1APNext_Click);
        _addDeviceStep1APImg=(ImageView)findViewById(R.id.add_device_step1_ap_img);

        _addDeviceStep1APDevice1=(TextView)findViewById(R.id.add_device_step1_ap_device1);
        _addDeviceStep1APDevice1.setOnClickListener(_addDeviceStep1APDevice1_Click);
        _addDeviceStep1APDevice1.setVisibility(View.GONE);
        _addDeviceStep1APDevice2=(TextView)findViewById(R.id.add_device_step1_ap_device2);
        _addDeviceStep1APDeviceSelect=(ImageView)findViewById(R.id.add_device_step1_ap_select);
        _addDeviceStep1APDevices=(LinearLayout)findViewById(R.id.add_device_step1_ap_devices);
        _addDeviceStep1APDevices.setOnClickListener(_addDeviceStep1APDevices_Click);
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
    View.OnClickListener _addDeviceStep1APBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Next
     */
    boolean scaned=false;
    View.OnClickListener _addDeviceStep1APNext_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            scaned=false;
            mWifiAdmin = new WLANAPI(getApplicationContext());
            //if(mWifiAdmin.getSSID().toString().contains("LTH_"))
            {
                _scanner = new Scanner(AddDeviceStep1AP.this);
                _scanner.setOnScanOverListener(new Scanner.OnScanOverListener() {
                    @Override
                    public void onResult(Map<InetAddress, String> data, InetAddress gatewayAddress) {
                        if ((_scanProgressDialog != null) && (_scanProgressDialog.isShowing()))
                            _scanProgressDialog.dismiss();
                        if (data != null) {//扫描到的设备
                            for (Map.Entry<InetAddress, String> entry : data.entrySet()) {
                                scaned=true;
                                String id = entry.getValue();
                                Intent intent = new Intent();
                                intent.putExtra("device_id", id);
                                intent.setClass(AddDeviceStep1AP.this, AddDeviceStep2AP.class);
                                startActivity(intent);
                            }
                        }
                        if(scaned==false){
                            DisplayToast(getApplication().getString(R.string.add_device_step1_ap_admin_connect_fail));
                        }
                    }
                });
                LayoutInflater getdeviceDialog_inflater =getLayoutInflater();
                View getdeviceDialog_admin=getdeviceDialog_inflater.inflate(R.layout.dialog_indicator, (ViewGroup) findViewById(R.id.dialog_indicator1));
                TextView dialog_indicator_title =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_title);
                TextView dialog_indicator_text =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_text);
                TextView dialog_indicator_line =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_line);
                dialog_indicator_line.setVisibility(View.GONE);
                LinearLayout dialog_indicator_btn =(LinearLayout)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_btn);
                dialog_indicator_btn.setVisibility(View.GONE);
                _scanProgressDialog.setCanceledOnTouchOutside(true);
                _scanProgressDialog.setContentView(getdeviceDialog_admin);
                dialog_indicator_title.setText(getApplication().getString(R.string.add_device_step1_ap_admin_connect_title));
                dialog_indicator_text.setText(R.string.add_device_step1_ap_admin_connect_note);
                _scanProgressDialog.show();
                _scanner.scanAll();
            }



//            ssids.clear();
//            rssis.clear();
//            ScanNum=0;
//            LayoutInflater getdeviceDialog_inflater =getLayoutInflater();
//            View getdeviceDialog_admin=getdeviceDialog_inflater.inflate(R.layout.dialog_indicator, (ViewGroup) findViewById(R.id.dialog_indicator1));
//            TextView dialog_indicator_title =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_title);
//            TextView dialog_indicator_text =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_text);
//            TextView dialog_indicator_line =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_line);
//            dialog_indicator_line.setVisibility(View.GONE);
//            LinearLayout dialog_indicator_btn =(LinearLayout)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_btn);
//            dialog_indicator_btn.setVisibility(View.GONE);
//            _scanProgressDialog.setCanceledOnTouchOutside(false);
//            _scanProgressDialog.setContentView(getdeviceDialog_admin);
//            dialog_indicator_title.setText(getApplication().getString(R.string.main_scan_indicator_title));
//            dialog_indicator_text.setText(R.string.main_scan_indicator);
//            _scanProgressDialog.show();
//
//            new AsyncTask<Void, Void, Void>()
//            {
//                protected Void doInBackground(Void... params)
//                {
//                    timeout=false;
//                    int count=0;
//                    while(ScanNum==0)//超时时间10s
//                    {
//                        ScanNetworkList();
//                        if(count<20)
//                        {
//                            count++;
//                            try
//                            {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e)
//                            {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                        else
//                        {
//                            timeout=true;
//                            break;
//                        }
//                    }
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void result)
//                {
//                    if ((_scanProgressDialog != null) && (_scanProgressDialog.isShowing()))
//                        _scanProgressDialog.dismiss();
//                    if(timeout)
//                    {
//                        timeout=false;
//                        DisplayToast(getApplication().getString(R.string.add_device_step1_ap_admin_failed));
//                    }
//                    else
//                    {
//                        //显示扫描到的网络
//                        if(ssids.size()>0)
//                        {
////                            if(ssids.size()==1)//只发现一个RAK模块，则手机自动连接模块
////                            {
////                                String ssidString=ssids.get(0).toString();
////                                //connectToModule(ssidString);
////                            }
////                            else
//                            {
//                                LayoutInflater getdeviceList_inflater =getLayoutInflater();
//                                View getdeviceList_admin=getdeviceList_inflater.inflate(R.layout.device_list_admin, (ViewGroup) findViewById(R.id.device_list_admin1));
//                                TextView device_list_admin_title =(TextView)getdeviceList_admin.findViewById(R.id.device_list_admin_title);
//                                device_list_admin_title.setText(getApplication().getString(R.string.add_device_step1_ap_admin_title));
//                                ApList=(ListView)getdeviceList_admin.findViewById(R.id.device_list_admin_list);
//                                ApListItem = new ArrayList<HashMap<String, Object>>();
//                                ApListItemAdapter = new SimpleAdapter(
//                                        AddDeviceStep1AP.this,ApListItem, R.layout.device_listitem,
//                                        new String[]
//                                                { "ssid", "rssi"},
//                                        new int[]
//                                                { R.id.item_ssid, R.id.item_rssi});
//                                ApList.setAdapter(ApListItemAdapter);
//                                ApList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        _deviceListDialog.dismiss();
//                                        String ssidString=ssids.get(position).toString();
//                                        connectToModule(ssidString);
//                                    }
//                                });
//
//                                for(int n=0;n<ssids.size();n++)
//                                {
//                                    HashMap<String, Object> map = new HashMap<String, Object>();
//                                    map.put("ssid", ssids.get(n).toString());
//                                    map.put("rssi", rssis.get(n).toString());
//                                    ApListItem.add(map);
//                                    ApListItemAdapter.notifyDataSetChanged();
//                                }
//
//                                _deviceListDialog.setCanceledOnTouchOutside(true);
//                                _deviceListDialog.setContentView(getdeviceList_admin);
//                                _deviceListDialog.show();
//                            }
//                        }
//                        else
//                        {
//                            DisplayToast(getApplication().getString(R.string.add_device_step1_ap_admin_failed));
//                        }
//                    }
//                }
//            }.execute();
        }
    };

    /*
	 * 扫描网络列表
	 */
    private void ScanNetworkList()
    {
        ScanNum=0;
        ssids.clear();
        rssis.clear();

        WifiManager wifiManager1 = (WifiManager) getSystemService(WIFI_SERVICE);
        if(wifiManager1.isWifiEnabled())
        {
            // 每次点击扫描之前清空上一次的扫描结果
            if(list!=null)
            {
                list.clear();
            }
            //开始扫描网络
            mWifiAdmin.startScan();
            list=mWifiAdmin.getWifiList();

            if(list!=null)
            {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        for(int i=0;i<list.size();i++)
                        {
                            //得到扫描结果
                            mScanResult=list.get(i);
                            if(mScanResult.SSID.startsWith(ssidKey)) //获取到wifi的SSID填入
                            {
                                ssids.add(mScanResult.SSID);
                                rssis.add(mScanResult.level+"");
                                ScanNum++;
                            }
                        }
                    }
                });
            }
        }
        else
        {
            DisplayToast(getApplication().getString(R.string.add_device_step1_ap_wifi_failed));
        }
    }

    /*
	 * 手机连接到模块
	 */
    void connectToModule(String ssid)
    {
        final String ssidString=ssid;
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
        dialog_indicator_title.setText(getApplication().getString(R.string.add_device_step1_ap_admin_connect_title));
        dialog_indicator_text.setText(getApplication().getString(R.string.add_device_step1_ap_admin_connect_text)+ssidString+"...");
        _scanProgressDialog.show();
        new AsyncTask<Void, Void, Void>()
        {
            protected Void doInBackground(Void... params)
            {
                timeout=false;
                mWifiAdmin.addNetWork(mWifiAdmin.CreateWifiInfo(ssidString, "", 1));
                try
                {
                    Thread.sleep(500);
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                int count=0;
                while(true)
                {
                    if(count<TIMEOUT)
                    {
                        count++;
                        try
                        {
                            Thread.sleep(1000);
                        } catch (InterruptedException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Log.e("count==>", ""+count);
                        Log.e("ssidString==>", ""+ssidString);
                        mWifiAdmin = new WLANAPI(getApplicationContext());
                        if(mWifiAdmin.isNetworkConnected(getApplicationContext()))
                        {
                            if(mWifiAdmin.getSSID().contains(ssidString))
                            {
                                try
                                {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e)
                                {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                    else
                    {
                        timeout=true;
                        break;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result)
            {
                if ((_scanProgressDialog != null) && (_scanProgressDialog.isShowing()))
                    _scanProgressDialog.dismiss();
                if(timeout)
                {
                    timeout=false;
                    DisplayToast(getApplication().getString(R.string.add_device_step1_ap_admin_connect_failed));
                } else {
                    DisplayToast(getApplication().getString(R.string.add_device_step1_ap_admin_connect_ok));
                    Intent intent = new Intent();
                    intent.putExtra("ssid", ssidString);
                    intent.setClass(AddDeviceStep1AP.this, AddDeviceStep2AP.class);
                    startActivity(intent);
                }
            }
        }.execute();
    }

    /**
     *  Select Device
     */
    View.OnClickListener _addDeviceStep1APDevice1_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_addDeviceStep1APDevice1.getText().toString().equals(getApplication().getString(R.string.add_device_step1_device1))){
                _addDeviceStep1APDevice1.setText(getApplication().getString(R.string.add_device_step1_device2));
                _addDeviceStep1APDevice2.setText(getApplication().getString(R.string.add_device_step1_device1));
                _addDeviceStep1APImg.setImageResource(R.drawable.add_image1);
            }
            else{
                _addDeviceStep1APImg.setImageResource(R.drawable.add_image2);
                _addDeviceStep1APDevice1.setText(getApplication().getString(R.string.add_device_step1_device1));
                _addDeviceStep1APDevice2.setText(getApplication().getString(R.string.add_device_step1_device2));
            }
            _addDeviceStep1APDevice1.setVisibility(View.INVISIBLE);
            _addDeviceStep1APDeviceSelect.setImageResource(R.drawable.up);
        }
    };

    /**
     *  Show Select Device
     */
    View.OnClickListener _addDeviceStep1APDevices_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_addDeviceStep1APDevice1.getVisibility()==View.VISIBLE){
                _addDeviceStep1APDevice1.setVisibility(View.INVISIBLE);
                _addDeviceStep1APDeviceSelect.setImageResource(R.drawable.up);
            }else{
                _addDeviceStep1APDevice1.setVisibility(View.VISIBLE);
                _addDeviceStep1APDeviceSelect.setImageResource(R.drawable.down);
            }
        }
    };

    /**
     *  Self
     */
    public static AddDeviceStep1AP self() {
        return _self;
    }

    /*
	 * 显示信息
	 */
    public void DisplayToast(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}


