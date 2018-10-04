package com.example.administrator.flagapitest.mvp.model;

import java.util.ArrayList;
import java.util.List;

public class ModelIpm implements MyModel{
    @Override
    public void loadData(final OnLoadListener listener) {
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();

                // 加载数据
                List<String> list = new ArrayList<>();
                for (int i = 0; i < Character.MAX_VALUE; i++){
                    list.add(i + "");
                }


                // 返回数据到presenter去处理
                listener.onComplete(list.subList(0,10));
//            }
//        }.start();

    }
}
