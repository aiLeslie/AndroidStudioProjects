package com.leslie.javabase.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.leslie.javabase.view.view.RockerView;
import com.leslie.javabase.view.toast.ToastProxy;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RockerView.OnEventListener {
    @BindView(R.id.rocker)
    RockerView rockerView;
    ToastProxy toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        bindViews();
        toast = new ToastProxy(this);
    }

    private void bindViews() {
        ButterKnife.bind(this);
        rockerView.setListener(this);
    }


    int o;
    @Override
    public void onEvent(final RockerView.Quadrant quadrant, final double radian, double progress) {

        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {

                        o = RockerView.EventParese.parseOrientation(quadrant, radian);
//                        if (o == temp) {
//                            return;
//                        } else {
//                            o = temp;
//                        }
                        switch (o) {
                            case RockerView.EventParese.TOP:
//                                Toast.makeText(MainActivity.this, "上", Toast.LENGTH_SHORT).show();
                                toast.setText("上");
                                break;

                            case RockerView.EventParese.LEFT:
//                                Toast.makeText(MainActivity.this, "左", Toast.LENGTH_SHORT).show();
                                toast.setText("左");
                                break;

                            case RockerView.EventParese.RIGHT:
                                Toast.makeText(MainActivity.this, "右", Toast.LENGTH_SHORT).show();
                                toast.setText("右");
                                break;

                            case RockerView.EventParese.BOTTOM:
//                                Toast.makeText(MainActivity.this, "下", Toast.LENGTH_SHORT).show();
                                toast.setText("下");
                                break;
                        }


                    }
                }
        );
    }



}
