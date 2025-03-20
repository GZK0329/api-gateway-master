package com.gzk.gateway.core.session.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @className: BaseHandler
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/20
 **/
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, T t) throws Exception {

    }

    protected abstract void session(ChannelHandlerContext channelHandlerContext, Channel channel, T t);
}
