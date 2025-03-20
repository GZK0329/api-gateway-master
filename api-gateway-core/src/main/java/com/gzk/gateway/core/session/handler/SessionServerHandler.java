package com.gzk.gateway.core.session.handler;

import com.gzk.gateway.core.bind.IGenericReference;
import com.gzk.gateway.core.session.Configuration;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 * @className: SessionServerHandler
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/20
 **/
@Slf4j
public class SessionServerHandler extends BaseHandler<FullHttpRequest> {

    private final Configuration configuration;

    public SessionServerHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext channelHandlerContext, Channel channel, FullHttpRequest request) {
        log.info("网关收到请求,request uri{}, method{}", request.uri(), request.method());

        //过滤favicon.ico请求
        String methodName = request.uri().substring(1);
        if ("favicon.ico".equals(methodName)) {
            return;
        }

        IGenericReference genericReference = configuration.getGenericReference("sayHi");
        String result = genericReference.$invoke("test");

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
