package com.gzk.gateway.core.datasource.unpooled;

import com.gzk.gateway.core.bind.Configuration;
import com.gzk.gateway.core.datasource.Connection;
import com.gzk.gateway.core.datasource.DataSource;
import com.gzk.gateway.core.datasource.DataSourceType;
import com.gzk.gateway.core.datasource.connection.DubboConnection;
import com.gzk.gateway.core.mapping.HttpStatement;
import lombok.Data;

/**
 * @className: UnpooledDataSource
 * @description: 无池化数据源
 * @author: gzk
 * @since: 2025/3/25
 **/
@Data
public class UnpooledDataSource implements DataSource {

    protected DataSourceType dataSourceType;
    protected Configuration configuration;
    protected HttpStatement httpStatement;

    @Override
    public Connection getConnection() {
        switch (dataSourceType) {
            case HTTP:
                break;
            case DUBBO:
                String applicationName = httpStatement.getApplicationName();
                String interfaceName = httpStatement.getInterfaceName();
                return new DubboConnection(configuration.getApplicationConfig(applicationName), configuration.getRegistryConfig(applicationName), configuration.getReferenceConfig(interfaceName));
            default:
                break;
        }
        throw new RuntimeException("unknown data source type");
    }
}
