package com.example.administrator.buttondemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    int count = 0;
    private Button button;
    private Button button2;
    private TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.buttonGet);
        Button button2 = (Button) findViewById(R.id.buttonRet);
        final TextView textview = (TextView) findViewById(R.id.textView);
        //ButtonListener buttonListener = new ButtonListener();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                switch (count % 3) {
                    case 0:
                        textview.setTextColor(Color.parseColor("#00FF00"));
                        break;
                    case 1:
                        textview.setTextColor(Color.parseColor("#FF165AB9"));
                        break;
                    case 2:
                        textview.setTextColor(Color.parseColor("#FFE10505"));
                        break;


                }
                textview.setText(count + "");
            }

        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = 0;
                textview.setTextColor(Color.parseColor("#00FF00"));
                textview.setText(count + "");

            }
        });

    }

}




