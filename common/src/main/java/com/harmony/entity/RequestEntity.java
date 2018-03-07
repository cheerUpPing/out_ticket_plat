package com.harmony.entity;

import com.harmony.entity.emu.IsRequest;
import com.harmony.entity.emu.PrintType;

import java.io.Serializable;

/**
 * 2017/9/14 13:46.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 请求实体
 */
public class RequestEntity<T> extends Communication<T> implements Serializable {

    private String printIp;

    private PrintType printType;

    @Deprecated
    public RequestEntity() {
        this.setIsRequest(IsRequest.IS);
    }

    public RequestEntity(String actionCode, T data, String ip) {
        this.setIsRequest(IsRequest.IS);
        this.setActionCode(actionCode);
        this.setData(data);
        this.setIp(ip);
    }

    public RequestEntity(String actionCode, T data, String printIp, String ip) {
        this.setIsRequest(IsRequest.IS);
        this.setActionCode(actionCode);
        this.setData(data);
        this.setIp(ip);
        this.printIp = printIp;
    }

    public String getPrintIp() {
        return printIp;
    }

    public void setPrintIp(String printIp) {
        this.printIp = printIp;
    }

    public PrintType getPrintType() {
        return printType;
    }

    public void setPrintType(PrintType printType) {
        this.printType = printType;
    }


}
