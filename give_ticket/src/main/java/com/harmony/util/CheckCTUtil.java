package com.harmony.util;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by CM on 2017/4/25.
 */
public class CheckCTUtil {

    protected final static Logger __logger = Logger.getLogger(CheckCTUtil.class);

    /**
     * 比较两个号码是否相等
     *
     * @param lotName
     * @param manner
     * @param voteNumber1
     * @param voteNumber2
     * @return
     */
    public static boolean execute(int lotName, String manner, String voteNumber1, String voteNumber2) {
        boolean result = false;

        switch (lotName) {
            case 105://排列3
                if (Integer.parseInt(manner) == 11) {//单式直选
                    result = checkSFCDS(voteNumber1, voteNumber2);
                } else if (Integer.parseInt(manner) == 13 || Integer.parseInt(manner) == 16) {//单式组选3,单式组选6
                    result = checkP3ZuxianDs(voteNumber1, voteNumber2);
                } else if (Integer.parseInt(manner) == 21) {//复式直选
                    result = checkP5Fs(voteNumber1, voteNumber2);
                } else if (Integer.parseInt(manner) == 23 || Integer.parseInt(manner) == 26 || Integer.parseInt(manner) == 31 || Integer.parseInt(manner) == 32) {//复式组选3,复试组选6,和值组选,和值组选
                    result = checkP3z3z6Fs(voteNumber1, voteNumber2);
                } else if (Integer.parseInt(manner) == 51 || Integer.parseInt(manner) == 52 || Integer.parseInt(manner) == 53) {
                    result = checkP3Dantou(voteNumber1, voteNumber2);
                } else if (Integer.parseInt(manner) == 42) { //直选组合复式
                    result = checkP3z3z6Fs(voteNumber1, voteNumber2);
                }
                break;
            case 110://大乐透
                if (Integer.parseInt(manner) == 1 || Integer.parseInt(manner) == 5 || Integer.parseInt(manner) == 8) {
                    result = checkDltDS(voteNumber1, voteNumber2);
                } else if (Integer.parseInt(manner) == 2 || Integer.parseInt(manner) == 6) {
                    result = checkDltFS(voteNumber1, voteNumber2);
                } else if (Integer.parseInt(manner) == 3 || Integer.parseInt(manner) == 7) {
                    result = checkDltDT(voteNumber1, voteNumber2);
                }
                break;
            case 108://七星彩  同类型按升序排
            case 109://排列5
                if (Integer.parseInt(manner) == 1) {
                    result = checkSFCDS(voteNumber1, voteNumber2);
                } else {
                    result = checkP5Fs(voteNumber1, voteNumber2);
                }
                break;
            case 102://胜负彩  同类型按降序排
            case 103://任选九
            case 107: //六场半
                if (Integer.parseInt(manner) == 1) {
                    result = checkSFCDS(voteNumber1, voteNumber2);
                } else {
                    result = checkSFCFS(voteNumber1, voteNumber2);
                }
                break;
            case 106://4场半
                if (Integer.parseInt(manner) == 1) {
                    result = checkSFCDS(voteNumber1, voteNumber2);
                } else {
                    result = checkSFC4FS(voteNumber1, voteNumber2);
                }
                break;
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 406:
            case 407:
            case 408:
            case 409:
                result = checkP115(manner, voteNumber1, voteNumber2);
                break;
            case 700:
                result = true;
                break;
        }

        return result;
    }

