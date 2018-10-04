package com.example.administrator.flagapitest.alert.thread;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.administrator.flagapitest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AlertThread extends Thread {
    private static AlertThread myself;
    public static boolean isAlert = false;
    public static boolean aswitch = true;

    private Context context;

    private AlertThread(Context context) {

        this.context = context;
        isAlert = true;
    }
    public static AlertThread getInstance(Context context){
        if (myself == null || !myself.isAlive()){
            myself = new AlertThread(context);
        }
        return myself;
    }


    @Override
    public void run() {
        super.run();
        if (aswitch){

            MediaPlayer mediaPlayer = new MediaPlayer();
            InputStream in = null;
            OutputStream out = null;
            try {
                File audioFile = new File(context.getExternalCacheDir(), "alert.wav");
                if (!audioFile.exists()) {
                    in = context.getResources().openRawResource(R.raw.alert);
                    out = new FileOutputStream(audioFile);
                    byte[] bytes = new byte[in.available()];
                    in.read(bytes);
                    out.write(bytes);
                }
                mediaPlayer.setDataSource(context, Uri.fromFile(audioFile));

                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                while (isAlert && aswitch) ;

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }

    }
}
