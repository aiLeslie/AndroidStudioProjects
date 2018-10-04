package com.example.administrator.flagapitest.mvp.presenter;


import com.example.administrator.flagapitest.mvp.view.MyView;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<T extends MyView> {
    // view层的弱引用

    protected WeakReference<T> mViewWf;
    /**
     * 引用进行绑定
     * @param view
     */
    public void attachView(T view){
        mViewWf = new WeakReference<>(view);
    }

    /**
     * 引用进行解绑
     */
    public void detachView(){
        mViewWf.clear();
    }

    public abstract void fetch();
}
