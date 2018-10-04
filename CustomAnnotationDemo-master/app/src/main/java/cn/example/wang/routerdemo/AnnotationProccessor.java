package cn.example.wang.routerdemo;


import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import cn.example.wang.routerdemo.annotation.MyCar;
import cn.example.wang.routerdemo.annotation.MyTag;
import cn.example.wang.routerdemo.bean.Car;

/**
 * Created by WANG on 17/11/21.
 *
 */

public class AnnotationProccessor {

    private static AnnotationProccessor annotationProccessor;
    public static AnnotationProccessor instance(){
        synchronized (AnnotationProccessor.class){
            if(annotationProccessor == null){
                annotationProccessor = new AnnotationProccessor();
            }
            return annotationProccessor;
        }
    }

    public void inject(Object o){
        Class<?> aClass = o.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field:declaredFields) {
           /* if(field.getName().equals("car")){
                Annotation[] annotations = field.getDeclaredAnnotations();
                for (int i = 0; i <annotations.length; i++) {
                    Annotation annotation = annotations[i];
                    Class<? extends Annotation> aClass1 = annotation.annotationType();
                    Log.e("WANG","AnnotationProccessor.MyCar"+aClass1 );
                }
                MyCar annotation = field.getAnnotation(MyCar.class);
                MyTag[] value = annotation.value();
                for (int i = 0; i <value.length; i++) {
                    MyTag myTag = value[i];
                    Log.e("WANG","AnnotationProccessor.MyTag   name  value   is  "+myTag.name() );
                }
            }*/
            if(field.getName().equals("car") && field.isAnnotationPresent(MyTag.class)) {
                MyTag annotation = field.getAnnotation(MyTag.class);
                field.getDeclaredAnnotations();
                Class<?> type = field.getType();
                if(Car.class.equals(type)) {
                    try {
                        field.setAccessible(true);
                        field.set(o, new Car(annotation.name(), annotation.size()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
