package com.harmony.entity.emu;

import java.io.Serializable;

/**
 * 2017/9/14 13:55.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 是否是请求
 * 0 是请求 1是响应
 */
public enum IsRequest implements Serializable {

    IS("0"), NO("1");

    private String value = "0";

    IsRequest(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
