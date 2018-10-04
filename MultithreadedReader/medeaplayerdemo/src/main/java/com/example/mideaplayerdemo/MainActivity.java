package com.example.mideaplayerdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {
    private MediaPlayer mediaPlayer;
    private final String strurl = "http://m10.music.126.net/20180124161433/9a4033e7e5df60c425ca1e82c26bd2f4/ymusic/5c85/8cf8/de58/e3a6f3ca875b5580eccbafed5e5a0ea3.mp3";
    private boolean isPrepare = false;
    private PalyerHandler handler = new PalyerHandler();
    private final int netWorkOverTime = 0;
    private final int loadfinish = 1;
    private ProgressDialog progressDialog;
    private String input = "";

    private SoundPool soundPool;
    private final int maxStreams = 3;
    private int[] soundId = new int[maxStreams];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://music.2333.me/");
        Button imageButton = (Button) this.findViewById(R.id.imageButtonStart);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*synchronized (MainActivity.this.findViewById(R.id.imageButtonStart)){*/
                EditText editText = (EditText) findViewById(R.id.editText);
                    if (!isPrepare || !editText.getText().toString().equals(input))//如果还没开始准备或editText和这次输入与上次输入不一样
                    {
                        if(isPrepare && !editText.getText().toString().equals(input) && mediaPlayer != null){//如果这次输入与上次输入不一样
                            mediaPlayer.stop();//另当前palyer进入stop状态
                            isPrepare =false;//设置为还没开始准备状态
                        }
                        mediaPlayer = new MediaPlayer();//实例化palyer
                        try {
                            /**
                             * 根据edittext的进行判断
                             */


                            if (editText.getText().toString().equals("")) {
                                File file = new File(Environment.getExternalStorageDirectory(),"coctail dresses.mp3");
                                mediaPlayer.setDataSource(file.getPath());
                                input = "";
                            } else {
                                mediaPlayer.setDataSource(MainActivity.this, Uri.parse(editText.getText().toString()));
                                input = editText.getText().toString();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        /**
                         * 创建进度条
                         */

                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setTitle("Loading");
                        progressDialog.setMessage("just a moment");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new Thread() {
                            @Override
                            public void run() {
                                try {


                                    mediaPlayer.prepare();//palyer调用prepare方法准备
                                    handler.sendMessage(handler.obtainMessage(loadfinish));//palyer准备好在handler中发送信息
                                    mediaPlayer.start();//palyer调用start方法播放
                                    isPrepare = true;//设置为运行播放状态
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    for (int i = 0; i < 4; i++) {
                                        Thread.sleep(1000);
                                        if (isPrepare) {//运行播放状态跳出循环
                                            break;
                                        }
                                    }
                                    if (!isPrepare)//加载超时
                                        handler.sendMessage(handler.obtainMessage(netWorkOverTime));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    } else {
                        mediaPlayer.start();
                    }
                }



            /*}*/


        });
        imageButton = (Button) this.findViewById(R.id.imageButtonPause);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null)mediaPlayer.pause();
            }
        });
        Button button = (Button) findViewById(R.id.buttonStop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                }
                isPrepare = false;


            }
        });


    }

    class PalyerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            /**
             * 根据信息情况进行处理
             */
            if (msg.what == loadfinish) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Loaded succeed", Toast.LENGTH_SHORT).show();

            } else if (msg.what == netWorkOverTime) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "NetWork OverTime", Toast.LENGTH_SHORT).show();
                mediaPlayer.stop();
                isPrepare = false;
            }
        }
    }
    private void initSoundPoll(){
        //soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, getResources().openRawResourceFd(R.raw.onclick.mp3));

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setMessage("Are you sure you want to exit now? ");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AudioManager audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);

        switch (item.getItemId()){
            case R.id.item_increaseVolume:
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);

                break;
            case R.id.item_reduceVolume:
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_PLAY_SOUND);
                break;
        }
        return true;
    }
}
