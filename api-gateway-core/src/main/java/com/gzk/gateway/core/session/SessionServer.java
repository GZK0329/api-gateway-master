package com.gzk.gateway.core.session;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.Callable;

/**
 * @className: SessionServer
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/20
 **/
public class SessionServer implements Callable<Channel> {

    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;
    private final Configuration configuration;

    public SessionServer(Configuration configuration) {
        this.configuration = configuration;
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Channel call() throws Exception {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture future = serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //等到连接的队列大小
                .option(ChannelOption.SO_BACKLOG, 128)
                //负责每个连接的channel的初始化
                .childHandler(new SessionChannelInitializer(configuration))
                .channel(NioServerSocketChannel.class)
                .bind(7899)
                .sync();
        this.channel = future.channel();
        return channel;
    }
}
