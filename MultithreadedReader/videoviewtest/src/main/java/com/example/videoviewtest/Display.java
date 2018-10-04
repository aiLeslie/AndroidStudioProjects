package com.example.videoviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

public class Display extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        File file = new File(getIntent().getStringExtra("data"));
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        try {
            MediaController mediaController = new MediaController(this);

            videoView.setMediaController(mediaController);
            videoView.setVideoPath(file.getAbsolutePath());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
