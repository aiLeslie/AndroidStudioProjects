package com.example.volumesetting;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public AudioManager audioManager;
    private ModeHandler modeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        modeHandler = new ModeHandler((TextView) findViewById(R.id.textViewCurrentMode),(RadioGroup)findViewById(R.id.radioGroup));
        new Thread() {
            @Override
            public void run() {
                int initialMode = -1;
                while (true) {
                    if(initialMode != audioManager.getRingerMode()) {
                        initialMode = audioManager.getRingerMode();
                        modeHandler.sendMessage(modeHandler.obtainMessage(initialMode));
                    }
                    try {
                        Thread.sleep((long) (1000 / 60));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();


        initButton();
        initRadioGroup();


    }

    class ModeHandler extends Handler {
        private TextView textView;
        RadioGroup radioGroup ;

        public ModeHandler(TextView textView,RadioGroup radioGroup) {
            this.textView = textView;
            this.radioGroup = radioGroup;
        }

        @Override
        public void handleMessage(Message msg) {
            CompoundButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
            //if(radioButton != null)radioButton.setChecked(false);//错误做法
            switch (msg.what) {

                case AudioManager.RINGER_MODE_NORMAL:
                    textView.setText("正常模式");
                    radioButton = (RadioButton)findViewById(R.id.buttonNormalMode);
                    radioButton.setChecked(true);

                    break;
                case AudioManager.RINGER_MODE_SILENT:
                    textView.setText("静音模式");
                    radioButton = (RadioButton)findViewById(R.id.buttonSilentMode);
                    radioButton.setChecked(true);
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    textView.setText("震动模式");
                    radioButton = (RadioButton)findViewById(R.id.buttonVibrateMode);
                    radioButton.setChecked(true);
                    break;
            }
        }
    }

    private void initButton() {
        /******************************************媒体音量************************************************/
        Button button = (Button) findViewById(R.id.buttonIncreaseMusicVolume);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //audioManager.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(audioManager.STREAM_MUSIC, audioManager.getStreamVolume(audioManager.STREAM_MUSIC) + 1,
                        AudioManager.FLAG_SHOW_UI);
            }
        });
        button = (Button) findViewById(R.id.buttonReduceMusicVolume);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //audioManager.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(audioManager.STREAM_MUSIC, audioManager.getStreamVolume(audioManager.STREAM_MUSIC) - 1,
                        AudioManager.FLAG_SHOW_UI);
            }
        });
        /******************************************闹钟音量************************************************/
        button = (Button) findViewById(R.id.buttonIncreaseAlarmVolume);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //audioManager.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(audioManager.STREAM_ALARM, audioManager.getStreamVolume(audioManager.STREAM_ALARM) + 1,
                        AudioManager.FLAG_SHOW_UI);
            }
        });
        button = (Button) findViewById(R.id.buttonReduceAlarmVolume);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //audioManager.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(audioManager.STREAM_ALARM, audioManager.getStreamVolume(audioManager.STREAM_ALARM) - 1,
                        AudioManager.FLAG_SHOW_UI);
            }
        });
        /******************************************通知音量************************************************/
        button = (Button) findViewById(R.id.buttonIncreaseInfoVolume);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //audioManager.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(audioManager.STREAM_NOTIFICATION, audioManager.getStreamVolume(audioManager.STREAM_NOTIFICATION) + 1,
                        AudioManager.FLAG_SHOW_UI);
            }
        });
        button = (Button) findViewById(R.id.buttonReduceInfoVolume);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //audioManager.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(audioManager.STREAM_NOTIFICATION, audioManager.getStreamVolume(audioManager.STREAM_NOTIFICATION) - 1,
                        AudioManager.FLAG_SHOW_UI);
            }
        });

    }

    private void initRadioGroup() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.buttonNormalMode:
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        Toast.makeText(MainActivity.this, "启动正常模式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.buttonSilentMode:
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        Toast.makeText(MainActivity.this, "启动静音模式", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.buttonVibrateMode:
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        Toast.makeText(MainActivity.this, "启动震动模式", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AudioManager audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);

        switch (item.getItemId()) {
            /*case R.id.item_increaseVolume:
                Toast.makeText(this, "raise", Toast.LENGTH_SHORT).show();
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

                break;
            case R.id.item_reduceVolume:
                Toast.makeText(this, "lower", Toast.LENGTH_SHORT).show();
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                break;*/
            case R.id.item_BatteryInfo:
                Intent intent = new Intent(MainActivity.this,BatteryInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.item_VolumeSetting:
                break;
        }
        return true;
    }
}
