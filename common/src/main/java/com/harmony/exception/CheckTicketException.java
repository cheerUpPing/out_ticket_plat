package com.harmony.exception;

/**
 * 业务异常
 * Created by cm on 2017/1/12.
 */
public class CheckTicketException extends RuntimeException{
    private String errNo;

    private String errMsg;

    public CheckTicketException(){}

    public CheckTicketException(String errMsg){
        super(errMsg);
        this.errMsg = errMsg;
    }

    public CheckTicketException(String errNo,String errMsg){
        super(errMsg);
        this.errMsg = errMsg;
        this.errNo = errNo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrNo() {
        return errNo;
    }

    public void setErrNo(String errNo) {
        this.errNo = errNo;
    }
}
