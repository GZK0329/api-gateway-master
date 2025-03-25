package com.gzk.gateway.core.socket.handler;

import com.gzk.gateway.core.bind.IGenericReference;
import com.gzk.gateway.core.session.GatewaySession;
import com.gzk.gateway.core.session.GatewaySessionFactory;
import com.gzk.gateway.core.socket.BaseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @className: SessionServerHandler
 * @description: channel 处理器
 * @author: gzk
 * @since: 2025/3/20
 **/
public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {

    private final Logger log = LoggerFactory.getLogger(GatewayServerHandler.class);
    private final GatewaySessionFactory gatewaySessionFactory;

    public GatewayServerHandler(GatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void session(ChannelHandlerContext channelHandlerContext, Channel channel, FullHttpRequest request) throws ExecutionException, InterruptedException {
        log.info("网关收到请求,request uri{}, method{}", request.uri(), request.method());

        //过滤favicon.ico请求
        String methodName = request.uri().substring(1);
        if ("favicon.ico".equals(methodName)) {
            return;
        }

        GatewaySession gatewaySession = gatewaySessionFactory.openSession(request.uri());

        IGenericReference reference = gatewaySession.getMapper(request.uri());
        String result = reference.$invoke("test");

        // 返回信息处理
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        response.content().writeBytes(result.getBytes());
        HttpHeaders headers = response.headers();

        //返回内容类型
        headers.add(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        //内容长度
        headers.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        //长链接
        headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        //跨域
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        channel.writeAndFlush(response);
    }
}