    /**
     * 排列3组选单式
     *
     * @param voteNumber1
     * @param voteNumber2
     * @return
     */
    public static boolean checkP3ZuxianDs(String voteNumber1, String voteNumber2) {
        HashSet set = build(voteNumber2);

        String[] lines = voteNumber1.split(",");
        String[] lines1 = voteNumber2.split(",");
        if (lines.length == lines1.length) {
            for (int i = 0; i < lines.length; i++) {
                String haoMa = sortAsc(lines[i]);
                if (!set.contains(haoMa)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;

    }

    /**
     * 排列3 组选复式
     *
     * @param voteNumber1
     * @param voteNumber2
     * @return
     */
    public static boolean checkP3z3z6Fs(String voteNumber1, String voteNumber2) {
        String msg = sortAsc(voteNumber1);
        if (!msg.equals(voteNumber2)) {
            return false;
        }

        return true;
    }

    /**
     * 排列3胆拖
     *
     * @param voteNumber1
     * @param voteNumber2
     * @return
     */
    public static boolean checkP3Dantou(String voteNumber1, String voteNumber2) {
        String[] msgs = voteNumber1.split("//");
        String hao = sortAsc(msgs[0]) + "//" + sortAsc(msgs[1]);
        if (!hao.equals(voteNumber2)) {
            return false;
        }

        return true;
    }


    /**
     * 排列3直选组合复式
     *
     * @param voteNumber1
     * @param voteNumber2
     * @return
     */
    public static boolean checkP3zhiXuanZuHeFs(String voteNumber1, String voteNumber2) {
        String[] msgs = voteNumber1.split("//");
        String hao = sortAsc(msgs[0]) + "//" + sortAsc(msgs[1]);
        if (!hao.equals(voteNumber2)) {
            return false;
        }

        return true;
    }

    /**
     * 检验胜负彩单式
     */
    public static boolean checkSFCDS(String voteNumber1, String voteNumber2) {
        HashSet set = build(voteNumber2);

        String[] lines = voteNumber1.split(",");
        String[] lines1 = voteNumber2.split(",");
        if (lines.length == lines1.length) {
            for (int i = 0; i < lines.length; i++) {
                if (!set.contains(lines[i])) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * 检验胜负彩复式
     *
     * @param voteNumber1
     * @param voteNumber2
     * @return
     */
    public static boolean checkSFCFS(String voteNumber1, String voteNumber2) {

        StringBuffer buf = new StringBuffer();
        String[] msgs = voteNumber1.split("//");
        for (int i = 0; i < msgs.length; i++) {
            String temp = sortDesc(msgs[i]);
            if (i != msgs.length - 1) {
                buf.append(temp).append("//");
            } else {
                buf.append(temp);
            }
        }

        __logger.info("buf=" + buf.toString() + " voteNumber2=" + voteNumber2);
        if (!buf.toString().equals(voteNumber2)) {
            return false;
        }

        return true;
    }


    /**
     * 检验4场进球复式
     *
     * @param voteNumber1
     * @param voteNumber2
     * @return
     */
    public static boolean checkSFC4FS(String voteNumber1, String voteNumber2) {

        StringBuffer buf = new StringBuffer();
        String[] msgs = voteNumber1.split("//");
        for (int i = 0; i < msgs.length; i++) {
            String temp = sortAsc(msgs[i]);
            if (i != msgs.length - 1) {
                buf.append(temp).append("//");
            } else {
                buf.append(temp);
            }
        }

        __logger.info("buf=" + buf.toString() + " voteNumber2=" + voteNumber2);
        if (!buf.toString().equals(voteNumber2)) {
            return false;
        }

        return true;
    }


    public static boolean checkP5Fs(String voteNumber1, String voteNumber2) {
        StringBuffer buf = new StringBuffer();
        String[] msgs = voteNumber1.split("//");
        for (int i = 0; i < msgs.length; i++) {
            String temp = sortAsc(msgs[i]);
            if (i != msgs.length - 1) {
                buf.append(temp).append("//");
            } else {
                buf.append(temp);
            }
        }

        if (!buf.toString().equals(voteNumber2)) {
            return false;
        }

        return true;
    }


    public static boolean checkP115(String manner, String voteNumber1, String voteNumber2) {
        //任选
        if (Integer.parseInt(manner) == 1 || Integer.parseInt(manner) == 2 || Integer.parseInt(manner) == 3 || Integer.parseInt(manner) == 4 ||
                Integer.parseInt(manner) == 5 || Integer.parseInt(manner) == 6 || Integer.parseInt(manner) == 7 || Integer.parseInt(manner) == 8) {//任选
            StringBuffer buf = new StringBuffer();
            if (-1 != voteNumber1.indexOf("//")) { //复式
                buf.append(sortAsc115RenXuanFs(voteNumber1));
                if (!buf.toString().equals(voteNumber2)) {
                    return false;
                } else {
                    return true;
                }
            } else if (-1 != voteNumber1.indexOf("$")) { //胆拖
                return checkp115RenXuanDt(voteNumber1, voteNumber2);
            } else {
                if (-1 != voteNumber1.indexOf(",")) { //单式多注
                    return checkp115RenXuanDS(voteNumber1, voteNumber2);
                } else { //单式单注
                    return checkp115RenXuanDS(voteNumber1, voteNumber2);
                }
            }
        } else if (Integer.parseInt(manner) == 9 || Integer.parseInt(manner) == 13) {//前2直选
            if (-1 != voteNumber1.indexOf("//")) {//复式
                return check115qian2Fs(voteNumber1, voteNumber2);
            } else {//单式，无胆拖
                return checkp115qian2DS(voteNumber1, voteNumber2);
            }
        } else if (Integer.parseInt(manner) == 10) {//前2组选
            if (-1 != voteNumber1.indexOf("//")) {//复式
                return check115qian2Zufs(voteNumber1, voteNumber2);
            } else if (-1 != voteNumber1.indexOf("$")) {
                return checkp115RenXuanDt(voteNumber1, voteNumber2);
            } else {//单式
                return checkp115RenXuanDS(voteNumber1, voteNumber2);
            }
        } else if (Integer.parseInt(manner) == 11 || Integer.parseInt(manner) == 14) {
            if (-1 != voteNumber1.indexOf("//")) {//复式
                return check115qian2Fs(voteNumber1, voteNumber2);
            } else {//单式，无胆拖
                return checkp115qian2DS(voteNumber1, voteNumber2);
            }
        } else if (Integer.parseInt(manner) == 12) {
            if (-1 != voteNumber1.indexOf("//")) {//复式
                return check115qian2Zufs(voteNumber1, voteNumber2);
            } else if (-1 != voteNumber1.indexOf("$")) {
                return checkp115RenXuan3Dt(voteNumber1, voteNumber2);
            } else {//单式
                return checkp115RenXuanDS(voteNumber1, voteNumber2);
            }
        } else if (Integer.parseInt(manner) == 15 || Integer.parseInt(manner) == 16) {
            return checkp115RenXuanDS(voteNumber1, voteNumber2);
        } else {
            return false;
        }
    }


    public static boolean checkP3ZuFs(String voteNumber1, String voteNumber2) {
        StringBuffer buf = new StringBuffer();
        buf.append(sortAsc(voteNumber1));


        if (!buf.toString().equals(voteNumber2)) {
            return false;
        }

        return true;
    }


    /**
     * 检验大乐透单式
     */
    public static boolean checkDltDS(String voteNumber1, String voteNumber2) {
        HashSet set = build(voteNumber2);

        String[] lines = voteNumber1.split(",");
        String[] lines1 = voteNumber2.split(",");
        if (lines.length == lines1.length) {
            for (int i = 0; i < lines.length; i++) {
                String[] line = lines[i].split("//");
                String qianQu = sortAsc(line[0]);
                String houQu = sortAsc(line[1]);
                String haoMa = qianQu + "//" + houQu;
                if (!set.contains(haoMa)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }


    /**
     * 检验大乐透复式
     */
    public static boolean checkDltFS(String voteNumber1, String voteNumber2) {
        StringBuffer buf = new StringBuffer();
        String[] msgs = voteNumber1.split("//");
        for (int i = 0; i < msgs.length; i++) {
            if (i != msgs.length - 1) {
                buf.append(sortAsc(msgs[i])).append("//");
            } else {
                buf.append(sortAsc(msgs[i]));
            }
        }

        if (!buf.toString().equals(voteNumber2)) {
            return false;
        }

        return true;
    }


    /**
     * 01/02/03/04//05/06,01/02
     * 01/02/03/04/05,01//02/03/04/05
     * <p>
     * 检验大乐透双区胆拖 01/02/03/04//05/06, 01//02/02/03/04/05
     */
    public static boolean checkDltDT(String voteNumber1, String voteNumber2) {
        StringBuffer buf = new StringBuffer();
        String[] msgs = voteNumber1.split(",");
        String qiangQu = msgs[0];
        String houQu = msgs[1];

        String[] qiangQuArgs = qiangQu.split("//");
        if (qiangQuArgs.length > 1) {
            String qiangQuDan = sortAsc(qiangQuArgs[0]);
            String qiangQuTou = sortAsc(qiangQuArgs[1]);
            buf.append(qiangQuDan).append("//").append(qiangQuTou).append(",");
        } else {
            String qiangQuTou = sortAsc(qiangQuArgs[0]);
            buf.append(qiangQuTou).append(",");
        }

        String[] houQuArgs = houQu.split("//");
        if (houQuArgs.length > 1) {
            String houQuDan = sortAsc(houQuArgs[0]);
            String houQuTou = sortAsc(houQuArgs[1]);
            buf.append(houQuDan).append("//").append(houQuTou);
        } else {
            String houQuTou = sortAsc(houQuArgs[0]);
            buf.append(houQuTou);
        }

        __logger.info("对比 voteNumber1=" + buf.toString() + " voteNumber2=" + voteNumber2);

        if (!buf.toString().equals(voteNumber2)) {
            return false;
        }

        return true;
    }


    /**
     * 将投注号码存入HashSet
     *
     * @param msg
     * @return
     */
    public static HashSet build(String msg) {
        HashSet set = new HashSet();
        String[] lines = msg.split(",");
        for (int i = 0; i < lines.length; i++) {
            set.add(lines[i]);
        }
        return set;
    }


    /**
     * @param msg
     * @return
     */
    public static String sortAsc(String msg) {
        StringBuffer buf = new StringBuffer();
        List list = new ArrayList();

        String[] msgs = msg.split("/");
        for (int i = 0; i < msgs.length; i++) {
            list.add(msgs[i]);
        }

        Collections.sort(list);

        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                buf.append(list.get(i)).append("/");
            } else {
                buf.append(list.get(i));
            }
        }

        return buf.toString();
    }


    /**
     * @param msg
     * @return
     */
    public static String sortAsc115RenXuanFs(String msg) {
        StringBuffer buf = new StringBuffer();
        List list = new ArrayList();

        String[] msgs = msg.split("//");
        for (int i = 0; i < msgs.length; i++) {
            list.add(msgs[i]);
        }

        Collections.sort(list);

        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                buf.append(list.get(i)).append("//");
            } else {
                buf.append(list.get(i));
            }
        }

        return buf.toString();
    }


    /**
     * 前2直选复式
     *
     * @return
     */
    public static boolean check115qian2Fs(String voteNumber1, String voteNumber2) {
        StringBuffer buf = new StringBuffer();
        String[] msgs = voteNumber1.split("//");
        for (int i = 0; i < msgs.length; i++) {
            String temp = sortAsc(msgs[i]);
            if (i != msgs.length - 1) {
                buf.append(temp).append("//");
            } else {
                buf.append(temp);
            }
        }

        if (!buf.toString().equals(voteNumber2)) {
            return false;
        }

        return true;
    }


    public static boolean check115qian2Zufs(String voteNumber1, String voteNumber2) {
        StringBuffer buf = new StringBuffer();
        buf.append(sortAsc115RenXuanFs(voteNumber1));
        if (!buf.toString().equals(voteNumber2)) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 检验11选5单式
     */
    public static boolean checkp115RenXuanDS(String voteNumber1, String voteNumber2) {
        HashSet set = build(voteNumber2);

        String[] lines = voteNumber1.split(",");
        String[] lines1 = voteNumber2.split(",");
        if (lines.length == lines1.length) {
            for (int i = 0; i < lines.length; i++) {
                String hao = sortAsc(lines[i]);
                System.out.println("_____________hao=" + hao);
                if (!set.contains(hao)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }


    /**
     * 检验11选5单式
     */
    public static boolean checkp115qian2DS(String voteNumber1, String voteNumber2) {
        HashSet set = build(voteNumber2);

        String[] lines = voteNumber1.split(",");
        String[] lines1 = voteNumber2.split(",");
        if (lines.length == lines1.length) {
            for (int i = 0; i < lines.length; i++) {
                String hao = lines[i];
//			System.out.println("_____________hao=" + hao);
                if (!set.contains(hao)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }


    /**
     * 检验11选5任选胆拖
     */
    public static boolean checkp115RenXuanDt(String voteNumber1, String voteNumber2) {
        final String meiYuan = "\\u0024";
        String[] lines = voteNumber1.split(meiYuan);
        String danHao = sortAsc(lines[0]);
        String toHao = sortAsc(lines[1]);
        String hao = danHao + "$" + toHao;
        //System.out.println("11选5任选胆拖 number1=" + hao + " number2=" + voteNumber2);

        if (!voteNumber2.equals(hao)) {
            return false;
        }

        return true;
    }


    /**
     * 检验11选5任选胆拖
     */
    public static boolean checkp115RenXuan3Dt(String voteNumber1, String voteNumber2) {
        final String meiYuan = "\\u0024";
        String[] lines = voteNumber1.split(meiYuan);
        String danHao = sortAsc(lines[0]);
        String toHao = sortAsc(lines[1]);
        String hao = danHao + "$" + toHao;
        //System.out.println("11选5任选胆拖 number1=" + hao + " number2=" + voteNumber2);

        if (!voteNumber2.equals(hao)) {
            return false;
        }

        return true;
    }


    /**
     * @param msg
     * @return
     */
    public static String sortDesc(String msg) {
        StringBuffer buf = new StringBuffer();
        List list = new ArrayList();

        String[] msgs = msg.split("/");
        for (int i = 0; i < msgs.length; i++) {
            list.add(msgs[i]);
        }

        Comparator r = Collections.reverseOrder();
        Collections.sort(list, r);

        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                buf.append(list.get(i)).append("/");
            } else {
                buf.append(list.get(i));
            }
        }

        return buf.toString();
    }


    public static void main(String[] args) {
//		System.out.println(CheckCTUtil.execute(110, "1",
//				"11/13/18/29/35//02/05,03/11/14/26/27//08/12,08/12/15/17/28//10/11",
//				"03/11/14/26/27//08/12,08/12/15/17/28//10/11,11/13/18/29/35//02/05"));//大乐透
        System.out.println(CheckCTUtil.execute(102, "2",
                "0/3//3//3//1//0//3//0//0//3//0//1//0//1//3",
                "3/0//3//3//1//0//3//0//0//3//0//1//0//1//3"));
        //System.out.println(CheckCTUtil.sort("10/02/18/29/35"));
    }
}

