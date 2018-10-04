package com.example.jean.video;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.demo.sdk.Controller;
import com.demo.sdk.Enums;
import com.demo.sdk.Lx520;
import com.demo.sdk.Module;
import com.demo.sdk.Player;
import com.example.jean.component.DeviceEntity;
import com.example.jean.component.RequestPermission;
import com.example.jean.rakvideotest.R;
import com.example.jean.component.TcpSocket;
import com.nabto.api.RemoteTunnel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import paintss.common.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class VideoPlay extends Activity{
    public static RelativeLayout _videoPlayView;
    public static VideoPlay _self;
    KeyguardManager mKeyguardManager = null;
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    private String _deviceName="";
    public static String _deviceId="";
    public static String _deviceIp="";
    public static String _devicePsk="";
    public static int _devicePort=554;
    private int _voicePort=80;
    public static int _fps=20;
    private String _version="";
    private boolean _isLx520=true;

    private LinearLayout _videoConnectLayout;
    LinearLayout _layout;
    private com.example.jean.component.MainMenuButton _videoConnecttingBack;
    private TextView _videoConnecttingText;
    private ImageView _videoConnectingImg;
    private AnimationDrawable _loadingAnimation;

    private RelativeLayout _videoLayout;
    private com.demo.sdk.DisplayView _videoView;
    private com.demo.sdk.DisplayView _videoView2;
    private LinearLayout _videoTitle;
    private com.example.jean.component.MainMenuButton _videoBack;
    private TextView _videoName;
    private LinearLayout _videoControl;
    private LinearLayout _videoChangePipe;
    public static TextView _videoPipe;
    private TextView _videoAuto;
    private TextView _videoVHD;
    private TextView _videoHD;
    private TextView _videoBD;
    private com.example.jean.component.MainMenuButton _videoPlayBack;
    private com.example.jean.component.MainMenuButton _videoVoice;
    private com.example.jean.component.MainMenuButton _videoTakePhoto;
    private com.example.jean.component.MainMenuButton _videoRecord;
    private com.example.jean.component.MainMenuButton _videoAudio;
    private com.example.jean.component.MainMenuButton _videoUart;
    private com.example.jean.component.MainMenuButton _videoSettings;
    private com.example.jean.component.MainMenuButton videoSdRecordImg;
    private TextView _videoRecordTime;
    private LinearLayout _videoAudioIndicator;
    private TextView _videoAudioTime;

    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID
    private int music_begin;
    private int music_end;
    private String _pipe_not_520="1";
    public static  int _decoderType=0;
    public static  int _videoType=0;
    public static  int _videoScreen=1;
    private TextView video_sd_record_time;
    private int _viewWidth;
    private int _viewHeight;
    public static View _floatView;
    public static com.demo.sdk.DisplayView _floatVideoView;
    public static ImageView _floatVideoExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_vertical);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);//自动旋转
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        _viewWidth = metric.widthPixels;  // 屏幕宽度（像素）
        _viewHeight = metric.heightPixels;  // 屏幕高度（像素）
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        wakeLock.acquire();
        mKeyguardLock = mKeyguardManager.newKeyguardLock("");
        mKeyguardLock.disableKeyguard();
        _self=this;
        _requestPermission=new RequestPermission();
        _videoPlayView=(RelativeLayout) findViewById(R.id.video_play_view);
        _videoConnectLayout=(LinearLayout)findViewById(R.id.video_connecting_layout);
        _layout=(LinearLayout)findViewById(R.id.layout);
        _videoConnecttingBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_connecting_back);
        _videoConnecttingBack.setOnClickListener(_videoConnecttingBack_Click);
        _videoConnecttingText=(TextView)findViewById(R.id.video_connecting_text);
        _videoConnectingImg=(ImageView)findViewById(R.id.video_connecting_img);

        _videoLayout=(RelativeLayout)findViewById(R.id.video_layout);
        _videoView=(com.demo.sdk.DisplayView)findViewById(R.id.video_view);
        _videoView.setOnClickListener(_videoView_Click);
        _videoView2=(com.demo.sdk.DisplayView)findViewById(R.id.video_view2);
        _videoView2.setOnClickListener(_videoView_Click);
        _videoTitle=(LinearLayout)findViewById(R.id.video_title);
        _videoBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_back);
        _videoBack.setOnClickListener(_videoBack_Click);
        _videoPlayBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_playback);
        _videoPlayBack.setOnClickListener(_videoPlayBack_Click);
        _videoName=(TextView)findViewById(R.id.video_name);
        _videoControl=(LinearLayout)findViewById(R.id.video_control);
        _videoChangePipe=(LinearLayout)findViewById(R.id.video_change_pipe);
        _videoPipe=(TextView)findViewById(R.id.video_pipe);
        _videoPipe.setOnClickListener(_videoPipe_Click);
        _videoAuto=(TextView)findViewById(R.id.video_auto);
        _videoAuto.setOnClickListener(_videoAuto_Click);
        _videoVHD=(TextView)findViewById(R.id.video_vhd);
        _videoVHD.setOnClickListener(_videoVHD_Click);
        _videoHD=(TextView)findViewById(R.id.video_hd);
        _videoHD.setOnClickListener(_videoHD_Click);
        _videoBD=(TextView)findViewById(R.id.video_bd);
        _videoBD.setOnClickListener(_videoBD_Click);
        _videoVoice=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_voice);
        _videoVoice.setOnClickListener(_videoVoice_Click);
        _videoTakePhoto=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_take_photo);
        _videoTakePhoto.setOnClickListener(_videoTakePhoto_Click);
        _videoRecord=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_record);
        _videoRecord.setOnClickListener(_videoRecord_Click);
        _videoAudio=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_audio);
        _videoAudio.setOnTouchListener(_videoAudio_Touch);
        _videoUart=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_uart);
        _videoUart.setOnClickListener(_videoUart_Click);
        _videoSettings=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_settings);
        _videoSettings.setOnClickListener(_videoSettings_Click);
        _videoRecordTime=(TextView)findViewById(R.id.video_record_time);
        _videoAudioIndicator=(LinearLayout)findViewById(R.id.video_audio_indicator);
        _videoAudioTime=(TextView)findViewById(R.id.video_audio_time);
        _videoConnectingImg.setBackgroundResource(R.drawable.preloader);
        _loadingAnimation = (AnimationDrawable)_videoConnectingImg.getBackground();
        videoSdRecordImg=(com.example.jean.component.MainMenuButton)findViewById(R.id.video_sd_record_img);
        videoSdRecordImg.setOnClickListener(videoSdRecordImgClick);
        video_sd_record_time=(TextView)findViewById(R.id.video_sd_record_time);

        _videoConnectLayout.setVisibility(View.VISIBLE);
        _videoLayout.setVisibility(View.GONE);
        _loadingAnimation.start();

        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.photo_voice, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        music_begin = sp.load(this, R.raw.begin_record, 2);
        music_end = sp.load(this, R.raw.end_record, 3);
        Intent intent = getIntent();
        _deviceName = intent.getStringExtra("devicename");
        _deviceId = intent.getStringExtra("deviceid");
        _deviceIp = intent.getStringExtra("deviceip");
        _devicePsk = intent.getStringExtra("devicepsk");
        _fps = intent.getIntExtra("devicefps",20);
        _version = intent.getStringExtra("version");
        _decoderType = intent.getIntExtra("decodertype",0);
        _videoType = intent.getIntExtra("videotype",0);
        _videoScreen= intent.getIntExtra("videoscreen",1);
        if (_videoType==0){
            _pipe = Enums.Pipe.H264_PRIMARY;
        }
        else{
            _pipe = Enums.Pipe.MJPEG_PRIMARY;
        }
        _videoName.setText(_deviceName);
        Toast.show(this,_deviceIp);
        if(_version.toLowerCase().contains("wifiv")){
            _isLx520=false;
            _videoVHD.setVisibility(View.VISIBLE);
        }
        else{
            _isLx520=true;
            _videoVHD.setVisibility(View.GONE);
        }

        //悬浮框播放的view
