package com.leslie.clocktest;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * MM   6
 * MMM  6月
 * MMMM 六月
 * EE   周六
 * EEEE 星期六
 * aa   上午/下午
 * HH   24小时制
 * hh   12小时制
 * dd   日
 * yyyy 年份
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.textView)
    TextView textView;

    private Calendar mTime = new GregorianCalendar();
    private final CharSequence mFormat12 = "MM月dd日 EEEE hh:mm";
    private final CharSequence mFormat24 = "MM月dd日 EEEE HH:mm";
    private CharSequence mFormat = mFormat24;
    private boolean mStopTicking = false;
    private Handler handler = new Handler();
    private Chronograph chronograph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTicker.run();

        chronograph = new Chronograph(new Chronograph.OnChangeListener() {
            @Override
            public void onChange(long count) {
                textView.setTextColor(RandomColorUtils.getColor());
                textView.setText(String.valueOf(count));
                MyScaleAnimation.start(textView, 4);
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStopTicking = true;
        chronograph.close();
    }

    private final Runnable mTicker = new Runnable() {
        public void run() {
            if (mStopTicking) {
                return; // Test disabled the clock ticks
            }

            onTimeChanged();

            long now = SystemClock.uptimeMillis();

            long next = now + (1000 - now % 1000);

            handler.postAtTime(mTicker, next);
        }
    };

    private void onTimeChanged() {

        mTime.setTimeInMillis(System.currentTimeMillis());
        setTitle(DateFormat.format(mFormat, mTime));
    }


    @OnClick(R.id.textView)
    public void onViewClicked() {
    }

    public void stop(View view) {
        MyScaleAnimation.start(view, 1.5f);
        chronograph.stop();
    }

    public void start(View view) {
        MyScaleAnimation.start(view, 1.5f);
        chronograph.start();
    }

    public void pause(View view) {
        MyScaleAnimation.start(view, 1.5f);
        chronograph.pause();
    }
}
