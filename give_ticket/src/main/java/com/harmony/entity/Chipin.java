package com.harmony.entity;

import java.io.Serializable;

public class Chipin implements Serializable {

    private static final long serialVersionUID = 7002573674012816056L;

    private String chipinNums;//投注号
    private int multiple;//投注倍数
    private int bets;//投注金额
    private String manner;//投注方式
    private String issue;//期号
    private String spValue; //sp值

    public String getChipinNums() {
        return chipinNums;
    }

    public void setChipinNums(String chipinNums) {
        this.chipinNums = chipinNums;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public int getBets() {
        return bets;
    }

    public void setBets(int bets) {
        this.bets = bets;
    }

    public String getManner() {
        return manner;
    }

    public void setManner(String manner) {
        this.manner = manner;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getSpValue() {
        return spValue;
    }

    public void setSpValue(String spValue) {
        this.spValue = spValue;
    }
}

