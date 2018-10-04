package com.leslie.javabase.fragmentproject.Fragment;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 */
public class MethodsManage {
    private static final MethodsManage ourInstance = new MethodsManage();
    private HashMap<String, Method> methods = new HashMap<>();

    public static MethodsManage getInstance() {
        return ourInstance;
    }

    private MethodsManage() {

    }

    public void addMethod(String methodName, Class<?> classType, Class<?>... parameterTypes) {
        try {
            Method method = classType.getDeclaredMethod(methodName, parameterTypes);
            methods.put(methodName, method);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Method method(String methodName) {
        if (methods.containsKey(methodName)) {
            return null;
        }
        return methods.get(methodName);
    }

    private String convertName(String methodName, Class<?> classType) {
        return classType.getName() + "." + methodName;
    }
}
