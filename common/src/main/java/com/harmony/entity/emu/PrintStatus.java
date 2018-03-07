package com.harmony.entity.emu;

import java.io.Serializable;

/**
 * 2017/9/15 9:53.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 票机状态 -- 对应票机currentOperating字段
 */
public enum PrintStatus implements Serializable {
    /**
     * 离线
     */
    stop(0),
    /**
     * 在线
     */
    on(1);

    private int status;

    PrintStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
