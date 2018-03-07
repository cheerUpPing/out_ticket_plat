package com.harmony.server;

import com.harmony.action.AbstractAction;
import com.harmony.action.ActionGiveFactory;
import com.harmony.entity.Communication;
import com.harmony.entity.Contants;
import com.harmony.entity.GiveCommon;
import com.harmony.thread.BusinessThread;
import com.harmony.util.LogUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

/**
 * Email cheerupping@163.com
 * Time  2017/5/13 16:51
 * <p>
 * 描述 通道事件驱动器
 */
public class ServerHandler extends SimpleChannelInboundHandler {

    // 业务逻辑线程池(业务逻辑最好跟netty io线程分开处理，线程切换虽会带来一定的性能损耗，但可以防止业务逻辑阻塞io线程)
    private final static ExecutorService workerThreadService = newBlockingExecutor(Runtime.getRuntime().availableProcessors() * 2);

    /**
     * 读取到信息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    protected void channelRead0(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Communication) {
            Communication communication = (Communication) msg;
            //心跳，那么就不用新建线程
            if (Contants.Action.A0003.equals(communication.getActionCode())) {
                AbstractAction action = ActionGiveFactory.getAction(communication.getActionCode());
                Communication back = action.doBusiness(communication);
                //是否需要返回数据
                if (back != null) {
                    ctx.writeAndFlush(back);
                }
            } else {
                BusinessThread businessThread = new BusinessThread(communication, ctx);
                workerThreadService.submit(businessThread);
            }
        } else {
            //无效的请求
            LogUtil.info(ServerHandler.class, "socket客户端请求服务端动作", "来自" + ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress() + "的无效动作请求,系统关闭链接");
            ctx.close();
        }
    }

    /**
     * 通道建立连接
     * 保存通道 保存对应票机信息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (channel.remoteAddress() instanceof InetSocketAddress) {
            String ip = ((InetSocketAddress) (channel.remoteAddress())).getAddress().getHostAddress();
            GiveCommon.addChannel(ip, channel);
        } else {
            LogUtil.info(ServerHandler.class, "建立连接", "来自" + channel.remoteAddress() + "的非法的链接,系统关闭链接");
            ctx.close();
        }

    }

    /**
     * 服务器发生的所有异常都会被拦截
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        String ip = ((InetSocketAddress) (channel.remoteAddress())).getAddress().getHostAddress();
        LogUtil.info(ServerHandler.class, "socket服务器发生异常", "服务器发生异常" + LogUtil.getStackTrace(cause));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    //客户端掉线
                    //关闭掉线的客户端
                    GiveCommon.removeOffLineChannel(ctx.channel());
                    ctx.channel().close();
                    break;
                case WRITER_IDLE:
                    break;
                case ALL_IDLE:
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 阻塞的ExecutorService
     *
     * @param size
     * @return
     */
    private static ExecutorService newBlockingExecutor(int size) {
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        try {
                            executor.getQueue().put(r);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
}


