package com.leslie.codebase.arouterproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leslie.codebase.arouterproject.R;
import com.leslie.codebase.router.annotation.Router;
import com.leslie.codebase.router_api.ARouter;
@Router("/app/main2")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ARouter.start("main");
    }
}