//        if(_floatView==null) {
//            InitFloatView();
//        }
        _startPlay();
    }

    /**
     *  初始化悬浮框
     */
    void InitFloatView(){
        LayoutInflater _floatViewInflater = getLayoutInflater();
        _floatView = _floatViewInflater.inflate(R.layout.float_view, (ViewGroup) findViewById(R.id.float_view1));
        _floatVideoView = (com.demo.sdk.DisplayView) _floatView.findViewById(R.id.video_view);
        _floatVideoExit = (ImageView) _floatView.findViewById(R.id.video_exit);
        _floatVideoExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent(FloatViewBroadCastReceiver.ACTION_MOVIE_STOP));
            }
        });
    }

    /**
     *  启动悬浮框
     */
    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        //在这里进行相关的处理
        //sendBroadcast(new Intent(FloatViewBroadCastReceiver.ACTION_MOVIE_START));
    }


    /**
     *  关闭悬浮框
     */
    void CloseFloatView(){
        sendBroadcast(new Intent(FloatViewBroadCastReceiver.ACTION_MOVIE_STOP));
        InitFloatView();
        if(_player!=null)
            _player.setDisplayView(getApplication(),_videoView,_floatVideoView,_decoderType);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("==>","Resume");
        //CloseFloatView();//关闭悬浮框
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Stop();
        finish();
    }


    /**
     * Play Video
     */
    private static Module _module;
    private Player _player;
    private Controller _controller;
    private boolean _recording = false;
    private Enums.Pipe _pipe = Enums.Pipe.H264_PRIMARY;
    private Thread _trafficThread;
    private long _traffic;
    private long _lastTraffic;
    private boolean _getTraffic = false;
    private boolean _stopTraffic = false;
    private boolean _openVoice = false;
    private long videotime=0;
    public static String photofile_path;
    public static String videofile_path;
    public static String voicefile_path;
    public static String playback_path;
    private FileOutputStream photofile;
    private int _connectTime=0;
    public void PlayVideo()
    {
        if(_isLx520==false){
            getPipe();
        }

        _connectTime=0;
        if (_module == null)
        {
            _module = new Module(this);
        }
        else
        {
            _module.setContext(this);
        }

        _module.setLogLevel(Enums.LogLevel.VERBOSE);
        _module.setUsername("admin");
        _module.setPassword(_devicePsk);
        _module.setPlayerPort(_devicePort);
        _module.setModuleIp(_deviceIp);
        _controller = _module.getController();
        _player = _module.getPlayer();
        _player.setRecordFrameRate(_fps);
        _player.setAudioOutput(_openVoice);
        _player.setTimeout(20000);
        _player.setOnTimeoutListener(new Player.OnTimeoutListener()
        {
            @Override
            public void onTimeout() {
                // TODO Auto-generated method stub
            }
        });
        _recording = _player.isRecording();
        if (_videoScreen==1) {
            _videoView2.setVisibility(View.GONE);
            _player.setDisplayView(getApplication(), _videoView, null, _decoderType);
        }
        else {
            _videoView2.setVisibility(View.VISIBLE);
            _player.setDisplayView(getApplication(), _videoView, _videoView2, _decoderType);
        }
        _player.setOnStateChangedListener(new Player.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(Enums.State state) {
                updateState(state);
            }
        });
        _player.setOnVideoSizeChangedListener(new Player.OnVideoSizeChangedListener()
        {
            @Override
            public void onVideoSizeChanged(int width, int height)
            {

            }

            @Override
            public void onVideoScaledSizeChanged(int arg0, int arg1)
            {
                // TODO Auto-generated method stub

            }
        });

        if (_player.getState() == Enums.State.IDLE)
        {
            if(_deviceIp.equals("127.0.0.1")){
                if (_videoType==0){
                    _pipe = Enums.Pipe.H264_SECONDARY;
                }
                else{
                    _pipe = Enums.Pipe.MJPEG_SECONDARY;
                }

                try {
                    _player.setImageSize(320,240);
                    if(_deviceId.equals("www.sunnyoptical.com")) {
                        String url="rtsp://"+_deviceIp+"/live1.sdp";//为了兼容一个特殊的模块
                        _player.playUrl(url,Enums.Transport.TCP);
                    }
                    else{
                        _player.play(_pipe, Enums.Transport.TCP);
                    }
                }
                catch (Exception e){
                    Log.e("====>","psk error");
                }
            }
            else{
                if (_videoType==0){
                    _pipe = Enums.Pipe.H264_PRIMARY;
                }
                else{
                    _pipe = Enums.Pipe.MJPEG_PRIMARY;
                }

                try {
                    _player.setImageSize(1280,720);
                    if(_deviceId.equals("www.sunnyoptical.com")) {
                        String url="rtsp://"+_deviceIp+"/live1.sdp";
                        _player.playUrl(url,Enums.Transport.UDP);
                    }
                    else{
                        _player.play(_pipe, Enums.Transport.UDP);
                    }
                }
                catch (Exception e){
                    Log.e("====>","psk error");
                }
            }
        }
        else
        {
            if(_player!=null)
                _player.stop();
        }
        updateState(_player.getState());
        final int id = android.os.Process.myUid();
        _lastTraffic = TrafficStats.getUidRxBytes(id);

        _trafficThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;; ) {
                    if (_stopTraffic) {
                        break;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //检测到断开进行重连
                            if(_player!=null){
                                Log.e("Reconnect...","");
                                if(_player.getState()== Enums.State.IDLE){
                                    _videoConnectLayout.setVisibility(View.VISIBLE);
                                    _videoLayout.setVisibility(View.GONE);
                                    _player.stop();
                                    if(_deviceIp.equals("127.0.0.1")){
                                        if (_videoType==0){
                                            _pipe = Enums.Pipe.H264_SECONDARY;
                                        }
                                        else{
                                            _pipe = Enums.Pipe.MJPEG_SECONDARY;
                                        }

                                        if(_deviceId.equals("www.sunnyoptical.com")) {
                                            String url="rtsp://"+_deviceIp+"/live1.sdp";
                                            _player.playUrl(url,Enums.Transport.TCP);
                                        }
                                        else{
                                            _player.play(_pipe, Enums.Transport.TCP);
                                        }
                                    }
                                    else{
                                        if (_videoType==0){
                                            _pipe = Enums.Pipe.H264_PRIMARY;
                                        }
                                        else{
                                            _pipe = Enums.Pipe.MJPEG_PRIMARY;
                                        }

                                        if(_deviceId.equals("www.sunnyoptical.com")) {
                                            String url="rtsp://"+_deviceIp+"/live1.sdp";
                                            _player.playUrl(url,Enums.Transport.UDP);
                                        }
                                        else{
                                            _player.play(_pipe, Enums.Transport.UDP);
                                        }
                                    }
                                }
                            }

                            if(_recording)
                            {
                                videotime++;
                                _videoRecordTime.setVisibility(View.VISIBLE);
                                _videoRecordTime.setText("REC "+showTimeCount(videotime));
                            }
                            else
                            {
                                videotime=0;
                                _videoRecordTime.setVisibility(View.INVISIBLE);
                            }

                            if(Is_Sd_Record){
                                sdvideotime++;
                                video_sd_record_time.setVisibility(View.VISIBLE);
                                video_sd_record_time.setText("REC "+showTimeCount(sdvideotime));
                            }
                            else{
                                sdvideotime=0;
                                video_sd_record_time.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {}
                    if(_player.getState()!= Enums.State.PLAYING){
                        _connectTime++;
                        if(_connectTime>30){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Stop();
                                    finish();
                                    Intent intent = new Intent();
                                    intent.setClass(VideoPlay.this, DeviceConnectFailed.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        Log.e("_connectTime==>",""+_connectTime);
                    }
                }
            }
        });

        _trafficThread.start();
    }

    private void updateState(Enums.State state) {
        switch (state) {
            case IDLE:
                break;
            case PREPARING:
                break;
            case PLAYING:
                //DeviceEntity.saveDevicesById(_self,_deviceId,_deviceName,_deviceIp);
                _getTraffic = true;
                _videoConnectLayout.setVisibility(View.GONE);
                _videoLayout.setVisibility(View.VISIBLE);
            case STOPPED:
                _getTraffic = false;
                break;
        }
    }

    /**
     * Start Play
     */
    private RemoteTunnel _remoteTunnel1=null;
    void _startPlay(){
        _videoConnectLayout.setVisibility(View.VISIBLE);
        _videoLayout.setVisibility(View.GONE);
        if(_deviceIp.equals("127.0.0.1")){
            if(_remoteTunnel1==null)
                _remoteTunnel1=new RemoteTunnel(getApplicationContext());
            _remoteTunnel1.openTunnel(1, 5555, _devicePort, _deviceId);
            _remoteTunnel1.setOnResultListener(new RemoteTunnel.OnResultListener() {
                @Override
                public void onResult(int id, String result) {
                    // TODO Auto-generated method stub
                    if (result.equals("CONNECT_TIMEOUT") ||
                            result.equals("NTCS_CLOSED") ||
                            result.equals("NTCS_UNKNOWN") ||
                            result.equals("FAILED")) {
                        //Toast.show(getApplicationContext(), getString(R.string.device_connect_network_error));
                        if (_remoteTunnel1 != null) {
                            _remoteTunnel1.closeTunnels();
                            _remoteTunnel1 = null;
                        }
                        Stop();
                        finish();
                        Intent intent=new Intent();
                        intent.setClass(VideoPlay.this,DeviceConnectFailed.class);
                        startActivity(intent);
                    } else {
                        _devicePort=5555;
                        PlayVideo();
                        _audioRemoteConnect();
                        Log.e("_devicePort==>",""+_devicePort);
                    }
                }
            });
        }
        else{
            _devicePort=554;
            _voicePort=80;
            getSdStatus();//获取SD卡录制状态
            PlayVideo();
        }
    }

    /**
     * Audio Remote Connect
     */
    private RemoteTunnel _remoteTunnel=null;
    void _audioRemoteConnect(){
        if(_remoteTunnel==null)
            _remoteTunnel=new RemoteTunnel(getApplicationContext());
        _remoteTunnel.openTunnel(0, 3333, _voicePort, _deviceId);
        _remoteTunnel.setOnResultListener(new RemoteTunnel.OnResultListener() {
            @Override
            public void onResult(int id, String result) {
                // TODO Auto-generated method stub
                if (result.equals("CONNECT_TIMEOUT") ||
                        result.equals("NTCS_CLOSED") ||
                        result.equals("NTCS_UNKNOWN") ||
                        result.equals("FAILED")) {
                    Toast.show(getApplicationContext(), getString(R.string.device_connect_network_error));
                    if (_remoteTunnel != null) {
                        _remoteTunnel.closeTunnels();
                        _remoteTunnel = null;
                    }
                } else {
                    _voicePort = 3333;
                    getSdStatus();//获取SD卡录制状态
                    Log.e("_voicePort==>",""+_voicePort);
                }
            }
        });
    }

    /**
     *  Video Pipe
     */
    View.OnClickListener _videoPipe_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_videoChangePipe.getVisibility()==View.VISIBLE){
                _videoChangePipe.setVisibility(View.GONE);
            }
            else{
                _videoChangePipe.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     *  Video Auto
     */
    View.OnClickListener _videoAuto_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_deviceIp.equals("127.0.0.1")){
                _videoPipe.setText(getString(R.string.video_BD));
                _player.setImageSize(320,240);
                if(_isLx520){
                    if((_pipe==Enums.Pipe.H264_SECONDARY)||(_pipe== Enums.Pipe.MJPEG_SECONDARY)){
                        Toast.show(_self,getApplication().getString(R.string.video_BD_ok));
                        return;
                    }
                    _videoChangePipe.setVisibility(View.GONE);
                    _videoConnectLayout.setVisibility(View.VISIBLE);
                    _videoLayout.setVisibility(View.GONE);
                    if (_videoType==0){
                        _pipe = Enums.Pipe.H264_SECONDARY;
                    }
                    else{
                        _pipe = Enums.Pipe.MJPEG_SECONDARY;
                    }
                    _player.changePipe(_pipe);
                }
                else{

                }

            }
            else{
                _videoPipe.setText(getString(R.string.video_HD));
                _player.setImageSize(1280,720);
                if(_isLx520){
                    if((_pipe==Enums.Pipe.H264_PRIMARY)||(_pipe== Enums.Pipe.MJPEG_PRIMARY)){
                        Toast.show(_self,getApplication().getString(R.string.video_HD_ok));
                        return;
                    }
                    _videoChangePipe.setVisibility(View.GONE);
                    _videoConnectLayout.setVisibility(View.VISIBLE);
                    _videoLayout.setVisibility(View.GONE);
                    if (_videoType==0){
                        _pipe = Enums.Pipe.H264_PRIMARY;
                    }
                    else{
                        _pipe = Enums.Pipe.MJPEG_PRIMARY;
                    }
                    _player.changePipe(_pipe);
                }
                else{

                }
            }
        }
    };


    /**
     *  Get Video Pipe
     */
    private  void getPipe(){
        Lx520 lx520 = new Lx520(_deviceIp + ":" + _voicePort, _devicePsk);
        lx520.setOnResultListener(new Lx520.OnResultListener() {
            @Override
            public void onResult(Lx520.Response result) {
                if (result.type == 22) {
                    if (result.statusCode == 200) {
                        String ff=result.body.replace(" ","");
                        String keyStr="\"value\":\"";
                        int index=ff.indexOf(keyStr);
                        if (index!=-1){
                            int index2=ff.indexOf("\"",index+keyStr.length());
                            if(index2!=-1){
                                _pipe_not_520=ff.substring(index+keyStr.length(),index2);
                                if(_pipe_not_520.equals("0")){
                                    _videoPipe.setText(getString(R.string.video_BD));
                                }
                                else if(_pipe_not_520.equals("1")){
                                    _videoPipe.setText(getString(R.string.video_BD));
                                }
                                else if(_pipe_not_520.equals("2")){
                                    _videoPipe.setText(getString(R.string.video_HD));
                                }
                                else if(_pipe_not_520.equals("3")){
                                    _videoPipe.setText(getString(R.string.video_VHD));
                                }
                            }
                        }
                    }
                }
            }
        });
        lx520.Get_Resolution();
    }

    /**
     *  Get Sd Record Status
     */
    private  void getSdStatus(){
        Lx520 lx520 = new Lx520(_deviceIp + ":" + _voicePort, _devicePsk);
        lx520.setOnResultListener(new Lx520.OnResultListener() {
            @Override
            public void onResult(Lx520.Response result) {
                    if (result.type == 7) {
                        Log.e("==>", "Get sd-record status success");
                        int index = result.body.indexOf(value);
                        if (index != -1) {
                            int index1 = result.body.indexOf(end, index + value.length());
                            if (index1 != -1) {
                                String rString = result.body.substring(index + value.length(), index1);
                                if (rString.equals("0")) {
                                    Is_Sd_Record = false;
                                    videoSdRecordImg.setImageResource(R.drawable.ico_sdcard);
                                } else {
                                    Is_Sd_Record = true;
                                    videoSdRecordImg.setImageResource(R.drawable.ico_sdcarding);
                                }
                            }
                        } else {
                            Log.e("==>", "Get sd-record status failed");
                        }
                    }
            }
        });
        lx520.isRecord(0);
    }

    /**
     *  Set Video Pipe
     */
    private  void setPipe(final int type){
        Lx520 lx520 = new Lx520(_deviceIp + ":" + _voicePort, _devicePsk);
        lx520.setOnResultListener(new Lx520.OnResultListener() {
            @Override
            public void onResult(Lx520.Response result) {
                if (result.type == 22) {
                    if (result.statusCode == 200) {
                        _pipe_not_520=type+"";
                    }
                }
            }
        });
        lx520.Set_Resolution(type);
    }

    /**
     *  Video VHD
     */
    View.OnClickListener _videoVHD_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _videoPipe.setText(getString(R.string.video_VHD));
            if(_isLx520){

            }
            else{
                _player.setImageSize(1920,1080);
                if(_pipe_not_520.equals("3")){
                    Toast.show(_self, getApplication().getString(R.string.video_VHD_ok));
                }
                _videoChangePipe.setVisibility(View.GONE);
                _videoConnectLayout.setVisibility(View.VISIBLE);
                _videoLayout.setVisibility(View.GONE);
                _pipe_not_520 = "3";
                setPipe(3);
            }
        }
    };


    /**
     *  Video HD
     */
    View.OnClickListener _videoHD_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _videoPipe.setText(getString(R.string.video_HD));
            _player.setImageSize(1280,720);
            if(_isLx520){
                if((_pipe==Enums.Pipe.H264_PRIMARY)||(_pipe== Enums.Pipe.MJPEG_PRIMARY)){
                    Toast.show(_self, getApplication().getString(R.string.video_HD_ok));
                    return;
                }
                _videoChangePipe.setVisibility(View.GONE);
                _videoConnectLayout.setVisibility(View.VISIBLE);
                _videoLayout.setVisibility(View.GONE);
                if (_videoType==0){
                    _pipe = Enums.Pipe.H264_PRIMARY;
                }
                else{
                    _pipe = Enums.Pipe.MJPEG_PRIMARY;
                }
                _player.changePipe(_pipe);
            }
            else{
                if(_pipe_not_520.equals("2")){
                    Toast.show(_self, getApplication().getString(R.string.video_HD_ok));
                }
                _videoChangePipe.setVisibility(View.GONE);
                _videoConnectLayout.setVisibility(View.VISIBLE);
                _videoLayout.setVisibility(View.GONE);
                _pipe_not_520 = "2";
                setPipe(2);
            }
        }
    };

    /**
     *  Video BD
     */
    View.OnClickListener _videoBD_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _videoPipe.setText(getString(R.string.video_BD));
            _player.setImageSize(320,240);
            if(_isLx520){
                if((_pipe==Enums.Pipe.H264_SECONDARY)||(_pipe== Enums.Pipe.MJPEG_SECONDARY)){
                    Toast.show(_self, getApplication().getString(R.string.video_BD_ok));
                    return;
                }
                _videoChangePipe.setVisibility(View.GONE);
                _videoConnectLayout.setVisibility(View.VISIBLE);
                _videoLayout.setVisibility(View.GONE);
                if (_videoType==0){
                    _pipe = Enums.Pipe.H264_SECONDARY;
                }
                else{
                    _pipe = Enums.Pipe.MJPEG_SECONDARY;
                }

                _player.changePipe(_pipe);
            }
            else{
                if(_pipe_not_520.equals("1")){
                    Toast.show(_self, getApplication().getString(R.string.video_BD_ok));
                }
                _videoChangePipe.setVisibility(View.GONE);
                _videoConnectLayout.setVisibility(View.VISIBLE);
                _videoLayout.setVisibility(View.GONE);
                _pipe_not_520 = "1";
                setPipe(1);
            }
        }
    };

    /**
     *  Video Voice
     */
    View.OnClickListener _videoVoice_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _openVoice=!_openVoice;
            if(_openVoice){
                _videoVoice.setImageResource(R.drawable.video_voice_on);
            }
            else{
                _videoVoice.setImageResource(R.drawable.video_voice_off);
            }
            _player.setAudioOutput(_openVoice);
        }
    };

    /**
     *  Take Photo
     */
    View.OnClickListener _videoTakePhoto_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(photofile_path==null)
            {
                _requestPermission.requestWriteSettings(VideoPlay.this);
                return;
            }
            File file = new File(photofile_path);//获取本地已有视频数量
            if(file==null){
                _requestPermission.requestWriteSettings(VideoPlay.this);
                return;
            }
            File[] filephoto = file.listFiles();
            if(filephoto==null){
                _requestPermission.requestWriteSettings(VideoPlay.this);
                return;
            }

            sp.play(music, 1, 1, 0, 0, 1);
            SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str   = formatter.format(curDate);

            int photolength=filephoto.length;

            int length=1;
            for(int i=0;i<photolength;i++)
            {
                int start=filephoto[i].getName().indexOf("_");
                int end=filephoto[i].getName().indexOf("  ");
                String aa=filephoto[i].getName().substring(start+1, end);
                if(Integer.parseInt(aa)>=length)
                {
                    length=Integer.parseInt(aa);
                    length=length+1;
                }
            }

            try
            {

                if(length<10)
                    photofile=new FileOutputStream(photofile_path+"/IMG "+"_0"+length+"  "+str+".jpg");
                else
                    photofile=new FileOutputStream(photofile_path+"/IMG "+"_"+length+"  "+str+".jpg");
            }
            catch (Exception e)
            {
                // TODO: handle exception
            }

            if(photofile!=null)
            {
                _player.takePhoto().compress(Bitmap.CompressFormat.JPEG, 100, photofile);
                if(length<10)
                    Toast.show(_self, getApplication().getString(R.string.video_take_photo_text) + photofile_path + "/IMG " + "_0" + length + "  " + str + ".jpg");
                else
                    Toast.show(_self, getApplication().getString(R.string.video_take_photo_text) + photofile_path + "/IMG " + "_" + length + "  " + str + ".jpg");

                try
                {
                    photofile.flush();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    photofile.close();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                photofile=null;
            }
        }
    };

    /**
     *  Record Video
     */
    private String path="";
    View.OnClickListener _videoRecord_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_recording)
            {
                sp.play(music_end, 1, 1, 0, 0, 1);
                //_btnRecord.setImageResource(R.drawable.videodis);
                _player.endRecord();
                _recording = false;
                Toast.show(_self,getApplication().getString(R.string.video_record_text) + path);
            }
            else
            {
                if(videofile_path==null)
                {
                    _requestPermission.requestWriteSettings(VideoPlay.this);
                    return;
                }
                File file = new File(videofile_path);//获取本地已有视频数量
                if(file==null){
                    _requestPermission.requestWriteSettings(VideoPlay.this);
                    return;
                }
                File[] filephoto = file.listFiles();
                if(filephoto==null){
                    _requestPermission.requestWriteSettings(VideoPlay.this);
                    return;
                }

                sp.play(music_begin, 1, 1, 0, 0, 1);
                //_btnRecord.setImageResource(R.drawable.videoen);
                videotime=0;
                SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
                Date curDate = new  Date(System.currentTimeMillis());//获取当前时间
                String str   = formatter.format(curDate);
                int photolength=filephoto.length;

                int length=1;
                for(int i=0;i<photolength;i++)
                {
                    int start=filephoto[i].getName().indexOf("_");
                    int end=filephoto[i].getName().indexOf("  ");
                    String aa=filephoto[i].getName().substring(start+1, end);
                    if(Integer.parseInt(aa)>=length)
                    {
                        length=Integer.parseInt(aa);
                        length=length+1;
                    }
                }
                if(length<10)
                    path=videofile_path+"/VIDEO "+"_0"+length+"  "+str+".mp4";
                else
                    path=videofile_path+"/VIDEO "+"_"+length+"  "+str+".mp4";

                if (_player.beginRecord0(videofile_path, "/VIDEO "+"_"+length+"  "+str))
                {
                    _recording = true;
                }
            }
        }
    };

    /**
     *  Video Audio
     */
    private AudioRecord recorder_vioce=null;
    private FileOutputStream voicefile;
    private FileInputStream voicefilein;
    private boolean isRecording = true ;
    private boolean Is_Recore_Audio=false;
    private int len = 0;
    private boolean Isaudio=false;
    private Timer timer = null;
    private TimerTask task;
    private long audiotime=0;
    TcpSocket audioSocket=null;
    String Audio_Post1="POST /audio.input HTTP/1.1\r\nHost: ";
    String Audio_Post2="\r\nContent-Type: audio/wav\r\nContent-Length: ";
    String Audio_Post3="\r\nAccept: */*\r\n\r\n";
    View.OnTouchListener _videoAudio_Touch=new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            // TODO Auto-generated method stub
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN: //按下
                {
                    Log.i("ACTION_DOWN==>","true");
                    Isaudio=true;

                    if(timer!=null)
                    {
                        timer.cancel();//关闭定时器
                        timer=null;
                        task.cancel();//关闭定时器
                        task=null;
                    }
                    audiotime=0;//计时清零
                    _videoAudioIndicator.setVisibility(View.VISIBLE);
                    timer = new Timer();//初始化定时器
                    //1s定时
                    task = new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            audiotime++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    _videoAudioTime.setText(showTimeCount(audiotime));
                                }
                            });
                        }
                    };
                    timer.schedule(task, 0, 1000);//每1s发送一次扫描

                    new AsyncTask<Void, Void, Void>()
                    {
                        protected Void doInBackground(Void... params)
                        {
                            try
                            {
                                if(voicefile_path==null)
                                {
                                    _requestPermission.requestWriteSettings(VideoPlay.this);
                                    return null;
                                }
                                File file = new File(voicefile_path);//获取本地已有视频数量
                                if(file==null){
                                    _requestPermission.requestWriteSettings(VideoPlay.this);
                                    return null;
                                }
                                File[] filephoto = file.listFiles();
                                if(filephoto==null){
                                    _requestPermission.requestWriteSettings(VideoPlay.this);
                                    return null;
                                }
                                voicefile=new FileOutputStream(voicefile_path+"/voice.pcm");
                            }
                            catch (Exception e)
                            {
                                // TODO: handle exception
                            }

                            int m_in_buf_size =AudioRecord.getMinBufferSize(8000,
                                    AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                                    AudioFormat.ENCODING_PCM_16BIT);
                            byte[] buffer=new byte[m_in_buf_size];
                            recorder_vioce = new AudioRecord(MediaRecorder.AudioSource.MIC,
                                    8000,
                                    AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                                    AudioFormat.ENCODING_PCM_16BIT,
                                    m_in_buf_size);


                            if(recorder_vioce!=null)
                            {
                                recorder_vioce.startRecording();

                                Is_Recore_Audio=true;
                                if(voicefile!=null)
                                {
                                    while(Is_Recore_Audio)
                                    {
                                        try
                                        {
                                            int bufferReadResult =recorder_vioce.read(buffer, 0, m_in_buf_size);
                                            byte[] tmpBuf = new byte[bufferReadResult];
                                            System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
                                            byte[] PCM_Data=new byte[bufferReadResult/2];
                                            for(int i=0;i<bufferReadResult/2;i++)
                                            {
                                                int v=tmpBuf[i*2+1]*256+ tmpBuf[i*2];
                                                PCM_Data[i]=((byte)PCMA2PCM.linear2ulaw(v));
                                            }
                                            voicefile.write(PCM_Data);
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void result)
                        {
                            if(recorder_vioce!=null)
                            {
                                recorder_vioce.stop();
                                recorder_vioce.release();
                                recorder_vioce=null;
                            }
                            if(voicefile!=null)
                            {
                                try
                                {
                                    voicefile.flush();
                                    voicefile.close();
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                voicefile=null;
                            }
                            Isaudio=false;
                        }
                    }.execute();

                }
                break;
                case MotionEvent.ACTION_MOVE: //移动
                {}
                break;
                case MotionEvent.ACTION_UP: //抬起
                {
                    Log.i("ACTION_UP==>","true");
                    Isaudio=false;
                    Is_Recore_Audio=false;
                    _videoAudioIndicator.setVisibility(View.INVISIBLE);
                    _videoAudioTime.setText("00:00");
                    if(timer!=null)
                    {
                        timer.cancel();//关闭定时器
                        timer=null;
                        task.cancel();//关闭定时器
                        task=null;
                    }
                    if(voicefilein==null)
                    {
                        try
                        {
                            voicefilein=new FileInputStream(voicefile_path+"/voice.pcm");
                        } catch (FileNotFoundException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if(voicefilein!=null)
                    {
                        try
                        {
                            len = voicefilein.available();
                        } catch (IOException e1)
                        {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }


                        new AsyncTask<Void, Void, Void>()
                        {
                            protected Void doInBackground(Void... params)
                            {
                                if(audioSocket==null)
                                {
                                    audioSocket=new TcpSocket();
                                    try
                                    {
                                        Log.e("len==>", len + "");
                                        int len_ys=len;
                                        int send_len=4096;
                                        byte[] buf=new byte[send_len];
                                        if(audioSocket.Connect(_deviceIp, _voicePort))
                                        {
                                            Log.e("len2==>", len+"");
                                            String Audio_Str=Audio_Post1+_deviceIp+Audio_Post2+len+Audio_Post3;
                                            audioSocket.Send_Str(Audio_Str);
                                            try
                                            {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e1) {
                                                // TODO Auto-generated catch block
                                                e1.printStackTrace();
                                            }
                                            while(len>0)
                                            {
                                                if(len>send_len)
                                                {
                                                    voicefilein.read(buf, 0, send_len);
                                                    audioSocket.Send_Byte(buf, 0, send_len);
                                                    len-=send_len;
                                                    Log.i("len==>", len+"");
                                                }
                                                else
                                                {
                                                    voicefilein.read(buf, 0, len);
                                                    audioSocket.Send_Byte(buf, 0, len);
                                                    len=0;
                                                    //Log.i("len==>", len+"");
                                                }
                                            }
                                        }
                                    }
                                    catch (IOException e)
                                    {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    Log.i("bufferReadResult==>", "连接失败");
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result)
                            {
                                if(audioSocket!=null)
                                {
                                    audioSocket.Close();
                                    audioSocket=null;
                                }
                                try
                                {
                                    voicefilein.close();
                                    voicefilein=null;
                                } catch (IOException e)
                                {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }.execute();
                    }
                    else
                    {
                        Log.i("bufferReadResult==>","读取文件失败");
                    }
                }
                break;
            }
            return true;
        }
    };

    /**
     * SD record
     **/
    boolean Is_Sd_Record=false;
    private String value="{\"value\": \"";
    private String end="\"}";
    private int sdvideotime=0;
    View.OnClickListener videoSdRecordImgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sdvideotime=0;
            Is_Sd_Record=!Is_Sd_Record;
            Lx520 lx520 = new Lx520(_deviceIp+":"+_voicePort, _devicePsk);
            lx520.setOnResultListener(new Lx520.OnResultListener() {
                @Override
                public void onResult(Lx520.Response result) {
                    if (result.statusCode == 200) {
                        if (result.type == 5) {
                            int index = result.body.indexOf(value);
                            if (index != -1) {
                                int index1 = result.body.indexOf(end, index + value.length());
                                if (index1 != -1) {
                                    String rString = result.body.substring(index + value.length(), index1);
                                    int c = Integer.parseInt(rString);
                                    if (c < 0) {
                                        Is_Sd_Record = false;
                                        videoSdRecordImg.setImageResource(R.drawable.ico_sdcard);
                                        if (c == -4) {
                                            Toast.show(VideoPlay.this,"Sd-card is recording");
                                        } else {
                                            Toast.show(VideoPlay.this,"Sd-card not found");
                                        }
                                    } else {
                                        Is_Sd_Record = true;
                                        videoSdRecordImg.setImageResource(R.drawable.ico_sdcarding);
                                        Toast.show(VideoPlay.this,"Start Sd-Record success");
                                    }
                                }
                            }
                        }
                        if (result.type == 6) {
                            int index = result.body.indexOf(value);
                            if (index != -1) {
                                int index1 = result.body.indexOf(end, index + value.length());
                                if (index1 != -1) {
                                    String rString = result.body.substring(index + value.length(), index1);
                                    if (rString.equals("0")) {
                                        Is_Sd_Record = false;
                                        Toast.show(VideoPlay.this,"Stop Sd-Record success");
                                        videoSdRecordImg.setImageResource(R.drawable.ico_sdcard);
                                    } else {
                                        Is_Sd_Record = true;
                                        videoSdRecordImg.setImageResource(R.drawable.ico_sdcarding);
                                        Toast.show(VideoPlay.this,"Stop Sd-Record failed");
                                    }
                                }
                            }
                        }
                    } else {
                        if (result.type == 5) {
                            Is_Sd_Record = false;
                            Toast.show(VideoPlay.this,"Start Sd-Record failed");
                        }
                        if (result.type == 6) {
                            Is_Sd_Record = true;
                            Toast.show(VideoPlay.this,"Stop Sd-Record failed");
                        }
                    }
                }
            });
            if(Is_Sd_Record){
                lx520.startSdReaord(0);
            }
            else{
                lx520.stopSdReaord(0);
            }
        }
    };

    /**
     *  Video Uart Send Data
     */
    View.OnClickListener _videoUart_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(_self, DeviceUart.class);
            intent.putExtra("deviceip", _deviceIp);
            if(_isLx520)
                intent.putExtra("sendport", _voicePort);
            else
                intent.putExtra("sendport", 1008);
            startActivity(intent);
        }
    };


    /**
     *  Video Settings
     */
    View.OnClickListener _videoSettings_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(_self, DeviceSettings.class);
            intent.putExtra("devicename", _deviceName);
            intent.putExtra("deviceid", _deviceId);
            intent.putExtra("deviceip", _deviceIp);
            intent.putExtra("devicepsk", _devicePsk);
            intent.putExtra("voicport", _voicePort);
            intent.putExtra("version", _version);
            intent.putExtra("fps", _fps);
            startActivity(intent);
        }
    };

    /**
     *  Connecting Back
     */
    View.OnClickListener _videoConnecttingBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Back
     */
    View.OnClickListener _videoBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Stop();
            finish();
        }
    };

    /**
     *  PlayBack
     */
    View.OnClickListener _videoPlayBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
			intent.putExtra("ip", _deviceIp);
            intent.putExtra("psk", _devicePsk);
            intent.putExtra("controlport", _voicePort);
			intent.setClass(VideoPlay.this,PlayBackFolderListActivity.class);
			startActivity(intent);
        }
    };

    /**
     *  点击屏幕，隐藏状态栏
     */
    View.OnClickListener _videoView_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_videoTitle.getVisibility()==View.VISIBLE){
                _videoTitle.setVisibility(View.GONE);
                _videoChangePipe.setVisibility(View.GONE);
                _videoControl.setVisibility(View.GONE);
            }
            else{
                _videoTitle.setVisibility(View.VISIBLE);
                _videoControl.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 功能说明：录像计时
     */
    private String showTimeCount(long time)
    {
        if(time >= 360000)
        {
            return "00:00:00";
        }
        String timeCount = "";
        long hourc = time/3600;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length()-2, hour.length());

        long minuec = (time-hourc*3600)/(60);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length()-2, minue.length());

        long secc = (time-hourc*3600-minuec*60);
        String sec = "0" + secc;
        sec = sec.substring(sec.length()-2, sec.length());
        timeCount = minue + ":" + sec;
        return timeCount;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (_videoScreen==1){
            _videoView2.setVisibility(View.GONE);
        }
        else{
            _videoView2.setVisibility(View.VISIBLE);
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            _player.setViewSize(_viewHeight,_viewWidth);
            _layout.setOrientation(LinearLayout.HORIZONTAL);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            _player.setViewSize(_viewWidth,_viewHeight);
            _layout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    /**
     * Stop
     */
    void Stop(){
        _stopTraffic = true;
        if(_player!=null)
            _player.stop();
        if (_remoteTunnel1 != null) {
            _remoteTunnel1.closeTunnels();
            _remoteTunnel1 = null;
        }
        if (_remoteTunnel != null) {
            _remoteTunnel.closeTunnels();
            _remoteTunnel = null;
        }
    }

    /**
     *  Self
     */
    public static VideoPlay self() {
        return _self;
    }

    private RequestPermission _requestPermission;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == _requestPermission.WRITE_EXTERNAL_STORAGE) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
            if (granted){
                _requestPermission.createSDCardDir(MainActivity.RAKVideo);
                _requestPermission.createSDCardDir(MainActivity.RAKVideo_Photo);
                _requestPermission.createSDCardDir(MainActivity.RAKVideo_Video);
                _requestPermission.createSDCardDir(MainActivity.RAKVideo_Voice);
                _requestPermission.createSDCardDir(MainActivity.RAKVideo_PlayBack);
            }else{
            }
        }
    }
}


