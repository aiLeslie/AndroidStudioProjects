package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.jean.rakvideotest.R;

/**
 * Created by Jean on 2016/1/12.
 */
public class DeviceMedia extends Activity{
    private com.example.jean.component.MainMenuButton _deviceMediaBack;
    private static DeviceMedia _self;
    private LinearLayout _devicePhoto;
    private LinearLayout _deviceVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_media_play);

        _deviceMediaBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.media_play_back);
        _deviceMediaBack.setOnClickListener(_deviceMediaBack_Click);
        _devicePhoto=(LinearLayout)findViewById(R.id.media_play_photo);
        _devicePhoto.setOnClickListener(_devicePhoto_Click);
        _deviceVideo=(LinearLayout)findViewById(R.id.media_play_video);
        _deviceVideo.setOnClickListener(_deviceVideo_Click);
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
    View.OnClickListener _deviceMediaBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Goto Photo
     */
    View.OnClickListener _devicePhoto_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(DeviceMedia.this,photolistActivity.class);
            startActivity(intent);
        }
    };

    /**
     *  Goto Video
     */
    View.OnClickListener _deviceVideo_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(DeviceMedia.this,videolistActivity.class);
            startActivity(intent);
        }
    };
    /**
     *  Self
     */
    public static DeviceMedia self() {
        return _self;
    }
}


