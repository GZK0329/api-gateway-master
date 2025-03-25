package com.gzk.gateway.core.session.defaults;

import com.gzk.gateway.core.bind.Configuration;
import com.gzk.gateway.core.bind.IGenericReference;
import com.gzk.gateway.core.datasource.Connection;
import com.gzk.gateway.core.datasource.DataSource;
import com.gzk.gateway.core.session.GatewaySession;
import lombok.Data;

/**
 * @className: DefaultGatewaySession
 * @description: 会话，获取映射器对象，执行泛化调用
 * @author: gzk
 * @since: 2025/3/21
 **/

@Data
public class DefaultGatewaySession implements GatewaySession {

    private final Configuration configuration;
    private final String uri;
    private final DataSource dataSource;

    public DefaultGatewaySession(Configuration configuration, String uri, DataSource dataSource) {
        this.configuration = configuration;
        this.uri = uri;
        this.dataSource = dataSource;
    }

    @Override
    public Object doGet(String methodName, Object parameter) {

        Connection connection = dataSource.getConnection();
        return connection.execute(methodName, new String[]{"java.lang.String"}, new String[]{"name"}, new Object[]{parameter});
    }

    @Override
    public IGenericReference getMapper(String uri) {
        return configuration.getMapper(uri, this);
    }
}
