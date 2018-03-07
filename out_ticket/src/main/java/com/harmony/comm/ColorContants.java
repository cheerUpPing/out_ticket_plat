package com.harmony.comm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cm on 2017/1/10.
 */
public class ColorContants {
    public static final Map<String, String> COLOR_KEY_MAP = new HashMap<String, String>();
    //竞彩玩法进入按键
    public static final Map<String, String> JC_KEY_MAP = new HashMap<String, String>();
    //高频玩法进入按键
    public static final Map<String, String> GP_KEY_MAP = new HashMap<String, String>();
    //日期
    public static final Map<String, String> DATE_MAP = new HashMap<String, String>();
    //过关方式
    public static final Map<String, String> GGFS_MAP = new HashMap<String, String>();
    //竞彩足球胜平负
    public static final Map<String, String> JC_ZQ_SPF_MAP = new HashMap<String, String>();

    //竞彩足球比分
    public static final Map<String, String> JC_ZQ_BF_MAP = new HashMap<String, String>();
    //竞彩足球总进球
    public static final Map<String, String> JC_ZQ_ZJQ_MAP = new HashMap<String, String>();
    //竞彩足球半全场
    public static final Map<String, String> JC_ZQ_BQC_MAP = new HashMap<String, String>();
    //竞彩篮球胜负/让分胜负
    public static final Map<String, String> JC_LQ_SF_MAP = new HashMap<String, String>();

    //竞彩篮球胜分差
    public static final Map<String, String> JC_LQ_SFC_MAP = new HashMap<String, String>();

    //竞彩篮球大小分
    public static final Map<String, String> JC_LQ_DXF_MAP = new HashMap<String, String>();

    //排三玩法按键
    public static final Map<String, String> PAI3MAP = new HashMap<String, String>();

    //其余高频玩法按键
    public static final Map<String, String> GAOP_MAP = new HashMap<String, String>();

    //广东高频玩法按键
    public static final Map<String, String> GAOP_MAP401 = new HashMap<String, String>();

    //自由过关 manner - key
    public static final Map<String,String> FREE_KEY_MAP = new HashMap<String,String>();

