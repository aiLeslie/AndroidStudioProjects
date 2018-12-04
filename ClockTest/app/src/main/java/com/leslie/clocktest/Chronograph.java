package com.leslie.clocktest;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chronograph {
    private Timer timer = new Timer();
    private final char STOP_STATUS = 0;
    private final char START_STATUS = 1;
    private final char PAUSE_STATUS = 2;
    private char currStatus = STOP_STATUS;
    private long mCount;
    private ExecutorService mSingleThreadPool = Executors.newSingleThreadExecutor();
    private OnChangeListener onChangeListener;

    public Chronograph() {
    }


    public Chronograph(OnChangeListener onChangeListener) {
        setOnChangeListener(onChangeListener);
    }

    public Chronograph setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
        return this;
    }

    private final Runnable mTicker = new Runnable() {
        public void run() {
            synchronized (Chronograph.this){
            switch (currStatus) {

                case STOP_STATUS:
                    return;
                case PAUSE_STATUS:
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mTicker.run();
                        }
                    }, 100L);
                    break;
                case START_STATUS:
                    mCount++;
                    if (onChangeListener != null) {

                        onChangeListener.onChangeForUI(mCount);
                    }

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mTicker.run();
                        }
                    }, 1000L);
                    break;
            }
        }

    }
};

public synchronized void start(){
        switch(currStatus){

        case STOP_STATUS:
        mSingleThreadPool.execute(mTicker);

        case PAUSE_STATUS:
        currStatus=START_STATUS;

        break;

        case START_STATUS:
        break;
        }

        }

public synchronized void stop(){
        switch(currStatus){

        case STOP_STATUS:
        break;

        case PAUSE_STATUS:
        case START_STATUS:
        reset();
        currStatus=STOP_STATUS;
        break;
        }

        }

public synchronized void pause(){
        switch(currStatus){
        case STOP_STATUS:
        break;
        case PAUSE_STATUS:
        break;
        case START_STATUS:
        currStatus=PAUSE_STATUS;
        break;
        }

        }

public void close(){
        stop();
        if(!mSingleThreadPool.isShutdown())mSingleThreadPool.shutdownNow();

        }

private void reset(){
        mCount=0;
        if(onChangeListener!=null){

        onChangeListener.onChangeForUI(mCount);
        }
        }

public abstract static class OnChangeListener {
    private Handler handler = new Handler(Looper.getMainLooper());

    public abstract void onChange(long count);

    void onChangeForUI(final long count) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onChange(count);
            }
        });

    }


}
}
