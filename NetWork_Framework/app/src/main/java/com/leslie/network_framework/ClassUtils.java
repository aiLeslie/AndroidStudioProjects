package com.leslie.network_framework;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtils {
    public static Class<?> genericity(Object obj, int index) {
        // 获取原始类型,参数化类型,数组类型,类型变量,基本类型
        Type type = obj.getClass().getGenericSuperclass();
        // 获取参数化类型
        Type[] args = ((ParameterizedType) type).getActualTypeArguments();
        return (Class<?>) args[index];
    }
}
