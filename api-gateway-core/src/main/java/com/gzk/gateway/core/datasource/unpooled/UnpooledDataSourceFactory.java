package com.gzk.gateway.core.datasource.unpooled;

import com.gzk.gateway.core.bind.Configuration;
import com.gzk.gateway.core.datasource.DataSource;
import com.gzk.gateway.core.datasource.DataSourceFactory;
import com.gzk.gateway.core.datasource.DataSourceType;

/**
 * @className: UnpooledDataSourceFactory
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/25
 **/
public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected UnpooledDataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Configuration configuration, String uri) {
        this.dataSource.setConfiguration(configuration);
        this.dataSource.setDataSourceType(DataSourceType.DUBBO);
        this.dataSource.setHttpStatement(configuration.getHttpStatement(uri));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
