package com.leslie.message.msgutil;

import android.text.TextUtils;

public class Msg {
    // 信息内容
    private String content;

    // 私有构造方法
    private Msg() {
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.content);
    }

    public String getContent() {
        return content;
    }

    /**
     * 建立Msg对象工具类
     */
    public static class Builder {

        private String content;
        Msg msg = new Msg();

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Msg build() {
            msg.content = this.content;
            return msg;
        }
    }
}
