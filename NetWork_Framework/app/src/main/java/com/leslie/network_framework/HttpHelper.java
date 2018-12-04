package com.leslie.network_framework;

import java.util.Map;
import java.util.Set;

public class HttpHelper implements IHttpProcessor {
    private static IHttpProcessor mHttpProcessor;
    private static HttpHelper _instance;

    public static void init(IHttpProcessor httpProcessor) {
        if (httpProcessor != null){
            mHttpProcessor = httpProcessor;
        }

    }

    /**
     *
     * @return
     */
    public static HttpHelper obtain() {
        if (_instance == null) {
            _instance = new HttpHelper();
        }
        return _instance;
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallback callback) {
        final String finalUrl = addParams(url, params);
        if (mHttpProcessor != null) {
            mHttpProcessor.post(finalUrl, params, callback);
        } else {
            new IllegalStateException("IHttpProcessor was not initialized!");
        }

    }

    @Override
    public void get(String url, Map<String, Object> params, ICallback callback) {
        final String finalUrl = addParams(url, params);
        if (mHttpProcessor != null) {
            mHttpProcessor.get(finalUrl, params, callback);
        }else {
            new IllegalStateException("IHttpProcessor was not initialized!");
        }


    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    private static String addParams(String url, Map<String, Object> params) {
        // 如果网址传入的参数为空
        if (params == null || params.isEmpty()) {
            return url;
        }
        // 拼接请求网址
        StringBuilder stringBuilder = new StringBuilder(url);
        if (url != null) {
            if (!url.contains("?")) {
                // 没有请求参数的网址,直接追加 ?
                stringBuilder.append("?");
            } else {
                if (!url.endsWith("?")) {
                    // 有请求参数的网址,直接追加 &
                    stringBuilder.append("&");
                }
            }
            // 添加参数值
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            for (Map.Entry entry : entries) {
                stringBuilder.append("&")
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue());
            }

        }
        return url;
    }
}
