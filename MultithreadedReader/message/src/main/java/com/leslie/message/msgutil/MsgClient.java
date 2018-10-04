package com.leslie.message.msgutil;


import android.content.Context;
import android.content.Intent;


import java.util.List;

public class MsgClient {
    private String phoneNum;
    private Context mContext;

    public MsgClient(Context mContext) {
        this.mContext = mContext;


    }


    public MsgClient phoneNum(String phoneNum) {
        checkNum(phoneNum);
        return this;
    }

    private void checkNum(String phoneNum) {
        if (phoneNum.startsWith("tel:")){
            this.phoneNum = phoneNum;
        } else {
            this.phoneNum = "tel:" + phoneNum;
        }
    }

    public boolean sendMsg(Msg msg) {
        if (msg.isEmpty()) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        //address为数据表中存放电话号码的字段
        intent.putExtra("address", phoneNum);
        // 要发短信的手机号，如果没有传空串就行
        intent.putExtra("sms_body", msg.getContent());//短信内容
        mContext.startActivity(intent);


        return true;


    }


}
