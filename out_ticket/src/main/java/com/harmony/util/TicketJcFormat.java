package com.harmony.util;

import com.harmony.comm.ColorContants;
import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.PrintServiceDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by cm on 2017/1/10.
 */
public class TicketJcFormat {
    private final static Logger __logger = Logger.getLogger(TicketJcFormat.class);

    public static byte[] formatResult(ChipinTempDTO chipinTempDTO, PrintServiceDTO printServiceDTO) {
        String number = jcZqBlendFormat(chipinTempDTO, printServiceDTO.getUserpassword());
        LogUtil.info(TicketCTFormat.class, "获取键盘码", "键盘码为:" + number);
        return KeyUtil.getKeyCodesByKeys(number);
    }


    //拆分数据：5 1 5 1 1 0 0 4 3 F1 1 0 0 8 0 F1 F2 0 2 F5
    public static void main(String[] args) {
        ChipinTempDTO chipinTempDTO = new ChipinTempDTO();
        chipinTempDTO.setChipinNums("周五001,0//周六001,0//周六011,3//周六013,3");
        chipinTempDTO.setMultiple("1");
        chipinTempDTO.setManner("2,0");
        chipinTempDTO.setBets(200);
        chipinTempDTO.setGameCode(501);
        System.out.println(jcZqBlendFormat(chipinTempDTO, "123456"));
    }


    /**
     * @param chipinTempDTO
     * @return
     */
    public static String jcZqBlendFormat(ChipinTempDTO chipinTempDTO, String userPassword) {
        StringBuffer sb = new StringBuffer();
        String number = "";
        String manner = chipinTempDTO.getManner();
        if (chipinTempDTO.getGameCode() >= 501 && chipinTempDTO.getGameCode() <= 515) {
            sb.append(ColorContants.COLOR_KEY_MAP.get("JC"));
            sb.append(ColorContants.JC_KEY_MAP.get(String.valueOf(chipinTempDTO.getGameCode())));
            switch ((int) chipinTempDTO.getGameCode()) {
                case 501:
                    number = jcRfsfcNumber(chipinTempDTO.getChipinNums());
                    break;
                case 502:
                    number = jcBFfcNumber(chipinTempDTO.getChipinNums());
                    break;
                case 503:
                    number = jcZjqNumber(chipinTempDTO.getChipinNums());
                    break;
                case 504:
                    number = jcBqcNumber(chipinTempDTO.getChipinNums());
                    break;
                case 505:
                    number = jcLanQSfNumber(chipinTempDTO.getChipinNums());
                    break;
                case 506:
                    number = jcLanQSfNumber(chipinTempDTO.getChipinNums());
                    break;
                case 507:
                    number = jcLanQSfcNumber(chipinTempDTO.getChipinNums());
                    break;
                case 508:
                    number = jcLanQDxfNumber(chipinTempDTO.getChipinNums());
                    break;
                case 509:
                    number = jcZqHunheNumber(chipinTempDTO.getChipinNums());
                    break;
                case 510:
                    number = jcLanQHunheNumber(chipinTempDTO.getChipinNums());
                    break;
                case 511:
                    number = jcRfsfcNumber(chipinTempDTO.getChipinNums());
                    break;
            }
            sb.append(number);

            if (StringUtils.isNotEmpty(manner)) {
                if (manner.contains(",")) {
                    sb.append("F8 ");
                    String[] manners = manner.split(",");
                    for (int i = 0; i < manners.length - 1; i++) {
                        sb.append(ColorContants.FREE_KEY_MAP.get(manners[i]));
                    }
                    sb.append("F8 ");
                } else {
                    sb.append("F2 ");
                    sb.append(ColorContants.GGFS_MAP.get(manner));
                }
            }
            if (" ".equals(sb.substring(sb.length() - 1, sb.length()))) {
                sb.delete(sb.length() - 1, sb.length());
            }
        }
        return sb.toString();
    }

