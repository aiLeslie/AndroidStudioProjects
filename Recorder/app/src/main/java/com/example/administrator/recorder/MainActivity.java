package com.example.administrator.recorder;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private Button recordButton,stopButton;
    private MediaRecorder mr;
    private TextView console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordButton = (Button)findViewById(R.id.button);
        stopButton  = (Button)findViewById(R.id.button2);
        console = (TextView)findViewById(R.id.textView);

        setListener();

    }



    private void setListener(){
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File fileDir = null;
                File file = null;

                try{
                    //openFileOutput("file",MODE_PRIVATE).getClass();
//                    file = new File("内部存储设备/microlog.txt");
//                    console.setText(file.exists() + "");

                    fileDir = new File("/sdcard/" + "录音文件");
                   if(!fileDir.exists())fileDir.mkdir();
                    Toast.makeText(MainActivity.this, fileDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//                    console.setText(fileDir.getAbsolutePath());

                    file = new File(fileDir.getAbsolutePath() + "录音" + System.currentTimeMillis() + ".amr");

                    if(!file.exists())file.createNewFile();


                    Toast.makeText(getApplicationContext(), "录音中~~~" + file.getAbsolutePath(),Toast.LENGTH_LONG).show();

                    mr = new MediaRecorder();

                    //mr.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);


                   mr.setOutputFile(file.getAbsolutePath());
                    mr.prepare();
                    mr.start();
                    recordButton.setText("~~录音中~~");
                }catch(IOException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    console.setText(e.getMessage() + "\n");
                    Log.d(TAG, e.getMessage());


                }catch(IllegalStateException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    console.setText(console.getText() + e.getMessage()+ "\n");
                    Log.d(TAG, e.getMessage());

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    console.setText(console.getText() + e.getMessage()+ "\n");
                    Log.d(TAG, e.getMessage());

                }

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mr != null){
                    mr.stop();
                    mr.release();
                    mr = null;
                   recordButton.setText("录音");
                    Toast.makeText(getApplicationContext(), "录音完毕", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "您还没有开始录音哦~", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
