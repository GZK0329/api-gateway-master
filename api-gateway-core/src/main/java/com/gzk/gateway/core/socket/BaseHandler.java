package com.gzk.gateway.core.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutionException;

/**
 * @className: BaseHandler
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/20
 **/
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, T t) throws Exception {
        session(channelHandlerContext, channelHandlerContext.channel(), t);
    }

    protected abstract void session(ChannelHandlerContext channelHandlerContext, Channel channel, T t) throws ExecutionException, InterruptedException;
}
