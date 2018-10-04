package com.example.administrator.flagapitest.mvp.presenter;


import com.example.administrator.flagapitest.mvp.model.MyModel;
import com.example.administrator.flagapitest.mvp.view.MyView;

import java.util.List;

/**
 * 表示层
 */
public class Presenter<T extends MyView> extends BasePresenter<T> {
    // 第一步: view层的引用 (不能使用new实例化)
//    MyView viewLayer;


    // 第二步: model层引用 (可以直接使用new实例化)
    protected MyModel modelLayer;

    // 第三步: 构造方法

    public Presenter(MyModel model) {
        modelLayer = model;

    }



    // 第四步: 执行操作
    @Override
    public void fetch(){
        if (mViewWf.get() != null){
            mViewWf.get().showLoading();

            if (modelLayer != null){
                modelLayer.loadData(new MyModel.OnLoadListener() {
                    @Override
                    public void onComplete(List datas) {
                        mViewWf.get().showInfos(datas);
                    }
                });
            }
        }

    }
}
