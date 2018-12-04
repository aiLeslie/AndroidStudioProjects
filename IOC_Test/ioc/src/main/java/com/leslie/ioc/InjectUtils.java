package com.leslie.ioc;

import android.app.Activity;

import com.leslie.ioc.annotations.ContentView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InjectUtils {
    public static void inject(Activity activity) {
        injectLayout(activity);
        injectView(activity);
        injectEvents(activity);
    }

    private static void injectLayout(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        ContentView contentViewAnnotation = aClass.getAnnotation(ContentView.class);
        if (contentViewAnnotation != null) {

            try {
                int layoutId = contentViewAnnotation.value();
                Method method = aClass.getMethod("setContentView", int.class);
                method.invoke(activity, layoutId);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }
    private static void injectView(Activity activity) {
    }
    private static void injectEvents(Activity activity) {
    }
}
