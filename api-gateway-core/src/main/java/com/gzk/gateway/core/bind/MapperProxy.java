package com.gzk.gateway.core.bind;

import com.gzk.gateway.core.session.GatewaySession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

/**
 * @className: GenericReferenceProxy
 * @description: 泛化调用代理
 * @author: gzk
 * @since: 2025/3/20
 **/
public class MapperProxy implements MethodInterceptor {

    private final String uri;
    private final GatewaySession session;

    public MapperProxy(String uri, GatewaySession session) {
        this.uri = uri;
        this.session = session;
    }

    /**
     * 执行泛化调用，具体的接口的实现MapperMethod
     * dubbo文档: https://dubbo.apache.org/zh/docsv2.7/user/examples/generic-reference/
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) {
        MapperMethod mapperMethod = new MapperMethod(uri, session.getConfiguration());
        return mapperMethod.execute(session, args[0]);
    }
}
