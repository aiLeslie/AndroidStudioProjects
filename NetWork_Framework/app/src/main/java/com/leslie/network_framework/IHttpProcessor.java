package com.leslie.network_framework;

import java.util.Map;

/**
 * 网络抽象层
 */
public interface IHttpProcessor {
    // 网络访问
    void post(String url, Map<String,Object>params, ICallback callback);

    void get(String url, Map<String,Object>params, ICallback callback);
}
