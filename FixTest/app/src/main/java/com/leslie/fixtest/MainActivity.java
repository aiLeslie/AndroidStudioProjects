package com.leslie.fixtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.leslie.fixtest.error.Calc;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_calc)
    public void calc() {
        try {
            int result = new Calc().startCalc();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.btn_fix)
    public void fix() {

    }
}
