package com.harmony.entity;

import com.harmony.entity.emu.IsRequest;

import java.io.Serializable;

/**
 * 2017/9/14 13:46.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 响应实体
 */
public class ResponseEntity<T> extends Communication<T> implements Serializable {

    private String result = "0000";


    @Deprecated
    public ResponseEntity() {
        this.setIsRequest(IsRequest.NO);
    }

    public ResponseEntity(String actionCode, T data, String ip) {
        this.setIsRequest(IsRequest.NO);
        this.setActionCode(actionCode);
        this.setData(data);
        this.setIp(ip);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}
