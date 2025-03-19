package com.gzk.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @className: NettyClient
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/18
 **/
public class NettyClient {
    public static void main(String[] args) {
        new NettyClient().connect("127.0.0.1", 6999);
    }

    private void connect(String inetHost, int inetPort) {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.AUTO_READ, true);
            bootstrap.handler(new MyChannelInitializer());
            ChannelFuture channelFuture = bootstrap.connect(inetHost, inetPort).sync();
            System.out.println("netty client start done.");
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
