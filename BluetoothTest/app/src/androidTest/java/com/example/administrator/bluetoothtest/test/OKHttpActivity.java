package com.example.administrator.bluetoothtest.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.bluetoothtest.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OKHttpActivity extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mesure);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    final String info = OKHttpActivity.this.run("http://www.baidu.com");


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OKHttpActivity.this, info, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } .start();


    }


    /**
     * 向服务器获取数据
     * @param url(要访问的网址)
     * @return(服务器返回的内容)
     * @throws IOException
     */
    public  String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
