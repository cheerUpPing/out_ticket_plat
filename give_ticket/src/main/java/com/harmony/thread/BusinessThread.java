package com.harmony.thread;

import com.harmony.action.AbstractAction;
import com.harmony.action.ActionGiveFactory;
import com.harmony.entity.Communication;
import com.harmony.server.ServerHandler;
import com.harmony.util.LogUtil;
import io.netty.channel.ChannelHandlerContext;

/**
 * 2017/10/11 14:53.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 业务逻辑线程
 */
public class BusinessThread implements Runnable {

    private Communication communication = null;

    private ChannelHandlerContext context = null;

    public BusinessThread(Communication communication, ChannelHandlerContext context) {
        this.communication = communication;
        this.context = context;
    }

    @Override
    public void run() {
        LogUtil.info(ServerHandler.class, "socket客户端请求服务端动作", "动作编码" + communication.getActionCode());
        AbstractAction action = ActionGiveFactory.getAction(communication.getActionCode());
        Communication back = null;
        if (action != null) {
            try {
                back = action.doBusiness(communication);
                //是否需要返回数据
                if (back != null) {
                    context.writeAndFlush(back);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogUtil.info(ServerHandler.class, "socket客户端请求服务端动作", "动作" + communication.getActionCode() + "不存在");
        }

    }
}
