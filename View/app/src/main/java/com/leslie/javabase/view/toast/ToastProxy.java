package com.leslie.javabase.view.toast;

import android.content.Context;
import android.widget.Toast;

import com.leslie.javabase.view.R;


public class ToastProxy {
    private Toast mToast;

    public ToastProxy(Context context) {
        setContext(context);
    }

    public void setContext(Context context) {
        mToast = Toast.makeText(context,new String(),Toast.LENGTH_SHORT);

    }

    public ToastProxy setText(CharSequence text) {
        mToast.setText(text);
        mToast.show();
        return this;
    }




}
