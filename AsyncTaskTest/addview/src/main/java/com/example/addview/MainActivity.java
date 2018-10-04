package com.example.addview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
int count=0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) (LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null));
//                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
                TextView textView = new TextView(MainActivity.this);
                count++;
                textView.setText("hi" + " + " + count);
                linearLayout.addView(textView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT ));
            }
        });
    }
}
