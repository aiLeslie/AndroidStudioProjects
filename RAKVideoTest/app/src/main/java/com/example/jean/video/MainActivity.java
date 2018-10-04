package com.example.jean.video;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.demo.sdk.Scanner;
import com.example.jean.rakvideotest.R;
import com.example.jean.component.DeviceEntity;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private Context _self;//上下文引用
    private Dialog _scanProgressDialog;
    private Scanner _scanner;
    private GridView _deviceList;
    private com.example.jean.component.MainMenuButton _deviceRefresh;
    private TextView _help;
    private ImageView _deviceMedia;
    public static String RAKVideo="/RAKVideo";
    public static String RAKVideo_Photo="/RAKVideo/Photo";
    public static String RAKVideo_Video="/RAKVideo/Video";
    public static String RAKVideo_Voice="/RAKVideo/Voice";
    public static String RAKVideo_PlayBack="/RAKVideo/PlayBack";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 配置存储路径
         */
        createSDCardDir(RAKVideo);
        createSDCardDir(RAKVideo_Photo);
        createSDCardDir(RAKVideo_Video);
        createSDCardDir(RAKVideo_PlayBack);


        _self=this;
        _help=(TextView)findViewById(R.id.main_help);
        _help.setOnClickListener(_help_click);
        _deviceList=(GridView)findViewById(R.id.main_device);
        _deviceList.setOnItemClickListener(deviceList_listener);
        _deviceList.setOnItemLongClickListener(deviceList_long_listener);
        _deviceList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        _scanProgressDialog = new Dialog(this,R.style.myDialogTheme);
        _deviceRefresh=(com.example.jean.component.MainMenuButton)findViewById(R.id.main_refresh);
        _deviceRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDevices();
            }
        });
        _deviceMedia=(ImageView)findViewById(R.id.main_logo);
        _deviceMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,DeviceMedia.class);
                startActivity(intent);
            }
        });
        _scanner = new Scanner(this);
        _scanner.setOnScanOverListener(new Scanner.OnScanOverListener() {
            @Override
            public void onResult(Map<InetAddress, String> data, InetAddress gatewayAddress) {
                ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                ArrayList<String> _deviceIds = new ArrayList<String>();
                _deviceIds = DeviceEntity.getDevicesId(_self);
                if (data != null) {//扫描到的设备
                    for (Map.Entry<InetAddress, String> entry : data.entrySet()) {//从data中获取数据
                        String id = entry.getValue();
                        String ip = entry.getKey().getHostAddress();
                        String name = DeviceEntity.getDeviceNameFromId(_self, id);
                        if(id.equals("www.sunnyoptical.com")){
                            name="Flylink";
                        }
                        Log.e("id=", id);
                        Log.e("ip=", ip);
                        Log.e("name=", name);

                        boolean same = false;
                        for (int i = 0; i < _deviceIds.size(); i++) {
                            if (_deviceIds.get(i).toString().equals(id)) {
                                _deviceIds.remove(i);//已保存设备中删除扫描到的设备
                                same = true;
                                break;
                            }
                        }

                        if (!same) {
                            //未保存只是本地扫描到的，直接添加到列表
                        } else {//已保存，更新保存的值,主要是更新IP地址，再添加到列表
                            DeviceEntity.saveDevicesById(_self, id, name, ip);
                        }
                        //添加到列表
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("item_image", R.drawable.main_device_local);
                        map.put("item_name", name);
                        map.put("item_id", id);
                        map.put("item_ip", ip);
                        list.add(map);
                    }
                }
                //添加已保存但未扫描到的设备到列表
                for (int l = 0; l < _deviceIds.size(); l++) {
                    String id = _deviceIds.get(l).toString();
                    String ip = "127.0.0.1";
                    String name = DeviceEntity.getDeviceNameFromId(_self, id);
                    if(id.equals("www.sunnyoptical.com")){
                        name="Flylink";
                    }
                    DeviceEntity.saveDevicesById(_self, id, name, ip);//已保存，更新保存的值,主要是更新IP地址，再添加到列表
                    //添加到列表
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("item_image", R.drawable.main_device_remote);
                    map.put("item_name", name);
                    map.put("item_id", id);
                    map.put("item_ip", ip);
                    list.add(map);
                }

                //添加"添加设备"到列表
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("item_image", R.drawable.main_device_add);
                map.put("item_name", getApplication().getString(R.string.add_device_list_name));
                list.add(map);

                SimpleAdapter adapter = new SimpleAdapter(_self, list,
                        R.layout.gridview_item, new String[]{"item_image", "item_name", "item_id", "item_ip"},
                        new int[]{R.id.item_image, R.id.item_txt, R.id.item_id, R.id.item_ip});
                _deviceList.setAdapter(adapter);

                if ((_scanProgressDialog != null) && (_scanProgressDialog.isShowing()))
                    _scanProgressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDevices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    View.OnClickListener _help_click =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,DeviceHelp.class);
            startActivity(intent);
        }
    };

    /**
     * 获取设备提示
     */
    private void loadDevices() {
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
        dialog_indicator_title.setText(getApplication().getString(R.string.main_scan_indicator_title));
        dialog_indicator_text.setText(R.string.main_scan_indicator);
        _scanProgressDialog.show();
        //获取设备
        _scanner.scanAll();
    }

    /**
     * 点击设备，播放视频
     */
    AdapterView.OnItemClickListener deviceList_listener=new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
        {
            // TODO Auto-generated method stub
            TextView _getName=(TextView)_deviceList.getChildAt(arg2-_deviceList.getFirstVisiblePosition()).findViewById(R.id.item_txt);
            if(_getName.getText().equals(getApplication().getString(R.string.add_device_list_name)))
            {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, AddDeviceStep0.class);
                startActivity(intent);
            }
            else
            {
                //TextView _getName=(TextView)_deviceList.getChildAt(arg2-_deviceList.getFirstVisiblePosition()).findViewById(R.id.item_txt);
                TextView _getId=(TextView)_deviceList.getChildAt(arg2-_deviceList.getFirstVisiblePosition()).findViewById(R.id.item_id);
                TextView _getIp=(TextView)_deviceList.getChildAt(arg2-_deviceList.getFirstVisiblePosition()).findViewById(R.id.item_ip);
                Log.e("id=", _getId.getText().toString());
                Log.e("ip=", _getIp.getText().toString());

                Intent intent=new Intent();
                intent.setClass(MainActivity.this, DeviceConnect.class);
                intent.putExtra("devicename", _getName.getText().toString());
                intent.putExtra("deviceid", _getId.getText().toString());
                intent.putExtra("deviceip", _getIp.getText().toString());
                startActivity(intent);
            }
        }
    };

    /**
     * 长按编辑或删除设备
     */
    AdapterView.OnItemLongClickListener deviceList_long_listener=new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            TextView _getName=(TextView)_deviceList.getChildAt(position-_deviceList.getFirstVisiblePosition()).findViewById(R.id.item_txt);
            if(_getName.getText().equals(getApplication().getString(R.string.add_device_list_name))==false) {
                TextView _getId=(TextView)_deviceList.getChildAt(position-_deviceList.getFirstVisiblePosition()).findViewById(R.id.item_id);
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, ModifyDeviceName.class);
                intent.putExtra("deviceid", _getId.getText().toString());
                startActivity(intent);
            }
            return false;
        }
    };

    /**
     * 功能说明：SD卡创建路径为path，名字为title的文件夹 （华为手机上总是获取到内存的根目录，暂未解决）
     */
    private void createSDCardDir(String path)
    {
        File sdcardDir =Environment.getExternalStorageDirectory();
        String pathcat=sdcardDir.getPath()+path;
        File path1 = new File(pathcat);
        if (!path1.exists())
        {
            path1.mkdirs();
            setTitle("path ok,path:"+path);
        }
        VideoPlay.photofile_path=sdcardDir.getPath()+RAKVideo_Photo;//路径已经到Photo文件夹
        VideoPlay.videofile_path=sdcardDir.getPath()+RAKVideo_Video;//路径已经到Video文件夹
        VideoPlay.voicefile_path=sdcardDir.getPath()+RAKVideo_Voice;//路径已经到Voice文件夹
        VideoPlay.playback_path=sdcardDir.getPath()+RAKVideo_PlayBack;//路径已经到PlayBack文件夹
    }

    /**
     * 退出软件
     */
    private static Boolean isExit = false;
    void exit()
    {
        Timer tExit = null;
        if (isExit == false)
        {
            isExit = true;
            Toast.makeText(this, getApplication().getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    isExit = false; // 取消退出
                }
            }, 2000);

        }
        else
        {
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
