package cn.example.wang.routerdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.alibaba.android.arouter.facade.annotation.Route;
import cn.example.wang.routerdemo.AnnotationProccessor;
import cn.example.wang.routerdemo.R;
import cn.example.wang.routerdemo.annotation.MyTag;
import cn.example.wang.routerdemo.bean.Car;

@Route(path = "/activity/HomeActivity")
public class HomeActivity extends AppCompatActivity {
    @MyTag(name = "大众"  ,size = 200)
    private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AnnotationProccessor.instance().inject(this);
        Log.e("WANG", "HomeActivity.onCreate." + car.toString());
    }
}
