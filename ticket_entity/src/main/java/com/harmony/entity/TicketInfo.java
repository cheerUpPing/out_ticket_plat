package com.harmony.entity;


import java.io.InputStream;


public class TicketInfo implements BaseEntity {

    private int chipinid;

    private long tempId;

    private InputStream ticketImage; // 票信息

    private long voteProccessTime; //最迟出票时间

    public int getChipinid() {
        return chipinid;
    }

    public void setChipinid(int chipinid) {
        this.chipinid = chipinid;
    }

    public long getTempId() {
        return tempId;
    }

    public void setTempId(long tempId) {
        this.tempId = tempId;
    }

    public InputStream getTicketImage() {
        return ticketImage;
    }

    public void setTicketImage(InputStream ticketImage) {
        this.ticketImage = ticketImage;
    }

    public long getVoteProccessTime() {
        return voteProccessTime;
    }

    public void setVoteProccessTime(long voteProccessTime) {
        this.voteProccessTime = voteProccessTime;
    }
}