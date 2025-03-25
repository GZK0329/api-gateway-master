package com.gzk.gateway.core.datasource.connection;

import com.gzk.gateway.core.datasource.Connection;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * @className: DubboConnection
 * @description: dubbo连接管理
 * @author: gzk
 * @since: 2025/3/25
 **/
public class DubboConnection implements Connection {

    protected final GenericService genericService;

    public DubboConnection(ApplicationConfig applicationConfig, RegistryConfig registryConfig, ReferenceConfig<GenericService> reference) {
        DubboBootstrap instance = DubboBootstrap.getInstance();
        instance.application(applicationConfig).registry(registryConfig).reference(reference).start();
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        this.genericService = cache.get(reference);
    }

    /**
     * 泛化调用执行
     * @param method
     * @param parameterTypes
     * @param parameterNames
     * @param args
     * @return
     */
    @Override
    public Object execute(String method, String[] parameterTypes, String[] parameterNames, Object[] args) {
        return genericService.$invoke(method, parameterTypes, args);
    }
}
