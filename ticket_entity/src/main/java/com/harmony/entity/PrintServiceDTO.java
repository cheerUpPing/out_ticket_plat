package com.harmony.entity;

/**
 * 出票服务端
 * <p>
 * version 1.0
 * 实体类必须实现序列化 否则无法发送
 */
public class PrintServiceDTO implements BaseEntity, Cloneable, Comparable<PrintServiceDTO> {

    /**
     * 终端编号
     **/

    private Long terminalNumber;

    /**
     * 服务端名称
     **/

    private String serviceName;

    /**
     * 终端所在地址
     **/

    private String address;

    /**
     * 终端IP
     **/

    private String WebAddress;

    /**
     * 终端负责人
     **/

    private String contactName;

    /**
     * 联系电话
     **/

    private String telephone;

    /**
     * 终端数据解密KEY(暂不使用)
     **/

    private String serviceKey;

    /**
     * 终端是否在线
     **/

    private String isOnline;

    /**
     * 数据是否有效
     **/

    private String isActive;

    /**
     * 端口号
     **/

    private String port;

    /**
     * 票机数量
     **/

    private Long machine;

    /**
     * 票数量
     **/

    private Long maxTicket;

    /**
     * 终端权重
     **/

    private Long weight;

    /**
     * 指定票最高金额
     **/

    private Long maxAmount;

    /**
     * 发送串口
     **/

    private int sendCom;

    /**
     * 接收串口
     **/

    private int receiveCom;

    /**
     * 发送串口状态
     **/
//    
    private String sendStatus = "N";

    /**
     * 接收串口状态
     **/
//    
    private String receiveStatus = "N";

    /**
     * 指定票最少金额
     **/

    private Long mixAmount;

    /**
     * 终端可出票大类型
     **/

    private String bigType;

    /**
     * 票数
     **/
    private Long ticketCount = 0L;

    /**
     * 赔率更新时间
     **/
    private Long frequencyTime;


    private String username;


    private String userpassword;


    private long currentOperating; //当前操作 0登录 1投注, 2兑奖  3空闲  4余额 5出纸  6不出纸, 7全部登录, 8全部出纸， 9全部不出纸    20:登录中


    private int loginStatus; //登录状态: 0未登录 1为已登录


    private int paperStatus; //0不出纸, 1出纸


    private double balance;//余额


    private String beginTime;//开始时间


    private String endTime;//结束时间


    private double votebets;//投注余额


    private double awardbets;//兑奖余额


    private int voteCount;//投注票数


    private int awardCount;//投注票数


    private String tempid; //中奖TEMPID


    private int gameCode;


    private int oneStatus; //兑奖刷新余额只发送一次 0未发送， 1为已发送


    private String lotteryTypeList;//玩法按键序列

    private String timeReportKeySeq;//时段报表按键

    private String balanceKeySeq;//缴款报表按键

    private String returnLotteryResult;//是否抓取11选5开奖结果

    private String returnScreenText;//是否抓取屏幕文本


    private String balanceprinttime;//结束时间


    private long loginTime; //登录时间


    private long printInterval; //发票间隔


    private long awardPostStatus; //状态


    private int defaultLoginStatus; //登录成功后默认的状态


    private double stopAwardMoney; //兑奖停止金额


    private String version; //版本


    private String upwd; //u盾密码


    private long awardInterval; //兑奖的间隔时间


    private String TerminalType; //终端类型


    private String awardReceiptStatus; //是否有兑奖回执单


    private String isCompet; //是否有竞彩

    private String num;


    private long startPrintTime;//开始打印错误票时间


    private long endPrintTime;//结束打印错误票时间


