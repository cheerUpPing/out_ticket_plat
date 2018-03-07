package com.harmony.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.harmony.comm.OutCommon;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.entity.Ticket;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 2017/9/15 12:53.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class PrintUtil {


    /**
     * 根据票机ip获取出 票机
     *
     * @param printIp
     * @return
     */
    public static PrintServiceDTO getPrint(String printIp) {
        for (Map.Entry<String, PrintServiceDTO> entry : OutCommon.printServiceDTOMap.entrySet()) {
            PrintServiceDTO printServiceDTO = entry.getValue();
            if (printIp.equals(printServiceDTO.getWebAddress())) {
                return printServiceDTO;
            }
        }
        return null;
    }

    /**
     * "000" ==> "0 0 0"
     *
     * @param str
     * @return
     */
    public static String plusTrim(String str) {
        if (StringUtils.isNotEmpty(str)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                sb.append(c);
                if (i != str.length() - 1) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 格式化出票警告
     *
     * @param ticket
     * @param msg
     * @return
     */
    public static String formatPrint(Ticket ticket, String msg) {
        final String SPLIT = "<br>";
        StringBuilder buf = new StringBuilder();
        buf.append("票机控制系统:::::");
        buf.append("").append(msg);
        buf.append("\n原始订单详细参数信息").append(SPLIT);
        buf.append("[").append(SPLIT);
        if (null != ticket) {
            if (null != ticket.getPrintServiceDTO()) {
                buf.append("ip地址:").append(ticket.getPrintServiceDTO().getWebAddress()).append(SPLIT);
            }
            buf.append("流水号:").append(ticket.getTicketId()).append(SPLIT);
            if (null != ticket.getGameCode()) {
                buf.append("彩种:").append(getGameName(Integer.parseInt(ticket.getGameCode()))).append(SPLIT);
            }
            buf.append("投注号码:").append(ticket.getSourceVoteNumber()).append(SPLIT);
            buf.append("倍数:").append(ticket.getMultiple()).append(SPLIT);
            buf.append("金额:").append(ticket.getBets()).append(SPLIT);
            buf.append("投注方式:").append(ticket.getManner()).append(SPLIT);
            buf.append("期号:").append(ticket.getIssue()).append(SPLIT);
        }
        buf.append("]");
        return buf.toString();
    }

    private static String getGameName(int gameCode) {
        String result = "";
        switch (gameCode) {
            case 111:
                result = "22选5";
                break;
            case 105:
                result = "排列3";
                break;
            case 109:
                result = "排列5";
                break;
            case 108:
                result = "七星彩";
                break;
            case 110:
                result = "大乐透";
                break;
            case 102:
                result = "胜负彩";
                break;
            case 103:
                result = "任选九";
                break;
            case 106:
                result = "四场进球数";
                break;
            case 107:
                result = "六场半全场";
                break;
            case 301:
                result = "北单让球胜平负";
                break;
            case 302:
                result = "北单上下单双";
                break;
            case 303:
                result = "北单总进球数";
                break;
            case 304:
                result = "北单比分";
                break;
            case 305:
                result = "北单半全场胜平负";
                break;
            case 306:
                result = "北单胜负过关";
                break;
            case 501:
                result = "竞彩足球胜平负";
                break;
            case 505:
                result = "竞彩篮球胜负";
                break;
            case 506:
                result = "竞彩篮球让分胜负";
                break;
            case 507:
                result = "竞彩篮球胜分差";
                break;
            case 508:
                result = "竞彩篮球大小分";
                break;
            case 502:
                result = "竞彩足球比分";
                break;
            case 503:
                result = "竞彩足球总进球数";
                break;
            case 504:
                result = "竞彩足球半全场";
                break;
            case 511:
                result = "竞彩足球让球胜平负";
                break;
            case 509:
                result = "竞彩足球混合";
                break;
            case 510:
                result = "竞彩篮球混合";
                break;
            case 401:
                result = "广东11选5";
                break;
            case 402:
                result = "山东11选5";
                break;
            default:
                result = "未知玩法" + gameCode;
                break;
        }
        return result;
    }

    /**
     * 获取传统跨期 的按键
     * 传统游戏：102 103 106 107
     *
     * @param issue
     * @param sellingissue
     * @return
     */
    public static int getIssueNum(String issue, String sellingissue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //需要报警
        if (StringUtils.isEmpty(sellingissue)) {
            return -1;
        }
        JSONObject obj = JSON.parseObject(sellingissue);
        Set<String> issueSet = new TreeSet<String>();
        Iterator it = obj.keySet().iterator();
        while (it.hasNext()) {
            String issueTemp = String.valueOf(it.next());
            String stoptime = (String) obj.get(issueTemp);
            String curtime = sdf.format(new Date());
            if (Long.parseLong(curtime) <= Long.parseLong(stoptime)) {
                issueSet.add(issueTemp);
            }
        }
        int i = 1;
        for (Iterator iter = issueSet.iterator(); iter.hasNext(); ) {
            String iterIssue = String.valueOf(iter.next());
            if (issue.equals(iterIssue)) {
                break;
            }
            i++;
        }
        //需要报警
        if (issueSet.size() < i) {
            return -1;
        }
        System.out.println(i);
        System.out.println(issueSet.toString());
        return i;
    }
}
