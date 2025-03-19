package com.gzk.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * @className: XRedisPublisher
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/19
 **/
@Component
public class XRedisPublisher {

    @Autowired
    private Jedis jedis;

    public void publish(String channel, String message) {
        jedis.publish(channel, message);
    }
}
