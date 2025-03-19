package com.gzk.netty.client;

import com.gzk.netty.server.MyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * @className: MyChannelInitializer
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/18
 **/
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：本客户端链接到服务端。channelId：" + channel.id());
        System.out.println("链接报告完毕");

        channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        //在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(new MyClientHandler());
    }

}

