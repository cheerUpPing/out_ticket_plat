package com.harmony.server;

import com.harmony.entity.GiveCommon;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Email cheerupping@163.com
 * Time  2017/5/13 16:43
 * <p>
 * 描述
 */
public class ServerInitializer extends ChannelInitializer {

    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        //15s没有读事件 那么就说明该链接已经断开，服务器要关闭相应的资源
        channelPipeline.addLast("idle", new IdleStateHandler(GiveCommon.read_idle, 0, 0));
        channelPipeline.addLast("object-decoder", new ObjectDecoder(10240, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        channelPipeline.addLast("object-encoder", new ObjectEncoder());
        // 自己的逻辑Handler
        channelPipeline.addLast("handler", new ServerHandler());
    }
}
