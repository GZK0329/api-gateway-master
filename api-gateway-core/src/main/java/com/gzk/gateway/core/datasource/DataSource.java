package com.gzk.gateway.core.datasource;

/**
 * @className: DataSource
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/25
 **/
public interface DataSource {
    Connection getConnection();
}
