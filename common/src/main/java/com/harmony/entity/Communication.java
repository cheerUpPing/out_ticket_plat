package com.harmony.entity;

import com.harmony.entity.emu.IsRequest;

import java.io.Serializable;

/**
 * 2017/9/14 13:48.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 请求/响应实体父类
 * 泛型对象必须实现序列化
 */
public abstract class Communication<T> implements Serializable {

    //标记是否是请求  0 是请求 1 是响应
    private IsRequest isRequest = IsRequest.IS;

    //每次请求/响应的标示
    private String uuid;

    private String actionCode;

    //交流信息的地址来源 ip
    private String ip;

    private T data;

    public IsRequest getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(IsRequest isRequest) {
        this.isRequest = isRequest;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
