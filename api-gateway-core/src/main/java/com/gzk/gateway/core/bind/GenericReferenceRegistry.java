package com.gzk.gateway.core.bind;


import com.gzk.gateway.core.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className: GenericReferenceRegistry
 * @description: 泛化调用注册 需要ApplicationConfig、RegistryConfig、ReferenceConfig
 * @author: gzk
 * @since: 2025/3/20
 **/
public class GenericReferenceRegistry {

    private final Configuration configuration;

    private Map<String, GenericReferenceFactory> knownedGenericReferenceFactory = new ConcurrentHashMap<>();

    public GenericReferenceRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public IGenericReference getGenericReference(String methodName) {
        GenericReferenceFactory genericReferenceFactory = knownedGenericReferenceFactory.get(methodName);
        if (genericReferenceFactory == null) {
            throw new RuntimeException();
        }
        return genericReferenceFactory.newInstance(methodName);
    }

    /**
     * 注册泛化调用服务接口方法
     *
     * @param application   服务：api-gateway-test
     * @param interfaceName 接口：
     * @param methodName    方法：sayHi 全局唯一
     */
    public void addGenericReference(String application, String interfaceName, String methodName) {

        // 获取基础服务（创建成本较高，内存存放获取）
        ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
        RegistryConfig registryConfig = configuration.getRegistryConfig(application);
        ReferenceConfig<GenericService> reference = configuration.getReferenceConfig(interfaceName);

        // 构建Dubbo服务
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(applicationConfig).registry(registryConfig).reference(reference).start();

        // 获取泛化调用服务
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(methodName);
        knownedGenericReferenceFactory.put(methodName, new GenericReferenceFactory(genericService));
    }

}
