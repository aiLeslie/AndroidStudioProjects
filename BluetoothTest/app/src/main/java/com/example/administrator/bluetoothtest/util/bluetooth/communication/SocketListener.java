package com.example.administrator.bluetoothtest.util.bluetooth.communication;

/**
 * 判定Socket连接是否成功监听器
 * @param <M>
 */
public interface SocketListener<M> {
    void onSucceed(final M m);
    void onFail(final M m);
}
