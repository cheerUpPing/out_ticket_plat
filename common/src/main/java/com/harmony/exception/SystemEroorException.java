package com.harmony.exception;

/**
 * Created by cm on 2017/1/5.
 */
public class SystemEroorException extends RuntimeException{
    private String errNo;

    private String errMsg;

    public SystemEroorException(){}

    public SystemEroorException(String errMsg){
        super(errMsg);
        this.errMsg = errMsg;
    }

    public SystemEroorException(Throwable e) {
        super(e);
    }

    public SystemEroorException(String errNo,String errMsg){
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
