package com.leslie.javabase.interfacetest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leslie.javabase.interfacetest.R;

public class MyFragment extends BaseFragment {
    public static final String className = MyFragment.class.getName();
    // 定义接口
    public static final String interfaceName = className + ".onClick()";
    private static final String TAG = "MyFragment";
    public EditText editText;
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_button, container, false);

        Button btn = contentView.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonProcessing();
            }
        });
        editText = contentView.findViewById(R.id.et);

        return contentView;
    }


    private void onButtonProcessing() {
        /**********************调用接口方法**********************/

        /**
         * 调用有参数有返回值的方法
         */
//        String result =  mFManage.invokeFunction(interfaceName, String.class, "fragment",null);
//        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        /**
         * 调用有参数无返回值的方法
         */
        mFManage.invokeFunction(interfaceName, String.class, "fragment");
        /**
         * 调用无参数无返回值的方法
         */
//        mFManage.invokeFunction(interfaceName);

    }
}
