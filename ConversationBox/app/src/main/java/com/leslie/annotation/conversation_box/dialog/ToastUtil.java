package com.leslie.annotation.conversation_box.dialog;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ToastUtil {
    private  Toast toast;

    public ToastUtil(Context context) {
        this.contextWR = new WeakReference<Context>(context);
    }

    private WeakReference<Context> contextWR;

    public  void showToast(String content) {
        if (contextWR.get() == null) {
            throw new RuntimeException("Context Not Pointer Exception");
        }

        if (toast == null) {
            toast = Toast.makeText(contextWR.get(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}
