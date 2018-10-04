package com.leslie.javabase.interfacetest.struct;

public abstract class HasParamNoResultFunction<Param> extends Function {
    public HasParamNoResultFunction(String mFunctionName) {
        super(mFunctionName);
    }

    public abstract void function(Param data);

}
