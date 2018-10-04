package com.leslie.javabase.interfacetest.struct;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

/**
 * 单例模式
 * 方法管理器
 */
public class FunctionManage {
    private static final String TAG = "FunctionManage";
    private static FunctionManage _instance;
    private HashMap<String, NoParamNoResultFunction> nPnRFuns = new HashMap<>();
    private HashMap<String, HasParamNoResultFunction> hPnRFuns = new HashMap<>();
    private HashMap<String, HasParamHasResultFunction> hPhRFuns = new HashMap<>();

    /**
     * 私有构造方法
     */
    private FunctionManage() {
    }

    /**
     * 得到方法管理器对象
     *
     * @return FunctionManage对象
     */
    public static FunctionManage getInstance() {
        if (_instance == null) {
            _instance = new FunctionManage();

        }
        return _instance;
    }
    /*************************方法添加到管理器中*************************/
    /**
     * 添加无参数无返回值的方法
     *
     * @param fun
     * @return FunctionManage对象
     */
    public FunctionManage addFunction(NoParamNoResultFunction fun) {
        nPnRFuns.put(fun.mFunctionName, fun);
        return this;
    }

    /**
     * 添加有参数无返回值的方法
     *
     * @param fun
     * @return FunctionManage对象
     */
    public FunctionManage addFunction(HasParamNoResultFunction fun) {
        hPnRFuns.put(fun.mFunctionName, fun);
        return this;
    }


    /**
     * 添加有参数有返回值的方法
     *
     * @param fun
     * @return
     */
    public FunctionManage addFunction(HasParamHasResultFunction fun) {
        hPhRFuns.put(fun.mFunctionName, fun);
        return this;
    }


    /*************************调用管理器中方法*************************/

    /**
     * 调用无参数无返回值的方法
     * @param funName
     * @return
     */
    public void invokeFunction(String funName) {
        if (!nPnRFuns.containsKey(funName)) {
            return;
        }
      nPnRFuns.get(funName).function();
    }


    /**
     * 调用有参数无返回值的方法
     * @param funName
     * @param paramType
     * @param param
     */
    public void invokeFunction(String funName, Class<?> paramType, Object param) {
        if (!hPnRFuns.containsKey(funName)) {
            return;
        }
        hPnRFuns.get(funName).function(paramType.cast(param));
    }

    /**
     * 调用有参数有返回值的方法
     * @param funName
     * @param paramType
     * @param param
     * @return
     */
    public <Result> Result invokeFunction(String funName, Class<?> paramType, Object param, Class<?> resultType) {
        if (!hPhRFuns.containsKey(funName)) {
            return null;
        }
        Log.i(TAG, "invokeFunction: ");
        return (Result) hPhRFuns.get(funName).function(paramType.cast(param));
    }


}
