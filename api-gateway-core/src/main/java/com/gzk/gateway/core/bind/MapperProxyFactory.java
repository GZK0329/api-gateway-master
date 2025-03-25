package com.gzk.gateway.core.bind;

import com.gzk.gateway.core.mapping.HttpStatement;
import com.gzk.gateway.core.session.GatewaySession;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className: GenericReferenceFactory
 * @description: 泛化调用代理工厂
 * @author: gzk
 * @since: 2025/3/20
 **/
public class MapperProxyFactory {

    private final String uri;

    private static final Map<String, IGenericReference> GenericReferenceProxyCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(String uri) {
        this.uri = uri;
    }

    /**
     * 创建泛化调用接口
     * @param gatewaySession
     * @return
     */
    public IGenericReference newInstance(GatewaySession gatewaySession) {
        return GenericReferenceProxyCache.computeIfAbsent(uri, k -> {

            HttpStatement httpStatement = gatewaySession.getConfiguration().getHttpStatement(uri);

            //构建代理对象
            MapperProxy genericReferenceProxy = new MapperProxy(uri, gatewaySession);

            //接口创建
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(new Signature(httpStatement.getMethodName(), Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
            Class<?> interfaceClass = interfaceMaker.create();

            // IGenericReference 统一泛化调用接口
            // interfaceClass    根据泛化调用注册信息创建的接口，建立 http -> rpc 关联
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(new Class[]{IGenericReference.class, interfaceClass});
            enhancer.setCallback(genericReferenceProxy);

            return (IGenericReference) enhancer.create();
        });
    }
}
