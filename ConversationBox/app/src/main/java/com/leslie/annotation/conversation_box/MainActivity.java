package com.leslie.annotation.conversation_box;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.leslie.annotation.conversation_box.dialog.ToastUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    protected ToastUtil toastUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       init();
    }

    private void init() {
        bindViews();
        toastUtil = new ToastUtil(this);
    }


    private void bindViews() {
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_toast)
    protected void showToast() {
//        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        toastUtil.showToast("hello");

    }

    @OnClick(R.id.btn_dialog)
    protected void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title")
                .setMessage("Dialog content.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }
    @OnClick(R.id.btn_Snackbar)
    protected void showSnackbar(View view) {
        Snackbar.make(view, "hello",Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();


    }


}
