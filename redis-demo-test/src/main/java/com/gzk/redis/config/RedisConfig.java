package com.gzk.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @className: RedisConfig
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/18
 **/

@Configuration
public class RedisConfig {

    @Resource
    private XRedisConfig xRedisConfig;

    //单点模式
    @Bean
    public Jedis jedis(){
        Jedis jedis = new Jedis(xRedisConfig.getHost(), xRedisConfig.getPort());
        jedis.select(0);
        return jedis;
    }


    //主从哨兵模式
//    @Bean
//    public Jedis jedis(){
//
//        Set<String> sentinels = new HashSet<>(Arrays.asList(
//                "127.0.0.1:26360",
//                "127.0.0.1:26361",
//                "127.0.0.1:26362"
//        ));
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(5);
//        JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels, jedisPoolConfig);
//        Jedis jedis = pool.getResource();
//        jedis.select(0);
//        return jedis;
//    }

    //集群模式
//    @Bean
//    public JedisCluster jedisCluster() {
//        Set<HostAndPort> clusterNodes = new HashSet<>();
//        clusterNodes.add(new HostAndPort("127.0.0.1", 6359));
//        clusterNodes.add(new HostAndPort("127.0.0.1", 6360));
//        clusterNodes.add(new HostAndPort("127.0.0.1", 6361));
//        clusterNodes.add(new HostAndPort("127.0.0.1", 6362));
//        clusterNodes.add(new HostAndPort("127.0.0.1", 6363));
//        clusterNodes.add(new HostAndPort("127.0.0.1", 6364));
//        // 配置连接池
//        GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<>();
//        config.setMaxTotal(128);  // 最大连接数
//        config.setMaxIdle(64);    // 最大空闲连接数
//        config.setMinIdle(16);    // 最小空闲连接数
//        config.setTestOnBorrow(true);
//
//        // 设置连接超时和读取超时
//        int connectionTimeout = 2000; // 连接超时 (毫秒)
//        int soTimeout = 2000; // 读取超时 (毫秒)
//
//        // 创建并返回 JedisCluster 实例
//        JedisCluster jedisCluster = new JedisCluster(clusterNodes, connectionTimeout, soTimeout, config);
//        return jedisCluster;
//    }


}
