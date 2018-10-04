package com.leslie.javabase.interfacetest.fragment;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.leslie.javabase.interfacetest.struct.FunctionManage;

public abstract class BaseFragment extends Fragment {
    protected FunctionManage mFManage;


    public void setmFManage(FunctionManage mFManage) {
        this.mFManage = mFManage;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
