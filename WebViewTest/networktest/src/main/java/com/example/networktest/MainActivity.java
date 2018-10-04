package com.example.networktest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private  TextView reponseText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reponseText = (TextView) findViewById(R.id.textViewResponse);
        Button button = (Button) findViewById(R.id.buttonSendRequest);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonSendRequest) sendRequestWithHttpURLConnection();
    }
    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                BufferedReader bufferedReader = null;

                try {
                    //URL url = new URL("http://www.163.com");
                    URL url = new URL("http://www."+ ((EditText) findViewById(R.id.urlText)).getText().toString()+".com");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while((line = bufferedReader.readLine()) != null){
                        builder.append(line);
                        builder.append(System.getProperty("line.separator"));
                    }
                    showResponseText(builder.toString());
                    new FileOutputStream(getExternalFilesDir("download").getAbsolutePath() + "/data.apk").write(builder.toString().getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (bufferedReader != null) bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(urlConnection != null)urlConnection.disconnect();
                }
            }
        }).start();
    }
    private void showResponseText(final String reseponse){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reponseText.setText(reseponse);


            }
        });
    }
}
