package com.harmony.thread;

import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.GiveCommon;
import com.harmony.entity.PrintOperation;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.service.ServiceFactory;
import com.harmony.service.impl.ChipinTempService;
import com.harmony.util.CommmUtil;
import com.harmony.util.JdbcUtil;
import com.harmony.util.LogUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AssisctThread extends Thread {

    /**
     * 每次推送5张票
     */
    final private int ticketCount = 5;

    public void run() {

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(2);
                synchronized (GiveCommon.normalPrintsLock) {
                    if (GiveCommon.normalPrints.size() > 0) {
                        for (Map.Entry<String, String> entry : GiveCommon.normalPrints.entrySet()) {
                            String printId = entry.getKey();
                            PrintThread printThread = GiveCommon.getPrintThread(printId);
                            if (printThread != null) {
                                //出票
                                if (GiveCommon.getLatestPrint(printId).getCurrentOperating() == PrintOperation.TPT_PRINT_STATUS) {// && (clientSend.getStatus() == ClientSend.TPTBusinessStatus.TPT_kongxian || 	clientSend.getStatus() == ClientSend.TPTBusinessStatus.TPT_print)
                                    int num = printThread.queue.size();
                                    if (num < ticketCount) {
                                        doLotteryPrint(printThread, GiveCommon.getLatestPrint(printId), ticketCount - num, false);
                                    }
                                }
                            }
                        }
                    } else {
                        LogUtil.info(AssisctThread.class, "分票", "没有正常连接的票机");
                    }
                }
            } catch (Exception e) {
                LogUtil.info(AssisctThread.class, "分票", "分票异常:" + LogUtil.getStackTrace(e));
            }
        }
    }


    /**
     * 分票到printThread的queue中
     */
    private void doLotteryPrint(PrintThread printThread, PrintServiceDTO printServiceDTO, int num, boolean testStatus) throws Exception {
        if (printThread != null && printThread.queue.size() < 5) {
            long systemTime = Long.parseLong(CommmUtil.getTime(CommmUtil.yyyyMMddHHmmss));
            ChipinTempService chipinTempService = (ChipinTempService) ServiceFactory.getService("chipinTempService");
            //准备分的票
            List<ChipinTempDTO> noallocationList = chipinTempService.findNoAllocationPrint(printServiceDTO, num, systemTime);
            if (noallocationList != null && noallocationList.size() > 0) {
                StringBuilder tempBuffer = new StringBuilder();
                for (int i = 0; i < noallocationList.size(); i++) {
                    ChipinTempDTO chipinTemp = noallocationList.get(i);
                    if (i != noallocationList.size() - 1) {
                        tempBuffer.append(chipinTemp.getTempId()).append(",");
                    } else {
                        tempBuffer.append(chipinTemp.getTempId());
                    }
                }
                //状态为status=1,出票中
                String sql = "update t_chipin_temp set status=1,postcount=postcount+1,postTime=" + systemTime + ",printservicenumber='" + printServiceDTO.getTerminalNumber() + "' where tempId in (" + tempBuffer.toString() + ")";
                int count = JdbcUtil.getQueryRunner().update(sql);
                if (noallocationList.size() == count) {
                    for (int i = 0; i < noallocationList.size(); i++) {
                        ChipinTempDTO chipinTemp = noallocationList.get(i);
                        chipinTemp.setTestStatus(testStatus);
                        LogUtil.info(AssisctThread.class, "分票", "往ip=" + printServiceDTO.getWebAddress() + "队列中分入票=" + chipinTemp.getTempId());
                        printThread.queue.add(chipinTemp);
                    }
                } else {
                    LogUtil.info(AssisctThread.class, "分票", "分票失败,相关票tempid:" + tempBuffer.toString());
                }
            } else {
                LogUtil.info(AssisctThread.class, "分票", "没有查询到要给票机" + printServiceDTO.getWebAddress() + "分的票");
            }
        } else {
            LogUtil.info(AssisctThread.class, "分票", "票机" + printServiceDTO.getWebAddress() + "工作线程队列已满，不允许分票");
        }
    }
}