package com.example.administrator.flagapitest.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.flagapitest.mvp.presenter.Presenter;


public abstract class BaseActivity<P extends Presenter> extends AppCompatActivity implements MyView{


    protected P presenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        if (presenter == null) {
            throw new RuntimeException("presenter not found!");
        }
        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) presenter.detachView();
        super.onDestroy();
    }
}
