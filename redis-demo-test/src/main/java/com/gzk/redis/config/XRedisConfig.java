package com.gzk.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @className: XRedisConfig
 * @description: redis配置类
 * @author: gzk
 * @since: 2025/3/18
 **/
@ConfigurationProperties("redis")
public class XRedisConfig {

    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
