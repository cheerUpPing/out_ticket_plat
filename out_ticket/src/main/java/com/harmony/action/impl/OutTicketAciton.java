package com.harmony.action.impl;

import com.harmony.action.AbstractAction;
import com.harmony.comm.OutCommon;
import com.harmony.entity.*;
import com.harmony.thread.StreamReadThread;
import com.harmony.util.CommmUtil;
import com.harmony.util.LogUtil;
import com.harmony.util.SerialUtil;
import com.harmony.util.TicketUtil;
import gnu.io.SerialPort;

import java.util.concurrent.TimeUnit;

/**
 * 2017/9/18 14:35.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 出票动作,向票机发送按键
 */
public class OutTicketAciton extends AbstractAction {

    public Communication doBusiness(Communication communication) throws Exception {
        RequestEntity requestEntity = (RequestEntity) communication;
        String printIp = requestEntity.getPrintIp();
        ChipinTempDTO chipinTempDTO = (ChipinTempDTO) communication.getData();
        Ticket ticket = TicketUtil.toTicketOjbect(chipinTempDTO, OutCommon.printServiceDTOMap.get(printIp));
        SerialPort channel = OutCommon.getSendChannel(printIp);
        if (channel != null) {
            //获取对应票机对象
            PrintServiceDTO printServiceDTO = OutCommon.printServiceDTOMap.get(printIp);
            if (ticket != null) {
                if (chipinTempDTO.getGameCode() == 402 || chipinTempDTO.getGameCode() == 404 || chipinTempDTO.getGameCode() == 401 ||
                        chipinTempDTO.getGameCode() == 406 || chipinTempDTO.getGameCode() == 407
                        || chipinTempDTO.getGameCode() == 408 || chipinTempDTO.getGameCode() == 409) {
                    long date = Long.parseLong(CommmUtil.getTime(CommmUtil.yyyyMMddHHmmss));
                    if (date < chipinTempDTO.getLastScreeningTime()) {
                        System.out.println("@1");
                        SerialUtil.sendToPort(printIp, channel, ticket.getBytes());
                    }
                } else if (301 == chipinTempDTO.getGameCode() || 302 == chipinTempDTO.getGameCode() || 303 == chipinTempDTO.getGameCode()
                        || 304 == chipinTempDTO.getGameCode() || 305 == chipinTempDTO.getGameCode() || 306 == chipinTempDTO.getGameCode() || 307 == chipinTempDTO.getGameCode()) {
                    System.out.println("@2");
                    SerialUtil.sendToPort(printIp, channel, ticket.getBytes());
                } else {
                    System.out.println("@3");
                    SerialUtil.sendToPort(printIp, channel, ticket.getBytes());
                }
                Thread.sleep(100);
                if (ticket.getMulBytes() != null && ticket.getMulBytes().length > 0) {
                    System.out.println("@4");
                    SerialUtil.sendToPort(printIp, channel, ticket.getMulBytes());
                }
                Thread.sleep(100);
                if (ticket.getEnterBytes() != null && ticket.getEnterBytes().length > 0) {
                    System.out.println("@5");
                    SerialUtil.sendToPort(printIp, channel, ticket.getEnterBytes());
                }
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println("@6");
                TicketUtil.recovery(printIp, channel, printServiceDTO);
                //这里保证串口数据准备好
                TimeUnit.SECONDS.sleep(2);
                new StreamReadThread(printIp, chipinTempDTO.getTempId() + "", OutCommon.getReceivedChannel(printIp)).start();
                LogUtil.info(OutTicketAciton.class, "出票端出票", "票机" + printIp + "出票成功,票" + chipinTempDTO.getTempId());
            }
        } else {
            LogUtil.info(OutTicketAciton.class, "出票端出票", "票机" + printIp + "的发送串口出现异常");
        }
        return null;
    }
}
