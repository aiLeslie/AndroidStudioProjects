package com.leslie.javabase.view.toast;

import android.content.Context;
import android.widget.Toast;


public class ToastProxy {
    private Context context;
    private Toast mToast;

    public ToastProxy(Context context) {
        setContext(context);
    }

    public void setContext(Context context) {
        this.context = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

    }

    public ToastProxy setText(CharSequence text) {
        mToast.setText(text);
        mToast.show();
        return this;
    }




}
