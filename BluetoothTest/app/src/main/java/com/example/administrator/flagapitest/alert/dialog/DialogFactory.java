package com.example.administrator.flagapitest.alert.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.lang.ref.WeakReference;

public class DialogFactory {
    private WeakReference<Context> contextReference;

    public DialogFactory(Context context) {
        this.contextReference = new WeakReference<>(context);
    }

    public ProgressDialog createProgressDialog(String title, String msg, boolean cancelabe) {
        // 实例化进度条对话框
        final ProgressDialog dialog = new ProgressDialog(contextReference.get());
        // 设置标题
        dialog.setTitle(title);
        // 设置信息
        dialog.setMessage(msg);
        // 是否允许取消对话框
        dialog.setCancelable(cancelabe);
        // 显示对话框
        dialog.show();
        return dialog;
    }

    public AlertDialog createAlernDialog(String title, String msg, boolean cancelabe, DialogInterface.OnClickListener listener) {
        // 实例化警告对话框建造者
        final AlertDialog.Builder builder = new AlertDialog.Builder(contextReference.get());
        // 设置标题
        builder.setTitle(title);
        // 设置信息
        builder.setMessage(msg);
        // 是否允许取消对话框
        builder.setCancelable(cancelabe);
        // 设置回调函数
        builder.setPositiveButton("ok", listener);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.show();

    }
}
