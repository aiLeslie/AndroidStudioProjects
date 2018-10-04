package com.example.cameratest;


import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.io.File;

import static android.app.ActionBar.LayoutParams;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        Button button = findViewById(R.id.takePhoto);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.takePhoto) {
            intent = new Intent("android.media.action.IMAGE_CAPTURE");

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getExternalCacheDir(), "picture.png")));
            startActivityForResult(intent, 0);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {


            View contentView = LayoutInflater.from(this).inflate(R.layout.picture, null);
            PopupWindow popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
            View rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
            ImageView imageView = (ImageView)contentView. findViewById(R.id.imageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(new File(getExternalCacheDir(), "picture.png").getAbsolutePath()));
            popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,100);

        }
    }
}
