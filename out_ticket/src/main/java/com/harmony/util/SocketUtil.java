package com.harmony.util;

import com.harmony.comm.OutCommon;
import com.harmony.entity.Communication;
import com.harmony.entity.RequestEntity;

/**
 * 2017/9/15 9:13.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class SocketUtil {

    /**
     * 客户端发送数据到服务端
     *
     * @param communication
     */
    public static void sendSocketToServer(Communication communication) {
        if (OutCommon.toGiveChannel != null) {
            OutCommon.toGiveChannel.writeAndFlush(communication);
        } else {
            LogUtil.info(SocketUtil.class, "向socket服务端发送数据", "未和socket服务端建立通信");
        }
    }

    /**
     * 客户端发送数据到服务端
     *
     * @param actionCode
     * @param data
     * @param <T>
     */
    public static <T> void sendSocketToServer(String actionCode, T data) {
        RequestEntity<T> requestEntity = new RequestEntity<>(actionCode, data, OutCommon.ip);
        sendSocketToServer(requestEntity);
    }

    /**
     * 客户端发送数据到服务端
     *
     * @param actionCode
     * @param data
     * @param <T>
     */
    public static <T> void sendSocketToServer(String actionCode, T data, String printIp) {
        RequestEntity<T> requestEntity = new RequestEntity<>(actionCode, data, printIp, OutCommon.ip);
        sendSocketToServer(requestEntity);
    }

}
