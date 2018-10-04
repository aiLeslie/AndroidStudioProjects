package com.example.administrator.checkedbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CheckBox cBox ;
    private CheckBox javaBox;
    private CheckBox c1Box;
    //private OnBoxlistener onBoxlistener;
    private int c = 0;
    private int c1 = 0;
    private int java = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox cBox = (CheckBox)findViewById(R.id.cBox) ;
        CheckBox avaBox = (CheckBox)findViewById(R.id.javaBox) ;
        CheckBox c1Box = (CheckBox)findViewById(R.id.c1Box) ;

        /*OnBoxlistener onBoxlistner = new OnBoxlistener();*/

        cBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                c++;
                if (c == 2) c = 0;
                if (c == 0)
                    Toast.makeText(MainActivity.this, "c语言选项取消", Toast.LENGTH_SHORT).show();
                else Toast.makeText(MainActivity.this, "c语言选项被选定", Toast.LENGTH_SHORT).show();
            }
        });
        javaBox.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            c1++;
            if (c1 == 2) c1 = 0;
            if (c1 == 0)
                Toast.makeText(MainActivity.this, "c++语言选项取消", Toast.LENGTH_SHORT).show();
            else Toast.makeText(MainActivity.this, "c++语言选项被选定", Toast.LENGTH_SHORT).show();
        }
    });
        c1Box.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                java++;
                if (java == 2) java = 0;
                if (java == 0)
                    Toast.makeText(MainActivity.this, "java语言选项取消", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "java语言选项被选定", Toast.LENGTH_SHORT).show();
            }
        });



    }

    /*class OnBoxlistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cBox:
                    c++;
                    if (c == 2) c = 0;
                    if (c == 0)
                        Toast.makeText(MainActivity.this, "c语言选项取消", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MainActivity.this, "c语言选项被选定", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.c1Box:
                    c1++;
                    if (c1 == 2) c1 = 0;
                    if (c1 == 0)
                        Toast.makeText(MainActivity.this, "c++语言选项取消", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MainActivity.this, "c++语言选项被选定", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.javaBox:
                    java++;
                    if (java == 2) java = 0;
                    if (java == 0)
                        Toast.makeText(MainActivity.this, "java语言选项取消", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "java语言选项被选定", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }*/

}

