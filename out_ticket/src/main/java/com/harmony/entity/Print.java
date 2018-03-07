package com.harmony.entity;

import java.io.Serializable;

/**
 * 2017/9/14 10:05.
 * <p>
 * Email: cheerUpPing@163.com
 *
 * 配置文件的json对象
 */
public class Print implements Serializable {

    private String name;

    private String ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
