package com.gzk.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className: MyServerHandler
 * @description: inbound入站事件处理器，处理客户端的消息
 * @author: gzk
 * @since: 2025/3/17
 **/
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ChannelHandler.channelGroup.add(ctx.channel());
        SocketChannel channel = (SocketChannel) ctx.channel();
        String msg = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ByteBuf buffer = Unpooled.buffer(msg.getBytes().length);
        buffer.writeBytes(msg.getBytes("GBK"));
        ctx.writeAndFlush(buffer);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接");
        ChannelHandler.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" + msg);
        String message = "服务器端收到消息" + new Date() + msg + "\r\n";
        ChannelHandler.channelGroup.writeAndFlush(Unpooled.buffer(message.getBytes().length).writeBytes(message.getBytes("GBK")));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
