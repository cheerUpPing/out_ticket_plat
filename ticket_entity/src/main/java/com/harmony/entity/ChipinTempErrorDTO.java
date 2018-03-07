package com.harmony.entity;

import java.io.InputStream;

/**
 * Created by cm on 2017/1/9.
 */


public class ChipinTempErrorDTO implements BaseEntity {


    private long tempId;

    private String serialNo;

    private int chipinType;

    private long gameCode;

    private String issue;

    private String manner;

    private String chipinNums;

    private int status; //0未出票, 1已下发, 2已出票

    private long record;

    private String multiple;

    private double bets;

    private long userId;

    private String userNickName;

    private long recordId;

    private String condition;

    private long voteProccessTime;

    private String loginName;

    private String spValue;//spValue

    private int isPrint;

    private int isCountAward;//是否比赛完成(0:未完成, 1已完成)

    private double awardBets; //奖金金额

    private double tempAwardBets; //临时中奖金额

    private String tempInfo;//临时中奖信息

    private double taxAwardBets;//税后的中奖金额

    private String ticketId; //落地号

    private InputStream ticketImage; //票信息

    private String strpassword; //票信息

    private String psdcheck; //票信息

    private String blackmark; //票信息

    private String printservicenumber; //终端机编号

    private int postcount; //post次数

    private long printLaterTime; //出票时间

    private int ticketAward; //是否已兑奖

    private int awardPostCount; //兑奖次数

    private String awardservicenumber; //兑奖终端机

    private long postTime;  //post时间

    private long lastScreeningTime; //最迟出票时间

    private long awardPostTime; //兑奖post时间

    private String lotterytype; //大字段类型

    private String exceptionInfo; //异常信息

    private String fromserialNo; //客户交易号

    private int awardPostStatus; //兑奖状态

    private String TerminalType;//票根信息。

    public String getTerminalType() {
        return TerminalType;
    }

    public void setTerminalType(String terminalType) {
        TerminalType = terminalType;
    }

    public long getTempId() {
        return tempId;
    }

    public void setTempId(long tempId) {
        this.tempId = tempId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public int getChipinType() {
        return chipinType;
    }

    public void setChipinType(int chipinType) {
        this.chipinType = chipinType;
    }

    public long getGameCode() {
        return gameCode;
    }

    public void setGameCode(long gameCode) {
        this.gameCode = gameCode;
    }

    public String getManner() {
        return manner;
    }

    public void setManner(String manner) {
        this.manner = manner;
    }


    public String getChipinNums() {
        return chipinNums;
    }

    public void setChipinNums(String chipinNums) {
        this.chipinNums = chipinNums;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRecord() {
        return record;
    }

    public void setRecord(long record) {
        this.record = record;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public double getBets() {
        return bets;
    }

    public void setBets(double bets) {
        this.bets = bets;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public long getVoteProccessTime() {
        return voteProccessTime;
    }

    public void setVoteProccessTime(long voteProccessTime) {
        this.voteProccessTime = voteProccessTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


//	public ChipinTemp(long gameCode, double bets) {
//		this.gameCode = gameCode;
//		this.bets = bets;
//	}
//	public ChipinTemp(String userNickName,long userId, double bets) {
//		this.userNickName = userNickName;
//		this.userId = userId;
//		this.bets = bets;
//	}

//	public ChipinTemp() {
//	}

//	public String getBetsResult() {
//		return betsResult;
//	}
//
//	public void setBetsResult(String betsResult) {
//		this.betsResult = betsResult;
//	}


    public String getSpValue() {
        return spValue;
    }

    public void setSpValue(String spValue) {
        this.spValue = spValue;
    }

    public int getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(int isPrint) {
        this.isPrint = isPrint;
    }

    public int getIsCountAward() {
        return isCountAward;
    }

    public void setIsCountAward(int isCountAward) {
        this.isCountAward = isCountAward;
    }

    public double getAwardBets() {
        return awardBets;
    }

    public void setAwardBets(double awardBets) {
        this.awardBets = awardBets;
    }

    public double getTempAwardBets() {
        return tempAwardBets;
    }

    public void setTempAwardBets(double tempAwardBets) {
        this.tempAwardBets = tempAwardBets;
    }

    public String getTempInfo() {
        return tempInfo;
    }

    public void setTempInfo(String tempInfo) {
        this.tempInfo = tempInfo;
    }

    public double getTaxAwardBets() {
        return taxAwardBets;
    }

    public void setTaxAwardBets(double taxAwardBets) {
        this.taxAwardBets = taxAwardBets;
    }

    public InputStream getTicketImage() {
        return ticketImage;
    }

    public void setTicketImage(InputStream ticketImage) {
        this.ticketImage = ticketImage;
    }

    public String getStrpassword() {
        return strpassword;
    }

    public void setStrpassword(String strpassword) {
        this.strpassword = strpassword;
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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getPrintservicenumber() {
        return printservicenumber;
    }

    public void setPrintservicenumber(String printservicenumber) {
        this.printservicenumber = printservicenumber;
    }

    public int getPostcount() {
        return postcount;
    }

    public void setPostcount(int postcount) {
        this.postcount = postcount;
    }

    public long getPrintLaterTime() {
        return printLaterTime;
    }

    public void setPrintLaterTime(long printLaterTime) {
        this.printLaterTime = printLaterTime;
    }

    public int getTicketAward() {
        return ticketAward;
    }

    public void setTicketAward(int ticketAward) {
        this.ticketAward = ticketAward;
    }

    public int getAwardPostCount() {
        return awardPostCount;
    }

    public void setAwardPostCount(int awardPostCount) {
        this.awardPostCount = awardPostCount;
    }

    public String getAwardservicenumber() {
        return awardservicenumber;
    }

    public void setAwardservicenumber(String awardservicenumber) {
        this.awardservicenumber = awardservicenumber;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public long getAwardPostTime() {
        return awardPostTime;
    }

    public void setAwardPostTime(long awardPostTime) {
        this.awardPostTime = awardPostTime;
    }

    public long getLastScreeningTime() {
        return lastScreeningTime;
    }

    public void setLastScreeningTime(long lastScreeningTime) {
        this.lastScreeningTime = lastScreeningTime;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getLotterytype() {
        return lotterytype;
    }

    public void setLotterytype(String lotterytype) {
        this.lotterytype = lotterytype;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public String getFromserialNo() {
        return fromserialNo;
    }

    public void setFromserialNo(String fromserialNo) {
        this.fromserialNo = fromserialNo;
    }

    public int getAwardPostStatus() {
        return awardPostStatus;
    }

    public void setAwardPostStatus(int awardPostStatus) {
        this.awardPostStatus = awardPostStatus;
    }

}

