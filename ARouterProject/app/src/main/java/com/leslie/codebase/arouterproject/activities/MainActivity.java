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


@Router("main")
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connectable main$$Router = new Main$$Router();
        Log.d(TAG, "onCreate: " + main$$Router.toString());
        ARouter.register("main", MainActivity.class);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    public void btn_start(View view) {
        ARouter.start("main");
    }
}
