package com.example.jean.video;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jean.rakvideotest.R;

/**
 * Created by Jean on 2016/1/12.
 */
public class DeviceHelp extends Activity{
    private com.example.jean.component.MainMenuButton _deviceHelpBack;
    private static DeviceHelp _self;
    private TextView _deviceUrlAndroid;
    private TextView _deviceUrlIos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_help);
        _self = this;
        _deviceHelpBack=(com.example.jean.component.MainMenuButton)findViewById(R.id.device_help_back);
        _deviceHelpBack.setOnClickListener(_deviceHelpBack_Click);
        _deviceUrlAndroid=(TextView)findViewById(R.id.help_url_android);
        _deviceUrlAndroid.setOnClickListener(_deviceUrlAndroid_Click);
        _deviceUrlIos=(TextView)findViewById(R.id.help_url_ios);
        _deviceUrlIos.setOnClickListener(_deviceUrlIos_Click);
        _deviceUrlAndroid.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG);
        _deviceUrlIos.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void onResume(){
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
     *  Goto download Android APP
     */
    View.OnClickListener _deviceUrlAndroid_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(_deviceUrlAndroid.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };

    /**
     *  Goto download IOS APP
     */
    View.OnClickListener _deviceUrlIos_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(_deviceUrlIos.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };

    /**
     *  Back
     */
    View.OnClickListener _deviceHelpBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Self
     */
    public static DeviceHelp self() {
        return _self;
    }
}


