package com.harmony.server;

import com.harmony.util.LogUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 2017/9/13 14:37.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class Server {

    /**
     * 启动socket服务
     *
     * @param port
     */
    public static void openServer(int port) {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentGroup, childGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ServerInitializer());
        Channel channel = null;
        try {
            channel = serverBootstrap.bind(port).sync().channel();
            LogUtil.info(Server.class, "socket服务端启动", "启动成功,端口号" + port);
            //关闭服务端
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

}
