package com.harmony.util;

import com.harmony.entity.GameContent;
import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.entity.Ticket;
import com.harmony.entity.emu.PrintType;
import gnu.io.SerialPort;

/**
 * 2017/9/18 14:40.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class TicketUtil {

    /**
     * 将投注号码转换成票机格式[串口按键]
     * 不向外抛错
     *
     * @return
     */
    public static Ticket toTicketOjbect(ChipinTempDTO chipinTemp, PrintServiceDTO printServiceDTO) {
        try {
            Ticket ticket = new Ticket();
            ticket.setTicketId(String.valueOf(chipinTemp.getTempId()));
            ticket.setGameCode(String.valueOf(chipinTemp.getGameCode()));
            ticket.setPrintServiceDTO(printServiceDTO);
            ticket.setBets(String.valueOf(chipinTemp.getBets()));
            ticket.setAwardBets(chipinTemp.getTempAwardBets());
            ticket.setManner(chipinTemp.getManner());
            ticket.setGid(chipinTemp.getTicketId());
            ticket.setIssue(chipinTemp.getIssue());
            ticket.setMultiple(Integer.parseInt(chipinTemp.getMultiple()));
            ticket.setRecord((int) chipinTemp.getRecord());
            ticket.setTestStatus(chipinTemp.isTestStatus());
            chipinTemp.setMultiple(String.valueOf(Integer.parseInt(chipinTemp.getMultiple())));
            if (null != chipinTemp.getBlackmark() && !chipinTemp.getBlackmark().equals("null")) {
                ticket.setBlackmark(chipinTemp.getBlackmark());
            }
            if (null != chipinTemp.getStrpassword() && !chipinTemp.getStrpassword().equals("null")) {
                ticket.setPassword(chipinTemp.getStrpassword());
            }
            ticket.setSourceVoteNumber(chipinTemp.getChipinNums());

            //大乐透  01/02/03/04/05/06/07/08//01/02/03/04*1*1*110*0  大乐透：投注号码*倍数*[单式1|复式2|胆拖3]*彩票类型*[追加1|不追加0]
            if (Integer.parseInt(ticket.getGameCode()) == GameContent.dlt || chipinTemp.getGameCode() == 102 || chipinTemp.getGameCode() == 103 ||
                    chipinTemp.getGameCode() == 105 || chipinTemp.getGameCode() == 106 || chipinTemp.getGameCode() == 107 ||
                    chipinTemp.getGameCode() == 109 || chipinTemp.getGameCode() == 108) {
                byte[] srcFormat = TicketCTFormat.formatResult(chipinTemp, printServiceDTO);
                ticket.setBytes(srcFormat);
            } else if (Integer.parseInt(ticket.getGameCode()) == GameContent.p11_5 || Integer.parseInt(ticket.getGameCode()) == GameContent.h11_5 ||
                    Integer.parseInt(ticket.getGameCode()) == GameContent.wl11_5 || Integer.parseInt(ticket.getGameCode()) == GameContent.yunlan11_5 ||
                    Integer.parseInt(ticket.getGameCode()) == GameContent.ln11_5 ||
                    Integer.parseInt(ticket.getGameCode()) == GameContent.jx11_5 || Integer.parseInt(ticket.getGameCode()) == GameContent.sx11_5 ||
                    Integer.parseInt(ticket.getGameCode()) == GameContent.hb11_5 || Integer.parseInt(ticket.getGameCode()) == GameContent.qh11_5) {
                byte[] srcFormat = TicketGpFormat.formatResult(chipinTemp, printServiceDTO);
                ticket.setBytes(srcFormat);
            } else if (301 == chipinTemp.getGameCode() || 302 == chipinTemp.getGameCode() || 303 == chipinTemp.getGameCode()
                    || 304 == chipinTemp.getGameCode() || 305 == chipinTemp.getGameCode() || 306 == chipinTemp.getGameCode() || 307 == chipinTemp.getGameCode()) {
                byte[] srcFormat = BjdcUtil.formatResult(chipinTemp);
                ticket.setBytes(srcFormat);
            } else {
                byte[] srcFormat = TicketJcFormat.formatResult(chipinTemp, printServiceDTO);
                ticket.setBytes(srcFormat);
            }
            byte[] mulVector = getMultiple(chipinTemp.getMultiple());
            if (mulVector != null && mulVector.length > 0) {
                ticket.setMulBytes(mulVector);
            }
            byte[] enterVector = getEnter(chipinTemp, printServiceDTO);
            if (enterVector != null && enterVector.length > 0) {
                ticket.setEnterBytes(enterVector);
            }
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info(TicketUtil.class, "拆分键盘码出错", "票" + printServiceDTO.getTempid() + "拆分出错");
            return null;
        }

    }

    private static byte[] getMultiple(String multiple) {
        StringBuilder sb = new StringBuilder();
        if (!"1".equals(multiple)) {
            sb.append("F5 ");
            sb.append(PrintUtil.plusTrim(multiple));
            sb.append(" Enter");
        }
        if (sb.length() > 0) {
            LogUtil.info(TicketUtil.class, "获取键盘码", "倍数键盘码:" + sb.toString());
        }
        return KeyUtil.getKeyCodesByKeys(sb.toString());
    }

    private static byte[] getEnter(ChipinTempDTO chipinTempDTO, PrintServiceDTO printServiceDTO) {
        StringBuilder sb = new StringBuilder();
        if (chipinTempDTO.getBets() >= 500) {
            sb.append("Enter");
            sb.append(" ").append(PrintUtil.plusTrim(printServiceDTO.getUserpassword()));
            sb.append(" Enter");
        }
        //出票enter键[必须有]
        sb.append("ENTER");
        LogUtil.info(TicketUtil.class, "获取键盘码", "enter键盘码:" + sb.toString());
        return KeyUtil.getKeyCodesByKeys(sb.toString());
    }

    /**
     * 让票机回到最开始页面
     *
     * @param printServiceDTO
     */
    public static void recovery(String printIp, SerialPort channel, PrintServiceDTO printServiceDTO) {
        byte[] bytes = null;
        try {
            if (PrintType.CP86.getType().equals(printServiceDTO.getTerminalType())) {
                bytes = recoveryCp86();
            } else if (PrintType.C8.getType().equals(printServiceDTO.getTerminalType()) || PrintType.C8USB.getType().equals(printServiceDTO.getTerminalType())
                    || PrintType.HS.getType().equals(printServiceDTO.getTerminalType()) || PrintType.SC6.getType().equals(printServiceDTO.getTerminalType())
                    || PrintType.LDR.getType().equals(printServiceDTO.getTerminalType())) {
                bytes = recoveryC8_SC();
            }
            SerialUtil.sendToPort(printIp, channel, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //生成CP86恢复发送串
    public static byte[] recoveryCp86() throws InterruptedException {
        String rec = "H BACK BACK BACK ESC ESC ESC ESC ESC";
        LogUtil.info(SerialUtil.class, "获取键盘码", "恢复吗:" + rec);
        return KeyUtil.getKeyCodesByKeys(rec);
    }

    //生成C8恢复发送串
    public static byte[] recoveryC8_SC() throws InterruptedException {
        String rec = "F BACK BACK BACK ESC ESC ESC ESC ESC";
        LogUtil.info(SerialUtil.class, "获取键盘码", "恢复吗:" + rec);
        return KeyUtil.getKeyCodesByKeys(rec);
    }

    //cp86出票超时恢复发送串
    public static byte[] ticketRecovery() throws InterruptedException {
        String rec = "H ESC ESC ESC ESC ESC";
        LogUtil.info(SerialUtil.class, "获取键盘码", "恢复吗:" + rec);
        return KeyUtil.getKeyCodesByKeys(rec);
    }


}
