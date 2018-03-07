package com.harmony.thread;

import com.harmony.entity.GiveCommon;
import com.harmony.server.Server;

/**
 * 2017/9/18 17:45.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class StartUpSocketThread extends Thread {

    @Override
    public void run() {
        Server.openServer(GiveCommon.port);
    }
}
