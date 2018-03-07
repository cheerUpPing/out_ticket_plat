package com.harmony.entity;

/**
 * 系统异常信息数据实体
 *
 * @author Yoan.Liang
 * @version 1.0
 *
 */
public class SystemExceptionDTO implements BaseEntity{

    /** 类唯一标识 **/
    private static final long serialVersionUID = 8790472966220143207L;

    /** 异常信息ID **/
    private Long sid;

    /** 异常提示信息 **/
    
    private String message;

    /** 异常堆栈 **/
    
    private String throwable;

    /** 异常时间 **/
    
    private String systemTime;

    /** 是否已提示 **/
    
    private String isRead;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThrowable() {
        return throwable;
    }

    public void setThrowable(String throwable) {
        this.throwable = throwable;
    }

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

}
