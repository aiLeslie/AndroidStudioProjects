package com.leslie.javabase.fragmentproject.Fragment;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    protected MethodsManage methodsManage = MethodsManage.getInstance();

    public void setMethodsManage(MethodsManage methodsManage) {
        this.methodsManage = methodsManage;
    }
}
