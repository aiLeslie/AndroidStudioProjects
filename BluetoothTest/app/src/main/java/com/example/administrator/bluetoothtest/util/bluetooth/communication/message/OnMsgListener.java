package com.example.administrator.bluetoothtest.util.bluetooth.communication.message;

/**
 * 信息接收监听器
 * @param <M>
 */
public interface OnMsgListener<M> {
    void OnMsg(final M msg);
}
