package com.harmony.entity.emu;

import java.io.Serializable;

/**
 * 2017/9/14 14:00.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 票机类型
 */
public enum PrintType implements Serializable{

    C8("C8"), C8USB("C8USB"), CP86("CP86"), SC4("SC4"), SC6("SC6"), HS("HS"), LOT("LOT"), LDR("LDR"), YTD("YTD");

    private String type = null;

    PrintType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
