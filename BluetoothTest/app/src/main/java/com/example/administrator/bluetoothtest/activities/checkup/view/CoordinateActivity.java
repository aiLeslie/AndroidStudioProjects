package com.example.administrator.bluetoothtest.activities.checkup.view;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bluetoothtest.ApplicationUtil;
import com.example.administrator.bluetoothtest.R;
import com.example.administrator.bluetoothtest.activities.checkup.controlor.Controlor;
import com.example.administrator.bluetoothtest.activities.checkup.model.AbstractDatabase;
import com.example.administrator.bluetoothtest.handle.CheckUpHandler;

import java.util.HashMap;
import java.util.List;


public class CoordinateActivity extends AppCompatActivity {

    public CoordinateView tempCoordView;
    public CoordinateView bodyTCoordView;
    public CoordinateView boCoordView;
    public CoordinateView heartCoordView;
    public TextView showTemp;
    public TextView showBodyT;
    public TextView showBO;
    public TextView showHeart;
    private List<HashMap<String, Object>> data;
    public Controlor controlor;


    private void initMode() {
        if (ApplicationUtil.MODE.isClient()) {
            ApplicationUtil.connectThread.getMsg().getCounter().setHandler(new CheckUpHandler(this));
        }else{
            Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate);

        // 初始化模式
        initMode();

        setTitle("小美护士");


        // 隐藏行动栏
//        getSupportActionBar().hide();

        // 设置手机方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        mapView = (MapView) findViewById(R.id.mapView);
        // 从模型层获取数据在显示层显示
//        presenter.fetch();

        controlor = new Controlor(this);
        controlor.loadData(new AbstractDatabase.OnFetchListener() {
            @Override
            public void onFetch(List data) {
                CoordinateActivity.this.data = data;
            }
        });

        initView();


    }

    private void initView() {
//        recyclerView = (RecyclerView) findViewById(R.id.recycler);
//        //设置布局管理器
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
//        //设置适配器
//        recyclerView.setAdapter(new CoordAdapter(this, data));
        /***********************coordView初始化***********************/
        tempCoordView = (CoordinateView) findViewById(R.id.temp);
        tempCoordView.getCoord().setY_MIN(0).setY_MAX(50).setY_UNIT(10).setY_DESC("环境温度").updeate();
        tempCoordView.getCoord().setX_MIN(1).setX_MAX(10).setX_UNIT(1).setX_DESC("次数").updeate();

        bodyTCoordView = (CoordinateView) findViewById(R.id.bodyTemp);
        bodyTCoordView.getCoord().setY_MIN(34).setY_MAX(42).setY_UNIT(1).setY_DESC("身体温度").updeate();
        bodyTCoordView.getCoord().setX_MIN(1).setX_MAX(10).setX_UNIT(1).setX_DESC("次数").updeate();

        boCoordView = (CoordinateView) findViewById(R.id.bo);
        boCoordView.getCoord().setY_MIN(80).setY_MAX(110).setY_UNIT(5).setY_DESC("血氧浓度").updeate();
        boCoordView.getCoord().setX_MIN(1).setX_MAX(10).setX_UNIT(1).setX_DESC("次数").updeate();

        heartCoordView = (CoordinateView) findViewById(R.id.heart);
        heartCoordView.getCoord().setY_MIN(60).setY_MAX(120).setY_UNIT(20).setY_DESC("心跳次数").updeate();
        heartCoordView.getCoord().setX_MIN(1).setX_MAX(10).setX_UNIT(1).setX_DESC("次数").updeate();

        /***********************textView初始化***********************/

        showTemp = (TextView) findViewById(R.id.showTemp);
        showBodyT = (TextView) findViewById(R.id.showBodyT);
        showBO = (TextView) findViewById(R.id.showBO);
        showHeart = (TextView) findViewById(R.id.showHeart);






    }


}
