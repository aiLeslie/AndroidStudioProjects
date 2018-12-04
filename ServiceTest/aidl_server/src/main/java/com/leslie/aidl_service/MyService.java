package com.leslie.aidl_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {
    private String data;
    private AidlStubImpl aidlStub = new AidlStubImpl();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return aidlStub;
    }

    class AidlStubImpl extends IMyAidlInterface.Stub {
        @Override
        public void setData(String data) throws RemoteException {
            MyService.this.data = data;
        }

        @Override
        public String getData() throws RemoteException {
            return MyService.this.data;
        }
    }

}
