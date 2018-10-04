package com.example.mediarecorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int currentIndex = -1;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isStop = false;
    private ViewPropertyAnimator animator;
    private boolean isAnimation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        else {
            initialView();
        }
    }

    private void initialView() {
        Button button = (Button) this.findViewById(R.id.buttonStart);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.buttonReset);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.buttonPaly);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.buttonPause);
        button.setOnClickListener(this);
        button = (Button) this.findViewById(R.id.buttonStop);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextView state = (TextView) findViewById(R.id.stateText);
        //if (isAnimation == false)textAnimation(state);
        switch (v.getId()) {
            case R.id.buttonStart:
                currentIndex = getCurrentIndex();
                record();
                state.setText("Start");
                break;

            case R.id.buttonReset:
                reset();
                state.setText("Reset");
                break;

            case R.id.buttonPause:
                if (mediaRecorder == null) {
                    Toast.makeText(this, "record  not start,so can not pause the record", Toast.LENGTH_SHORT).show();
                    return;
                }
                stop();
                state.setText("Pause");
                break;

            case R.id.buttonPaly:
                paly();
                state.setText("Paly");
                break;

            case R.id.buttonStop:
                stopPaly();
                state.setText("Stop");
                break;
        }
    }

    /**
     * 需要修改才能实现
     * 线程如何变成同步
     * @param v
     */
    private synchronized void textAnimation(View v){
        animator = v.animate();
        isAnimation = true;
        for (int i = 0 ; i < 4 ; i++)
        {

            animator.setDuration(1000).alpha(0).start();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            animator.cancel();
                            animator.setDuration(1000).alpha(1).start();
                        }
                    });

                }
            }.start();

        }
    }


    private void record() {
        if (mediaRecorder != null) return;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置录音源头
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置输出格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//设置编码器
        try {
            mediaRecorder.setOutputFile(getPathByIndex(currentIndex));//设置输出文件路径
            mediaRecorder.prepare();//开始准备工作
            mediaRecorder.start();//开始录音
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "start record", Toast.LENGTH_SHORT).show();
    }

    private void stop() {
        mediaRecorder.stop();//录音停止
        mediaRecorder.reset();//回复mediaRecord新建状态
        mediaRecorder.release();
        mediaRecorder = null;
        Toast.makeText(this, "stop record", Toast.LENGTH_SHORT).show();
    }

    private void reset() {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Are you sure remove the continuous audio data?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentIndex = getCurrentIndex();
                for (int index = 0; index < currentIndex; index++) {
                    File file = new File(getPathByIndex(index));
                    if (file.exists()) {
                        file.delete();
                    }
                }
                Toast.makeText(MainActivity.this, "reset succeed", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();

    }

    @SuppressLint("NewApi")
    private void pause() {
        stop();
        Toast.makeText(this, "pause record", Toast.LENGTH_SHORT).show();
    }

    private String getOutPutFilePath() {
        return getExternalFilesDir("corder").getAbsolutePath() + "/sound.amr";
    }

    private String getPathByIndex(int i) {
        return getExternalCacheDir().getAbsolutePath() + "/sound" + i + ".amr";
    }

    private int getCurrentIndex() {
        int i = 0;
        for (i = 0; new File(getPathByIndex(i)).exists(); i++) ;
        return i;
    }

    private void useIntentForPaly() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        intent.setDataAndType(Uri.fromFile(new File(getOutPutFilePath())), "audio/amr");

        startActivity(intent);
    }


    private synchronized void paly() {
        if ((currentIndex = getCurrentIndex()) == 0) {
            Toast.makeText(this, "You didn't record the audio", Toast.LENGTH_SHORT).show();
            return;
        } else if (mediaPlayer != null) {
            return;
        }
        Toast.makeText(this, "Paly sound", Toast.LENGTH_SHORT).show();
        try {

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        isStop = false;
                        currentIndex = getCurrentIndex();
                        for (int i = 0; i < currentIndex; i++) {
                            if (isStop == true) break;
                            mediaPlayer = new MediaPlayer();
                            File file = new File(getPathByIndex(i));
                            if (!file.exists()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "File Not Found", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                mediaPlayer.setDataSource(MainActivity.this, Uri.fromFile(file));
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                this.sleep(mediaPlayer.getDuration());
                                mediaPlayer.release();
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPaly() {

        if (mediaPlayer != null) {
            isStop = true;
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Stop paly", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[grantResults.length - 1] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "授权成功,欢迎使用", Toast.LENGTH_SHORT).show();
            initialView();
        } else {
            finish();
        }
    }
}
