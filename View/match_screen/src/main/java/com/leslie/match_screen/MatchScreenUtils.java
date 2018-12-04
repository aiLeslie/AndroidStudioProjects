package com.leslie.match_screen;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class MatchScreenUtils {
    private MatchScreenUtils mUtils;

    private int mDisplayWidth;
    private int mDisplayHeight;


    private MatchScreenUtils(Context context) {
        WindowManager windowManager =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mDisplayWidth = displayMetrics.widthPixels;
        mDisplayHeight = displayMetrics.heightPixels;

        if (mDisplayWidth > mDisplayHeight) {
            int temp = mDisplayWidth;
            mDisplayWidth = mDisplayHeight;
            mDisplayHeight = temp;
        }
    }

    public MatchScreenUtils getInstance(Context context) {
        if (mUtils == null) {
            mUtils = new MatchScreenUtils(context);
        }
        return mUtils;
    }


    public float getHoizontalScale() {
        return  mDisplayHeight / mDisplayWidth;
    }

    public float getVerticalScale() {
        return  mDisplayWidth / mDisplayHeight;
    }
}
