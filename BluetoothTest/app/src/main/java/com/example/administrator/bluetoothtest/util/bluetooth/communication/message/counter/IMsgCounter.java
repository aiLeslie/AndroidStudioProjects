package com.example.administrator.bluetoothtest.util.bluetooth.communication.message.counter;

import com.example.administrator.bluetoothtest.util.bluetooth.communication.message.task.AbstractTask;

public interface IMsgCounter<T extends AbstractTask, D> {
    // 数据加载
    void add(D t);

    // 数据包检验
    boolean verify(T task);

    // 计数复位
    void resetCount();
}
