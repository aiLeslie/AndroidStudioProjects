package com.example.intentservicetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.buttonStartSevice);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonStartSevice:
                Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(this,MyIntentService.class);
                startService(intent);
                break;
        }

    }
}
