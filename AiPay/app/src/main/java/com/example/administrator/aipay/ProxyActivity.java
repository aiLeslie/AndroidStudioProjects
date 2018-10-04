package com.example.administrator.aipay;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

public class ProxyActivity extends Activity {
    private  String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);

        className = getIntent().getStringExtra("className");
    }

    /**
     * Class文件 资源文件
     */
    @Override
    public ClassLoader getClassLoader() {
        return PluginManage.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManage.getInstance().getResources();
    }
}
