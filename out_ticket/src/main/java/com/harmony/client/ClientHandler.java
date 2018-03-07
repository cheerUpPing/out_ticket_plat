package com.harmony.client;

import com.harmony.action.AbstractAction;
import com.harmony.action.ActionOutFactory;
import com.harmony.comm.OutCommon;
import com.harmony.entity.Communication;
import com.harmony.entity.Contants;
import com.harmony.entity.Print;
import com.harmony.entity.RequestEntity;
import com.harmony.util.LogUtil;
import com.harmony.util.SocketUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Email cheerupping@163.com
 * Time  2017/5/13 16:51
 * <p>
 * 描述 通道事件驱动器
 * <p>
 * 出票端socket 处理器
 */
public class ClientHandler extends SimpleChannelInboundHandler {


    /**
     * 读取到信息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Communication) {
            Communication communication = (Communication) msg;
            AbstractAction action = ActionOutFactory.getAction(communication.getActionCode());
            if (action != null) {
                LogUtil.info(ClientHandler.class, "socket服务端请求客户端动作", "动作编码" + communication.getActionCode());
                Communication back = action.doBusiness(communication);
                if (back != null) {
                    ctx.writeAndFlush(back);
                }
            } else {
                LogUtil.info(ClientHandler.class, "socket服务端请求客户端动作", "动作" + communication.getActionCode() + "不存在");
            }
        } else {
            //无效的请求
            LogUtil.info(ClientHandler.class, "socket服务端请求客户端动作", "来自" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + "的无效动作请求,系统关闭链接");
            ctx.close();
        }
    }

    /**
     * 通道建立连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //非线程安全
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Channel channel = ctx.channel();
        OutCommon.toGiveChannel = channel;
        OutCommon.ip = ((InetSocketAddress) channel.localAddress()).getAddress().getHostAddress();
        LogUtil.info(ClientHandler.class, "出票端和分票端建立socket连接", "时间" + sdf.format(new Date()) + "出票端ip" + ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress());
        //请求该分票模块对应的票机数据
        StringBuilder sb = new StringBuilder();
        for (Print print : OutCommon.prints) {
            sb.append("'").append(print.getIp()).append("'").append(",");
        }
        if (sb.lastIndexOf(",") != -1) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        RequestEntity<String> requestEntity = new RequestEntity<String>(Contants.Action.A0002, sb.toString(), OutCommon.ip);
        ctx.writeAndFlush(requestEntity);
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断线重连
        Channel channel = ctx.channel();
        if (channel != null && !channel.isActive()) {
            LogUtil.info(ClientInitializer.class, "socket客户端" + OutCommon.ip + "掉线", "客户端重连服务端");
            while (true) {
                if (Client.connectServer(OutCommon.remoteIp, OutCommon.port)) {
                    LogUtil.info(ClientInitializer.class, "socket客户端", "客户端" + OutCommon.ip + "重连成功");
                    break;
                } else {
                    LogUtil.info(ClientInitializer.class, "socket客户端", "客户端" + OutCommon.ip + "重连失败,5s后重新尝试重连");
                }
                TimeUnit.SECONDS.sleep(5);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil.info(ClientHandler.class, "socket客户端" + OutCommon.ip + "发生异常", LogUtil.getStackTrace(cause));
    }

    /**
     * 每次超时时间触发，就向服务器发送心跳，服务器接收到心跳后就回复心跳，然后客户端根据收到的信息确实是否连接正常
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    break;
                case WRITER_IDLE:
                    break;
                case ALL_IDLE:
                    LogUtil.info(ClientHandler.class, "发送心跳", "出票模块" + OutCommon.ip + "发送信息");
                    //向socket服务端发送心跳
                    SocketUtil.sendSocketToServer(Contants.Action.A0003, "ping");
                    break;
                default:
                    break;
            }
        }
    }
}
