package com.leslie.valueanimationtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        textView.setText("123",TextView.BufferType.EDITABLE);


        startValueAnimation();

        startObjectAnimation();
    }


    private void startValueAnimation() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                Log.d(TAG, "current value is " + currentValue);
            }
        });
        anim.start();

    }


    private void startObjectAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "rotation", 0f, 360f);
        animator.setDuration(5000);
        animator.addListener(listenerAdapter);
        animator.start();
    }


    private void playAnimationSet() {
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(textView, "translationX", textView.getTranslationY(), 300f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(textView, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(fadeInOut).after(moveIn);
        animSet.setDuration(5000);

        animSet.start();
    }

    @SuppressLint("WrongConstant")
    @OnClick(R.id.textView)
    public void onViewClicked() {

        ValueAnimator animator = ObjectAnimator.ofObject(new ColorEvaluator(), "#0000FF", "#FF0000");
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setTextColor(Color.parseColor((String) animation.getAnimatedValue()));

            }
        });
        animator.start();
    }


    private AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            Log.d(TAG, "onAnimationEnd: ");
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            super.onAnimationRepeat(animation);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            Log.d(TAG, "onAnimationStart: ");
        }

        @Override
        public void onAnimationPause(Animator animation) {
            super.onAnimationPause(animation);
        }

        @Override
        public void onAnimationResume(Animator animation) {
            super.onAnimationResume(animation);
        }
        
    };
}
