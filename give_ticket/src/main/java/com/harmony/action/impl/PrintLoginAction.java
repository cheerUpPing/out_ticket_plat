package com.harmony.action.impl;

import com.harmony.action.AbstractAction;
import com.harmony.entity.Communication;
import com.harmony.entity.GiveCommon;
import com.harmony.entity.emu.PrintStatus;
import com.harmony.service.impl.PrintService;
import com.harmony.service.ServiceFactory;
import com.harmony.util.LogUtil;

/**
 * 2017/9/15 9:00.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 票机发送/接收串口 连接/重连成功 动作
 */
public class PrintLoginAction extends AbstractAction {

    public Communication doBusiness(Communication communication) throws Exception {
        String printIp = (String) communication.getData();
        String ip = communication.getIp();
        //保存到正常票机map中
        GiveCommon.addPrintIfAbsent(printIp,ip);
        LogUtil.info(PrintLoginAction.class, "收到来自客户端的通知", "票机" + printIp + "发送/接收串口初始化成功");
        PrintService printService = (PrintService) ServiceFactory.getService("printService");
        printService.updateStatusByIp(PrintStatus.on, printIp);
        return null;
    }
}
