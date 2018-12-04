package com.example.administrator.bluetoothtest.mvp.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.administrator.bluetoothtest.mvp.model.ModelImpl;
import com.example.administrator.bluetoothtest.mvp.presenter.Presenter;

import java.util.List;

public class MvpActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected Presenter createPresenter() {
        return new Presenter<>(new ModelImpl());
    }

    @Override
    public void showLoading() {
        Toast.makeText(this, "加载成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInfos(List infos) {
        Toast.makeText(this, infos.toString(), Toast.LENGTH_SHORT).show();
    }


}
