package com.leslie.codebase.arouterproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.leslie.codebase.arouterproject.R;
import com.leslie.codebase.router.annotation.Router;
import com.leslie.codebase.router_api.ARouter;
@Router("/app/main2")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String qualifierName = getClass().getName();
        setTitle(qualifierName.substring(qualifierName.lastIndexOf(".") + 1));
    }
    public void btn_start(View view) {
        ARouter.start("/app/main1");
    }
}
