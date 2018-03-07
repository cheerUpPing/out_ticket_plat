package com.harmony;

import com.harmony.client.Client;
import com.harmony.comm.OutCommon;

/**
 * 2017/9/15 13:46.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 出票系统入口
 */
public class OutTicketSystemEntrance {

    public static void main(String[] args) throws InterruptedException {
        //启动客户端
        Client.connectServer(OutCommon.remoteIp, OutCommon.port);
    }

}
