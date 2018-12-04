package com.leslie.ioc_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.leslie.ioc.InjectUtils;
import com.leslie.ioc.annotations.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private View textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtils.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView = findViewById(R.id.textView);
    }
}
