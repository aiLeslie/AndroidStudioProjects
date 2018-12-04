package com.hjq.baselibrary.utils;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 *    author : HJQ
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 多个CompoundButton选中处理辅助类
 */
public final class RadioButtonGroupHelper implements CompoundButton.OnCheckedChangeListener {

    private List<RadioButton> mViewSet;//RadioButton集合

    private OnCheckedChangeListener mListener;//多个RadioButton监听对象

    public RadioButtonGroupHelper(RadioButton... groups) {
        mViewSet = new ArrayList<>(groups.length - 1);

        for (RadioButton view : groups) {
            // 如果这个RadioButton没有设置id的话
            if (view.getId() == View.NO_ID) {
                throw new IllegalArgumentException("The resource id must be set for the RadioButton");
            }
            view.setOnCheckedChangeListener(this);
            mViewSet.add(view);
        }
    }

    public RadioButtonGroupHelper(View rootView, @IdRes int... ids) {
        mViewSet = new ArrayList<>(ids.length - 1);
        for (int id : ids) {
            RadioButton view = rootView.findViewById(id);
            view.setOnCheckedChangeListener(this);
            mViewSet.add(view);
        }
    }

    private boolean mTag; // 监听标记，避免重复回调

    /**
     * {@link CompoundButton.OnCheckedChangeListener}
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && !mTag) {
            mTag = true;
            for (CompoundButton view : mViewSet) {
                if (view != buttonView && view.isChecked()) {
                    // 这个 API 会触发监听事件
                    view.setChecked(false);
                }
            }
            if (mListener != null) {
                mListener.onCheckedChanged((RadioButton) buttonView, buttonView.getId());
            }
            mTag = false;
        }
    }

    /**
     * 移除监听，避免内存泄露
     */
    public void removeViews() {
        if (mViewSet == null) return;

        for (CompoundButton view : mViewSet) {
            view.setOnCheckedChangeListener(null);
        }
        mViewSet.clear();
        mViewSet = null;
    }

    /**
     * 取消选中
     */
    public void clearCheck() {
        for (CompoundButton view : mViewSet) {
            if (view.isChecked()) {
                view.setChecked(false);
            }
        }
    }

    /**
     * 设置多个RadioButton的监听
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener l)  {
        mListener = l;
    }

    /**
     * 多个CompoundButton选中监听
     */
    public interface OnCheckedChangeListener {
        /**
         * 被选中的CompoundButton对象
         *
         * @param radioButton            选中的RadioButton
         * @param checkedId             选中的资源id
         */
        void onCheckedChanged(RadioButton radioButton, @IdRes int checkedId);
    }
}