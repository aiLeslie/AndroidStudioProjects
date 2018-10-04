package com.example.administrator.flagapitest.mvp.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.administrator.flagapitest.mvp.model.ModelIpm;
import com.example.administrator.flagapitest.mvp.presenter.Presenter;

import java.util.List;

public class MVPactivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.fetch();
    }
    @Override
    protected Presenter createPresenter() {
        return new Presenter<>(new ModelIpm());
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
