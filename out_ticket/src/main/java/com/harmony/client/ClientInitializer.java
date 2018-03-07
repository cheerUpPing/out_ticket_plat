package com.harmony.client;

import com.harmony.comm.OutCommon;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Email cheerupping@163.com
 * Time  2017/5/13 16:43
 * <p>
 * 描述
 */
public class ClientInitializer extends ChannelInitializer {

    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast("idle", new IdleStateHandler(0, 0, OutCommon.read_write_idle, TimeUnit.SECONDS));
        channelPipeline.addLast("object-decoder", new ObjectDecoder(10240, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
        channelPipeline.addLast("object-encoder", new ObjectEncoder());
        //心跳机制
        // 自己的逻辑Handler
        channelPipeline.addLast("handler", new ClientHandler());
    }
}
