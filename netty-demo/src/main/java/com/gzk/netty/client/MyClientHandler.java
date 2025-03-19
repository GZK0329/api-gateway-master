package com.gzk.netty.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @className: MyClientHandler
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/18
 **/
public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端链接建立");
        String msg = "客户端链接建立成功！";
        ctx.writeAndFlush(Unpooled.buffer(msg.getBytes().length).writeBytes(msg.getBytes("GBK")));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端链接断开！");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = "客户端收到消息:" + msg;
        ctx.writeAndFlush(Unpooled.buffer(message.getBytes().length).writeBytes(message.getBytes("GBK")));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
