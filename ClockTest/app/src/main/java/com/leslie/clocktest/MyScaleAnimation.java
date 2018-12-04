package com.leslie.clocktest;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class MyScaleAnimation {
    public static void start(final View view, float scale) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f,0.5f);
        alphaAnimation.setDuration(800L);
        view.startAnimation(alphaAnimation);

        AnimatorSet set = new AnimatorSet();
        ValueAnimator scaleAnimator1 = ValueAnimator.ofFloat(1, scale);
        scaleAnimator1.setDuration(600L);
        scaleAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setScaleX((float)animation.getAnimatedValue());
                view.setScaleY((float)animation.getAnimatedValue());
            }
        });



        ValueAnimator scaleAnimator2 = ValueAnimator.ofFloat(scale, 1);
        scaleAnimator2.setDuration(200L);
        scaleAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setScaleX((float)animation.getAnimatedValue());
                view.setScaleY((float)animation.getAnimatedValue());
            }
        });



        set.play(scaleAnimator1).before(scaleAnimator2);

        set.start();

    }
}
