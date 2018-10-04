package cn.example.wang.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by WANG on 2018/8/7.
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
public @interface MyAnnotion {

    String value() default "ssssss";

}
