package com.leslie.network_framework;

import com.google.gson.Gson;


public abstract class HttpCallback<Result> implements ICallback {


    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Class<?> clazz = ClassUtils.genericity(this, 0);
        Result obj = (Result) gson.fromJson(result, clazz);
        onSuccess(obj);
    }

    public abstract void onSuccess(Result result);



}
