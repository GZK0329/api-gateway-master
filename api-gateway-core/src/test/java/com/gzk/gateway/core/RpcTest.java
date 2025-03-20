package com.gzk.gateway.core;

import com.gzk.gateway.core.session.Configuration;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @className: RpcTest
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/20
 **/
@Slf4j
public class RpcTest {

    @Test
    public void testRpc(){
        ApplicationConfig application = new ApplicationConfig();
        application.setName("api-gateway-test");
        application.setQosEnable(false);

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");
        registry.setRegister(false);

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface("cn.bugstack.gateway.rpc.IActivityBooth");
        reference.setVersion("1.0.0");
        reference.setGeneric("true");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(application)
                .registry(registry)
                .reference(reference)
                .start();

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);

        Object result = genericService.$invoke("sayHi", new String[]{"java.lang.String"}, new Object[]{"world"});

        System.out.println(result);


    }

//    @Test
//    public void testGenericReference() throws InterruptedException, ExecutionException {
//        Configuration configuration = new Configuration();
//        configuration.addGenericReference("api-gateway-test", "cn.bugstack.gateway.rpc.IActivityBooth", "sayHi");
//
//        GenericReferenceSessionFactoryBuilder builder = new GenericReferenceSessionFactoryBuilder();
//        Future<Channel> future = builder.build(configuration);
//
//        log.info("服务启动完成 {}", future.get().id());
//
//        Thread.sleep(Long.MAX_VALUE);
//
//    }
}
