package com.leslie.activity_mode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.btn)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getApplicationContext().getPackageManager().get


        ButterKnife.bind(this);


        setTitle(getPackageResourcePath());


    }

    @OnClick
    public void startActivity(View view) {
        startActivity(new Intent(this, this.getClass()));
    }
}
