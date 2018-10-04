package com.leslie.javabase.interfacetest.struct;

public abstract class HasParamHasResultFunction<Param, Result> extends Function {
    public HasParamHasResultFunction(String mFunctionName) {
        super(mFunctionName);
    }

    public abstract Result function(Param data);

}
