package com.harmony.util;

import com.harmony.entity.Chipin;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CM on 2017/5/9.
 */
public class FormatConvertV3 {
    private final static Logger __logger = Logger.getLogger(FormatConvertV3.class);
    private static final String TRADITION_REG =

            "第\\s*(\\d+)期[\\s*\\S*]*[\\s+](\\d+)倍\\s*合计(\\d+)元";//传统足球&数字彩期号倍数金额正则
    private static final String TRADITIONNEW_REG =

            "第\\s*(\\d+)期[\\s*\\S*]*追加投注(\\d+)倍\\s*合计(\\d+)元";//传统足球&数字彩期号倍数金额正则

    private static final String SINGLE_REG =

            "len:\\d+\\s*(\\d{5})PrintData[\\s*\\S*]*期[\\s*\\S*]*len:\\d+\\s+(\\d+)[\\s*\\S*]*倍" +

                    "[\\s*\\S*]*len:\\d+\\s+(\\d+)[\\s*\\S*]*元";//北单期号倍数金额正则

    private static final String ELEVENCHOOSEFIVE_REG =
            "(\\d+)期倍:(\\d+)\\s*合计:(\\d+)元";//乌鲁木齐以前版本11选5期号倍数金额正则1
    private static final String ELEVENCHOOSEFIVE_REGTWO =
            "第(\\d+)期.*[\\s*\\S]*票\\s*(\\d+)倍\\s*合计(\\d+)元";//乌鲁木齐11选5期号倍数金额正则2
    private static final String ELEVENCHOOSEFIVE_REGSD = "第(\\d+)期.*[\\s*\\S]*票(\\d+)倍\\s*合计(\\d+)元";//山东11选五期号倍数金额正则
    //	private static final String ELEVENCHOOSEFIVE_REG2 =
//
//			"(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)" +
//
//					"\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)";//11选5复式票胆拖票正则
    private static final String ELEVENCHOOSEFIVE_REG2 =

            "[胆|拖][:]?[码]?[:]?([\\d{2}\\s+]+)";//11选5复式票胆拖票正则
    //	private static final String ELEVENCHOOSEFIVE_REG3 ="元[\\s*]?([\\d{2}\\s*]+)";//11选5乐选投注号码正则
    private static final String ELEVENCHOOSEFIVE_REG4 = "元[\\s*]?([\\d{2}\\s*]+)";//11选5乐选投注号码正则
    private static final String ELEVENCHOOSEFIVE_REG3 = "元([\\S\\s]+)\\--";//11选5乐选投注号码正则
    private static final String SC_REG =

            "\\s*(\\d+)期\\s*合计:(\\d+)元[\\s*\\S*]*倍:(\\d+)\\S*";//幸运赛车期号倍数金额正则


