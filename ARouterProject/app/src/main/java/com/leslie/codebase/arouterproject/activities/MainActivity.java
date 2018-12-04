package com.leslie.codebase.arouterproject.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.leslie.codebase.arouterproject.R;
import com.leslie.codebase.arouterproject.router.Main$$Router;
import com.leslie.codebase.router.annotation.Router;
import com.leslie.codebase.router_api.ARouter;
import com.leslie.codebase.router_api.Connectable;


@Router("/app/main1")
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String qualifierName = getClass().getName();
        setTitle(qualifierName.substring(qualifierName.lastIndexOf(".") + 1));

    }




    public void btn_start(View view) {
        ARouter.start("/app/main2");
    }
}
