package com.example.cameratest;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,SurfaceHolder.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},0);
        initView();
    }
    private void initView(){
        Button  button = (Button) findViewById(R.id.buttonStart);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonClear);
        button.setOnClickListener(this);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.sufaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStart){
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(null);
            Intent intent=new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            // 根据文件地址创建文件
            File file = new File (this.getExternalCacheDir(),"picture.png");
            // 把文件地址转换成Uri格式
            Uri uri=Uri.fromFile(file);
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent,0);

        }else if (v.getId() == R.id.buttonClear){
            File file = new File (this.getExternalCacheDir(),"picture.png");
            if (file.exists()){
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"image/png");
                startActivity(intent);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK){
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(Uri.fromFile(new File(this.getExternalCacheDir(),"picture.png")));
        }
    }
}