    public static Chipin stringConvert(String ticketInfo, long gamecode)//,int gamecode
    {

        Chipin chipin = null;

        if (StringUtils.isNotEmpty(ticketInfo))

        {

            String data = "";
            data = ticketInfo.replace("\u001BE", "").replaceAll("\\x00", "").replace("P怂\u001B3", "").replace("\u001B3", "").replace("\u001B", "").replace("\u0001", "").replace("\u001E", "")
                    .replace("\u0002", "").replace("\u001D", "").replace("\u0010", "").replace("\u001D", "").replace("a", "").replace("\u001A", "")
                    .replace("d", "").replace("P", "").replace("怂", "").replace("!", "").replaceAll("M\\x00", "").replace("M", "").replace("#", "")
                    .replaceAll("\\x00", "").replace("a", "").replace("�", "").replace("湧", "").replace("璹", "").replace("ゆ", "").replace("顐", "").replace("庢", "")
                    .replaceAll("元[a-zA-Z||\\s]+", "元").replace("A0", "").replaceAll("[|,|J|\\(|%]+", "");//.replace("e", "").replace("ACK", "");;
            __logger.info(data);
            if (data.contains("买11选5任选五")) {
                data = data.replace("买11选5任选五", "");
            }

            /*****传统足球****/
            if (gamecode == 102) {

                chipin = getChipin(data, TRADITION_REG);

                if (chipin != null)

                {

                    if (data.contains("复式票"))

                    {

                        chipin.setManner("2");

                        String reg = "([\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]" +

                                "\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.])";

                        chipin.setChipinNums(getDoubleResult(data, reg));

                        return chipin;

                    } else

                    {

                        chipin.setManner("1");

                        String reg = "(\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}" +

                                "\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1})";

                        chipin.setChipinNums(getResult(data, reg));

                        return chipin;

                    }

                }

            }

//			if ((data1.indexOf("·任选9场胜负") != -1||data1.indexOf("<任选9场胜负>") != -1)&&103==gamecode)
            if (103 == gamecode) {

                chipin = getChipin(data, TRADITION_REG);

                if (chipin != null)

                {

                    if (data.contains("复式票"))

                    {

                        chipin.setManner("2");

                        String reg = "([\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]" +

                                "\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.])";

                        chipin.setChipinNums(getDoubleResult(data, reg));

                        return chipin;

                    } else

                    {

                        chipin.setManner("1");

                        String reg = "([\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]" +

                                "\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-]\\s+[\\d{1}|-])";

                        chipin.setChipinNums(getResult(data, reg));

                        return chipin;

                    }

                }

            }

//			if ((data1.indexOf("·4场进球") != -1||data1.indexOf("<4场进球>") != -1)&&106==gamecode)
            if (106 == gamecode) {

                chipin = getChipin(data, TRADITION_REG);

                if (chipin != null)

                {

                    if (data.contains("复式票"))

                    {

                        chipin.setManner("2");

                        chipin.setChipinNums(getZjqDoubleResult(data, "\\s+(\\d{1}|\\*)(\\d{1}|\\*)(\\d{1}|\\*)(\\d{1}|\\*|\\d{1}\\+)\\s+"));

                        return chipin;

                    } else

                    {

                        chipin.setManner("1");

                        String reg = "(\\d{1}\\S*\\s+\\d{1}\\S*\\s+\\d{1}\\S*\\s+\\d{1}\\S*\\s+\\d{1}\\S*\\s+\\d{1}\\S*\\s+\\d{1}\\S*\\s+\\d{1}\\S*)";

                        chipin.setChipinNums(getResult(data, reg));

                        return chipin;

                    }

                }

            }

//			if ((data1.indexOf("·6场半全场胜负") != -1||data1.indexOf("<6场半全场胜负>") != -1)&&107==gamecode)

            if (107 == gamecode) {

                chipin = getChipin(data, TRADITION_REG);

                if (chipin != null)

                {

                    if (data.contains("复式票"))

                    {

                        chipin.setManner("2");

                        String reg = "([\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]" +

                                "\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.]\\s+[\\d{1}|.])";

                        chipin.setChipinNums(getDoubleResult(data, reg));

                        return chipin;

                    } else

                    {

                        chipin.setManner("1");

                        String reg = "(\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}" +

                                "\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1})";

                        chipin.setChipinNums(getResult(data, reg));

                        return chipin;

                    }

                }

            }


            /*****数字彩****/
            if (110 == gamecode) {
                if (ticketInfo.contains("<超级大乐透>")) {
                    String regzjtz = "(追加投注\\s*\\d{1,2}倍\\s*合计\\d*元)";//匹配追加投注玩法正则
                    chipin = getChipin(data, TRADITION_REG);
                    if (chipin != null) {
                        if (data.contains("复式票")) {
                            if (judgeZJTZ(data, regzjtz)) {
                                chipin.setManner("6");
                            } else {
                                chipin.setManner("2");
                            }
                            String reg = "区([\\d{2}|.\\s]+)\\s+";
                            chipin.setChipinNums(getDltFsResult(data, reg));
                            return chipin;
                        } else if (data.contains("胆拖票")) {
                            if (judgeZJTZ(data, regzjtz)) {
                                chipin.setManner("7");
                            } else {
                                chipin.setManner("3");
                            }
                            StringBuilder numSb = new StringBuilder();
                            String qianD = getDltDtResult(data, "前区胆([\\d{2}|.\\s]+)");
                            if (!StringUtils.isEmpty(qianD))
                                numSb.append(qianD).append("//");
                            String qianT = getDltDtResult(data, "前区拖([\\d{2}|.\\s]+)");
                            numSb.append(qianT);
                            numSb.append(",");
                            String houD = getDltDtResult(data, "后区胆([\\d{2}|.\\s]+)");
                            if (!StringUtils.isEmpty(houD))
                                numSb.append(houD).append("//");
                            String houT = getDltDtResult(data, "后区拖([\\d{2}|.\\s]+)\\s+");
                            numSb.append(houT);
                            chipin.setChipinNums(numSb.toString());
                            return chipin;
                        } else {
                            if (judgeZJTZ(data, regzjtz)) {
                                chipin.setManner("5");
                            } else {
                                chipin.setManner("1");
                            }
                            String reg = "(\\d{2}\\+\\d{2}\\+\\d{2}\\+\\d{2}\\+\\d{2})\\s*(\\d{2}\\+\\d{2})";
                            chipin.setChipinNums(getDltDsResult(data, reg));
                            return chipin;
                        }
                    }
                } else {
//					追加投注\s*\d{1,2}倍\s*合计\d*元)
                    String regzjtz = "(追加投注\\d{1,2}倍\\s*合计\\d*元)";//匹配追加投注玩法正则
//					if (chipin != null)
//					{
                    if (data.contains("复式票")) {
                        if (judgeZJTZ(data, regzjtz)) {
                            chipin = getChipin(data, TRADITIONNEW_REG);
                            chipin.setManner("6");
                        } else {
                            chipin = getChipin(data, TRADITION_REG);
                            chipin.setManner("2");
                        }
                        data = data.replace("后区", "后后区 ");
                        String reg = "[前|后][区]\\s([\\d{2}|.\\s]+)[\\S|\\s+]";
                        chipin.setChipinNums(getDltFsResult(data, reg));
                        return chipin;
                    } else if (data.contains("胆拖票")) {
                        if (judgeZJTZ(data, regzjtz)) {
                            chipin = getChipin(data, TRADITIONNEW_REG);
                            chipin.setManner("7");
                        } else {
                            chipin = getChipin(data, TRADITION_REG);
                            chipin.setManner("3");
                        }
                        StringBuilder numSb = new StringBuilder();
                        String qianD = getDltDtResult(data, "前区胆([\\d{2}|.\\s]+)");
                        if (!StringUtils.isEmpty(qianD))
                            numSb.append(qianD).append("//");
                        String qianT = getDltDtResult(data, "前区拖([\\d{2}|.\\s]+)");
                        numSb.append(qianT);
                        numSb.append(",");
                        String houD = getDltDtResult(data, "后区胆([\\d{2}|.\\s]+)");
                        if (!StringUtils.isEmpty(houD))
                            numSb.append(houD).append("//");
                        String houT = getDltDtResult(data, "后区拖([\\d{2}|.\\s]+)");
                        numSb.append(houT);
                        chipin.setChipinNums(numSb.toString());
                        return chipin;
                    } else {
                        if (judgeZJTZ(data, regzjtz)) {
                            chipin = getChipin(data, TRADITIONNEW_REG);
                            chipin.setManner("5");
                        } else {
                            chipin = getChipin(data, TRADITION_REG);
                            chipin.setManner("1");
                        }
                        String reg = "(\\d{2}\\s*\\d{2}\\s*\\d{2}\\s*\\d{2}\\s*\\d{2})\\s*\\+\\s*(\\d{2}\\s*\\d{2})";
//							String reg = "(\\d{2}\\+\\d{2}\\+\\d{2}\\+\\d{2}\\+\\d{2})\\s*(\\d{2}\\+\\d{2})";
                        chipin.setChipinNums(getDltDsNewResult(data, reg));
                        return chipin;
                    }
//					}
                }
            }


//			if ((data1.indexOf("七星彩") != -1 || data1.indexOf("7星彩")!=-1)&&gamecode==108)
            if (gamecode == 108)

            {

                chipin = getChipin(data, TRADITION_REG);

                if (chipin != null)

                {

                    if (data.contains("第①位")) {
                        chipin.setManner("2");
                        data = data.replace(".", " ");
                        String reg = "位:\\s*([\\d|\\s*]+)";
                        chipin.setChipinNums(getDigitDoubleResult(data, reg));
                        return chipin;
                    } else {
                        chipin.setManner("1");
                        String reg = "(\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1})";
                        chipin.setChipinNums(getResult(data, reg));
                        return chipin;

                    }

                }

            }
            if (gamecode == 105) {
                chipin = getChipin(data, TRADITION_REG);
                if (chipin != null) {
                    if (data.contains("直选复式票")) {

                        chipin.setManner("21");
                        data = data.replace(".", "");
                        String reg = "位:\\s*([\\d{1}|\\s*]+)";

                        chipin.setChipinNums(getP3FsResult(data, reg));

                        return chipin;

                    } else if (data.contains("直选组合复式票")) {

                        chipin.setManner("42");
                        String reg = "元([\\d{1}\\s+]+)\\S+";

                        chipin.setChipinNums(getP3ZxResult(data, reg));

                        return chipin;

                    } else if (data.contains("组选6复式票")) {

                        chipin.setManner("26");

//						String reg = "元(.*)";
                        String reg = "元([\\d{1}\\s+]+)\\S+";
                        chipin.setChipinNums(getP3ZxResult(data, reg));

                        return chipin;

                    } else if (data.contains("组选3复式票"))

                    {

                        chipin.setManner("23");

                        String reg = "元([\\d{1}\\s+]+)\\S+";
//						String reg = "元(.*)";

                        chipin.setChipinNums(getP3ZxResult(data, reg));

                        return chipin;

                    } else if (data.contains("直选票"))

                    {

                        chipin.setManner("11");

                        String reg = "(\\d{1}\\s+\\d{1}\\s+\\d{1})";

                        chipin.setChipinNums(getResult(data, reg));

                        return chipin;

                    } else if (data.contains("组选票"))

                    {

                        String reg = "(\\d{1}\\s+\\d{1}\\s+\\d{1})";

                        String chipinNums = getResult(data, reg);

                        String manner = "16";

                        String[] tmps = chipinNums.split(",")[0].split("/");

                        boolean isZ3 = false;//是否为组3  两号码一样为组3

                        for (int i = 0; i < tmps.length; i++)

                        {

                            int j = i + 1;

                            if (j == tmps.length) j = 0;

                            if (tmps[i].equals(tmps[j]))

                            {
                                isZ3 = true;
                                break;
                            }

                        }

                        if (isZ3) manner = "13";

                        chipin.setChipinNums(chipinNums);

                        chipin.setManner(manner);

                        return chipin;

                    } else if (data.contains("直选和值票"))

                    {

                        chipin.setManner("31");

                        String reg = "元\\s*([\\d{2}\\s]+)";

                        String tmp = getP3ZxResult(data, reg);
                        String newtmp = new String();
                        String[] tmpArr = tmp.split("/");
                        for (int i = 0; i < tmpArr.length; i++) {
                            if (i > 0) {
                                newtmp = newtmp + "/";
                            }
                            if (tmpArr[i].length() == 1) {
                                tmpArr[i] = "0" + tmpArr[i];
                            }
                            newtmp = newtmp + tmpArr[i];
                        }
                        chipin.setChipinNums(newtmp);

                        return chipin;

                    } else if (data.contains("组选和值票"))

                    {

                        chipin.setManner("32");

                        String reg = "元\\s*([\\d{2}\\s]+)";

                        String tmp = getP3ZxResult(data, reg);
                        String newtmp = new String();
                        String[] tmpArr = tmp.split("/");
                        for (int i = 0; i < tmpArr.length; i++) {
                            if (i > 0) {
                                newtmp = newtmp + "/";
                            }
                            if (tmpArr[i].length() == 1) {
                                tmpArr[i] = "0" + tmpArr[i];
                            }
                            newtmp = newtmp + tmpArr[i];
                        }
                        chipin.setChipinNums(newtmp);

                        return chipin;

                    } else if (data.contains("直选组合胆拖票"))

                    {

                        chipin.setManner("51");

                        chipin.setChipinNums(getP3DtResult(data));

                        return chipin;

                    } else if (data.contains("组选3胆拖"))

                    {

                        chipin.setManner("52");

                        chipin.setChipinNums(getP3DtResult(data));

                        return chipin;

                    } else if (data.contains("组选6胆拖"))

                    {

                        chipin.setManner("53");

                        chipin.setChipinNums(getP3DtResult(data));

                        return chipin;

                    }

                }

//				}
            }
            if (gamecode == 109) {
                chipin = getChipin(data, TRADITION_REG);
                if (chipin != null) {
                    if (data.contains("第①位"))

                    {
                        chipin.setManner("2");
                        data = data.replace(".", "");
                        String reg = "位:\\s*([\\d|\\s*]+)";

                        chipin.setChipinNums(getDigitDoubleResult(data, reg));

                        return chipin;

                    } else

                    {

                        chipin.setManner("1");

                        String reg = "(\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1})";

                        chipin.setChipinNums(getResult(data, reg));

                        return chipin;

                    }

                }

            }


            /*****北单****/

            if (ticketInfo.contains("胜平负"))

            {

                chipin = getChipin(data, SINGLE_REG);

                if (chipin != null)

                {

                    chipin.setManner(getManner(data));

                    String reg = "(\\d+)..*\\s+([\\d{1}|-])\\s+([\\d{1}|-])\\s+([\\d{1}|-])";

                    chipin.setChipinNums(getSingleResult(data, reg));

                }

                return chipin;

            }

            if (ticketInfo.contains("上下盘单双数"))

            {

                chipin = getChipin(data, SINGLE_REG);

                if (chipin != null)

                {

                    chipin.setManner(getManner(data));

                    String reg = "(\\d+)..*\\s+(\\S{2}|\\S{4})\\s+(\\S{2}|\\S{4})\\s+(\\S{2}|\\S{4})\\s+(\\S{2}|\\S{4})\\s+";

                    chipin.setChipinNums(getSingleResult(data, reg));

                }

                return chipin;

            }

            if (ticketInfo.contains("总进球数"))

            {

                chipin = getChipin(data, SINGLE_REG);

                if (chipin != null)

                {

                    chipin.setManner(getManner(data));

                    String reg = "(\\d+)..*\\s+([\\d{1}|-])\\s+([\\d{1}|-])\\s+([\\d{1}|-])\\s+([\\d{1}|-])\\s+([\\d{1}|-])\\s+([\\d{1}|-])" +

                            "\\s+([\\d{1}|-])\\s+([\\d{1}|-][\\+|-])";

                    chipin.setChipinNums(getSingleResult(data, reg));

                }

                return chipin;

            }

            if (ticketInfo.contains("单场比分"))

            {

                chipin = getChipin(data, SINGLE_REG);

                if (chipin != null)

                {

                    chipin.setManner(getManner(data));

                    chipin.setChipinNums(getSingleBfResult(data));

                }

                return chipin;

            }

            if (ticketInfo.contains("半全场胜平负"))

            {

                chipin = getChipin(data, SINGLE_REG);

                if (chipin != null)

                {

                    chipin.setManner(getManner(data));

                    String reg = "(\\d+)..*\\s+([\\d{1}|-]-[\\d{1}|-])\\s+([\\d{1}|-]-[\\d{1}|-])\\s+([\\d{1}|-]-[\\d{1}|-])\\s+([\\d{1}|-]-[\\d{1}|-])\\s+" +

                            "([\\d{1}|-]-[\\d{1}|-])\\s+([\\d{1}|-]-[\\d{1}|-])\\s+([\\d{1}|-]-[\\d{1}|-])\\s+([\\d{1}|-]-[\\d{1}|-])\\s+([\\d{1}|-]-[\\d{1}|-])";

                    chipin.setChipinNums(getSingleResult(data, reg));

                }

                return chipin;

            }

            /**
             * 山东11选5
             */
//			if(gamecode==402){
//				System.out.println("进入山东11选5");
//			if (data.indexOf("·11选5") != -1)
//
//			{
//
//				chipin = getChipin(data, ELEVENCHOOSEFIVE_REGSD);
//				if (chipin != null)
//
//				{
//
//					if (data.indexOf("任选一") != -1)
//
//					{
//
//						chipin.setManner("1");
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("任选二") != -1)
//
//					{
//
//						chipin.setManner("2");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("任选三") != -1)
//
//					{
//
//						chipin.setManner("3");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("任选四") != -1)
//
//					{
//
//						chipin.setManner("4");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("任选五") != -1)
//
//					{
//
//						chipin.setManner("5");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("任选六") != -1)
//
//					{
//
//						chipin.setManner("6");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("任选七") != -1)
//
//					{
//
//						chipin.setManner("7");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("任选八") != -1)
//
//					{
//
//						chipin.setManner("8");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//					}
//
//					if (data.indexOf("选前2直选") != -1)
//
//					{
//
//						chipin.setManner("9");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("定位复式票") != -1)
//
//						{
//
//							String reg = "(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)";
//
//							chipin.setChipinNums(get11X5DwfsResult(data, reg));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("选前2组选") != -1)
//
//					{
//
//						chipin.setManner("10");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("选前3直选") != -1)
//
//					{
//
//						chipin.setManner("11");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("定位复式票") != -1)
//
//						{
//
//							String reg = "(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)\\s+(\\d{2}|\\.)";
//
//							chipin.setChipinNums(get11X5DwfsResult(data, reg));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//					if (data.indexOf("选前3组选") != -1)
//
//					{
//
//						chipin.setManner("12");
//
//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}
//
//						if (data.indexOf("复式票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//						if (data.indexOf("胆拖票") != -1)
//
//						{
//
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));
//
//						}
//
//					}
//
//				}
//
//				return chipin;
//
//			}
//			}

            /*****快乐扑克****/
