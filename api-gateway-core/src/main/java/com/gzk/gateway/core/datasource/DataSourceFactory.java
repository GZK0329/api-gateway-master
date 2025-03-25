package com.gzk.gateway.core.datasource;

import com.gzk.gateway.core.bind.Configuration;

public interface DataSourceFactory {

    void setProperties(Configuration configuration, String uri);

    DataSource getDataSource();
}
