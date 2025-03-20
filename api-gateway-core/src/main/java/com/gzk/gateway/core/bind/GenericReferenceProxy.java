package com.gzk.gateway.core.bind;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.dubbo.rpc.service.GenericService;


import java.lang.reflect.Method;

/**
 * @className: GenericReferenceProxy
 * @description: 泛化调用代理
 * @author: gzk
 * @since: 2025/3/20
 **/
public class GenericReferenceProxy implements MethodInterceptor {

    private final String methodName;
    private final GenericService genericService;

    public GenericReferenceProxy(String methodName, GenericService genericService) {
        this.methodName = methodName;
        this.genericService = genericService;
    }

    /**
     * dubbo文档: https://dubbo.apache.org/zh/docsv2.7/user/examples/generic-reference/
     *
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameters = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = parameterTypes[i].getName();
        }

        return genericService.$invoke(methodName, parameters, objects);
    }
}
