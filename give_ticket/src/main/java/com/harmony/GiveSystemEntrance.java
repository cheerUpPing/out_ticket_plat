package com.harmony;

import com.harmony.entity.GiveCommon;
import com.harmony.thread.AssisctThread;
import com.harmony.thread.PrintThread;
import com.harmony.thread.StartUpSocketThread;
import com.harmony.util.LogUtil;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 2017/9/13 14:34.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 系统入口类
 */
public class GiveSystemEntrance {

    public static void main(String[] args) throws InterruptedException {
        int recheckTime = 10;
        //线程池服务
        ExecutorService executorService = Executors.newCachedThreadPool();
        //启动服务端socket
        StartUpSocketThread startUpSocketThread = new StartUpSocketThread();
        //启动分票线程
        AssisctThread assisctThread = new AssisctThread();

        executorService.submit(startUpSocketThread);
        executorService.submit(assisctThread);
        //下面开始处理业务
        while (true) {
            try {
                //多个票机[正常连接的票机]
                synchronized (GiveCommon.normalPrintsLock) {
                    if (GiveCommon.normalPrints.size() > 0) {
                        for (Map.Entry<String, String> entry : GiveCommon.normalPrints.entrySet()) {
                            String printIp = entry.getKey();
                            String socketIp = entry.getValue();
                            Channel channel = GiveCommon.getChannel(socketIp);
                            if (GiveCommon.getPrintThread(printIp) == null) {
                                PrintThread printThread = new PrintThread(printIp, channel);
                                executorService.submit(printThread);
                            }
                        }
                    } else {
                        LogUtil.info(GiveSystemEntrance.class, "系统入口", "还没有正常连接的票机");
                    }
                }
            } catch (Throwable e) {
                LogUtil.info(GiveSystemEntrance.class, "系统入口", "系统发生异常" + LogUtil.getStackTrace(e));
            } finally {
                //每30s重新检测正常连接票机数量是否正常,然后循环业务主体
                //这个时间可以长一点
                LogUtil.info(GiveSystemEntrance.class, "系统入口", "等待" + recheckTime + "s后系统重新检测是否有正常票机并运行主体业务");
                TimeUnit.SECONDS.sleep(recheckTime);
            }
        }

    }
}
