package com.harmony.action.impl;

import com.harmony.action.AbstractAction;
import com.harmony.entity.Communication;
import com.harmony.entity.GiveCommon;
import com.harmony.entity.emu.PrintStatus;
import com.harmony.service.ServiceFactory;
import com.harmony.service.impl.PrintService;
import com.harmony.util.LogUtil;

/**
 * 2017/9/15 8:59.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 票机掉线动作
 */
public class PrintLogoutAction extends AbstractAction {

    public Communication doBusiness(Communication communication) throws Exception {
        String printIp = (String) communication.getData();
        if (GiveCommon.getPrint(printIp) != null) {
            //移除发送/接收串口出问题的票机
            GiveCommon.deletePrint(printIp);
            //票机掉线
            GiveCommon.printOffLine(printIp);
            LogUtil.info(PrintLoginAction.class, "收到来自客户端的通知", "票机" + printIp + "串口掉线");
            PrintService printService = (PrintService) ServiceFactory.getService("printService");
            printService.updateStatusByIp(PrintStatus.stop, printIp);
        } else {
            LogUtil.info(PrintLoginAction.class, "收到来自客户端的通知", "票机" + printIp + "串口掉线问题已经处理,这里不做处理");
        }
        return null;
    }
}
