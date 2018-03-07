package com.harmony.util;

import com.harmony.comm.ColorContants;
import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.service.ServiceFactory;
import com.harmony.service.impl.SystemExceptionService;

import java.sql.SQLException;

/**
 * Created by cm on 2017/4/10.
 */
public class TicketCTFormat {

    public static byte[] formatResult(ChipinTempDTO chipinTempDTO, PrintServiceDTO printServiceDTO) throws SQLException {
        String number = CtBlendFormat(chipinTempDTO, printServiceDTO.getUserpassword());
        LogUtil.info(TicketCTFormat.class, "获取键盘码", "键盘码为:" + number);
        return KeyUtil.getKeyCodesByKeys(number);
    }

    public static String CtBlendFormat(ChipinTempDTO chipinTempDTO, String userPassword) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append(ColorContants.COLOR_KEY_MAP.get(String.valueOf(chipinTempDTO.getGameCode())));
        String number = "";
        switch ((int) chipinTempDTO.getGameCode()) {
            case 110://大乐透
                number = dltNumber(chipinTempDTO);
                break;
            case 105://排列3
                number = pai3Number(chipinTempDTO);
                break;
            case 109://排列5
                number = pai5Number(chipinTempDTO);
                break;
            case 108://七星彩
                number = qiXCaiNumber(chipinTempDTO);
                break;
            case 102://胜负彩
                number = sfc102Number(chipinTempDTO);
                break;
            case 106://四场进球
                number = ScjqNumber(chipinTempDTO);
                break;
            case 107://六场半
                number = lcbNumber(chipinTempDTO);
                break;
            case 103://任选9
                number = rx103Number(chipinTempDTO);
                break;

        }
        sb.append(number);
        return sb.toString();
    }

    /**
     * 任选9彩格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String rx103Number(ChipinTempDTO chipinTempDTO) throws SQLException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        StringBuilder sb = new StringBuilder();
        //这里先确定期号的按键
        int issueKey = PrintUtil.getIssueNum(chipinTempDTO.getIssue(), chipinTempDTO.getSellingISSUE());
        if (issueKey == 0 || issueKey == -1) {
            systemExceptionService.insert("不允许出票,请确认票【" + chipinTempDTO.getTempId() + "】的跨期字段sellingissue是否为空", new RuntimeException("不允许出票..."));
            return sb.toString();
        }
        if (issueKey != 1) {
            sb.append("F8 ").append(issueKey).append(" ");
        }
        if ("1".equals(chipinTempDTO.getManner())) {
            String[] numbs = chipinTempDTO.getChipinNums().split(",");
            for (int i = 0; i < numbs.length; i++) {
                String number = numbs[i];
                StringBuffer sb1 = new StringBuffer();
                number = number.replaceAll("[^0-9]", "");
                sb1.append(number);
                String nums = rxPlusTrim(sb1);
                if (!"".equals(nums)) {
                    sb.append(plusTrim(nums).replace("9", "Right"));
                }
                if (i != numbs.length - 1) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        } else if ("2".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split("//");
            if (numbs.length == 14) {
                for (int i = 0; i < numbs.length; i++) {
                    if (i != 0) {
                        sb.append(" ");
                    }
                    sb.append(plusTrim(numbs[i].replaceAll("[^0-9]", "")));
                    if (i != numbs.length - 1) {
                        sb.append(" Right");
                    }
                }
            }
            return sb.toString().replace(" 9", "");
        }
        return "";
    }

    public static String rxPlusTrim(StringBuffer sb) {
        if (sb.length() > 0) {
            String last = sb.substring(sb.length() - 1).toString();
            if ("9".equals(last)) {
                sb = sb.delete(sb.length() - 1, sb.length());
                rxPlusTrim(sb);
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 4场进球彩格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String ScjqNumber(ChipinTempDTO chipinTempDTO) throws SQLException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        StringBuilder sb = new StringBuilder();
        //这里先确定期号的按键
        int issueKey = PrintUtil.getIssueNum(chipinTempDTO.getIssue(), chipinTempDTO.getSellingISSUE());
        if (issueKey == 0 || issueKey == -1) {
            systemExceptionService.insert("不允许出票,请确认票【" + chipinTempDTO.getTempId() + "】的跨期字段sellingissue是否为空", new RuntimeException("不允许出票..."));
            return sb.toString();
        }
        if (issueKey != 1) {
            sb.append("F8 ").append(issueKey).append(" ");
        }
        if ("1".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        } else if ("2".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split("//");
            if (numbs.length == 8) {
                for (int i = 0; i < numbs.length; i++) {
                    sb.append(plusTrim(numbs[i].replaceAll("[^0-9]", "")));
                    if (i != numbs.length - 1) {
                        sb.append(" Right ");
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 6场半彩格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String lcbNumber(ChipinTempDTO chipinTempDTO) throws SQLException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        StringBuilder sb = new StringBuilder();
        //这里先确定期号的按键
        int issueKey = PrintUtil.getIssueNum(chipinTempDTO.getIssue(), chipinTempDTO.getSellingISSUE());
        if (issueKey == 0 || issueKey == -1) {
            systemExceptionService.insert("不允许出票,请确认票【" + chipinTempDTO.getTempId() + "】的跨期字段sellingissue是否为空", new RuntimeException("不允许出票..."));
            return sb.toString();
        }
        if (issueKey != 1) {
            sb.append("F8 ").append(issueKey).append(" ");
        }
        if ("1".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        } else if ("2".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split("//");
            if (numbs.length == 12) {
                for (int i = 0; i < numbs.length; i++) {
                    sb.append(plusTrim(numbs[i].replaceAll("[^0-9]", "")));
                    if (i != numbs.length - 1) {
                        sb.append(" Right ");
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 胜负彩格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String sfc102Number(ChipinTempDTO chipinTempDTO) throws SQLException {
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        StringBuffer sb = new StringBuffer();
        //这里先确定期号的按键
        int issueKey = PrintUtil.getIssueNum(chipinTempDTO.getIssue(), chipinTempDTO.getSellingISSUE());
        if (issueKey == 0 || issueKey == -1) {
            systemExceptionService.insert("不允许出票,请确认票【" + chipinTempDTO.getTempId() + "】的跨期字段sellingissue是否为空", new RuntimeException("不允许出票..."));
            return sb.toString();
        }
        if (issueKey != 1) {
            sb.append("F8 ").append(issueKey).append(" ");
        }
        if ("1".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        } else if ("2".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split("//");
            if (numbs.length == 14) {
                for (int i = 0; i < numbs.length; i++) {
                    sb.append(plusTrim(numbs[i].replaceAll("[^0-9]", "")));
                    if (i != numbs.length - 1) {
                        sb.append(" Right ");
                    }
                }
            }
            return sb.toString();
        }

        return "";
    }

    /**
     * 七星彩格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String qiXCaiNumber(ChipinTempDTO chipinTempDTO) {
        StringBuffer sb = new StringBuffer();
        if ("1".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        } else if ("2".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split("//");
            if (numbs.length == 7) {
                for (int i = 0; i < numbs.length; i++) {
                    sb.append(plusTrim(numbs[i].replaceAll("[^0-9]", "")));
                    if (i != numbs.length - 1) {
                        sb.append(" Down ");
                    }
                }
            }
            return sb.toString();
        }

        return "";
    }

    /**
     * 排列五格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String pai5Number(ChipinTempDTO chipinTempDTO) {
        StringBuffer sb = new StringBuffer();
        if ("1".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        } else if ("2".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split("//");
            if (numbs.length == 5) {
                for (int i = 0; i < numbs.length; i++) {
                    sb.append(plusTrim(numbs[i].replaceAll("[^0-9]", "")));
                    if (i != numbs.length - 1) {
                        sb.append(" Down ");
                    }
                }
            }
            return sb.toString();
        }

        return "";
    }

    public static void main(String[] args) throws SQLException {
        ChipinTempDTO chipinTempDTO = new ChipinTempDTO();
        chipinTempDTO.setChipinNums("09/24/17/32//11/12,01/09");
        chipinTempDTO.setManner("7");
        chipinTempDTO.setGameCode(110);
        chipinTempDTO.setBets(2);
        chipinTempDTO.setMultiple("99");
        String number = CtBlendFormat(chipinTempDTO, "111111");
        System.out.println("拆分数据：" + number);
    }

    /**
     * 排列3格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String pai3Number(ChipinTempDTO chipinTempDTO) {
        StringBuffer sb = new StringBuffer();
        if ("11".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        } else if ("13".equals(chipinTempDTO.getManner()) || "16".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        } else if ("21".equals(chipinTempDTO.getManner())) {
            sb.append("F1 F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split("//");
            if (numbs.length == 3) {
                for (int i = 0; i < numbs.length; i++) {
                    sb.append(plusTrim(numbs[i].replaceAll("[^0-9]", "")));
                    if (i != numbs.length - 1) {
                        sb.append(" Down ");
                    }
                }
            }
            return sb.toString();
        } else if ("23".equals(chipinTempDTO.getManner()) || "26".equals(chipinTempDTO.getManner())) {
            sb.append("F2 ");
            sb.append(ColorContants.PAI3MAP.get(chipinTempDTO.getManner()));
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        } else if ("31".equals(chipinTempDTO.getManner()) || "32".equals(chipinTempDTO.getManner())) {
            sb.append("F2 ");
            sb.append(ColorContants.PAI3MAP.get(chipinTempDTO.getManner()));
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            return sb.toString();
        }
        return "";
    }

    /**
     * 大乐透格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String dltNumber(ChipinTempDTO chipinTempDTO) {
        StringBuffer sb = new StringBuffer();
        String number = "";
        if ("1".equals(chipinTempDTO.getManner()) || "5".equals(chipinTempDTO.getManner())) {
            number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(plusTrim(number));
            if ("5".equals(chipinTempDTO.getManner())) {
                sb.append(" +");
            }
            return sb.toString();
        } else if ("2".equals(chipinTempDTO.getManner()) || "6".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split("//");
            if (numbs.length == 2) {
                sb.append("Down ");
                sb.append(plusTrim(numbs[0].replaceAll("[^0-9]", "")));
                sb.append(" Down Down ");
                sb.append(plusTrim(numbs[1].replaceAll("[^0-9]", "")));
            }
            if ("6".equals(chipinTempDTO.getManner())) {
                sb.append(" +");
            }
            return sb.toString();
        } else if ("3".equals(chipinTempDTO.getManner()) || "7".equals(chipinTempDTO.getManner())) {
            sb.append("F1 ");
            String[] numbs = chipinTempDTO.getChipinNums().split(",");
            if (numbs.length == 2) {
                String[] numbers1 = numbs[0].split("//");
                if (numbers1.length == 1) {
                    sb.append("Down ");
                    sb.append(plusTrim(numbers1[0].replaceAll("[^0-9]", "")));
                    sb.append(" Down ");
                } else if (numbers1.length == 2) {
                    sb.append(plusTrim(numbers1[0].replaceAll("[^0-9]", "")));
                    sb.append(" Down ");
                    sb.append(plusTrim(numbers1[1].replaceAll("[^0-9]", "")));
                    sb.append(" Down ");
                }
                String[] numbers2 = numbs[1].split("//");
                if (numbers2.length == 1) {
                    sb.append("Down ");
                    sb.append(plusTrim(numbers2[0].replaceAll("[^0-9]", "")));
                } else if (numbers2.length == 2) {
                    sb.append(plusTrim(numbers2[0].replaceAll("[^0-9]", "")));
                    sb.append(" Down ");
                    sb.append(plusTrim(numbers2[1].replaceAll("[^0-9]", "")));
                }
            }
            if ("7".equals(chipinTempDTO.getManner())) {
                sb.append(" +");
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * "000" ==> "0 0 0"
     *
     * @param str
     * @return
     */
    public static String plusTrim(String str) {
        if (str != null) {
            StringBuffer sb = new StringBuffer();
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
}