//            if(gamecode==410)
//            {
//                chipin = Kuailepuke3.stringConvert(bytes, gamecode);
//                return chipin;
//            }

            /*****11选5****/

            if (gamecode == 401 || gamecode == 402 || gamecode == 403 || gamecode == 404 || gamecode == 405 || gamecode == 406 || gamecode == 407 || gamecode == 408 || gamecode == 409) {
                chipin = getChipin(data, ELEVENCHOOSEFIVE_REG);
                if (chipin.getMultiple() == 0 && chipin.getBets() == 0 && chipin.getIssue() == null) {
                    chipin = getChipin(data, ELEVENCHOOSEFIVE_REGTWO);
                }

                if (chipin != null)

                {

                    if (data.contains("任选一"))

                    {

                        chipin.setManner("1");

//						if (data.indexOf("单式票") != -1)
//
//						{
//
//							String reg = "(\\d{2})\\+(\\d{2})";
//
//							chipin.setChipinNums(get11X5DsResult(data, reg));
//
//						}

                        if (data.contains("复式票") || data.indexOf("单式票") != -1)

                        {

//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));
//							chipin.setChipinNums(get11X5LxResult(data, ELEVENCHOOSEFIVE_REG3));

                            if (data.contains("--")) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }
                        }

                    }

                    if (data.contains("任选二"))

                    {

                        chipin.setManner("2");

                        if (data.contains("单式票"))

                        {

//							String reg = "元(\\d{2})\\s(\\d{2})\\s+";
//							String reg = "[(\\d{2})\\s*]+\\-";
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));

                        }

                        if (data.contains("复式票"))

                        {
                            if (data.indexOf("--") != -1) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }
                        }

                        if (data.contains("胆拖票"))

                        {
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));

                        }

                    }

                    if (data.contains("任选三"))

                    {

                        chipin.setManner("3");

                        if (data.contains("单式票"))

                        {
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));

                        }

                        if (data.contains("复式票"))

                        {
                            if (data.contains("--")) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }

                        }

                        if (data.contains("胆拖票"))

                        {
//							chipin.setChipinNums(get11X5LxResult(data, ELEVENCHOOSEFIVE_REG3));
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));

                        }

                    }

                    if (data.contains("任选四"))

                    {

                        chipin.setManner("4");

                        if (data.contains("单式票"))

                        {
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));

                        }

                        if (data.contains("复式票")) {
                            if (data.contains("--")) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }
                        }

                        if (data.contains("胆拖票"))

                        {
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));

                        }
                    }

                    if (data.contains("任选五"))

                    {
                        chipin.setManner("5");

                        if (data.contains("单式票"))

                        {
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));

                        }

                        if (data.contains("复式票"))

                        {
                            if (data.contains("--")) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }

                        }

                        if (data.contains("胆拖票"))

                        {
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));

                        }

                    }

                    if (data.contains("任选六"))

                    {

                        chipin.setManner("6");

                        if (data.contains("单式票"))

                        {

//							String reg = "(\\d{2})\\.(\\d{2})\\.(\\d{2})\\.(\\d{2})\\.(\\d{2})\\.(\\d{2})";
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));

                        }

                        if (data.contains("复式票"))

                        {
//							chipin.setChipinNums(get11X5LxResult(data, ELEVENCHOOSEFIVE_REG3));
//							chipin.setChipinNums(get11X5FSResult(data,ELEVENCHOOSEFIVE_REG3));
                            if (data.contains("--")) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }
                        }
                        if (data.contains("胆拖票"))

                        {
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));
                        }

                    }

                    if (data.contains("任选七"))

                    {

                        chipin.setManner("7");

                        if (data.contains("单式票"))

                        {
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));

                        }

                        if (data.contains("复式票"))

                        {
                            if (data.contains("--")) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }
                        }

                        if (data.contains("胆拖票"))

                        {
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));
                        }

                    }

                    if (data.contains("任选八"))

                    {

                        chipin.setManner("8");

                        if (data.contains("单式票"))

                        {
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));
                        }

                    }

                    if (data.contains("选前2直选") || data.contains("选前二直选"))

                    {

                        chipin.setManner("9");

                        if (data.contains("单式票"))

                        {
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));
                        }

                        if (data.contains("定位复式票"))

                        {
                            String reg = "[①位|②位|③位]\\:([[\\d{2}|.]\\s*]+)";
                            chipin.setChipinNums(get11X5DWfsResult(data, reg));

                        }

                        if (data.contains("胆拖票"))

                        {
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));
                        }

                    }

                    if (data.contains("选前2组选") || data.contains("选前二组选"))

                    {

                        chipin.setManner("10");

                        if (data.contains("单式票"))

                        {
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));
                        }

                        if (data.contains("复式票"))

                        {
                            if (data.contains("--")) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }
                        }

                        if (data.contains("胆拖票"))

                        {
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));
                        }

                    }

                    if (data.contains("选前3直选") || data.contains("选前三直选"))

                    {
                        chipin.setManner("11");
                        if (data.contains("单式票")) {
                            String reg = "[元|①|②|③|④|⑤][\\s+]?(\\d{2}).(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));
                        }

                        if (data.contains("定位复式票"))

                        {
                            String reg = "[①位|②位|③位]\\:([[\\d{2}|.]\\s*]+)";
                            chipin.setChipinNums(get11X5DWfsResult(data, reg));
                        }

                        if (data.contains("胆拖票"))

                        {
//							chipin.setChipinNums(get11X5LxResult(data, ELEVENCHOOSEFIVE_REG3));
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));

                        }

                    }

                    if (data.indexOf("选前3组选") != -1 || data.indexOf("选前三组选") != -1)

                    {

                        chipin.setManner("12");

                        if (data.indexOf("单式票") != -1)

                        {

//							String reg = "(\\d{2})\\s(\\d{2})\\s(\\d{2})";
                            String reg = "[元|①|②|③|④|⑤][\\s]?(\\d{2}).(\\d{2}).(\\d{2})";
                            chipin.setChipinNums(get11X5DsResult(data, reg));

                        }

                        if (data.indexOf("复式票") != -1)

                        {
//							chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            if (data.indexOf("--") != -1) {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3));
                            } else {
                                chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4));
                            }
