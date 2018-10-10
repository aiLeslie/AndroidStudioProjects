package com.leslie.message.msgutil;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;


import java.util.List;

public class MsgClient {
    private static final String TAG = "MsgClient";

    private Context mContext;

    public MsgClient(Context mContext) {
        this.mContext = mContext;

    }

    public boolean sendMsg(Msg msg) {
        if (msg.isEmpty()) {
            return false;
        }

        sendSMS(msg);

        return true;


    }


    private void sendSMS(Msg msg) {
        PendingIntent pi = PendingIntent.getActivity(mContext,
                0,
                new Intent(),
                0);
        Log.i(TAG, "sendSMS: will init SMS Manager");
        SmsManager sms = SmsManager.getDefault();

        Log.i(TAG, "sendSMS: will send SMS");
        sms.sendTextMessage(msg.getPhoneNum(), null, msg.getContent(), pi, null);
    }


    private void smsView(Msg msg) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        //address为数据表中存放电话号码的字段
        intent.putExtra("address", msg.getPhoneNum());
        // 要发短信的手机号，如果没有传空串就行
        intent.putExtra("sms_body", msg.getContent());//短信内容
        mContext.startActivity(intent);
    }


}
