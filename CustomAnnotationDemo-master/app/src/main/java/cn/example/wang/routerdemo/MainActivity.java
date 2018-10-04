package cn.example.wang.routerdemo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;

import com.alibaba.android.arouter.launcher.ARouter;

import java.lang.annotation.Target;
import java.lang.reflect.Method;

import cn.example.wang.processor.BindView;
import cn.example.wang.processor.MyAnnotion;
import cn.example.wang.routerdemo.annotation.MyTag;

@MyAnnotion()
public class MainActivity extends AppCompatActivity {

    /**
     * 这是一个button
     */
    @MyAnnotion("HAHAHAH")
    Button button;

    @BindView()
    public void init(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       button = findViewById(R.id.button_router);
       button.setOnClickListener( v -> {
           ARouter.getInstance().build("/activity/HomeActivity")
                   .withString("id","123")
                   .navigation();
       });


    }
}
