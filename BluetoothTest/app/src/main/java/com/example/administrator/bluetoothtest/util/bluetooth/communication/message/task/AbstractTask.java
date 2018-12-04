package com.example.administrator.bluetoothtest.util.bluetooth.communication.message.task;

public abstract class AbstractTask<D>{
    protected D start;//数据包开头
    protected D length;//数据包长度
    protected D id;//任务id
    protected D[] importanceData;//重要的传输数据
    protected D checkAnd;//校验和
    protected D end;//数据包结尾
    protected D[] body;//表示任务的所有属性的数组

    public abstract boolean verify();

    public interface Handler<T extends AbstractTask>{
        void handleTask(T task);
    }
}
