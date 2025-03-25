package com.gzk.gateway.core.socket;

import com.gzk.gateway.core.session.GatewaySessionFactory;
import com.gzk.gateway.core.socket.handler.GatewayServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @className: SessionChannelInitializer
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/20
 **/
public class GatewaySessionChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final GatewaySessionFactory gatewaySessionFactory;

    public GatewaySessionChannelInitializer(GatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new HttpRequestDecoder())
                .addLast(new HttpResponseEncoder())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(new StringEncoder())
                .addLast(new StringDecoder())
                .addLast(new GatewayServerHandler(gatewaySessionFactory));
    }
}
