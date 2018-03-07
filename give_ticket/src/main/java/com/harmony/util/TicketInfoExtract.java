package com.harmony.util;

import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.GameContent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 票面信息提取
 * <p>
 * Created by cm on 2017/1/12.
 */
public class TicketInfoExtract {

    public static String format(String msg, ChipinTempDTO ticket) {
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
                result = formatJC(msg, ticket);
                break;
            case GameContent.JCHunhe:
                result = formatJCHunhe(msg, ticket);
                break;
            case GameContent.LCHunhe:
                result = formatLanHunhe(msg, ticket);
                break;
            case GameContent.JCGuanJun:
                result = formatJCGuanJun(msg, ticket);
                break;
            case GameContent.JCGuanYanJun:
                result = formatJCGuanYanJun(msg);
                break;
            case GameContent.sfc:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.sfc9:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.sfc4:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.sfc6:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.p3:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.p5:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.p22_5:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.star7:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.dlt:
                result = formatSFC(msg, ticket);
                break;
            case GameContent.p11_5:
            case GameContent.h11_5:
            case GameContent.wl11_5:
            case GameContent.yunlan11_5:
                result = formatSFC(msg, ticket);
                break;
        }
        return result;
    }

    /**
     * 格式化胜负彩
     *
     * @param msg
     * @param ticket
     * @return
     */
    public static String formatSFC(String msg, ChipinTempDTO ticket) {
        String result = "";
        return result;
    }

    /**
     * 格式化胜负彩
     *
     * @param msg
     * @param ticket
     * @return
     */
    public static String formatSFC9(String msg, ChipinTempDTO ticket) {
        StringBuffer result = new StringBuffer();
        if (ticket.getManner().equals("1")) {
            String line1 = msg.substring(msg.indexOf("①") + 1, msg.substring(msg.indexOf("①")).indexOf("..PrintData"));
            String line2 = msg.substring(msg.indexOf("②") + 1, msg.substring(msg.indexOf("②")).indexOf("..PrintData"));
            String line3 = msg.substring(msg.indexOf("③") + 1, msg.substring(msg.indexOf("③")).indexOf("..PrintData"));
            String line4 = msg.substring(msg.indexOf("④") + 1, msg.substring(msg.indexOf("④")).indexOf("..PrintData"));
            String line5 = msg.substring(msg.indexOf("⑤") + 1, msg.substring(msg.indexOf("⑤")).indexOf("..PrintData"));

            line1.replaceAll("\\s+", "/");
            line2.replaceAll("\\s+", "/");
            line3.replaceAll("\\s+", "/");
            line4.replaceAll("\\s+", "/");
            line5.replaceAll("\\s+", "/");

            String reg = "^\\d*$";
            if (line1.matches(reg)) {
                result.append(line1).append(",");
            }

            if (line2.matches(reg)) {
                result.append(line2).append(",");
            }

            if (line3.matches(reg)) {
                result.append(line3).append(",");
            }

            if (line4.matches(reg)) {
                result.append(line4).append(",");
            }

            if (line5.matches(reg)) {
                result.append(line5).append(",");
            }
        }

        return result.toString().substring(0, result.toString().length() - 1);
    }


    /**
     * 格式化竞彩
     *
     * @param msg
     * @param ticket
     * @return
     */
    public static String formatJC(String msg, ChipinTempDTO ticket) {//周三301,1B(150.5):1.700//周三302,1D(150.5):1.140//周三303,0(主-7.50)(150.5):1.700/1(主-7.50)(150.5):1.700
        String frfValue = ""; //浮动让分
        String grfValue = "";//固定让分
        String fysValue = "";//浮点预设
        String gysValue = "";//固定预设

        StringBuilder spBuf = new StringBuilder();
        String voteNumber = ticket.getChipinNums();

        String[] voteAry = voteNumber.split("//");
        for (int i = 0; i < voteAry.length; i++) {
            String[] voteMsg = voteAry[i].split(",");
            String changci = voteMsg[0];
            String[] voteResult = voteMsg[1].split("/");//周四001,3,0//周四002,3,0//周四003,1,0//周四004,0,0
            spBuf.append(changci).append(",");//-----连接场次

            int begin = 0, end = 0;
            if (i != voteAry.length - 1) {
                begin = msg.indexOf(changci); //该场次的开始位置
                end = msg.indexOf(voteAry[i + 1].split(",")[0]);//下一场的开始位置
            } else {
                begin = msg.indexOf(changci);
                end = msg.length() - 1;
            }

            //内容
            String content = msg.substring(begin, end);
            for (int j = 0; j < voteResult.length; j++) {//取结果内容
                int rfrIndex = content.indexOf("浮动奖让分:");
                int gIndex = content.indexOf("固定奖让分:");
                int fysIndex = content.indexOf("浮动奖预设总分:");
                int gysIndex = content.indexOf("固定奖预设总分:");

                if (-1 != rfrIndex) {//浮动奖让分:与固定奖让分:之间
                    frfValue = content.substring(rfrIndex + 6, gIndex);
                }

                if (-1 != gIndex) {//固定奖让分:与客队之间
                    int guestWeiZhi = content.indexOf("客队");
                    grfValue = content.substring(gIndex + 6, guestWeiZhi);
                }

                if (-1 != fysIndex) {
                    fysValue = content.substring(fysIndex + 8, gysIndex);
                }

                if (-1 != gysIndex) {
                    int guestIndex = content.indexOf("客队");
                    gysValue = content.substring(gysIndex + 8, guestIndex);
                }
                spBuf.append(voteResult[j]);//连接投注号码

                //如果是让分胜负和大小分连接让分胜负和预设分
                if (frfValue.trim().length() > 0) {
                    spBuf.append("*").append("(").append(frfValue.replaceAll("[\\s]", "")).append(")").append("*");
                }

                if (grfValue.trim().length() > 0) {
                    spBuf.append("(").append(grfValue.replaceAll("[\\s]", "")).append(")");
                }

                if (fysValue.trim().length() > 0) {
                    spBuf.append("*").append("(").append(fysValue.replaceAll("[\\s]", "")).append(")").append("*");
                }

                if (gysValue.trim().length() > 0) {
                    spBuf.append("(" + gysValue.replaceAll("[\\s]", "") + ")");
                }

                //如果有赔率连接赔率
                String toNumber = toVoteResult(Integer.parseInt(ticket.getGameCode() + ""), voteResult[j]);
                int selectIndex = content.indexOf(toNumber + "@");

                if (-1 != selectIndex) {
                    //取@元之间的内容
                    String bodyMessage = content.substring(selectIndex + toNumber.length());
                    int yuanIndex = bodyMessage.indexOf("元");
                    String spValue = bodyMessage.substring(0, yuanIndex).replaceAll("@", "");
                    spBuf.append(":").append(spValue);
                }

                if (j != voteResult.length - 1) {
                    spBuf.append("/");
                }
            }

            if (i != voteAry.length - 1) {
                spBuf.append("//");
            }
        }

        return spBuf.toString();
    }


    /**
     * 格式化竞彩混合
     *
     * @param msg
     * @param ticket
     * @return
     */
    public static String formatJCHunhe(String msg, ChipinTempDTO ticket) {
        String frfValue = ""; //浮动让分
        String grfValue = "";//固定让分
        String fysValue = "";//浮点预设
        String gysValue = "";//固定预设

        StringBuffer spBuf = new StringBuffer();
        String voteNumber = ticket.getChipinNums();

        String[] voteAry = voteNumber.split("//");
        for (int i = 0; i < voteAry.length; i++) {
            String[] voteMsg = voteAry[i].split(",");
            String changci = voteMsg[0];
            String[] voteResult = voteMsg[1].split("/");//周四001,3,0//周四002,3,0//周四003,1,0//周四004,0,0
            spBuf.append(changci).append(",");//-----连接场次

            int begin = 0, end = 0;
            if (i != voteAry.length - 1) {
                begin = msg.indexOf(changci); //该场次的开始位置
                end = msg.indexOf(voteAry[i + 1].split(",")[0]);//下一场的开始位置
            } else {
                begin = msg.indexOf(changci);
                end = msg.length() - 1;
            }

            //内容
            String content = msg.substring(begin, end);
            for (int j = 0; j < voteResult.length; j++) {//取结果内容
                int rfrIndex = content.indexOf("浮动奖让分:");
                int gIndex = content.indexOf("固定奖让分:");
                int fysIndex = content.indexOf("浮动奖预设总分:");
                int gysIndex = content.indexOf("固定奖预设总分:");

                if (-1 != rfrIndex) {//浮动奖让分:与固定奖让分:之间
                    frfValue = content.substring(rfrIndex + 6, gIndex);
                }

                if (-1 != gIndex) {//固定奖让分:与客队之间
                    int guestWeiZhi = content.indexOf("客队");
                    grfValue = content.substring(gIndex + 6, guestWeiZhi);
                }

                if (-1 != fysIndex) {
                    fysValue = content.substring(fysIndex + 8, gysIndex);
                }

                if (-1 != gysIndex) {
                    int guestIndex = content.indexOf("客队");
                    gysValue = content.substring(gysIndex + 8, guestIndex);
                }
                spBuf.append(voteResult[j]);//连接投注号码

                //如果是让分胜负和大小分连接让分胜负和预设分
                if (frfValue.trim().length() > 0) {
                    spBuf.append("*").append("(" + frfValue.replaceAll("[\\s]", "") + ")").append("*");
                }

                if (grfValue.trim().length() > 0) {
                    spBuf.append("(" + grfValue.replaceAll("[\\s]", "") + ")");
                }

                if (fysValue.trim().length() > 0) {
                    spBuf.append("*").append("(" + fysValue.replaceAll("[\\s]", "") + ")").append("*");
                }

                if (gysValue.trim().length() > 0) {
                    spBuf.append("(" + gysValue.replaceAll("[\\s]", "") + ")");
                }

                //如果有赔率连接赔率
                String toNumber = toVoteResult(Integer.parseInt(ticket.getGameCode() + ""), voteResult[j]);
                int selectIndex = content.indexOf(toNumber + "@");

                if (-1 != selectIndex) {
                    //取@元之间的内容
                    String bodyMessage = content.substring(selectIndex + toNumber.length());
                    int yuanIndex = bodyMessage.indexOf("元");
                    String spValue = bodyMessage.substring(0, yuanIndex).replaceAll("@", "");
                    spBuf.append(":").append(spValue);
                }

                if (j != voteResult.length - 1) {
                    spBuf.append("/");
                }
            }

            if (i != voteAry.length - 1) {
                spBuf.append("//");
            }
        }

        return spBuf.toString();
    }


    /**
     * 格式化竞彩混合篮球
     *
     * @param msg
     * @param ticket
     * @return
     */
    public static String formatLanHunhe(String msg, ChipinTempDTO ticket) {
        StringBuffer spBuf = new StringBuffer();
        String voteNumber = ticket.getChipinNums();

        String[] voteAry = voteNumber.split("//");
        for (int i = 0; i < voteAry.length; i++) {
            String[] voteMsg = voteAry[i].split(",");
            String changci = voteMsg[0];
            String[] voteResult = voteMsg[1].split("/");//周四001,3,0//周四002,3,0//周四003,1,0//周四004,0,0
            spBuf.append(changci).append(",");//-----连接场次

            int begin = 0, end = 0;
            if (i != voteAry.length - 1) {
                begin = msg.indexOf(changci); //该场次的开始位置
                end = msg.indexOf(voteAry[i + 1].split(",")[0]);//下一场的开始位置
            } else {
                begin = msg.indexOf(changci);
                end = msg.length() - 1;
            }

            //内容
            String content = msg.substring(begin, end);
            for (int j = 0; j < voteResult.length; j++) {//取结果内容

                String grfValue = "";//固定让分
                String gysValue = "";//固定预设

                //如果是让分胜负和大小分连接让分胜负和预设分
                if (voteResult[j].equals("1B") || voteResult[j].equals("2B")) {
                    int gysIndex = content.indexOf("固定奖预设总分:");
                    if (-1 != gysIndex) {
                        int guestIndex = content.indexOf("客队");
                        gysValue = content.substring(gysIndex + 8, guestIndex);
                    }
                }

                if (voteResult[j].equals("1D") || voteResult[j].equals("0D")) {
                    int gIndex = content.indexOf("固定奖让分:");
                    if (-1 != gIndex) {//固定奖让分:与客队之间
                        int guestWeiZhi = content.indexOf("客队");
                        grfValue = content.substring(gIndex + 6, guestWeiZhi);
                    } else {
                        voteResult[j] = voteResult[j].replace("D", "");
                    }
                }
                spBuf.append(voteResult[j]);//连接投注号码
                if (grfValue.trim().length() > 0) {
                    spBuf.append("(" + grfValue.replaceAll("[\\s]", "").replaceAll("主", "") + ")");
                }

                if (gysValue.trim().length() > 0) {
                    spBuf.append("(" + gysValue.replaceAll("[\\s]", "") + ")");
                }

                //如果有赔率连接赔率
                String toNumber = toVoteResult(Integer.parseInt(ticket.getGameCode() + ""), voteResult[j]);
                int selectIndex = content.indexOf(toNumber + "@");

                if (-1 != selectIndex) {
                    //取@元之间的内容
                    String bodyMessage = content.substring(selectIndex + toNumber.length());
                    int yuanIndex = bodyMessage.indexOf("元");
                    String spValue = bodyMessage.substring(0, yuanIndex).replaceAll("@", "");
                    spBuf.append(":").append(spValue);
                }

                if (j != voteResult.length - 1) {
                    spBuf.append("/");
                }
            }

            if (i != voteAry.length - 1) {
                spBuf.append("//");
            }
        }

        return spBuf.toString();
    }


    /**
     * 格式化竞彩冠军
     *
     * @param msg
     * @param ticket
     * @return
     */
    public static String formatJCGuanJun(String msg, ChipinTempDTO ticket) {//周三301,1B(150.5):1.700//周三302,1D(150.5):1.140//周三303,0(主-7.50)(150.5):1.700/1(主-7.50)(150.5):1.700

        StringBuffer spBuf = new StringBuffer();

        String reg = "([0-9]{2})\\s*\\S*[\u4e00-\u9fa5][\\S*\\s*]*@(\\S*)元";


        Pattern p3 = Pattern.compile(reg);
        Matcher m3 = p3.matcher(msg);


        if (m3.find()) {
            spBuf.append(m3.group(1)).append(",").append(formatConvert(m3.group(0)));
        }

        //__logger.info("spBuf.toString()=" + spBuf.toString());

        return spBuf.toString();
    }


    /**
     * 格式化竞彩冠亚军
     *
     * @param msg
     * @return
     */
    public static String formatJCGuanYanJun(String msg) {//周三301,1B(150.5):1.700//周三302,1D(150.5):1.140//周三303,0(主-7.50)(150.5):1.700/1(主-7.50)(150.5):1.700

        StringBuffer spBuf = new StringBuffer();

        String reg = "([0-9]{2})\\s*\\S*[\u4e00-\u9fa5][\\S*\\s*]*@(\\S*)元";

        Pattern p3 = Pattern.compile(reg);
        Matcher m3 = p3.matcher(msg);


        if (m3.find()) {
            spBuf.append(m3.group(1)).append(",").append(formatConvert(m3.group(0)));
        }

        return spBuf.toString();
    }


    private static String formatConvert(String msg) {
        //__logger.info("msg=" + msg);
        //msg = "(01 巴西)@2.650元+(02 德国)@4.500元+(03 阿根廷)@4.000元";
        StringBuffer resultBuf = new StringBuffer();
        //String reg = "([0-9]{2})\\s*@([0-9]*\\.[0-9]{3})元";

        String reg = "([0-9]{2})\\s*\\S*@(\\S*)元";
        Pattern p1 = Pattern.compile(reg);
        Matcher m1 = p1.matcher(msg);
        while (m1.find()) {
            String content = m1.group();
            String reg2 = "([0-9]{2})";
            String reg3 = "@(\\S*)元";

            Pattern p2 = Pattern.compile(reg2);
            Matcher m2 = p2.matcher(m1.group());

            Pattern p3 = Pattern.compile(reg3);
            Matcher m3 = p3.matcher(m1.group());
            if (m2.find() && m3.find()) {
                resultBuf.append(m2.group()).append(":").append(m3.group().replaceAll("@", "").replaceAll("元", "")).append("/");
            }
        }

        return resultBuf.toString().substring(0, resultBuf.toString().length() - 1);

    }


    /**
     * 转化投注结果
     *
     * @param gameCode
     * @param voteNumber
     * @return
     */
    public static String toVoteResult(int gameCode, String voteNumber) {
        StringBuilder result = new StringBuilder();

        switch (gameCode) {

            case GameContent.JCspf:

                return voteNumber.replaceAll("3", "胜").replaceAll("1", "平").replaceAll("0", "负");

            case GameContent.JCRFSPF:
                return voteNumber.replaceAll("3", "胜").replaceAll("1", "平").replaceAll("0", "负");

            case GameContent.JCbf:

                if (voteNumber.equals("4:4")) {
                    return voteNumber.replaceAll("4:4", "(平其它)");
                } else if (voteNumber.equals("3:4")) {
                    return voteNumber.replaceAll("3:4", "(负其它)");
                } else if (voteNumber.equals("4:3")) {
                    return voteNumber.replaceAll("4:3", "(胜其它)");
                } else {
                    return "(" + voteNumber + ")";
                }

            case GameContent.JCzjq:

                if (Integer.parseInt(voteNumber) < 7) {
                    return "(" + voteNumber + ")";
                } else {
                    return "(" + voteNumber + "+" + ")";
                }

            case GameContent.JCbqc:

                return voteNumber.replaceAll("3", "胜").replaceAll("1", "平").replaceAll("0", "负").replaceAll("_", "");

            case GameContent.JCLanQiuSF:

                return voteNumber.replaceAll("1", "胜").replaceAll("0", "负");

            case GameContent.JCLanQiuRFSF:

                return voteNumber.replaceAll("1", "胜").replaceAll("0", "负");

            case GameContent.JCLanQiuSFC:

                return voteNumber.replaceAll("01", "(客1-5)").replaceAll("02", "(客6-10)")
                        .replaceAll("03", "(客11-15)").replaceAll("04", "(客16-20)")
                        .replaceAll("05", "(客21-25)").replaceAll("06", "(客26+)")
                        .replaceAll("51", "(主1-5)").replaceAll("52", "(主6-10)")
                        .replaceAll("53", "(主11-15)").replaceAll("54", "(主16-20)")
                        .replaceAll("55", "(主21-25)").replaceAll("56", "(主26+)");

            case GameContent.JCLanQiuDX:

                return voteNumber.replaceAll("1", "大").replaceAll("2", "小");

            case GameContent.JCHunhe:
                String formatsrc = "";
                if (voteNumber.equals("3")) {
                    formatsrc = "胜";
                } else if (voteNumber.equals("1")) {
                    formatsrc = "平";
                } else if (voteNumber.equals("0")) {
                    formatsrc = "负";
                } else if (voteNumber.equals("3R")) {
                    formatsrc = "胜";
                } else if (voteNumber.equals("1R")) {
                    formatsrc = "平";
                } else if (voteNumber.equals("0R")) {
                    formatsrc = "负";
                } else if (voteNumber.equals("4:4")) {
                    formatsrc = voteNumber.replaceAll("4:4", "(平其它)");
                } else if (voteNumber.equals("3:4")) {
                    formatsrc = voteNumber.replaceAll("3:4", "(负其它)");
                } else if (voteNumber.equals("4:3")) {
                    formatsrc = voteNumber.replaceAll("4:3", "(胜其它)");
                } else if (voteNumber.equals("0B")) {
                    formatsrc = "(0)";
                } else if (voteNumber.equals("1B")) {
                    formatsrc = "(1)";
                } else if (voteNumber.equals("2B")) {
                    formatsrc = "(2)";
                } else if (voteNumber.equals("3B")) {
                    formatsrc = "(3)";
                } else if (voteNumber.equals("4B")) {
                    formatsrc = "(4)";
                } else if (voteNumber.equals("5B")) {
                    formatsrc = "(5)";
                } else if (voteNumber.equals("6B")) {
                    formatsrc = "(6)";
                } else if (voteNumber.equals("7B")) {
                    formatsrc = "(7+)";
                } else if (voteNumber.equals("3_3")) {
                    formatsrc = "胜胜";
                } else if (voteNumber.equals("3_1")) {
                    formatsrc = "胜平";
                } else if (voteNumber.equals("3_0")) {
                    formatsrc = "胜负";
                } else if (voteNumber.equals("1_3")) {
                    formatsrc = "平胜";
                } else if (voteNumber.equals("1_1")) {
                    formatsrc = "平平";
                } else if (voteNumber.equals("1_0")) {
                    formatsrc = "平负";
                } else if (voteNumber.equals("0_3")) {
                    formatsrc = "负胜";
                } else if (voteNumber.equals("0_1")) {
                    formatsrc = "负平";
                } else if (voteNumber.equals("0_0")) {
                    formatsrc = "负负";
                } else {
                    formatsrc = "(" + voteNumber + ")";
                }
                return formatsrc;

            case GameContent.LCHunhe:

                String formatlansrc = "";
                if (voteNumber.equals("1")) {
                    formatlansrc = "胜";
                } else if (voteNumber.equals("0")) {
                    formatlansrc = "负";
                } else if (voteNumber.equals("1D")) {
                    formatlansrc = "胜";
                } else if (voteNumber.equals("0D")) {
                    formatlansrc = "负";
                } else if (voteNumber.equals("01")) {
                    formatlansrc = "(客1-5)";
                } else if (voteNumber.equals("02")) {
                    formatlansrc = "(客6-10)";
                } else if (voteNumber.equals("03")) {
                    formatlansrc = "(客11-15)";
                } else if (voteNumber.equals("04")) {
                    formatlansrc = "(客16-20)";
                } else if (voteNumber.equals("05")) {
                    formatlansrc = "(客21-25)";
                } else if (voteNumber.equals("06")) {
                    formatlansrc = "(客26+)";
                } else if (voteNumber.equals("51")) {
                    formatlansrc = "(主1-5)";
                } else if (voteNumber.equals("52")) {
                    formatlansrc = "(主6-10)";
                } else if (voteNumber.equals("53")) {
                    formatlansrc = "(主11-15)";
                } else if (voteNumber.equals("54")) {
                    formatlansrc = "(主16-20)";
                } else if (voteNumber.equals("55")) {
                    formatlansrc = "(主21-25)";
                } else if (voteNumber.equals("56")) {
                    formatlansrc = "(主26+)";
                } else if (voteNumber.equals("1B")) {
                    formatlansrc = "大";
                } else if (voteNumber.equals("2B")) {
                    formatlansrc = "小";
                }
                return formatlansrc;

        }

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(formatConvert("(01 巴西)@2.650元+(02 德国)@4.500元+(03 阿根廷)@4.000元"));
//		StringBuffer stringBuffer = new StringBuffer();
//    	  try {
//    		    InputStream inputSteam = new FileInputStream("/Users/huxiaohua/Documents/upload/bifen");
//    		        byte[] buffer = new byte[10240];
// 				int len = 0;
// 				while ((len = inputSteam.read(buffer)) !=  -1) {
// 				}
//
// 				Charset charset = Charset.forName("GBK");
// 	        	CharBuffer charBuffer = charset.decode(ByteBuffer.wrap(buffer));
// 	        	stringBuffer.append(charBuffer);
//
//
//         	 String msg = stringBuffer.toString().replaceAll("\r", "").replaceAll("\n", "");
//
//         Ticket ticket = new Ticket();
//		 ticket.setTicketId("1111");
//  		 ticket.setGameCode("509");
//  		 ticket.setPrintServiceDTO(null);
//  		 ticket.setBets(String.valueOf(4));
//  		 ticket.setAwardBets(0);
//  		 ticket.setManner("3");
//  		 ticket.setGid("");
//  		 ticket.setIssue("");
//  		 ticket.setMultiple(2);
//  		 ticket.setRecord(1);
//  		 ticket.setTestStatus(false);
//  		 ticket.setSourceVoteNumber("周二004,0:4//周二005,1R//周二010,1");
//
//
//    		 System.out.println(TicketInfoExtract.format(msg, ticket));
//
//          } catch (FileNotFoundException e) {
//              e.printStackTrace();
//          } catch (IOException e) {
//              e.printStackTrace();
//          }

    }
}
