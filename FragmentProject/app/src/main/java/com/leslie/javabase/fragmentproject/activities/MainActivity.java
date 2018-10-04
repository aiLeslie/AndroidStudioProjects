package com.leslie.javabase.fragmentproject.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.leslie.javabase.fragmentproject.Fragment.MethodsManage;
import com.leslie.javabase.fragmentproject.Fragment.TopFragment;
import com.leslie.javabase.fragmentproject.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        MethodsManage.getInstance().addMethod(TopFragment.Interface_Name,MainActivity.class,new Class[]{String.class});

    }

    private void initView() {
        Button button = (Button) findViewById(R.id.btn1);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn2);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn3);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        repalceFragment(R.id.frameLayout, new TopFragment());
//        switch (view.getId()){
//            case R.id.btn1:
//                break;
//            case R.id.btn2:
//                break;
//            case R.id.btn3:
//                break;
//        }
    }

    /**
     * 替换碎片
     */
    private void repalceFragment(int id, TopFragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        transaction.addToBackStack(null);

        transaction.replace(id, fragment);

        transaction.commit();


    }


    public void impInterface(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}
