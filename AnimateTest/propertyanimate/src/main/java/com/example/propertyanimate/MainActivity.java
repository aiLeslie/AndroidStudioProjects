package com.example.propertyanimate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.buttonStart);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonStop);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        View v = findViewById(R.id.imageView);
        ViewPropertyAnimator animator = v.animate();
        if (view.getId() == R.id.buttonStart){
            Toast.makeText(this, "开始动画", Toast.LENGTH_SHORT).show();
//ViewPropertyAnimator每次只能执行一个动画 (如果不设置startDelay会与下次执行动画重合) <<重要设置动画时长>>
            //旋转动画
            animator.setDuration(2000).rotationBy(720);//.translationYBy(200).translationYBy(-10).translationYBy(10)/*.scaleX(1).scaleY(1).scaleXBy(1.0f / 2).scaleXBy(1.0f / 2)*/;
            animator.start();
            //平移动画
            animator.setDuration(2000).translationYBy(1000).setStartDelay(2000);
            animator.start();
            animator.setDuration(2000).translationY(v.getHeight() - 500).setStartDelay(2000);
            animator.start();
            animator.setDuration(2000).translationYBy(v.getHeight() + 500).setStartDelay(2000);
            animator.start();
            animator.setDuration(2000).translationY(v.getHeight() - 250).setStartDelay(2000);
            animator.start();
            animator.setDuration(2000).translationYBy(v.getHeight() + 250).setStartDelay(2000);
            animator.start();
//            //透明度动画
//            animator.setDuration(2000).alphaBy(0).setStartDelay(2000);
//            animator.start();
//            animator.setDuration(2000).alphaBy(1).setStartDelay(2000);
//            animator.start();
//            animator.setDuration(2000).alphaBy(0).setStartDelay(2000);
//            animator.start();
//            animator.setDuration(2000).alphaBy(1).setStartDelay(2000);
//            animator.start();
//            //缩放动画
//            animator.setDuration(2000).scaleXBy(1.5f).setStartDelay(2000);
//            animator.start();
//            animator.setDuration(2000).scaleYBy(1.5f).setStartDelay(2000);
//            animator.start();

        }else if (view.getId() == R.id.buttonStop){
            Toast.makeText(this, "停止动画", Toast.LENGTH_SHORT).show();
            animator.cancel();

        }
    }
}