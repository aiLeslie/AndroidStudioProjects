package com.hjq.demo.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hjq.baselibrary.utils.EditTextInputHelper;
import com.hjq.baselibrary.widget.CountdownView;
import com.hjq.demo.R;
import com.hjq.demo.common.CommonActivity;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;

/**
 *    author : HJQ
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 注册界面
 */
public class RegisterActivity extends CommonActivity
        implements View.OnClickListener {

    @BindView(R.id.et_register_phone)
    EditText mPhoneView;
    @BindView(R.id.cv_register_countdown)
    CountdownView mCountdownView;

    @BindView(R.id.et_register_code)
    EditText mCodeView;

    @BindView(R.id.et_register_password1)
    EditText mPasswordView1;
    @BindView(R.id.et_register_password2)
    EditText mPasswordView2;

    @BindView(R.id.btn_register_commit)
    Button mCommitView;

    private EditTextInputHelper mEditTextInputHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_register_title;
    }

    @Override
    protected void initView() {
        mCountdownView.setOnClickListener(this);
        mCommitView.setOnClickListener(this);

        mEditTextInputHelper = new EditTextInputHelper(mCommitView, false);
        mEditTextInputHelper.addViews(mPhoneView, mCodeView, mPasswordView1, mPasswordView2);
    }

    @Override
    protected void initData() {

    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        if (v == mCountdownView) { //获取验证码

            if (mPhoneView.getText().toString().length() != 11) {
                // 重置验证码倒计时控件
                mCountdownView.resetState();
                ToastUtils.show(getResources().getString(R.string.phone_input_error));
                return;
            }

            ToastUtils.show(getResources().getString(R.string.countdown_code_send_succeed));

        }else if (v == mCommitView) { //提交注册

            if (mPhoneView.getText().toString().length() != 11) {
                ToastUtils.show(getResources().getString(R.string.phone_input_error));
                return;
            }

            if (!mPasswordView1.getText().toString().equals(mPasswordView2.getText().toString())) {
                ToastUtils.show(getResources().getString(R.string.two_password_input_error));
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        mEditTextInputHelper.removeViews();
        super.onDestroy();
    }
}