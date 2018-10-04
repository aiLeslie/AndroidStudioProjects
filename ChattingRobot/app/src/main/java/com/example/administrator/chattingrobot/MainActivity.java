package com.example.administrator.chattingrobot;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager connectivityManager;
    Button send;
    ListView listView;
    EditText text;
    ArrayAdapter<Chatting> adapter;
    List<Chatting> list = new ArrayList<>();
    private static final String apiUrl = "http://www.tuling123.com/openapi/api?key=a0fbf366339c42278d985f5ed6499ac9&info=";
    private String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chatting chatting = list.get(position);
                if (chatting.getMyTalk() != null){
                    Toast.makeText(MainActivity.this, chatting.getMyTalk(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, chatting.getRobotTalk(), Toast.LENGTH_LONG).show();
                }
            }
        });
        text = (EditText) findViewById(R.id.editText);

        send = (Button) findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                content = text.getText().toString();
                list.add(new Chatting(R.mipmap.headmimap, content));
                text.setText("");
                Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();

                listView.setAdapter(new ChatAdapter(MainActivity.this, R.layout.talk_item, list));

                listView.setSelection(list.size());
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();//获取连接信息(是否有链接网络)

                if (info == null){
                    Toast.makeText(MainActivity.this, "网络出现问题,程序即将推出", Toast.LENGTH_SHORT).show();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("TAG", "finish");
                                    finish();
                                }
                            });
                        }
                    }.start();
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        URL url = null;
                        InputStreamReader inputStreamReader = null;
                        BufferedReader reader = null;
                        try {
                            url = new URL(apiUrl + content);
                            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                            // System.out.println(reader.hashCode());
                            String line = null;
                            while ((line = ((BufferedReader) reader).readLine()) != null) {

                                System.out.println(line);
                                content = line;
                            }
                            content = content.substring(content.lastIndexOf(':') + 2, content.lastIndexOf('"'));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    list.add(new Chatting( content,R.mipmap.robot));
                                    listView.setAdapter(new ChatAdapter(MainActivity.this, R.layout.talk_item, list));
                                    listView.setSelection(list.size());
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (reader != null)
                                    reader.close();
                                if (inputStreamReader != null)
                                    inputStreamReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();


            }
        });
    }
}