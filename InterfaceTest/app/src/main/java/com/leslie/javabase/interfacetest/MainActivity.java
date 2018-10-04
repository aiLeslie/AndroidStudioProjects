package com.leslie.javabase.interfacetest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.leslie.javabase.interfacetest.fragment.MyFragment;
import com.leslie.javabase.interfacetest.struct.FunctionManage;
import com.leslie.javabase.interfacetest.struct.HasParamHasResultFunction;
import com.leslie.javabase.interfacetest.struct.HasParamNoResultFunction;
import com.leslie.javabase.interfacetest.struct.NoParamNoResultFunction;

public class MainActivity extends AppCompatActivity {
    private MyFragment fragment;
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 动态添加碎片
        replaceFragment(new MyFragment());

        // 保存对象实例状态对象不为空
        if (savedInstanceState != null) {
            fragment.editText.setText(savedInstanceState.getString("string"));
        } else {
            // 实现接口
            fragment.setmFManage(FunctionManage.getInstance().addFunction(new HasParamNoResultFunction<String>(MyFragment.interfaceName) {


                @Override
                public void function(String data) {
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            }));

        }

    }

    /**
     * 对象状态保存
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("string", fragment.editText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * 动态添加碎片
     * @param fragment
     */
    private void replaceFragment(MyFragment fragment) {
        this.fragment = fragment;
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
