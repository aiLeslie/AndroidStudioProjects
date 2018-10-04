package com.leslie.codebase.bufferknifetest;

import android.animation.FloatArrayEvaluator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.textView)
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        textView.setText("李玮斌");
    }


    @OnClick(R.id.btn)
    public void showText() {
        Toast.makeText(this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}
