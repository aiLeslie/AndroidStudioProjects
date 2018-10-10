package com.leslie.fixtest.fix;

import com.leslie.fixtest.dex.Replace;

public class Calc {
    @Replace(clazz = "com.leslie.fixtest.error.Calc", method = "startCalc")
    public int startCalc() {
        int divide = 10;
        int num = 1;
         return divide / num;
    }
}