    static
    {
        //高频玩法按键
        GAOP_MAP.put("1","0 1 ");
        GAOP_MAP.put("2","0 2 ");
        GAOP_MAP.put("3","0 3 ");
        GAOP_MAP.put("4","0 4 ");
        GAOP_MAP.put("5","0 5 ");
        GAOP_MAP.put("6","0 6 ");
        GAOP_MAP.put("7","0 7 ");
        GAOP_MAP.put("8","0 8 ");
        GAOP_MAP.put("9","1 0 ");
        GAOP_MAP.put("10","0 9 ");
        GAOP_MAP.put("11","1 2 ");
        GAOP_MAP.put("12","1 1 ");
        //高频乐选
        GAOP_MAP.put("13","2 4 ");//乐选二
        GAOP_MAP.put("14","2 3 ");//乐选三
        GAOP_MAP.put("15","2 1 ");//乐选四
        GAOP_MAP.put("16","2 2 ");//乐选五
        //广东高频玩法按键
        GAOP_MAP401.put("1","0 1 ");
        GAOP_MAP401.put("2","0 2 ");
        GAOP_MAP401.put("3","0 3 ");
        GAOP_MAP401.put("4","0 4 ");
        GAOP_MAP401.put("5","0 5 ");
        GAOP_MAP401.put("6","0 6 ");
        GAOP_MAP401.put("7","0 7 ");
        GAOP_MAP401.put("8","0 8 ");
        GAOP_MAP401.put("9","1 0 ");
        GAOP_MAP401.put("10","0 9 ");
        GAOP_MAP401.put("11","1 2 ");
        GAOP_MAP401.put("12","1 1 ");
        //广东高频乐选
        GAOP_MAP401.put("13","2 2 ");//乐选二
        GAOP_MAP401.put("14","2 3 ");//乐选三
        GAOP_MAP401.put("15","2 4 ");//乐选四
        GAOP_MAP401.put("16","2 5 ");//乐选五


        //排三玩法按键

        PAI3MAP.put("23","0 3 ");
        PAI3MAP.put("26","0 4 ");
        PAI3MAP.put("31","0 5 ");
        PAI3MAP.put("32","0 6 ");
        PAI3MAP.put("109","0 3 ");
        PAI3MAP.put("108","0 4 ");
        PAI3MAP.put("102","0 5 ");

        //彩种对应按键
        COLOR_KEY_MAP.put("JC","5 1 ");
        COLOR_KEY_MAP.put("GP","0 8 ");
        COLOR_KEY_MAP.put("110","0 1 ");
        COLOR_KEY_MAP.put("105","0 2 ");
        COLOR_KEY_MAP.put("109","0 3 ");
        COLOR_KEY_MAP.put("108","0 4 ");
        COLOR_KEY_MAP.put("102","0 5 ");
        COLOR_KEY_MAP.put("106","0 6 ");
        COLOR_KEY_MAP.put("107","0 7 ");
        COLOR_KEY_MAP.put("103","0 9 ");

        //竞彩篮球胜负
        JC_LQ_SFC_MAP.put("01", "1 1 ");
        JC_LQ_SFC_MAP.put("02", "1 2 ");
        JC_LQ_SFC_MAP.put("03", "1 3 ");
        JC_LQ_SFC_MAP.put("04", "1 4 ");
        JC_LQ_SFC_MAP.put("05", "1 5 ");
        JC_LQ_SFC_MAP.put("06", "1 6 ");
        JC_LQ_SFC_MAP.put("51", "0 1 ");
        JC_LQ_SFC_MAP.put("52", "0 2 ");
        JC_LQ_SFC_MAP.put("53", "0 3 ");
        JC_LQ_SFC_MAP.put("54", "0 4 ");
        JC_LQ_SFC_MAP.put("55", "0 5 ");
        JC_LQ_SFC_MAP.put("56", "0 6 ");
        //竞彩篮球大小分
        JC_LQ_DXF_MAP.put("1", "1 ");//大分
        JC_LQ_DXF_MAP.put("2", "2 ");//小分
        //竞彩篮球胜负
        JC_LQ_SF_MAP.put("0", "2 ");//负
        JC_LQ_SF_MAP.put("1", "1 ");//胜
        //竞彩玩法对应按键
        GP_KEY_MAP.put("501","5 1 ");
        GP_KEY_MAP.put("502","5 2 ");
        GP_KEY_MAP.put("503","5 3 ");
        GP_KEY_MAP.put("504","5 4 ");
        GP_KEY_MAP.put("505","6 2 ");
        GP_KEY_MAP.put("506","6 1 ");
        GP_KEY_MAP.put("507","6 3 ");
        GP_KEY_MAP.put("508","6 4 ");
        GP_KEY_MAP.put("509","5 9 ");
        GP_KEY_MAP.put("510","6 9 ");
        GP_KEY_MAP.put("511","5 6 ");
        //高频玩法对应按键
        JC_KEY_MAP.put("501","5 1 ");
        JC_KEY_MAP.put("502","5 2 ");
        JC_KEY_MAP.put("503","5 3 ");
        JC_KEY_MAP.put("504","5 4 ");
        JC_KEY_MAP.put("505","6 2 ");
        JC_KEY_MAP.put("506","6 1 ");
        JC_KEY_MAP.put("507","6 3 ");
        JC_KEY_MAP.put("508","6 4 ");
        JC_KEY_MAP.put("509","5 9 ");
        JC_KEY_MAP.put("510","6 9 ");
        JC_KEY_MAP.put("511","5 6 ");
        //竞彩足球总进球
        JC_ZQ_ZJQ_MAP.put("0","0 ");
        JC_ZQ_ZJQ_MAP.put("1","1 ");
        JC_ZQ_ZJQ_MAP.put("2","2 ");
        JC_ZQ_ZJQ_MAP.put("3","3 ");
        JC_ZQ_ZJQ_MAP.put("4","4 ");
        JC_ZQ_ZJQ_MAP.put("5","5 ");
        JC_ZQ_ZJQ_MAP.put("6","6 ");
        JC_ZQ_ZJQ_MAP.put("7","7 ");

        //竞彩足球半全场
        JC_ZQ_BQC_MAP.put("0_0","0 0 ");
        JC_ZQ_BQC_MAP.put("0_1","0 1 ");
        JC_ZQ_BQC_MAP.put("0_3","0 3 ");
        JC_ZQ_BQC_MAP.put("1_0","1 0 ");
        JC_ZQ_BQC_MAP.put("1_1","1 1 ");
        JC_ZQ_BQC_MAP.put("1_3","1 3 ");
        JC_ZQ_BQC_MAP.put("3_0","3 0 ");
        JC_ZQ_BQC_MAP.put("3_1","3 1 ");
        JC_ZQ_BQC_MAP.put("3_3","3 3 ");
        //竞彩足球比分对应按键
        JC_ZQ_BF_MAP.put("0:0","0 0 ");
        JC_ZQ_BF_MAP.put("0:1","0 1 ");
        JC_ZQ_BF_MAP.put("0:2","0 2 ");
        JC_ZQ_BF_MAP.put("0:3","0 3 ");
        JC_ZQ_BF_MAP.put("0:4","0 4 ");
        JC_ZQ_BF_MAP.put("0:5","0 5 ");
        JC_ZQ_BF_MAP.put("3:4","0 9 ");
        JC_ZQ_BF_MAP.put("1:0","1 0 ");
        JC_ZQ_BF_MAP.put("1:1","1 1 ");
        JC_ZQ_BF_MAP.put("1:2","1 2 ");
        JC_ZQ_BF_MAP.put("1:3","1 3 ");
        JC_ZQ_BF_MAP.put("1:4","1 4 ");
        JC_ZQ_BF_MAP.put("1:5","1 5 ");
        JC_ZQ_BF_MAP.put("2:0","2 0 ");
        JC_ZQ_BF_MAP.put("2:1","2 1 ");
        JC_ZQ_BF_MAP.put("2:2","2 2 ");
        JC_ZQ_BF_MAP.put("2:3","2 3 ");
        JC_ZQ_BF_MAP.put("2:4","2 4 ");
        JC_ZQ_BF_MAP.put("2:5","2 5 ");
        JC_ZQ_BF_MAP.put("3:0","3 0 ");
        JC_ZQ_BF_MAP.put("3:1","3 1 ");
        JC_ZQ_BF_MAP.put("3:2","3 2 ");
        JC_ZQ_BF_MAP.put("3:3","3 3 ");
        JC_ZQ_BF_MAP.put("4:0","4 0 ");
        JC_ZQ_BF_MAP.put("4:1","4 1 ");
        JC_ZQ_BF_MAP.put("4:2","4 2 ");
        JC_ZQ_BF_MAP.put("5:0","5 0 ");
        JC_ZQ_BF_MAP.put("5:1","5 1 ");
        JC_ZQ_BF_MAP.put("5:2","5 2 ");
        JC_ZQ_BF_MAP.put("4:3","9 0 ");
        JC_ZQ_BF_MAP.put("4:4","9 9 ");
        //日期
        DATE_MAP.put("周一", "1 ");
        DATE_MAP.put("周二", "2 ");
        DATE_MAP.put("周三", "3 ");
        DATE_MAP.put("周四", "4 ");
        DATE_MAP.put("周五", "5 ");
        DATE_MAP.put("周六", "6 ");
        DATE_MAP.put("周日", "7 ");
        //过关方式
        GGFS_MAP.put("1", "0 1 ");
        GGFS_MAP.put("2", "0 2 ");
        GGFS_MAP.put("3", "0 2 ");
        GGFS_MAP.put("4", "0 3 ");
        GGFS_MAP.put("5", "0 4 ");
        GGFS_MAP.put("6", "0 2 ");
        GGFS_MAP.put("7", "0 3 ");
        GGFS_MAP.put("8", "0 4 ");
        GGFS_MAP.put("9", "0 5 ");
        GGFS_MAP.put("10", "0 6 ");
        GGFS_MAP.put("11", "0 2 ");
        GGFS_MAP.put("12", "0 3 ");
        GGFS_MAP.put("13", "0 4 ");
        GGFS_MAP.put("14", "0 5 ");
        GGFS_MAP.put("15", "0 6 ");
        GGFS_MAP.put("16", "0 7 ");
        GGFS_MAP.put("17", "0 8 ");
        GGFS_MAP.put("18", "0 2 ");
        GGFS_MAP.put("19", "0 3 ");
        GGFS_MAP.put("20", "0 4 ");
        GGFS_MAP.put("21", "0 5 ");
        GGFS_MAP.put("22", "0 6 ");
        GGFS_MAP.put("23", "0 7 ");
        GGFS_MAP.put("24", "0 8 ");
        GGFS_MAP.put("25", "0 9 ");
        GGFS_MAP.put("26", "1 0 ");
        GGFS_MAP.put("27", "1 1 ");
        GGFS_MAP.put("28", "0 2 ");
        GGFS_MAP.put("29", "0 3 ");
        GGFS_MAP.put("30", "0 4 ");
        GGFS_MAP.put("31", "0 5 ");
        GGFS_MAP.put("32", "0 6 ");
        GGFS_MAP.put("33", "0 7 ");
        GGFS_MAP.put("35", "0 2 ");
        GGFS_MAP.put("36", "0 3 ");
        GGFS_MAP.put("37", "0 4 ");
        GGFS_MAP.put("38", "0 5 ");
        GGFS_MAP.put("39", "0 6 ");
        GGFS_MAP.put("40", "0 7 ");
        GGFS_MAP.put("41", "0 8 ");
        //竞彩足球胜平负
        JC_ZQ_SPF_MAP.put("3", "3 ");
        JC_ZQ_SPF_MAP.put("1", "1 ");
        JC_ZQ_SPF_MAP.put("0", "0 ");

        //过关对面manner
        FREE_KEY_MAP.put("1","1 ");
        FREE_KEY_MAP.put("2","2 ");
        FREE_KEY_MAP.put("3","3 ");
        FREE_KEY_MAP.put("6","4 ");
        FREE_KEY_MAP.put("11","5 ");
        FREE_KEY_MAP.put("18","6 ");
        FREE_KEY_MAP.put("28","7 ");
        FREE_KEY_MAP.put("35","8 ");

    }
}
