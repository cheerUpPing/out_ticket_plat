package com.harmony.client;

import com.harmony.comm.OutCommon;
import com.harmony.util.LogUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 2017/9/13 16:10.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class Client {


    /**
     * 连接远程地址
     *
     * @param remoteIp
     * @param port
     */
    public static boolean connectServer(String remoteIp, int port) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ClientInitializer());
        Channel channel = null;
        try {
            channel = bootstrap.connect(remoteIp, port).sync().channel();
            OutCommon.toGiveChannel = channel;
            LogUtil.info(Client.class, "客户端连接socket服务端", "连接成功" + channel);
            if (channel != null) {
                channel.closeFuture().sync(); // 异步等待关闭连接channel
                LogUtil.info(Client.class, "关闭socket客户端", "关闭客户端");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
        return channel != null;
    }
}
