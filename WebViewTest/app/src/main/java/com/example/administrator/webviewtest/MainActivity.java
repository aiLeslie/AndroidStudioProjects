package com.example.administrator.webviewtest;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private WebView webView;

    @Override
    public void onBackPressed() {
        if (!webView.canGoBack()){
            new AlertDialog.Builder(this).setTitle("Are you sure exit your Application ?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }else {
            webView.goBack();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //请求权限
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.INTERNET"}, 0);
        /**
         * webview s使用方法
         */
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.uc123.com/");


/**
 * 使用okhttp实现network
 */

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://www.baidu.com").build();
                    Response response = client.newCall(request).execute();
                    String reponseData = response.body().string();
                    parseXMLWithPull(reponseData);
                }catch (Exception e){
                    Log.d(TAG, e.getMessage());
                }


            }
        }).start();*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "申请权限成功", Toast.LENGTH_SHORT).show();
        } else {
            this.finish();
        }


    }

    /*private void parseXMLWithPull(String XMLdata) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(XMLdata));
            int eventType = parser.getEventType();
            String id = null, name = null, version = null;
            while (eventType != parser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("id".equals(nodeName)) {
                            id = parser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = parser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)) {
                            Log.i(TAG, "id is" + id);
                            Log.i(TAG, "name is" + name);
                            Log.i(TAG, "version is" + version);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

    }*/
}
