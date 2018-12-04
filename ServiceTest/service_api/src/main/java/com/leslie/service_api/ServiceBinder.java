package com.leslie.service_api;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


import com.leslie.service_annotation.Binder;
import com.leslie.service_annotation.Connection;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


public class ServiceBinder {

    public static <A extends Activity> void bind(A activity) {
        Class<? extends Activity> aClass = activity.getClass();
        // 连接对象
        ConnectionLeslie connection = null;
        // activity中所有成员变量
        Field[] fields = aClass.getDeclaredFields();

        Class<?> sClass = null;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Binder.class)) {
                connection = new ConnectionLeslie(activity, field);
                sClass = field.getAnnotation(Binder.class).serviceClass();
                activity.bindService(new Intent(activity, sClass), connection, Context.BIND_AUTO_CREATE);

                for (Field conn : fields) {
                    if (conn.isAnnotationPresent(Connection.class) && sClass.equals(conn.getAnnotation(Connection.class).serviceClass())) {

                        try {
                            conn.setAccessible(true);
                            conn.set(activity, connection);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }


    }

    protected static class ConnectionLeslie implements ServiceConnection {
        private WeakReference<Field> binder;
        private WeakReference<Activity> activty;

        public ConnectionLeslie(Activity activty, Field binder) {
            this.binder = new WeakReference<>(binder);
            this.activty = new WeakReference<>(activty);
        }


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            try {
                binder.get().setAccessible(true);
                binder.get().set(activty.get(), binder.get().getType().cast(service));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