//							chipin.setChipinNums(get11X5LxResult(data, ELEVENCHOOSEFIVE_REG3));
//							chipin.setChipinNums(get11X5FsResult(data, ELEVENCHOOSEFIVE_REG2));

                        }

                        if (data.indexOf("胆拖票") != -1)

                        {
//							chipin.setChipinNums(get11X5LxResult(data,ELEVENCHOOSEFIVE_REG3));
                            chipin.setChipinNums(get11X5DTResult(data, ELEVENCHOOSEFIVE_REG2));
//							chipin.setChipinNums(get11X5DtResult(data, ELEVENCHOOSEFIVE_REG2));

                        }

                    }
                    if (data.indexOf("乐选二") != -1) {
                        chipin.setManner("13");
                        if (data.indexOf("--") != -1) {
                            chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3).replace("//", "/"));
                        } else {
                            chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4).replace("//", "/"));
                        }
//                        chipin.setChipinNums(get11X5LxResult(data,ELEVENCHOOSEFIVE_REG3));

                    }
                    if (data.indexOf("乐选三") != -1) {
                        chipin.setManner("14");
                        if (data.indexOf("--") != -1) {
                            chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3).replace("//", "/"));
                        } else {
                            chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4).replace("//", "/"));
                        }
//                        chipin.setChipinNums(get11X5LxResult(data, ELEVENCHOOSEFIVE_REG3));
                    }
                    if (data.indexOf("乐选四") != -1) {
                        chipin.setManner("15");
                        if (data.indexOf("--") != -1) {
                            chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3).replace("//", "/"));
                        } else {
                            chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4).replace("//", "/"));
                        }
//                        chipin.setChipinNums(get11X5LxResult(data, ELEVENCHOOSEFIVE_REG3));
                    }
                    if (data.indexOf("乐选五") != -1) {
                        chipin.setManner("16");
                        if (data.indexOf("--") != -1) {
                            chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG3).replace("//", "/"));
                        } else {
                            chipin.setChipinNums(get11X5FSResult(data, ELEVENCHOOSEFIVE_REG4).replace("//", "/"));
                        }
//                        chipin.setChipinNums(get11X5LxResult(data,ELEVENCHOOSEFIVE_REG3));
                    }

                }

                return chipin;

            }
            if (gamecode == 700)
