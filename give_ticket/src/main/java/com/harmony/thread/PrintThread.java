package com.harmony.thread;

import com.harmony.entity.*;
import com.harmony.service.ServiceFactory;
import com.harmony.service.impl.PrintService;
import com.harmony.util.LogUtil;
import io.netty.channel.Channel;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;


/**
 * 2017/9/18 9:23.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 每个票机对应的线程任务
 * 下面根据票机状态，执行某些任务
 */
public class PrintThread extends Thread {

    private String printIp;
    //连接socket客户端的channel
    private Channel channel;

    //票机是否工作正常
    public volatile boolean printCanWork = false;

    private PrintService printService = (PrintService) ServiceFactory.getService("printService");

    private PrintServiceDTO printServiceDTO = null;
    //是否可以推送票
    public volatile boolean canPutTicket = false;


    //最多保存5张票
    public ConcurrentLinkedQueue<ChipinTempDTO> queue = new ConcurrentLinkedQueue<ChipinTempDTO>();

    public PrintThread(String printIp, Channel channel) throws SQLException {
        this.printIp = printIp;
        this.channel = channel;
        printCanWork = true;
        canPutTicket = true;
        //保存线程到map中
        GiveCommon.addPrintThread(printIp, this);
        printServiceDTO = printService.selectByIp(printIp);
        if (printServiceDTO != null) {
            GiveCommon.addLatestPrint(printServiceDTO);
        }
    }

    @Override
    public void run() {
        while (printCanWork) {
            try {
                if (printServiceDTO != null) {
                    //如果是出票状态
                    if (GiveCommon.getLatestPrint(printIp).getCurrentOperating() == PrintOperation.TPT_PRINT_STATUS) {
                        //出票
                        if (!queue.isEmpty() && canPutTicket) {
                            canPutTicket = false;
                            TimeUnit.SECONDS.sleep(1);
                            ChipinTempDTO chipinTempDTO = queue.remove();
                            LogUtil.info(PrintThread.class, "出票", "向票机:" + printServiceDTO.getWebAddress() + "推送票:" + chipinTempDTO.getTempId());
                            RequestEntity<ChipinTempDTO> requestEntity = new RequestEntity<ChipinTempDTO>(Contants.Action.A0006, chipinTempDTO, printIp, GiveCommon.ip);
                            channel.writeAndFlush(requestEntity);
                        } else {
                            LogUtil.info(PrintThread.class, "出票", "票机:" + printServiceDTO.getWebAddress() + "出票队列为空");
                        }
                    } else {
                        LogUtil.info(PrintThread.class, "票机服务", "当前票机状态" + GiveCommon.getLatestPrint(printIp).getCurrentOperating() + "未知");
                    }


                    TimeUnit.SECONDS.sleep(1);//500毫秒
                    printServiceDTO = printService.selectByIp(printIp);
                    if (printServiceDTO != null) {
                        GiveCommon.addLatestPrint(printServiceDTO);
                    }
                } else {
                    LogUtil.info(PrintThread.class, "出票", "没有找到票机" + printIp);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
