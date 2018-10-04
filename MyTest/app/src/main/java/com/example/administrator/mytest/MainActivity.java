package com.example.administrator.mytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "李玮斌\nP20160307113", Toast.LENGTH_SHORT).show();
            }
        });
        button = (Button) findViewById(R.id.buttonStartAll);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("startActivity");
                startActivity(intent);
                intent.putExtra("id",R.id.buttonStartAll);
                startActivity(intent);

            }
        });
    }
}
