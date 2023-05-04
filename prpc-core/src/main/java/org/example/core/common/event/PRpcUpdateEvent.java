package org.example.core.common.event;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
public class PRpcUpdateEvent implements PRpcEvent{

    private Object data;

    public PRpcUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public PRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
