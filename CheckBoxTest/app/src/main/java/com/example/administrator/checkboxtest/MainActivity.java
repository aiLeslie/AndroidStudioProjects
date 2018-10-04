package com.example.administrator.checkboxtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {
    private CheckBox eat;
    private CheckBox dota;
    private CheckBox sleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eat = (CheckBox)findViewById(R.id.checkBox);
        dota = (CheckBox)findViewById(R.id.checkBox2);
        sleep = (CheckBox)findViewById(R.id.checkBox3);
        OnBoxClickListener listener = new OnBoxClickListener();
        eat.setOnClickListener(listener);

    }
    class OnBoxClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){

        }
    }
}


