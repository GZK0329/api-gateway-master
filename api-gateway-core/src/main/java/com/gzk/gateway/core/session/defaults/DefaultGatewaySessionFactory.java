package com.gzk.gateway.core.session.defaults;

import com.gzk.gateway.core.bind.Configuration;
import com.gzk.gateway.core.datasource.DataSource;
import com.gzk.gateway.core.datasource.unpooled.UnpooledDataSourceFactory;
import com.gzk.gateway.core.session.GatewaySession;
import com.gzk.gateway.core.session.GatewaySessionFactory;

/**
 * @className: DefaultGatewaySessionFactory
 * @description: 会话工厂
 * @author: gzk
 * @since: 2025/3/20
 **/
public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession(String uri) {
        UnpooledDataSourceFactory factory = new UnpooledDataSourceFactory();
        factory.setProperties(configuration, uri);
        DataSource dataSource = factory.getDataSource();

        return new DefaultGatewaySession(configuration, uri, dataSource);
    }
}
