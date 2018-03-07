package com.harmony.util;

import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.GameContent;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cm on 2017/1/12.
 */
public class GetTime {

    private final static Logger __logger = Logger.getLogger(GetTime.class);

    public static String proccess(ChipinTempDTO ticket, String msg) throws Exception {

        try {
            String result = "";

            switch (Integer.parseInt(ticket.getGameCode() + "")) {
                case GameContent.JCspf:
                case GameContent.JCbf:
                case GameContent.JCzjq:
                case GameContent.JCbqc:
                case GameContent.JCLanQiuSF:
                case GameContent.JCLanQiuRFSF:
                case GameContent.JCLanQiuSFC:
                case GameContent.JCLanQiuDX:
                case GameContent.JCRFSPF:
                case GameContent.JCGuanJun:
                case GameContent.JCGuanYanJun:
                    result = proccessJc(msg);
                    break;
                case GameContent.JCHunhe:
                    result = proccessJc(msg);
                    break;
                case GameContent.LCHunhe:
                    result = proccessJc(msg);
                    break;
                case GameContent.sfc:
                    result = proccessLaZucai(msg);
                    break;
                case GameContent.sfc9:
                    result = proccessLaZucai(msg);
                    break;
                case GameContent.sfc4:
                    result = proccessLaZucai(msg);
                    break;
                case GameContent.sfc6:
                    result = proccessLaZucai(msg);
                    break;
                case GameContent.p3:
                    result = proccessLaZucai(msg);
                    break;
                case GameContent.p5:
                    result = proccessLaZucai(msg);
                    break;
                case GameContent.p22_5:
                    result = proccessLaZucai(msg);
                    break;
                case GameContent.star7:
                    result = proccessLaZucai(msg);
                    break;
                case GameContent.dlt:
                    result = proccessDaLeTou(msg);
                    break;
                case GameContent.p11_5:
                    result = proccess11x5(msg);
                    break;
                case GameContent.h11_5:
                    result = proccess11x5(msg);
                    break;
                case GameContent.wl11_5:
                    result = proccess11x5(msg);
                    break;
                case GameContent.yunlan11_5:
                    result = proccess11x5(msg);
                    break;
                case GameContent.qh11_5:
                    result = proccess11x5(msg);
                    break;
                case GameContent.Bjdc_Bf:
                    result = proccessBeiDan(msg);
                    break;
                case GameContent.Bjdc_Bqcspf:
                    result = proccessBeiDan(msg);
                    break;
                case GameContent.Bjdc_Rqspf:
                    result = proccessBeiDan(msg);
                    break;
                case GameContent.Bjdc_Sxpds:
                    result = proccessBeiDan(msg);
                    break;
                case GameContent.Bjdc_Zjqs:
                    result = proccessBeiDan(msg);
                    break;
            }

            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 2013-05-26 18:20:13
     *
     * @param msg
     * @return
     */
    public static String proccessJc(String msg) throws Exception {
        try {
            //__logger.info("msg=" + msg);
            Pattern p1 = Pattern.compile("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}.[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}");
            Matcher m1 = p1.matcher(msg);
            m1.find();
            return (toStr(m1.group()));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 2013/05/26 18:20:13
     *
     * @param msg
     * @return
     */
    public static String proccess11x5(String msg) {
        Pattern p1 = Pattern.compile("[0-9]{4}\\/[0-9]{2}\\/[0-9]{2}\\s*[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}");
        Matcher m1 = p1.matcher(msg);
        if (m1.find()) {
            return (toStr(m1.group()));
        } else {
            Pattern p2 = Pattern.compile("[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}\\s*[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}"); //15/03/24 11:42:40
            Matcher m2 = p2.matcher(msg);
            m2.find();
            return (toStr("20" + m2.group()));
        }

    }

    /**
     * 2013/05/26 18:20:13
     *
     * @param msg
     * @return
     */
    public static String proccessLaZucai(String msg) {
        //__logger.info("msg=" + msg);
        Pattern p1 = Pattern.compile("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}\\s*[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}"); //2013/05/26 18:20:13
        Matcher m1 = p1.matcher(msg);
        if (m1.find()) {
            return (toStr(m1.group()));
        } else {
//            Pattern p2 = Pattern.compile("[0-9]{2}\\-[0-9]{2}\\-[0-9]{2}\\s*[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}"); //15/03/24 11:42:40
//            Matcher m2 = p2.matcher(msg);
//            m2.find();
//            return (toStr("20" + m2.group()));
            Pattern p2 = Pattern.compile("[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}\\s*[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}"); //15/03/24 11:42:40
            Matcher m2 = p2.matcher(msg);
            m2.find();
            return (toStr("20" + m2.group()));
        }
    }

    /**
     * 2013/05/26 18:20:13
     *
     * @param msg
     * @return
     */
    public static String proccessDaLeTou(String msg) {
        //__logger.info("msg=" + msg);
        Pattern p1 = Pattern.compile("[0-9]{4}\\/[0-9]{2}\\/[0-9]{2}\\s*[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}"); //2013/05/26 18:20:13
        Matcher m1 = p1.matcher(msg);
        if (m1.find()) {
            return (toStr(m1.group()));
        } else {
            Pattern p2 = Pattern.compile("[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}\\s*[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}"); //15/03/24 11:42:40
            Matcher m2 = p2.matcher(msg);
            m2.find();
            return (toStr("20" + m2.group()));
        }
    }

    /**
     * 2013-05-26 18:20:13
     *
     * @param msg
     * @return
     */
    public static String proccessBeiDan(String msg) {
        Pattern p1 = Pattern.compile("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}.[0-9]{2}\\:[0-9]{2}\\:[0-9]{2}");
        Matcher m1 = p1.matcher(msg);
        m1.find();
        return (toStr(m1.group()));
    }

    public static String toStr(String msg) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(msg);
        //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String time = "aaaaaaaaaaa 2013/06/02  20:47:10 aaaaaaaaaaaabbbbbbbbbbbbbbbbbbbb";
        System.out.println(toStr(proccessLaZucai(time)));
    }

}
