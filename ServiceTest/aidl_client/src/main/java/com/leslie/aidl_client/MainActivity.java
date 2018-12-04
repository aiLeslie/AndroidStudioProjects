package com.leslie.aidl_client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.leslie.aidl_service.IMyAidlInterface;
import com.leslie.aidl_test.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.editText)
    EditText editText;
    private IMyAidlInterface iMyAidlInterface;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iMyAidlInterface = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        requestServer();

    }

    private void requestServer() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.leslie.aidl_service", "com.leslie.aidl_service.MainActivity"));
        startActivity(intent);

        connectServer();
    }


    private void connectServer() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.leslie.aidl_service", "com.leslie.aidl_service.MyService"));
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @OnClick(R.id.btn_get)
    void fetchData() {
        try {
            editText.setText(iMyAidlInterface.getData());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_set)
    void setData() {

        try {
            String data = editText.getText().toString();
            iMyAidlInterface.setData(data);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
