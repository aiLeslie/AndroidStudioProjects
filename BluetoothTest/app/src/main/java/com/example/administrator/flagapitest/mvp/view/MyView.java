package com.example.administrator.flagapitest.mvp.view;

import java.util.List;

public  interface MyView {
    // 显示加载进度
    void showLoading();
    // 显示数据(使用回调的方式返回数据)
    void showInfos(List infos);
}
