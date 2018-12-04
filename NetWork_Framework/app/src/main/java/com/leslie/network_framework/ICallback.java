package com.leslie.network_framework;

public interface ICallback {
    void onSuccess(String result);
    void onFailure(String error);
}
