package com.leslie.javabase.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leslie.javabase.view.toast.ToastProxy;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPageActivity extends AppCompatActivity {
    ToastProxy toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        toast = new ToastProxy(this);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn)
    void showToast() {
        toast.setText("hello");
    }
}
