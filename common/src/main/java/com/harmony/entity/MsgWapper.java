package com.harmony.entity;

import java.io.Serializable;

/**
 * 2017/9/13 14:52.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * socket通信类
 */
public class MsgWapper implements Serializable{

    //动作码 出票 打印报表 打印票
    private String actionName;

    //每次请求的标示
    private String uuid;

    private String printIp;

    //票类型
    private String ticketType;

    private String result = "0000";

    //当result="0000" 那么为返回数据，否则为错误信息
    private Object data;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPrintIp() {
        return printIp;
    }

    public void setPrintIp(String printIp) {
        this.printIp = printIp;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
