package com.leslie.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leslie.message.msgutil.Msg;
import com.leslie.message.msgutil.MsgClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MsgClient client = new MsgClient(this);

        Msg msg = new Msg.Builder().content("hello").build();

        client.phoneNum("13534834099").sendMsg(msg);

    }
}
