package com.gzk.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

/**
 * @className: XRedisSubscriber
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/19
 **/
@Component
@Slf4j
public class XRedisSubscriber {

    @Autowired
    private Jedis jedis;

    public void subscribe(String channel) {
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                log.info("接收消息: " + message + "来自于channel: " + channel);
            }
        };

        // 订阅频道
        log.info("开始订阅channel: " + channel);
        jedis.subscribe(jedisPubSub, channel);
    }
}
