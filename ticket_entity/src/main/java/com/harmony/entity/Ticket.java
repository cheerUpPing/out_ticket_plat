package com.harmony.entity;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by cm on 2016/12/26.
 */
public class Ticket {
    public enum SendMode { Scanning, KeySeq, ScanningKeySeq}

    private String ticketId; //主键
    private String gameCode; //彩票类型
    private String issue; //期号
    private String inVoteNumber; //转化后的投注内容
    private String sourceVoteNumber; //含有中文的投注内容
    private String againstVoteNumber; //含有对阵的投注内容
    private String bets; //金额
    private String sumBets; //金额
    private double awardBets;//搅奖后中奖金额
    private double tptAwardBets;//票机返回的中奖金额
    private int status; //状态 2出票成功 3兑奖成功
    private String gid; //落地号
    private String password; //票密码
    private String printTime; //打印时间 yyyyddmm
    private PrintServiceDTO printServiceDTO; //终端机
    private byte[] outVoteNumber; //打印返回投注内容
    private String psdcheck; //票校验码
    private String blackmark; //黑标
    private String spValue; //赔率返回
    private String manner; //
    private String returnTicketId; //票机返回的主键
    private SendMode sendMode; //
    private int multiple;
    private int record; //注数
    private String lotteryKeySeq; //
    private boolean testStatus; //测试状态,true为测试状态，false为出票状态
    private Vector vector;
    private byte[] bytes;
    private Vector mulVector;
    private byte[] mulBytes;
    private Vector enterVector;
    private byte[] enterBytes;

    public String getSumBets() {
        return sumBets;
    }

    public void setSumBets(String sumBets) {
        this.sumBets = sumBets;
    }

    public Vector getEnterVector() {
        return enterVector;
    }

    public void setEnterVector(Vector enterVector) {
        this.enterVector = enterVector;
    }

    public Vector getMulVector() {
        return mulVector;
    }

    public void setMulVector(Vector mulVector) {
        this.mulVector = mulVector;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public byte[] getBytes() {
        return bytes;
    }


    public byte[] getMulBytes() {
        return mulBytes;
    }

    public void setMulBytes(byte[] mulBytes) {
        this.mulBytes = mulBytes;
    }

    public byte[] getEnterBytes() {
        return enterBytes;
    }

    public void setEnterBytes(byte[] enterBytes) {
        this.enterBytes = enterBytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getTicketId() {
        return ticketId;
    }
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    public String getGameCode() {
        return gameCode;
    }
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }
    public String getIssue() {
        return issue;
    }
    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getBets() {
        return bets;
    }
    public void setBets(String bets) {
        this.bets = bets;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getGid() {
        return gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }
    public String getPrintTime() {
        return printTime;
    }
    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getInVoteNumber() {
        return inVoteNumber;
    }
    public void setInVoteNumber(String inVoteNumber) {
        this.inVoteNumber = inVoteNumber;
    }
    public byte[] getOutVoteNumber() {
        return outVoteNumber;
    }
    public void setOutVoteNumber(byte[] outVoteNumber) {
        this.outVoteNumber = outVoteNumber;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPsdcheck() {
        return psdcheck;
    }
    public void setPsdcheck(String psdcheck) {
        this.psdcheck = psdcheck;
    }
    public String getBlackmark() {
        return blackmark;
    }
    public void setBlackmark(String blackmark) {
        this.blackmark = blackmark;
    }
    public String getSourceVoteNumber() {
        return sourceVoteNumber;
    }
    public void setSourceVoteNumber(String sourceVoteNumber) {
        this.sourceVoteNumber = sourceVoteNumber;
    }
    public String getAgainstVoteNumber() {
        return againstVoteNumber;
    }
    public void setAgainstVoteNumber(String againstVoteNumber) {
        this.againstVoteNumber = againstVoteNumber;
    }
    public String getSpValue() {
        return spValue;
    }
    public void setSpValue(String spValue) {
        this.spValue = spValue;
    }
    public String getManner() {
        return manner;
    }
    public void setManner(String manner) {
        this.manner = manner;
    }
    public PrintServiceDTO getPrintServiceDTO() {
        return printServiceDTO;
    }
    public void setPrintServiceDTO(PrintServiceDTO printServiceDTO) {
        this.printServiceDTO = printServiceDTO;
    }
    public String getReturnTicketId() {
        return returnTicketId;
    }
    public void setReturnTicketId(String returnTicketId) {
        this.returnTicketId = returnTicketId;
    }

    public SendMode getSendMode() {
        return sendMode;
    }
    public void setSendMode(SendMode sendMode) {
        this.sendMode = sendMode;
    }
    public double getAwardBets() {
        return awardBets;
    }
    public void setAwardBets(double awardBets) {
        this.awardBets = awardBets;
    }
    public int getMultiple() {
        return multiple;
    }
    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }
    public double getTptAwardBets() {
        return tptAwardBets;
    }
    public void setTptAwardBets(double tptAwardBets) {
        this.tptAwardBets = tptAwardBets;
    }
    public int getRecord() {
        return record;
    }
    public void setRecord(int record) {
        this.record = record;
    }

    public String getLotteryKeySeq() {
        return lotteryKeySeq;
    }
    public void setLotteryKeySeq(String lotteryKeySeq) {
        this.lotteryKeySeq = lotteryKeySeq;
    }
    public boolean isTestStatus() {
        return testStatus;
    }
    public void setTestStatus(boolean testStatus) {
        this.testStatus = testStatus;
    }
    @Override
    public String toString() {
        return "Ticket [ticketId=" + ticketId + ", gameCode=" + gameCode
                + ", issue=" + issue + ", inVoteNumber=" + inVoteNumber
                + ", sourceVoteNumber=" + sourceVoteNumber
                + ", againstVoteNumber=" + againstVoteNumber + ", bets=" + bets
                + ", awardBets=" + awardBets + ", tptAwardBets=" + tptAwardBets
                + ", status=" + status + ", gid=" + gid + ", password="
                + password + ", printTime=" + printTime + ", printServiceDTO="
                + printServiceDTO + ", outVoteNumber="
                + Arrays.toString(outVoteNumber) + ", psdcheck=" + psdcheck
                + ", blackmark=" + blackmark + ", spValue=" + spValue
                + ", manner=" + manner + ", returnTicketId=" + returnTicketId
                + ", multiple=" + multiple + "]";
    }
}
