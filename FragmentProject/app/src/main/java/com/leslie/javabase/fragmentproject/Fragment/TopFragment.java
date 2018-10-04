package com.leslie.javabase.fragmentproject.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.leslie.javabase.fragmentproject.R;
import com.leslie.javabase.fragmentproject.activities.MainActivity;

import java.lang.reflect.InvocationTargetException;

public class TopFragment extends BaseFragment {
    private static final String TAG = "TopFragment";
    public static String Interface_Name = "interface";
    public String impInterface;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toast.makeText(context, "onAttch", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "onActivityResult", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.top_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(),"onActivityCreate",Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onActivityCreated: ");
        if (methodsManage != null) {
            try {
                methodsManage.method(MainActivity.class.getName() + "." + Interface_Name).invoke(getContext(), new Object[]{"onActivityCreated"});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(),"onDestroyView",Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getActivity(),"onDestroy",Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Toast.makeText(getActivity(),"onDetach",Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDetach: ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "onActivityResult", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onActivityResult: ");
    }
}
