package com.example.administrator.animatetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Animation animation = null;
    private View img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 开启动画
        openingAni();

        // 绑定视图
        bindViews();

    }

    /**
     * 绑定视图
     */
    private void bindViews() {
        Button button = (Button) findViewById(R.id.buttonScale);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonRotate);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonTranslate);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonAlpha);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.startAni);
        button.setOnClickListener(this);
        img = findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.buttonScale) {
            Toast.makeText(this, "缩放动画", Toast.LENGTH_SHORT).show();
            createAnimation(R.anim.scale);

        } else if (view.getId() == R.id.buttonRotate) {
            Toast.makeText(this, "旋转动画", Toast.LENGTH_SHORT).show();
            createAnimation(R.anim.rotate);

        } else if (view.getId() == R.id.buttonTranslate) {
            Toast.makeText(this, "平移动画", Toast.LENGTH_SHORT).show();
            createAnimation(R.anim.translate);

        } else if (view.getId() == R.id.buttonAlpha) {
            Toast.makeText(this, "透明度动画", Toast.LENGTH_SHORT).show();
            createAnimation(R.anim.alpha);

        } else if (view.getId() == R.id.startAni) {
            Toast.makeText(this, "组合动画", Toast.LENGTH_SHORT).show();
            createAnimation(R.anim.ani_set);

        }

    }

    private synchronized void createAnimation(int xml) {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), xml);
        img.startAnimation(animation);
    }

    private void openingAni() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.start_ctrl);
        /**
         * 透明度渐变动画
         */
        Animation alphaAni = new AlphaAnimation(0, 1);
        alphaAni.setDuration(2000);

        /**
         * 缩放度渐变动画
         */
        Animation scaleAni = new ScaleAnimation(0.01f, 1, 0.01f, 1, layout.getMeasuredWidth() / 2, 1000);
        scaleAni.setDuration(2000);

        /**
         * 平移渐变动画
         */
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为500ms
        Animation transAni = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
        transAni.setDuration(1000); // 设置动画的过渡时间


        AnimationSet aniSet = new AnimationSet(true);
        aniSet.addAnimation(alphaAni);
        aniSet.addAnimation(scaleAni);
        aniSet.addAnimation(transAni);

        /**
         * 布局延时改变
         */
//        layout.postDelayed(new Runnable() {
//            @Override
//            public void run() {


        layout.startAnimation(aniSet);
        layout.setVisibility(View.VISIBLE);

//            }
//        }, 500l);
    }

    private void goneAni() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.start_ctrl);
        /**
         * 透明度渐变动画
         */
        Animation alphaAni = new AlphaAnimation(1, 0);
        alphaAni.setDuration(2000);

        /**
         * 缩放度渐变动画
         */
        Animation scaleAni = new ScaleAnimation(1f, 0.1f, 1f, 0.1f, 0.5f, 0.5f);
        scaleAni.setDuration(2000);

        /**
         * 平移渐变动画
         */
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为500ms
//        Animation transAni = new TranslateAnimation(
//                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
//                TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
//        transAni.setDuration(1000); // 设置动画的过渡时间


        AnimationSet aniSet = new AnimationSet(true);
        aniSet.addAnimation(alphaAni);
        aniSet.addAnimation(scaleAni);
//        aniSet.addAnimation(transAni);


        layout.startAnimation(aniSet);
        layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        goneAni();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.super.onBackPressed();
                    }
                });

            }
        }, 1000);


    }
}