    /**
     * 竞彩足球胜平负格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcRfsfcNumber(String num) {
        if (num != null) {
            StringBuffer sb = new StringBuffer();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length()))).append(" ");
                    String[] tmps = params[1].split("/");
                    for (int j = 0; j < tmps.length; j++) {
                        sb.append(ColorContants.JC_ZQ_SPF_MAP.get(tmps[j]));
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 竞彩足球比分格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcBFfcNumber(String num) {
        if (num != null) {
            StringBuffer sb = new StringBuffer();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length()))).append(" ");
                    String[] tmps = params[1].split("/");
                    for (int j = 0; j < tmps.length; j++) {
                        sb.append(ColorContants.JC_ZQ_BF_MAP.get(tmps[j]));
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 竞彩足球总进球格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcZjqNumber(String num) {
        if (num != null) {
            StringBuilder sb = new StringBuilder();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length())) + " ");
                    String[] tmps = params[1].split("/");
                    for (int j = 0; j < tmps.length; j++) {
                        sb.append(ColorContants.JC_ZQ_ZJQ_MAP.get(tmps[j]));
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 竞彩足球半全场格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcBqcNumber(String num) {
        if (num != null) {
            StringBuffer sb = new StringBuffer();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length()))).append(" ");
                    String[] tmps = params[1].split("/");
                    for (int j = 0; j < tmps.length; j++) {
                        sb.append(ColorContants.JC_ZQ_BQC_MAP.get(tmps[j]));
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 竞彩篮球胜负格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcLanQSfNumber(String num) {
        if (num != null) {
            StringBuffer sb = new StringBuffer();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length())) + " ");
                    String[] tmps = params[1].split("/");
                    for (int j = 0; j < tmps.length; j++) {
                        sb.append(ColorContants.JC_LQ_SF_MAP.get(tmps[j]));
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 竞彩篮球胜分差格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcLanQSfcNumber(String num) {
        if (num != null) {
            StringBuffer sb = new StringBuffer();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length()))).append(" ");
                    String[] tmps = params[1].split("/");
                    for (int j = 0; j < tmps.length; j++) {
                        sb.append(ColorContants.JC_LQ_SFC_MAP.get(tmps[j]));
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 竞彩篮球大小分格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcLanQDxfNumber(String num) {
        if (num != null) {
            StringBuilder sb = new StringBuilder();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length()))).append(" ");
                    String[] tmps = params[1].split("/");
                    for (int j = 0; j < tmps.length; j++) {
                        sb.append(ColorContants.JC_LQ_DXF_MAP.get(tmps[j]));
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 竞彩足球混合格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcZqHunheNumber(String num) {
        if (num != null) {
            StringBuilder sb = new StringBuilder();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length())) + " ");
                    if (params[1].contains("R")) {//让球
                        sb.append("6 ");
                        String[] tmps = params[1].split("/");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_ZQ_SPF_MAP.get(tmps[j].replace("R", "")));
                        }
                    } else if (params[1].contains(":")) {//比分
                        sb.append("2 ");
                        String[] tmps = params[1].split("/");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_ZQ_BF_MAP.get(tmps[j]));
                        }
                    } else if (params[1].contains("_")) {//半全场
                        sb.append("4 ");
                        String[] tmps = params[1].split("/");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_ZQ_BQC_MAP.get(tmps[j]));
                        }
                    } else if (params[1].contains("B")) {//总进球
                        sb.append("3 ");
                        String[] tmps = params[1].split("/");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_ZQ_ZJQ_MAP.get(tmps[j].replace("B", "")));
                        }
                    } else {
                        sb.append("1 ");
                        String[] tmps = params[1].split("/");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_ZQ_SPF_MAP.get(tmps[j].replace("R", "")));
                        }
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 竞彩足球混合格式转换(键盘)
     *
     * @param num
     * @return
     */
    public static String jcLanQHunheNumber(String num) {
        if (num != null) {
            StringBuffer sb = new StringBuffer();
            String[] nums = num.split("\\//");
            for (int i = 0; i < nums.length; i++) {
                String[] params = nums[i].split(",");
                if (params.length == 2) {
                    String gameNum = params[0];
                    sb.append(ColorContants.DATE_MAP.get(gameNum.substring(0, 2)));
                    sb.append(PrintUtil.plusTrim(gameNum.substring(2, gameNum.length()))).append(" ");
                    String[] tmps = params[1].split("/");
                    //下面if的判断顺序不能乱
                    if (params[1].contains("D")) {//让分胜负
                        sb.append("1 ");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_LQ_SF_MAP.get(tmps[j].replace("D", "")));
                        }
                    } else if (params[1].contains("B")) {//大小分
                        sb.append("4 ");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_LQ_DXF_MAP.get(tmps[j].replace("B", "")));
                        }
                    } else if (tmps[0].length() > 1) {//胜分差
                        sb.append("3 ");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_LQ_SFC_MAP.get(tmps[j]));
                        }
                    } else {
                        sb.append("2 ");
                        for (int j = 0; j < tmps.length; j++) {
                            sb.append(ColorContants.JC_LQ_SF_MAP.get(tmps[j]));
                        }
                    }
                    sb.append("F1 ");
                }
            }
            return sb.toString();
        }
        return "";
    }

}
