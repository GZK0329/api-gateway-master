package com.gzk.gateway.core;

import com.gzk.gateway.core.bind.Configuration;
import com.gzk.gateway.core.mapping.HttpCommandType;
import com.gzk.gateway.core.mapping.HttpStatement;
import com.gzk.gateway.core.session.defaults.DefaultGatewaySessionFactory;
import com.gzk.gateway.core.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @className: RpcTest
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/20
 **/
@Slf4j
public class RpcTest {


    /**
     * 测试：http://localhost:7899/test/helloworld
     */
    @Test
    public void test_gateway() throws InterruptedException, ExecutionException {
        // 1. 创建配置信息加载注册
        Configuration configuration = new Configuration();
        HttpStatement httpStatement = new HttpStatement(
                "api-gateway-test",
                "com.gzk.test.api-test",
                "helloworld",
                "/test/helloworld",
                HttpCommandType.GET);
        configuration.addMapper(httpStatement);

        // 2. 基于配置构建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3. 创建启动网关网络服务
        GatewaySocketServer server = new GatewaySocketServer(gatewaySessionFactory);

        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (null == channel) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            log.info("netty server gateway start ing ...");
            Thread.sleep(500);
        }
        log.info("netty server gateway start Done! {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);
    }

}
