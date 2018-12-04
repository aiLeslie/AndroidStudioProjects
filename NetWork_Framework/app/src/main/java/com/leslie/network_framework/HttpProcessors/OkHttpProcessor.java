package com.leslie.network_framework.HttpProcessors;

import com.leslie.network_framework.ICallback;
import com.leslie.network_framework.IHttpProcessor;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class OkHttpProcessor implements IHttpProcessor {
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void post(String url, Map<String, Object> params, ICallback callback) {
        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onSuccess(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onSuccess(response.body().string());
            }
        });
    }

    @Override
    public void get(String url, Map<String, Object> params, ICallback callback) {

    }
}
