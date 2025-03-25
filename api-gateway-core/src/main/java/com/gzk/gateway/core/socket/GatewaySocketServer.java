package com.gzk.gateway.core.socket;

import com.gzk.gateway.core.session.GatewaySessionFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * @className: SessionServer
 * @description: netty server启动，管理channel
 * @author: gzk
 * @since: 2025/3/20
 **/
public class GatewaySocketServer implements Callable<Channel> {
    private final Logger log = LoggerFactory.getLogger(GatewaySocketServer.class);
    private final NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup(10);
    private final GatewaySessionFactory gatewaySessionFactory;

    private Channel channel;

    public GatewaySocketServer(GatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
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
        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.group(bossGroup, workerGroup)
                    //等到连接的队列大小
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //负责每个连接的channel的初始化
                    .childHandler(new GatewaySessionChannelInitializer(gatewaySessionFactory))
                    .channel(NioServerSocketChannel.class)
                    .bind(new InetSocketAddress(7899))
                    .sync();

            this.channel = channelFuture.channel();
        } catch (Exception e) {
            log.error("socket server start error.", e);
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                log.info("socket server start done.");
            } else {
                log.error("socket server start error.");
            }
        }
        return channel;
    }
}
