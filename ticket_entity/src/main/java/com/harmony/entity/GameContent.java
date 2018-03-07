package com.harmony.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cm on 2017/1/6.
 */
public class GameContent {
    //保存玩法对应的串关方式
    public static Map<String, String> bjdc_manner_to_name = new HashMap<>(30);

    public final static int Bjdc_Rqspf = 301;
    public final static int Bjdc_Sxpds = 302;
    public final static int Bjdc_Zjqs = 303;
    public final static int Bjdc_Bf = 304;
    public final static int Bjdc_Bqcspf = 305;
    public final static int Bjdc_sf = 306;

    public final static int JCspf = 501;
    public final static int JCbf = 502;
    public final static int JCzjq = 503;
    public final static int JCbqc = 504;
    public final static int JCLanQiuSF = 505;
    public final static int JCLanQiuRFSF = 506;
    public final static int JCLanQiuSFC = 507;
    public final static int JCLanQiuDX = 508;
    public final static int JCHunhe = 509;
    public final static int LCHunhe = 510;
    public final static int JCRFSPF = 511;
    public final static int JCGuanJun = 512;
    public final static int JCGuanYanJun = 513;
    public final static int sfc6 = 107;
    public final static int p11_5 = 401;
    public final static int h11_5 = 402;
    public final static int ln11_5 = 403;
    public final static int wl11_5 = 404;
    public final static int yunlan11_5 = 405;
    public final static int jx11_5 = 406;
    public final static int sx11_5 = 407;
    public final static int hb11_5 = 408;
    public final static int qh11_5 = 409;
    public final static int sfc = 102;
    public final static int sfc9 = 103;
    public final static int sfc4 = 106;
    public final static int star7 = 108;
    public final static int p5 = 109;
    public final static int dlt = 110;
    public final static int p22_5 = 111;
    public final static int p3 = 105;

    static {
        bjdc_manner_to_name.put("1", "单关");
        bjdc_manner_to_name.put("2", "2串1");
        bjdc_manner_to_name.put("3", "2串3");
        bjdc_manner_to_name.put("4", "3串1");
        bjdc_manner_to_name.put("5", "3串4");
        bjdc_manner_to_name.put("6", "3串7");
        bjdc_manner_to_name.put("7", "4串1");
        bjdc_manner_to_name.put("8", "4串5");
        bjdc_manner_to_name.put("9", "4串11");
        bjdc_manner_to_name.put("10", "4串15");
        bjdc_manner_to_name.put("11", "5串1");
        bjdc_manner_to_name.put("12", "5串6");
        bjdc_manner_to_name.put("13", "5串16");
        bjdc_manner_to_name.put("14", "5串26");
        bjdc_manner_to_name.put("15", "5串31");
        bjdc_manner_to_name.put("16", "6串1");
        bjdc_manner_to_name.put("17", "6串7");
        bjdc_manner_to_name.put("18", "6串22");
        bjdc_manner_to_name.put("19", "6串42");
        bjdc_manner_to_name.put("20", "6串57");
        bjdc_manner_to_name.put("21", "6串63");
        bjdc_manner_to_name.put("22", "7串1");
        bjdc_manner_to_name.put("23", "8串1");
        bjdc_manner_to_name.put("24", "9串1");
        bjdc_manner_to_name.put("25", "10串1");
        bjdc_manner_to_name.put("26", "11串1");
        bjdc_manner_to_name.put("27", "12串1");
        bjdc_manner_to_name.put("28", "13串1");
        bjdc_manner_to_name.put("29", "14串1");
        bjdc_manner_to_name.put("30", "15串1");
    }
}
