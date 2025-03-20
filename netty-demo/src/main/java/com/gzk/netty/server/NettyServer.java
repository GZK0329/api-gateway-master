package com.gzk.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @className: NettyServer
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/17
 **/
public class NettyServer {

    public static void main(String[] args) {
        new NettyServer().bind(7397);
    }

    private void bind(int port) {
        //配置服务端NIO线程组
        //处理连接事件，一般设置1
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //处理已连接的channel的read/write事件，默认cpu核心数*2
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //等到连接的队列大小
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //负责每个连接的channel的初始化
                    .childHandler(new MyChannelInitializer());
            //启动server，sync同步等待服务器启动完成
            ChannelFuture f = b.bind(port).sync();
            //阻塞主线程，关闭各个channel后再退出
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