    public Long getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(Long terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebAddress() {
        return WebAddress;
    }

    public void setWebAddress(String webAddress) {
        WebAddress = webAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Long getMachine() {
        return machine;
    }

    public void setMachine(Long machine) {
        this.machine = machine;
    }

    public Long getMaxTicket() {
        return maxTicket;
    }

    public void setMaxTicket(Long maxTicket) {
        this.maxTicket = maxTicket;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getSendCom() {
        return sendCom;
    }

    public void setSendCom(int sendCom) {
        this.sendCom = sendCom;
    }

    public int getReceiveCom() {
        return receiveCom;
    }

    public void setReceiveCom(int receiveCom) {
        this.receiveCom = receiveCom;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public Long getMixAmount() {
        return mixAmount;
    }

    public void setMixAmount(Long mixAmount) {
        this.mixAmount = mixAmount;
    }

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }

    public Long getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(Long ticketCount) {
        this.ticketCount = ticketCount;
    }

    public Long getFrequencyTime() {
        return frequencyTime;
    }

    public void setFrequencyTime(Long frequencyTime) {
        this.frequencyTime = frequencyTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public long getCurrentOperating() {
        return currentOperating;
    }

    public void setCurrentOperating(long currentOperating) {
        this.currentOperating = currentOperating;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(int paperStatus) {
        this.paperStatus = paperStatus;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getVotebets() {
        return votebets;
    }

    public void setVotebets(double votebets) {
        this.votebets = votebets;
    }

    public double getAwardbets() {
        return awardbets;
    }

    public void setAwardbets(double awardbets) {
        this.awardbets = awardbets;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(int awardCount) {
        this.awardCount = awardCount;
    }

    public String getTempid() {
        return tempid;
    }

    public void setTempid(String tempid) {
        this.tempid = tempid;
    }

    public int getGameCode() {
        return gameCode;
    }

    public void setGameCode(int gameCode) {
        this.gameCode = gameCode;
    }

    public int getOneStatus() {
        return oneStatus;
    }

    public void setOneStatus(int oneStatus) {
        this.oneStatus = oneStatus;
    }

    public String getLotteryTypeList() {
        return lotteryTypeList;
    }

    public void setLotteryTypeList(String lotteryTypeList) {
        this.lotteryTypeList = lotteryTypeList;
    }

    public String getTimeReportKeySeq() {
        return timeReportKeySeq;
    }

    public void setTimeReportKeySeq(String timeReportKeySeq) {
        this.timeReportKeySeq = timeReportKeySeq;
    }

    public String getBalanceKeySeq() {
        return balanceKeySeq;
    }

    public void setBalanceKeySeq(String balanceKeySeq) {
        this.balanceKeySeq = balanceKeySeq;
    }

    public String getReturnLotteryResult() {
        return returnLotteryResult;
    }

    public void setReturnLotteryResult(String returnLotteryResult) {
        this.returnLotteryResult = returnLotteryResult;
    }

    public String getReturnScreenText() {
        return returnScreenText;
    }

    public void setReturnScreenText(String returnScreenText) {
        this.returnScreenText = returnScreenText;
    }

    public String getBalanceprinttime() {
        return balanceprinttime;
    }

    public void setBalanceprinttime(String balanceprinttime) {
        this.balanceprinttime = balanceprinttime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getPrintInterval() {
        return printInterval;
    }

    public void setPrintInterval(long printInterval) {
        this.printInterval = printInterval;
    }

    public long getAwardPostStatus() {
        return awardPostStatus;
    }

    public void setAwardPostStatus(long awardPostStatus) {
        this.awardPostStatus = awardPostStatus;
    }

    public int getDefaultLoginStatus() {
        return defaultLoginStatus;
    }

    public void setDefaultLoginStatus(int defaultLoginStatus) {
        this.defaultLoginStatus = defaultLoginStatus;
    }

    public double getStopAwardMoney() {
        return stopAwardMoney;
    }

    public void setStopAwardMoney(double stopAwardMoney) {
        this.stopAwardMoney = stopAwardMoney;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public long getAwardInterval() {
        return awardInterval;
    }

    public void setAwardInterval(long awardInterval) {
        this.awardInterval = awardInterval;
    }

    public String getTerminalType() {
        return TerminalType;
    }

    public void setTerminalType(String terminalType) {
        TerminalType = terminalType;
    }

    public String getAwardReceiptStatus() {
        return awardReceiptStatus;
    }

    public void setAwardReceiptStatus(String awardReceiptStatus) {
        this.awardReceiptStatus = awardReceiptStatus;
    }

    public String getIsCompet() {
        return isCompet;
    }

    public void setIsCompet(String isCompet) {
        this.isCompet = isCompet;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public long getStartPrintTime() {
        return startPrintTime;
    }

    public void setStartPrintTime(long startPrintTime) {
        this.startPrintTime = startPrintTime;
    }

    public long getEndPrintTime() {
        return endPrintTime;
    }

    public void setEndPrintTime(long endPrintTime) {
        this.endPrintTime = endPrintTime;
    }

    @Override
    public int compareTo(PrintServiceDTO printServiceDTO) {

        Long ticket = this.getMaxTicket() - this.getTicketCount();

        return ticket.compareTo((printServiceDTO.getMaxTicket() - printServiceDTO.getTicketCount()));

    }

    @Override
    public PrintServiceDTO clone() {

        PrintServiceDTO printServiceDTO = null;

        try {

            printServiceDTO = (PrintServiceDTO) super.clone();

            return printServiceDTO;

        } catch (CloneNotSupportedException e) {

            e.printStackTrace();

            return null;

        }
    }

    @Override
    public String toString() {
        return "PrintServiceDTO{" +
                "terminalNumber=" + terminalNumber +
                ", serviceName='" + serviceName + '\'' +
                ", address='" + address + '\'' +
                ", WebAddress='" + WebAddress + '\'' +
                ", contactName='" + contactName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", serviceKey='" + serviceKey + '\'' +
                ", isOnline='" + isOnline + '\'' +
                ", isActive='" + isActive + '\'' +
                ", port='" + port + '\'' +
                ", machine=" + machine +
                ", maxTicket=" + maxTicket +
                ", weight=" + weight +
                ", maxAmount=" + maxAmount +
                ", sendCom=" + sendCom +
                ", receiveCom=" + receiveCom +
                ", sendStatus='" + sendStatus + '\'' +
                ", receiveStatus='" + receiveStatus + '\'' +
                ", mixAmount=" + mixAmount +
                ", bigType='" + bigType + '\'' +
                ", ticketCount=" + ticketCount +
                ", frequencyTime=" + frequencyTime +
                ", username='" + username + '\'' +
                ", userpassword='" + userpassword + '\'' +
                ", currentOperating=" + currentOperating +
                ", loginStatus=" + loginStatus +
                ", paperStatus=" + paperStatus +
                ", balance=" + balance +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", votebets=" + votebets +
                ", awardbets=" + awardbets +
                ", voteCount=" + voteCount +
                ", awardCount=" + awardCount +
                ", tempid='" + tempid + '\'' +
                ", gameCode=" + gameCode +
                ", oneStatus=" + oneStatus +
                ", lotteryTypeList='" + lotteryTypeList + '\'' +
                ", timeReportKeySeq='" + timeReportKeySeq + '\'' +
                ", balanceKeySeq='" + balanceKeySeq + '\'' +
                ", returnLotteryResult='" + returnLotteryResult + '\'' +
                ", returnScreenText='" + returnScreenText + '\'' +
                ", balanceprinttime='" + balanceprinttime + '\'' +
                ", loginTime=" + loginTime +
                ", printInterval=" + printInterval +
                ", awardPostStatus=" + awardPostStatus +
                ", defaultLoginStatus=" + defaultLoginStatus +
                ", stopAwardMoney=" + stopAwardMoney +
                ", version='" + version + '\'' +
                ", upwd='" + upwd + '\'' +
                ", awardInterval=" + awardInterval +
                ", TerminalType='" + TerminalType + '\'' +
                ", awardReceiptStatus='" + awardReceiptStatus + '\'' +
                ", isCompet='" + isCompet + '\'' +
                ", num='" + num + '\'' +
                ", startPrintTime=" + startPrintTime +
                ", endPrintTime=" + endPrintTime +
                '}';
    }
}
