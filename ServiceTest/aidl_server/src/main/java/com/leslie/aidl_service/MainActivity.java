package com.leslie.aidl_service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.editText)
    EditText editText;
    private MyService.AidlStubImpl iBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinder = (MyService.AidlStubImpl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bindService(new Intent(this, MyService.class), connection, BIND_AUTO_CREATE);

    }

    @OnClick(R.id.btn_get)
    void fetchData() {
        try {
            editText.setText(iBinder.getData());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_set)
    void setData() {

        try {
            String data = editText.getText().toString();
            iBinder.setData(data);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
