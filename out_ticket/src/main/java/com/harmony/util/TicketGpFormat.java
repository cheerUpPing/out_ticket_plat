package com.harmony.util;

import com.harmony.comm.ColorContants;
import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.PrintServiceDTO;
import org.apache.log4j.Logger;

/**
 * Created by cm on 2017/4/10.
 */
public class TicketGpFormat {
    private final static Logger __logger = Logger.getLogger(TicketGpFormat.class);


    public static byte[] formatResult(ChipinTempDTO chipinTempDTO, PrintServiceDTO printServiceDTO) {
        String number = GpBlendFormat(chipinTempDTO, printServiceDTO.getUserpassword());
        LogUtil.info(TicketCTFormat.class, "获取键盘码", "键盘码为:" + number);
        return KeyUtil.getKeyCodesByKeys(number);
    }

    public static String GpBlendFormat(ChipinTempDTO chipinTempDTO, String userPassword) {
        StringBuffer sb = new StringBuffer();

        sb.append(ColorContants.COLOR_KEY_MAP.get("GP"));
        sb.append("F2 ");
        String number = "";
        if (chipinTempDTO.getGameCode() >= 400 && chipinTempDTO.getGameCode() < 500) {
            number = GaopNumber(chipinTempDTO);
        }
        sb.append(number);
        return sb.toString();
    }

    public static void main(String[] args) {
        ChipinTempDTO chipinTempDTO = new ChipinTempDTO();
        chipinTempDTO.setChipinNums("09/24/17/32//01/02/03/04/05/06/07/08/10/11/12/13/14/15/16/18/19/20/21/22/23/25/26/27/28/29/30/31/33,08//01/09");
        chipinTempDTO.setManner("3");
        chipinTempDTO.setGameCode(110);
        System.out.println(GaopNumber(chipinTempDTO));
    }

    /**
     * 高配格式转换(键盘)
     *
     * @param chipinTempDTO
     * @return
     */
    public static String GaopNumber(ChipinTempDTO chipinTempDTO) {
        StringBuilder sb = new StringBuilder();
        if (chipinTempDTO.getGameCode() == 401) {
            sb.append(ColorContants.GAOP_MAP401.get(chipinTempDTO.getManner()));
        } else {
            sb.append(ColorContants.GAOP_MAP.get(chipinTempDTO.getManner()));
        }


        if ("1".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums().replaceAll("[^0-9]", "");
            sb.append(PrintUtil.plusTrim(number));
        } else if ("2".equals(chipinTempDTO.getManner()) || "3".equals(chipinTempDTO.getManner()) ||
                "4".equals(chipinTempDTO.getManner()) || "5".equals(chipinTempDTO.getManner()) ||
                "6".equals(chipinTempDTO.getManner()) || "7".equals(chipinTempDTO.getManner()) ||
                "8".equals(chipinTempDTO.getManner()) || "10".equals(chipinTempDTO.getManner()) ||
                "12".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums();
            if (number.contains("//")) {
                sb.append("F1 ");
                sb.append("Down ");
                sb.append(PrintUtil.plusTrim(number.replaceAll("[^0-9]", "")));
            } else if (number.contains("$")) {
                sb.append("F1 ");
                sb.append(PrintUtil.plusTrim(number.replaceAll("[^0-9$]", "")).replace("$", "Down"));
            } else {
                sb.append(PrintUtil.plusTrim(number.replaceAll("[^0-9]", "")));
            }
        } else if ("9".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums();

            if (number.contains("//")) {
                sb.append("F1 ");
                String[] strings = number.split("//");
                if (strings.length == 2) {
                    sb.append("F1 ");
                    for (int i = 0; i < strings.length; i++) {
                        sb.append(PrintUtil.plusTrim(strings[i].replaceAll("[^0-9]", "")));
                        if (i != strings.length - 1) {
                            sb.append(" Down ");
                        }
                    }
                } else {
                    sb.append(PrintUtil.plusTrim(number.replaceAll("[^0-9]", "")));
                }
            } else {
                sb.append(PrintUtil.plusTrim(number.replaceAll("[^0-9]", "")));
            }
        } else if ("11".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums();
            if (number.contains("//")) {
                sb.append("F1 ");
                String[] strings = number.split("//");
                if (strings.length == 3) {
                    sb.append("F1 ");
                    for (int i = 0; i < strings.length; i++) {
                        sb.append(PrintUtil.plusTrim(strings[i].replaceAll("[^0-9]", "")));
                        if (i != strings.length - 1) {
                            sb.append(" Down ");
                        }
                    }
                } else {
                    sb.append(PrintUtil.plusTrim(number.replaceAll("[^0-9]", "")));
                }
            } else {
                sb.append(PrintUtil.plusTrim(number.replaceAll("[^0-9]", "")));
            }
            //乐选13 14 15 16
        } else if ("13".equals(chipinTempDTO.getManner()) || "14".equals(chipinTempDTO.getManner()) || "15".equals(chipinTempDTO.getManner()) || "16".equals(chipinTempDTO.getManner())) {
            String number = chipinTempDTO.getChipinNums();
            sb.append(PrintUtil.plusTrim(number.replaceAll("[^0-9]", "")));
        }
        return sb.toString();
    }
}
