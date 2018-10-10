package com.leslie.message.msgutil;

import android.text.TextUtils;

public class Msg {
    // 信息内容
    private String content;
    private String phoneNum;

    // 私有构造方法
    private Msg() {
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.content) || TextUtils.isEmpty(this.content);
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getContent() {
        return content;
    }

    /**
     * 建立Msg对象工具类
     */
    public static class Builder {

        private String content;
        private String phoneNum;
        Msg msg = new Msg();

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder phoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
            return this;
        }
         void checkNum(String phoneNum) {
            if (phoneNum.startsWith("tel:")){
                this.phoneNum = phoneNum;
            } else {
                this.phoneNum = "tel:" + phoneNum;
            }
        }

        public Msg build() {
            msg.content = this.content;
            msg.phoneNum = this.phoneNum;
            return msg;
        }
    }
}
