package com.gzk.gateway.core.bind;


import com.gzk.gateway.core.mapping.HttpStatement;
import com.gzk.gateway.core.session.GatewaySession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className: GenericReferenceRegistry
 * @description: 泛化调用注册 需要ApplicationConfig、RegistryConfig、ReferenceConfig
 * @author: gzk
 * @since: 2025/3/20
 **/
public class MapperRegistry {

    private final Configuration configuration;

    private Map<String, MapperProxyFactory> knownedMapperProxyFactory = new ConcurrentHashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public IGenericReference getMapper(String uri, GatewaySession gatewaySession) {
        MapperProxyFactory factory = knownedMapperProxyFactory.get(uri);
        if (factory == null) {
            throw new RuntimeException();
        }
        return factory.newInstance(gatewaySession);
    }

    /**
     * @param httpStatement
     */
    public void addMapperFactory(HttpStatement httpStatement) {
        String uri = httpStatement.getUri();

        if (knownedMapperProxyFactory.get(uri) != null) {
            throw new RuntimeException(uri + "已经注册过了");
        }
        knownedMapperProxyFactory.put(uri, new MapperProxyFactory(uri));
        configuration.addHttpStatement(httpStatement);
    }

}
