package com.example.administrator.chattingrobot;

import android.os.AsyncTask;

/**
 * Created by Administrator on 2017/12/25.
 */

public class HttpData extends AsyncTask<String,Void,String> {
    private String url;

    public HttpData(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(String... strings) {

        return null;
    }
}