//			if (data1.indexOf("幸运赛车") != -1)

            {

                chipin = new Chipin();

                String[] params = parseReg(data, SC_REG, "#", "").split("#");

                if (params.length == 3)

                {

                    chipin.setIssue(params[0]);

                    chipin.setBets(Integer.parseInt(params[1]));

                    chipin.setMultiple(Integer.parseInt(params[2]));

                }

                if (chipin != null)

                {

                    if (data.indexOf("颜色前一") != -1)

                    {

                        chipin.setManner("7");

                        String reg = "PrinterPrintString.str\\s*(红|蓝|绿|黄|银|紫)\\s*.PrintData";

                        chipin.setChipinNums(replaceScColor(parseReg(data, reg, "", ",")));

                    } else if (data.indexOf("颜色前二") != -1)

                    {

                        chipin.setManner("8");

                        if (data.indexOf("复式对位") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*([\\S*\\+]*[红|蓝|绿|黄|银|紫])\\s*.PrintData";

                            String tmp1 = data.substring(0, data.indexOf("亚"));

                            String num1 = replaceScColor(parseReg(tmp1, reg, "", "/"));

                            num1 = num1.replace("+", "/");

                            String tmp2 = data.substring(data.indexOf("亚"), data.length());

                            String num2 = replaceScColor(parseReg(tmp2, reg, "", "/"));

                            num2 = num2.replace("+", "/");

                            chipin.setChipinNums(num1 + "|" + num2);

                        } else if (data.indexOf("复式") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*([\\S*\\+]*[红|蓝|绿|黄|银|紫])\\s*.PrintData";

                            String tmp = replaceScColor(parseReg(data, reg, "", ""));

                            chipin.setChipinNums(tmp.replace("+", "//"));

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*([\\S*\\+]*[红|蓝|绿|黄|银|紫])\\s*.PrintData";

                            String tmp1 = data.substring(0, data.indexOf("拖码"));

                            String num1 = replaceScColor(parseReg(tmp1, reg, "", ""));

                            num1 = num1.replace("+", "/");

                            String tmp2 = data.substring(data.indexOf("拖码"), data.length());

                            String num2 = replaceScColor(parseReg(tmp2, reg, "", ""));

                            num2 = num2.replace("+", "/");

                            chipin.setChipinNums(num1 + "$" + num2);

                        } else

                        {

                            String reg = "PrinterPrintString.str\\s*(红|蓝|绿|黄|银|紫)\\+(红|蓝|绿|黄|银|紫)\\s*.PrintData";

                            chipin.setChipinNums(replaceScColor(parseReg(data, reg, "/", ",")));

                        }

                    } else if (data.indexOf("颜色前三") != -1)

                    {

                        chipin.setManner("9");

                        if (data.indexOf("复式对位") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*([\\S*\\+]*[红|蓝|绿|黄|银|紫])\\s*.PrintData";

                            String tmp1 = data.substring(0, data.indexOf("亚"));

                            String num1 = replaceScColor(parseReg(tmp1, reg, "", "/"));

                            num1 = num1.replace("+", "/");

                            String tmp2 = data.substring(data.indexOf("亚"), data.indexOf("季"));

                            String num2 = replaceScColor(parseReg(tmp2, reg, "", "/"));

                            num2 = num2.replace("+", "/");

                            String tmp3 = data.substring(data.indexOf("季"), data.length());

                            String num3 = replaceScColor(parseReg(tmp3, reg, "", "/"));

                            num3 = num3.replace("+", "/");

                            chipin.setChipinNums(num1 + "|" + num2 + "|" + num3);

                        } else if (data.indexOf("复式") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*([\\S*\\+]*[红|蓝|绿|黄|银|紫])\\s*.PrintData";

                            String tmp = replaceScColor(parseReg(data, reg, "", ""));

                            chipin.setChipinNums(tmp.replace("+", "//"));

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*([\\S*\\+]*[红|蓝|绿|黄|银|紫])\\s*.PrintData";

                            String tmp1 = data.substring(0, data.indexOf("拖码"));

                            String num1 = replaceScColor(parseReg(tmp1, reg, "", ""));

                            num1 = num1.replace("+", "/");

                            String tmp2 = data.substring(data.indexOf("拖码"), data.length());

                            String num2 = replaceScColor(parseReg(tmp2, reg, "", ""));

                            num2 = num2.replace("+", "/");

                            chipin.setChipinNums(num1 + "$" + num2);

                        } else

                        {

                            String reg = "PrinterPrintString.str\\s*(红|蓝|绿|黄|银|紫)\\+(红|蓝|绿|黄|银|紫)\\+(红|蓝|绿|黄|银|紫)\\s*.PrintData";

                            chipin.setChipinNums(replaceScColor(parseReg(data, reg, "/", ",")));

                        }

                    } else if (data.indexOf("组选前二") != -1)

                    {

                        chipin.setManner("10");

                        if (data.indexOf("复式") != -1)

                        {

                            chipin.setChipinNums(getScFsResult(data));

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            chipin.setChipinNums(getScDtResult(data));

                        } else

                        {

                            String reg = "PrinterPrintString.str\\s*(\\d+)\\+(\\d+)\\s*.PrintData";

                            chipin.setChipinNums(parseReg(data, reg, "/", ","));

                        }

                    } else if (data.indexOf("组选前三") != -1)

                    {

                        chipin.setManner("11");

                        if (data.indexOf("复式") != -1)

                        {

                            chipin.setChipinNums(getScFsResult(data));

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            chipin.setChipinNums(getScDtResult(data));

                        } else

                        {

                            String reg = "PrinterPrintString.str\\s*(\\d+)\\+(\\d+)\\+(\\d+)\\s*.PrintData";

                            chipin.setChipinNums(parseReg(data, reg, "/", ","));

                        }

                    } else if (data.indexOf("双车位置") != -1)

                    {

                        chipin.setManner("12");

                        if (data.indexOf("复式") != -1)

                        {

                            chipin.setChipinNums(getScFsResult(data));

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            chipin.setChipinNums(getScDtResult(data));

                        } else

                        {

                            String reg = "PrinterPrintString.str\\s*(\\d+)\\+(\\d+)\\s*.PrintData";

                            chipin.setChipinNums(parseReg(data, reg, "/", ","));

                        }

                    } else if (data.indexOf("大小奇偶") != -1)

                    {

                        chipin.setManner("13");

                        String reg = "PrinterPrintString.str\\s*(小奇|小偶|大奇|大偶)\\s*.PrintData";

                        chipin.setChipinNums(replaceScDxqo(parseReg(data, reg, "", ",")));

                    } else if (data.indexOf("前一") != -1)

                    {

                        chipin.setManner("1");

                        String reg = "PrinterPrintString.str\\s*(\\d+)\\s*.PrintData";

                        chipin.setChipinNums(parseReg(data, reg, "", ","));

                    } else if (data.indexOf("前二") != -1)

                    {

                        chipin.setManner("2");

                        if (data.indexOf("直选") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*(\\d+)\\+(\\d+)\\s*.PrintData";

                            chipin.setChipinNums(parseReg(data, reg, "/", ","));

                        } else if (data.indexOf("复式对位") != -1)

                        {

                            String reg = "(\\d{2})[\\+|.]";

                            String tmp1 = data.substring(0, data.indexOf("亚"));

                            String num1 = parseReg(tmp1, reg, "", "/");

                            String tmp2 = data.substring(data.indexOf("亚"), data.length());

                            String num2 = parseReg(tmp2, reg, "", "/");

                            chipin.setChipinNums(num1 + "|" + num2);

                        } else if (data.indexOf("复式") != -1)

                        {

                            chipin.setChipinNums(getScFsResult(data));

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            chipin.setChipinNums(getScDtResult(data));

                        }

                    } else if (data.indexOf("前三") != -1)

                    {

                        chipin.setManner("3");

                        if (data.indexOf("直选") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*(\\d+)\\+(\\d+)\\+(\\d+)\\s*.PrintData";

                            chipin.setChipinNums(parseReg(data, reg, "/", ","));

                        } else if (data.indexOf("复式对位") != -1)

                        {

                            String reg = "(\\d{2})[\\+|.]";

                            String tmp1 = data.substring(0, data.indexOf("亚"));

                            String num1 = parseReg(tmp1, reg, "", "/");

                            String tmp2 = data.substring(data.indexOf("亚"), data.indexOf("季"));

                            String num2 = parseReg(tmp2, reg, "", "/");

                            String tmp3 = data.substring(data.indexOf("季"), data.length());

                            String num3 = parseReg(tmp3, reg, "", "/");

                            chipin.setChipinNums(num1 + "|" + num2 + "|" + num3);

                        } else if (data.indexOf("复式") != -1)

                        {

                            chipin.setChipinNums(getScFsResult(data));

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            chipin.setChipinNums(getScDtResult(data));

                        }

                    } else if (data.indexOf("位置") != -1)

                    {

                        chipin.setManner("4");

                        String reg = "PrinterPrintString.str\\s*(\\d+)\\s*.PrintData";

                        chipin.setChipinNums(parseReg(data, reg, "", ","));

                    } else if (data.indexOf("过二关") != -1)

                    {

                        chipin.setManner("5");

                        if (data.indexOf("直选") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*(\\d+)\\+(\\d+)\\s*.PrintData";

                            chipin.setChipinNums(parseReg(data, reg, "/", ","));

                        } else if (data.indexOf("复式对位") != -1)

                        {

                            String reg = "(\\d{2})[\\+|.]";

                            String tmp1 = data.substring(0, data.indexOf("第二关"));

                            String num1 = parseReg(tmp1, reg, "", "/");

                            String tmp2 = data.substring(data.indexOf("第二关"), data.length());

                            String num2 = parseReg(tmp2, reg, "", "/");

                            chipin.setChipinNums(num1 + "|" + num2);

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            chipin.setChipinNums(getScDtResult(data));

                        }

                    } else if (data.indexOf("过三关") != -1)

                    {

                        chipin.setManner("6");

                        if (data.indexOf("直选") != -1)

                        {

                            String reg = "PrinterPrintString.str\\s*(\\d+)\\+(\\d+)\\+(\\d+)\\s*.PrintData";

                            chipin.setChipinNums(parseReg(data, reg, "/", ","));

                        } else if (data.indexOf("复式对位") != -1)

                        {

                            String reg = "(\\d{2})[\\+|.]";

                            String tmp1 = data.substring(0, data.indexOf("第二关"));

                            String num1 = parseReg(tmp1, reg, "", "/");

                            String tmp2 = data.substring(data.indexOf("第二关"), data.indexOf("第三关"));

                            String num2 = parseReg(tmp2, reg, "", "/");

                            String tmp3 = data.substring(data.indexOf("第三关"), data.length());

                            String num3 = parseReg(tmp3, reg, "", "/");

                            chipin.setChipinNums(num1 + "|" + num2 + "|" + num3);

                        } else if (data.indexOf("胆拖") != -1)

                        {

                            chipin.setChipinNums(getScDtResult(data));

                        }

                    }

                }

            }

        }

        return chipin;

    }


    private static String parseReg(String data, String reg, String gap, String gap2)

    {

        if (!StringUtils.isEmpty(data))

        {

            List<String> list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i <= matcher.groupCount(); i++)

                {

                    sb.append(matcher.group(i));

                    if (i != matcher.groupCount())

                    {

                        sb.append(gap);

                    }

                }

                list.add(sb.toString());

            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i != list.size() - 1)

                {

                    sb.append(gap2);

                }

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 赛车复式
     */

    public static String getScFsResult(String data)

    {

        String reg = "(\\d{2})[\\+|.]";

        return parseReg(data, reg, "", "//");

    }


    /**
     * 赛车胆拖
     */

    public static String getScDtResult(String data)

    {

        String d_tmp = data.substring(0, data.indexOf("拖码"));

        String d_reg = "PrinterPrintString.str\\s*([\\d+\\+]*)\\s*.PrintData";

        String d = parseReg(d_tmp, d_reg, "", "");

        d = d.replace("+", "/");

        String t_tmp = data.substring(data.indexOf("拖码"), data.length());

        String t_reg = "(\\d{2})[\\+|.]";

        String t = parseReg(t_tmp, t_reg, "", "/");

        return d + "$" + t;

    }


    public static String replaceScColor(String str)

    {

        return str.replace("红", "1").replace("蓝", "2").replace("绿", "3").replace("黄", "4").replace("银", "5").replace("紫", "6");

    }


    public static String replaceScDxqo(String str)

    {

        return str.replace("小奇", "1").replace("小偶", "2").replace("大奇", "3").replace("大偶", "4");

    }


    /**
     * @param data 012
     * @param str  "/"
     * @return 0/1/2
     */

    public static String plusStr(String data, String str)

    {

        if (data != null)

        {

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < data.length(); i++)

            {

                char c = data.charAt(i);

                sb.append(c);

                if (i != data.length() - 1)

                {

                    sb.append(str);

                }

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回排3复式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getP3FsResult(String data, String reg)

    {

        if (data != null)

        {

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            StringBuilder sb = new StringBuilder();

            while (matcher.find())

            {

                for (int i = 1; i <= matcher.groupCount(); i++)

                {

                    String tmp = matcher.group(i);
                    tmp = tmp.replaceAll("\\D+", "");
                    sb.append(plusStr(tmp, "/"));
//					list.add(plusStr(tmp, "/"));
//					if (tmp.matches("\\d{1}"))
//
//					{
//
//						sb.append(tmp);
//
//						sb.append("/");
//
//					}

                }

                if (sb.toString().endsWith("/"))

                {

                    sb.delete(sb.length() - 1, sb.length());

                }

                sb.append("//");

            }

            if (sb.toString().endsWith("//"))

            {

                sb.delete(sb.length() - 2, sb.length());

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回排3胆拖票信息
     */

    private static String getP3DtResult(String data)

    {

        if (!StringUtils.isEmpty(data))

        {

            try {

                List<String> dataList = regxpMakeDataList(data, "胆码:([\\s\\d]+)拖码:([\\s\\d]+)").get(0);

                if (dataList != null && dataList.size() == 2)

                {

                    StringBuilder sb = new StringBuilder();

                    String dTmp = dataList.get(0).replace(" ", "/");

                    if (dTmp.lastIndexOf("/") != -1) {

                        dTmp = dTmp.substring(1);

                    }

                    String tTmp = dataList.get(1).replace(" ", "/");

                    if (tTmp.lastIndexOf("/") != -1) {

                        tTmp = tTmp.substring(1);

                    }

                    sb.append(dTmp);

                    sb.append("//");

                    sb.append(tTmp);

                    return sb.toString();

                }

                //System.out.println(dataList);

            } catch (Exception e) {

                //System.out.println(e.getMessage());

            }

        }

        return "";

    }


    /**
     * 返回排3组选&和值票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getP3ZxResult(String data, String reg)

    {

        if (data != null)

        {

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            if (matcher.find())

            {

                String tmp = matcher.group(1);
                System.out.println(">>>>>>>>>" + tmp);
                tmp = tmp.replaceAll("\\D+", "/");

                if (tmp.endsWith("/"))

                {

                    tmp = tmp.substring(0, tmp.length() - 1);

                }

                return tmp;

            }

        }

        return "";

    }


    /**
     * 返回大乐透胆拖式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getDltDtResult(String data, String reg)

    {

        if (!StringUtils.isEmpty(data))

        {

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                String tmp = matcher.group(1);

                tmp = tmp.replace(".", "");

                tmp = tmp.replaceAll("\\D+", "/");

                if (tmp.lastIndexOf("/") != -1)

                {

                    tmp = tmp.substring(1);

                }

                if (tmp.endsWith("/"))

                {

                    tmp = tmp.substring(0, tmp.length() - 1);

                }

                return tmp;

            }

        }

        return "";

    }


    /**
     * 返回大乐透复式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getDltFsResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {
            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {
                System.out.println(matcher.group());
                String tmp = matcher.group(1);
                System.out.println(">>>>>>>>>>" + tmp);
                tmp = tmp.replace(".", "");
                tmp = tmp.replaceAll("\\D+", "/");
                StringBuilder sb = new StringBuilder();
                String[] tmps = tmp.split("/");
                for (String str : tmps) {
                    if (str.matches("\\d{2}")) {
                        sb.append(str).append("/");
                    }
                }
                if (sb.toString().endsWith("/")) {
                    sb.deleteCharAt(sb.toString().length() - 1);
                }
                list.add(sb.toString());

            }

        }

        if (list != null && list.size() == 2)

        {
            return list.get(0) + "//" + list.get(1);

        }

        return "";

    }


    /**
     * 返回大乐透单式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getDltDsResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                StringBuilder sb = new StringBuilder();

                sb.append(matcher.group(1).replace("+", "/"));

                sb.append("//");

                sb.append(matcher.group(2).replace("+", "/"));

                list.add(sb.toString());

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i != list.size() - 1)

                {

                    sb.append(",");

                }

            }

            return sb.toString();

        }

        return "";

    }

    /**
     * 返回大乐透单式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getDltDsNewResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                StringBuilder sb = new StringBuilder();

                sb.append(matcher.group(1).replace("  ", "/").replace(" ", "/"));

                sb.append("//");

                sb.append(matcher.group(2).replace("  ", "/").replace(" ", "/"));

                list.add(sb.toString());

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i != list.size() - 1)

                {

                    sb.append(",");

                }

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回数字彩复式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getDigitDoubleResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                String tmp = matcher.group(1);

                tmp = tmp.replaceAll("\\D+", "");

                list.add(plusStr(tmp, "/"));

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i != list.size() - 1)

                {

                    sb.append("//");

                }

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回11选5定位复式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return 01/02//03/04
     */

    private static String get11X5DwfsResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i <= matcher.groupCount(); i++)

                {

                    if (matcher.group(i).matches("\\d{2}"))

                    {

                        sb.append(matcher.group(i));

                        sb.append("/");

                    }

                }

                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }

                list.add(sb.toString());

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i != list.size() - 1)

                {

                    sb.append("//");

                }

            }

            return sb.toString();

        }

        return "";

    }

    /**
     * 返回11选5定位复式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return 01/02//03/04
     */

    private static String get11X5DWfsResult(String data, String reg)

    {

        if (data != null)

        {

//			list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            StringBuilder sb = new StringBuilder();
            while (matcher.find())

            {


                String temp = matcher.group();


                Pattern pattern1 = Pattern.compile("(\\d{2})");

                Matcher matcher1 = pattern1.matcher(temp);
                while (matcher1.find()) {
                    sb.append(matcher1.group());

                    sb.append("/");
                }
                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }
                sb.append("//");
            }
            for (int i = 0; i < 3; i++) {
                if (sb.toString().endsWith("$"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }
            }

            if (sb != null && sb.length() > 0)

            {
                if (sb.toString().endsWith("//"))

                {

                    return sb.substring(0, sb.length() - 2);

                } else {
                    return sb.toString();
                }

            }

        }

        return "";

    }


    /**
     * 返回11选5任选胆拖票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return 01$02/03/04
     */

    private static String get11X5DtResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i <= matcher.groupCount(); i++)

                {

                    String tmp = matcher.group(i);

                    if (tmp.matches("\\d{2}"))

                    {

                        sb.append(tmp);

                        sb.append("/");

                    }

                }

                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }

                if (sb != null && sb.length() > 0)

                {

                    list.add(sb.toString());

                }

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i == 0)

                {

                    sb.append("$");

                } else if (i != list.size() - 1)

                {

                    sb.append("/");

                }

            }

            return sb.toString();

        }

        return "";

    }

    /**
     * 返回11选5任选胆拖票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return 01$02/03/04
     */

    private static String get11X5DTResult(String data, String reg)

    {

//		List<String> list = null;

        if (data != null)

        {

//			list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            StringBuilder sb = new StringBuilder();
            while (matcher.find())

            {


                String temp = matcher.group();
                Pattern pattern1 = Pattern.compile("(\\d{2})");

                Matcher matcher1 = pattern1.matcher(temp);
                while (matcher1.find()) {
                    sb.append(matcher1.group());

                    sb.append("/");
                }
//
//				String[] strings = temp.replace("码", "").replace("\r\n","").split(" ");
//				for(int i=0;i<strings.length;i++){
//					if (strings[i].matches("\\d{2}")){
//						sb.append(strings[i]);
//
//						sb.append("/");
//					}
//				}


                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }
                sb.append("$");

            }
            for (int i = 0; i < 3; i++) {
                if (sb.toString().endsWith("$"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }
            }

            if (sb != null && sb.length() > 0)

            {

                return sb.toString();

            }

        }

        return "";

    }

    /**
     * 返回11选5任选胆拖票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return 01$02/03/04
     */
    private static String get11X5FSResult(String data, String reg)

    {

//		List<String> list = null;

        if (data != null)

        {

//			list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            StringBuilder sb = new StringBuilder();
            while (matcher.find())

            {


                String temp = matcher.group();

                Pattern pattern1 = Pattern.compile("(\\d{2})");

                Matcher matcher1 = pattern1.matcher(temp);
                while (matcher1.find()) {
                    sb.append(matcher1.group());

                    sb.append("//");
                }

            }
            if (sb != null && sb.length() > 0) {
                if (sb.toString().endsWith("//"))

                {
                    return sb.substring(0, sb.length() - 2);


                } else {
                    return sb.toString();
                }
            }

        }

        return "";

    }

    /**
     * 返回11选5任选胆拖票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return 01$02/03/04
     */

    private static String get11X5LxResult(String data, String reg)

    {

//		List<String> list = null;

        if (data != null)

        {

//			list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            StringBuilder sb = new StringBuilder();
            while (matcher.find())

            {


                String temp = matcher.group();

                String[] strings = temp.replace("元", "").replace("\r\n", "").split(" ");
                for (int i = 0; i < strings.length; i++) {
                    if (strings[i].matches("\\d{2}")) {
                        sb.append(strings[i]);

                        sb.append("/");
                    }
                }


                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }

            }
            if (sb != null && sb.length() > 0)

            {

                return sb.toString();

            }

        }

        return "";

    }


    /**
     * 返回11选5任选复式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return 01//02//03//04
     */

    private static String get11X5FsResult(String data, String reg)

    {

        if (data != null)

        {

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            StringBuilder sb = new StringBuilder();

            while (matcher.find())

            {

                for (int i = 1; i <= matcher.groupCount(); i++)

                {

                    String tmp = matcher.group(i);

                    if (tmp.matches("\\d{2}"))

                    {

                        sb.append(tmp);

                        sb.append("//");

                    }

                }

            }

            if (sb.toString().endsWith("//"))

            {

                sb.delete(sb.length() - 2, sb.length());

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回11选5任选单式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return 01/02/03/04
     */

    private static String get11X5DsResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i <= matcher.groupCount(); i++)

                {
                    sb.append(matcher.group(i));

                    if (i != matcher.groupCount())

                    {

                        sb.append("/");

                    }

                }

                list.add(sb.toString());

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i != list.size() - 1)

                {

                    sb.append(",");

                }

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回北单投注方式
     *
     * @param data
     * @return
     */

    private static String getManner(String data)

    {

        String reg = "(\\d+串\\d+)";

        Pattern pattern = Pattern.compile(reg);

        Matcher matcher = pattern.matcher(data);

        if (matcher.find())

        {

            return matcher.group(1);

        } else

        {

            return "单关";

        }

    }


    /**
     * 返回期号&倍数&金额
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static Chipin getChipin(String data, String reg)

    {

        Chipin chipin = null;

        if (data != null)

        {

            chipin = new Chipin();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            if (matcher.find())

            {

                System.out.println(matcher.group(1) + "====" + matcher.group(2) + "====" + matcher.group(3));

                String issue = matcher.group(1);

                int multiple = Integer.parseInt(matcher.group(2));

                int bets = Integer.parseInt(matcher.group(3));

                chipin.setIssue(issue);

                chipin.setMultiple(multiple);

                chipin.setBets(bets);

            }

        }

        return chipin;

    }


    /**
     * 返回北单比分票信息
     *
     * @param data 票机返回串
     * @return
     */

    private static String getSingleBfResult(String data)

    {

        if (data != null)

        {

            String reg1 = "\\(\\s*(\\d+)..*\\)";//对阵编号正则

            Pattern pattern1 = Pattern.compile(reg1);

            Matcher matcher1 = pattern1.matcher(data);

            List<String> list1 = new ArrayList<String>();

            while (matcher1.find())

            {

                list1.add(matcher1.group(1));

            }


            String reg2 = "([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+" +

                    "([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+(\\S+)\\s+";//胜&负正则

            Pattern pattern2 = Pattern.compile(reg2);

            Matcher matcher2 = pattern2.matcher(data);

            List<String> list2 = new ArrayList<String>();

            while (matcher2.find())

            {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i <= matcher2.groupCount(); i++)

                {

                    String tmp = matcher2.group(i);

                    tmp = tmp.replace("-", "").replace("胜其它", "4:3").replace("负其它", "3:4");

                    if (!tmp.equals(""))

                    {

                        sb.append(tmp);

                        sb.append("/");

                    }

                }

                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }

                list2.add(sb.toString());

            }


            String reg3 = "len:\\d+\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+([\\d{1}|-][:|-][\\d{1}|-])\\s+(\\S+)\\s+Pr";//平正则

            Pattern pattern3 = Pattern.compile(reg3);

            Matcher matcher3 = pattern3.matcher(data);

            List<String> list3 = new ArrayList<String>();

            while (matcher3.find())

            {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i <= matcher3.groupCount(); i++)

                {

                    String tmp = matcher3.group(i);

                    tmp = tmp.replace("-", "").replace("平其它", "4:4");

                    if (!tmp.equals(""))

                    {

                        sb.append(tmp);

                        sb.append("/");

                    }

                }

                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }

                list3.add(sb.toString());

            }

            if (list1.size() == list3.size() && list1.size() * 2 == list2.size())

            {

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < list1.size(); i++)

                {

                    sb.append(list1.get(i));

                    sb.append(",");

                    if (!list2.get(i * 2).equals(""))

                    {

                        sb.append(list2.get(i * 2));

                        sb.append("/");

                    }

                    if (!list3.get(i).equals(""))

                    {

                        sb.append(list3.get(i));

                        sb.append("/");

                    }

                    if (!list2.get(i * 2 + 1).equals(""))

                    {

                        sb.append(list2.get(i * 2 + 1));

                        sb.append("/");

                    }

                    if (sb.toString().endsWith("/"))

                    {

                        sb.deleteCharAt(sb.length() - 1);

                    }

                    if (i != list1.size() - 1)

                    {

                        sb.append("//");

                    }

                }

                return sb.toString();

            }

        }

        return "";

    }


    /**
     * 返回北单票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getSingleResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                StringBuilder sb = new StringBuilder();

                sb.append(matcher.group(1));

                sb.append(",");

                for (int i = 2; i <= matcher.groupCount(); i++)

                {

                    String tmp = matcher.group(i);

                    if (!tmp.matches("\\d{1}-\\d{1}"))

                    {

                        tmp = tmp.replace("-", "").replace("+", "");

                    }

                    if (!tmp.equals(""))

                    {

                        sb.append(tmp);

                        sb.append("/");

                    }

                }

                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }

                list.add(sb.toString());

                //System.out.println(sb.toString());

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i != list.size() - 1)

                {

                    sb.append("//");

                }

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回4场进球复式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getZjqDoubleResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i <= matcher.groupCount(); i++)

                {

                    String tmp = matcher.group(i);

                    tmp = tmp.replaceAll("\\D+", "");

                    if (tmp.matches("\\d{1}"))

                    {

                        sb.append(tmp);

                        sb.append("/");

                    }

                }

                if (sb.toString().endsWith("/"))

                {

                    sb.deleteCharAt(sb.length() - 1);

                }

                list.add(sb.toString());

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                sb.append(list.get(i));

                if (i != list.size() - 1)

                {

                    sb.append("//");

                }

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回传统足球复式票信息
     *
     * @param data ==> 票机返回串
     * @param reg  ==> 正则
     * @return
     */

    private static String getDoubleResult(String data, String reg)

    {

        Map<Integer, String[]> map = null;

        if (data != null)

        {

            map = new HashMap<Integer, String[]>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            int num = 0;

            while (matcher.find())

            {

                String tmp = matcher.group(1);

                String[] tmps = tmp.split("  ");

                map.put(num, tmps);

                num++;

            }

        }

        if (map != null && map.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            int length = map.get(0).length;

            for (int i = 0; i < length; i++)

            {

                for (int j = 0; j < map.size(); j++)

                {

                    String tmp = map.get(j)[i];

                    if (tmp.matches("\\d{1}"))

                    {

                        sb.append(tmp);

                        if (j != map.size() - 1)

                        {

                            boolean b = false;

                            for (int k = j + 1; k < map.size(); k++)

                            {

                                if (map.get(k)[i].matches("\\d{1}"))

                                {

                                    b = true;

                                    break;

                                }

                            }

                            if (b)

                            {

                                sb.append("/");

                            }

                        }

                    }

                }

                if (i == 0 && sb.length() == 0)

                {

                    sb.append("9");

                }

                if (i != length - 1)

                {

                    if (sb.toString().endsWith("/"))

                    {

                        sb.append("9");

                    }

                    sb.append("//");

                } else

                {

                    if (sb.toString().endsWith("/"))

                    {

                        sb.append("9");

                    }

                }

            }

            return sb.toString();

        }

        return "";

    }


    /**
     * 返回传统足球单式票信息
     *
     * @param data 票机返回串
     * @param reg  正则
     * @return
     */

    private static String getResult(String data, String reg)

    {

        List<String> list = null;

        if (data != null)

        {

            list = new ArrayList<String>();

            Pattern pattern = Pattern.compile(reg);

            Matcher matcher = pattern.matcher(data);

            while (matcher.find())

            {

                list.add(matcher.group(1));

            }

        }

        if (list != null && list.size() > 0)

        {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++)

            {

                String tmp = list.get(i);

                tmp = tmp.replace("-", "9");

                tmp = tmp.replaceAll("\\D+", "/");

                if (tmp.endsWith("/"))

                {

                    tmp = tmp.substring(0, tmp.length() - 1);

                }

                sb.append(tmp);

                if (i != list.size() - 1)

                {

                    sb.append(",");

                }

            }

            return sb.toString();

        }

        return "";

    }


    private static String filterData(String data)

    {
        String reg = "PrinterPrintString\\x00str\\s([\\S\\s]*?)\\x00";
        String reg1 = "PrinterPrintString\\s*str\\s([\\S\\s]*?)\\s*PrintData";

        Pattern pattern1 = Pattern.compile(reg1);

        Matcher matcher1 = pattern1.matcher(data);

        Pattern pattern = Pattern.compile(reg);

        Matcher matcher = pattern.matcher(data);

        StringBuilder sb = new StringBuilder();

        if (matcher.find()) {
            sb.append(matcher.group(1));
            while (matcher.find()) {
                sb.append(matcher.group(1));
            }

            return sb.toString();
        }
        if (matcher1.find()) {
            sb.append(matcher1.group(1));
            while (matcher1.find()) {
                sb.append(matcher1.group(1));
            }

            return sb.toString();
        }
        return sb.toString();

//		String reg = "PrinterPrintString\\x00str\\s([\\S\\s]*?)\\x00";
////		reg = "PrinterPrintString\\s*str\\s*(\\S*\\s*)\\s*PrintData";
//
//		Pattern pattern = Pattern.compile(reg);
//
//		Matcher matcher = pattern.matcher(data);
//
//		StringBuilder sb = new StringBuilder();
//
//		while (matcher.find())
//
//		{
//
//			sb.append(matcher.group(1));
//
//		}
//
//		return sb.toString();

    }

    /**
     * 判断是否是追加投注
     *
     * @param data
     * @param reg
     * @return boolean
     */
    private static boolean judgeZJTZ(String data, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            return true;
        } else {
            return false;
        }
    }

    private static List<List<String>> regxpMakeDataList(String str, String regxp)

    {

        List<List<String>> dataList = new ArrayList<List<String>>();

        if (!StringUtils.isEmpty(str) && !StringUtils.isEmpty(regxp))

        {

            Pattern pattern = Pattern.compile(regxp);

            Matcher matcher = pattern.matcher(str);

            while (matcher.find())

            {

                List<String> tmpDataList = new ArrayList<String>();

                for (int i = 1; i <= matcher.groupCount(); i++)

                {

                    tmpDataList.add(matcher.group(i));

                }

                if (tmpDataList != null && tmpDataList.size() > 0) {


                    dataList.add(tmpDataList);

                }

            }

        }

        return dataList;

    }

}

