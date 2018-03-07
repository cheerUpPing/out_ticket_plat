package com.harmony.action.impl;

import com.harmony.action.AbstractAction;
import com.harmony.entity.*;
import com.harmony.exception.CheckTicketException;
import com.harmony.exception.SystemEroorException;
import com.harmony.service.ServiceFactory;
import com.harmony.service.impl.ChipinTempService;
import com.harmony.service.impl.PrintService;
import com.harmony.service.impl.SystemExceptionService;
import com.harmony.service.impl.TicketInfoService;
import com.harmony.util.CommmUtil;
import com.harmony.util.JdbcUtil;
import com.harmony.util.LogUtil;
import com.harmony.util.TicketUtil;
import org.apache.commons.lang3.StringUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 2017/9/21 17:22.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 出票数据返回
 */
public class BackTicketAction extends AbstractAction {

    public Communication doBusiness(Communication communication) throws Exception {
        ChipinTempService chipinTempService = (ChipinTempService) ServiceFactory.getService("chipinTempService");
        TicketInfoService ticketInfoService = (TicketInfoService) ServiceFactory.getService("ticketInfoService");
        SystemExceptionService systemExceptionService = (SystemExceptionService) ServiceFactory.getService("systemExceptionService");
        PrintService printService = (PrintService) ServiceFactory.getService("printService");
        RequestEntity requestEntity = (RequestEntity) communication;
        String printIp = requestEntity.getPrintIp();
        //可以继续推送票
        GiveCommon.getPrintThread(printIp).canPutTicket = true;
        PrintServiceDTO printServiceDTO = printService.selectByIp(printIp);
        Map<String, String> map = (HashMap) requestEntity.getData();
        String tempId = map.get("tempId");
        String data = map.get("data");
        LogUtil.info(BackTicketAction.class, "socket服务端接收到出票信息", "出票端" + GiveCommon.getPrint(printIp) + "票机" + printIp + "的出票" + tempId + "信息" + data);
        if (StringUtils.isNotEmpty(data) && !"null".equals(data)) {//出票成功
            String[] info = null;
            double tptMoney = 0.0;
            ChipinTempDTO chipinTempDTO = chipinTempService.selectByTempid(Long.parseLong(tempId));
            try {
                if (chipinTempDTO.getLotterytype().equals("jc") || chipinTempDTO.getLotterytype().equals("lq")) {
                    info = TicketUtil.getJCPrintInfo(chipinTempDTO, data);
                    String manner = TicketUtil.getMannerInfo(data);
                    tptMoney = Double.parseDouble(info[6] == null ? "0" : info[6]);
                    if (TicketUtil.checkJC(info, chipinTempDTO, manner)) {
                        String spvalue = TicketUtil.formatSpValue(info[4], (int) chipinTempDTO.getGameCode());
                        String sql = "UPDATE T_CHIPIN_TEMP set terminalType = ?,strpassword = ?,ticketId = ?,psdcheck = ?,blackmark = ?,spValue = ?,printLaterTime = ?,status=2 where tempId = ?";
                        Object[] objects = new Object[]{printServiceDTO.getTerminalType(), info[2], info[0], StringUtils.isNotEmpty(info[1]) ? info[1] : "", StringUtils.isNotEmpty(info[3]) ? info[3] : "", spvalue, Long.parseLong(info[7]), chipinTempDTO.getTempId()};
                        JdbcUtil.getQueryRunner().update(sql, objects);
                        LogUtil.info(BackTicketAction.class, "出票信息保存到数据库", "票" + tempId + "保存时间" + info[7]);
                        ticketInfoService.insert(tempId, data);
                    }
                } else if (chipinTempDTO.getLotterytype().equals("gp") || chipinTempDTO.getLotterytype().equals("gp360")) {
                    info = TicketUtil.getJCPrintInfo(chipinTempDTO, data);
                    tptMoney = Double.parseDouble(info[6] == null ? "0" : info[6]);
                    if (TicketUtil.checkGP(chipinTempDTO, data, printServiceDTO)) {
                        String sql = "update t_chipin_temp set terminalType = ?,ticketId=?,psdcheck=?,strpassword=?,blackmark=?,printLaterTime=?,status=2 where tempId=?";
                        Object[] objects = new Object[]{printServiceDTO.getTerminalType(), info[0], info[1], info[2], info[3], Long.parseLong(info[7]), chipinTempDTO.getTempId()};
                        JdbcUtil.getQueryRunner().update(sql, objects);
                        LogUtil.info(BackTicketAction.class, "出票信息保存到数据库", "票" + tempId + "保存时间" + info[7]);
                        ticketInfoService.insert(tempId, data);
                    }
                } else if (chipinTempDTO.getLotterytype().equals("ct")) {
                    info = TicketUtil.getJCPrintInfo(chipinTempDTO, data);
                    tptMoney = Double.parseDouble(info[6] == null ? "0" : info[6]);
                    if (TicketUtil.checkCT(chipinTempDTO, data, printServiceDTO)) {
                        String sql = "update t_chipin_temp td set terminalType = ?,td.ticketId=?,td.psdcheck=?, td.strpassword=?,  td.blackmark=?, td.printLaterTime=?, td.status=2 where td.tempId=?";
                        Object[] objects = new Object[]{printServiceDTO.getTerminalType(), info[0], info[1], info[2], info[3], Long.parseLong(info[7]), chipinTempDTO.getTempId()};
                        JdbcUtil.getQueryRunner().update(sql, objects);
                        LogUtil.info(BackTicketAction.class, "出票信息保存到数据库", "票" + tempId + "保存时间" + info[7]);
                        ticketInfoService.insert(tempId, data);
                    }
                } else if ("bd".equals(chipinTempDTO.getLotterytype())) {//这里是北单出票信息
                    info = TicketUtil.getBdPrintInfo(chipinTempDTO, data);
                    tptMoney = Double.parseDouble(info[5] == null ? "0" : info[5]);
                    if (TicketUtil.checkBd(info, chipinTempDTO)) {
                        String sql = "UPDATE T_CHIPIN_TEMP set terminalType = ?,strpassword = ?,ticketId = ?,printLaterTime = ?,status=2 where tempId = ?";
                        Object[] objects = new Object[]{printServiceDTO.getTerminalType(), (StringUtils.isEmpty(info[7]) ? "" : info[7]), info[6], info[1] == null ? null : info[1].replaceAll("[^\\d]", ""), chipinTempDTO.getTempId()};
                        JdbcUtil.getQueryRunner().update(sql, objects);
                        LogUtil.info(BackTicketAction.class, "出票信息保存到数据库", "票" + tempId + "保存时间" + info[7]);
                        ticketInfoService.insert(tempId, data);
                    }
                }
                //更新票机金额
                printService.updateMoney(printIp, tptMoney);
            } catch (SystemEroorException e) {
                LogUtil.info(BackTicketAction.class, "出票异常", "出票异常" + LogUtil.getStackTrace(e));
                systemExceptionService.insert(e.getMessage(), e);
                String sql = "insert into T_CHIPIN_TEMP_ERROR (FROMSERIALNO,TERMINALTYPE,EXCEPTIONINFO,PRINTLATERTIME,VOTEPROCCESSTIME,TICKETIMAGE)values(?,?,?,?,?,?)";
                SerialBlob serialBlob = new SerialBlob(data.getBytes("gbk"));//这里可以用utf-8
                Object[] objects = new Object[]{tempId, printServiceDTO.getTerminalType(), "系统不能正确拆分的未知票", Long.parseLong(CommmUtil.getTime(CommmUtil.yyyyMMddHHmmss)), chipinTempDTO.getVoteProccessTime(), serialBlob};
                JdbcUtil.getQueryRunner().update(sql, objects);
                printService.updateOperation(10, printIp);
            } catch (CheckTicketException e) {
                LogUtil.info(BackTicketAction.class, "出票异常", "出票异常" + LogUtil.getStackTrace(e));
                //clientSend.setErrorCount(clientSend.getErrorCount() + 1);
                String sql = "insert into T_CHIPIN_TEMP_ERROR (FROMSERIALNO,TERMINALTYPE,EXCEPTIONINFO,PRINTLATERTIME,VOTEPROCCESSTIME,TICKETIMAGE)values(?,?,?,?,?,?)";
                SerialBlob serialBlob = new SerialBlob(data.getBytes("gbk"));//这里可以用utf-8
                Object[] objects = new Object[]{tempId, printServiceDTO.getTerminalType(), "系统不能正确拆分的未知票", Long.parseLong(CommmUtil.getTime(CommmUtil.yyyyMMddHHmmss)), chipinTempDTO.getVoteProccessTime(), serialBlob};
                JdbcUtil.getQueryRunner().update(sql, objects);
                chipinTempService.updateException(e.getMessage(), 11, Long.parseLong(tempId));

                Throwable throwable = e.getCause();
                if (throwable instanceof SQLException) {
                    systemExceptionService.insert("系统异常：存库异常，级别最高级别, 请迅速停止所有票机出票，立即联系开发人员处理", e);
                }
            } catch (Exception e) {
                LogUtil.info(BackTicketAction.class, "出票异常", "出票异常" + LogUtil.getStackTrace(e));
                String sql = "insert into T_CHIPIN_TEMP_ERROR (FROMSERIALNO,TERMINALTYPE,EXCEPTIONINFO,PRINTLATERTIME,VOTEPROCCESSTIME,TICKETIMAGE)values(?,?,?,?,?,?)";
                SerialBlob serialBlob = new SerialBlob(data.getBytes("gbk"));//这里可以用utf-8
                Object[] objects = new Object[]{tempId, printServiceDTO.getTerminalType(), "系统不能正确拆分的未知票", Long.parseLong(CommmUtil.getTime(CommmUtil.yyyyMMddHHmmss)), chipinTempDTO.getVoteProccessTime(), serialBlob};
                JdbcUtil.getQueryRunner().update(sql, objects);
                chipinTempService.updateException("系统不能正确拆分的未知票", 11, Long.parseLong(tempId));
                Throwable throwable = e.getCause();
                if (throwable instanceof SQLException) {
                    systemExceptionService.insert("系统异常：存库异常，级别最高级别, 请迅速停止所有票机出票，立即联系开发人员处理", e);
                }
                printService.updateOperation(10, printIp);
            }
        } else {//出票数据票机没有返回，票机有问题
            chipinTempService.updateException("票机出票失败", 11, Long.parseLong(tempId));
            printService.updateOperation(10, printIp);
        }
        return null;
    }
}
