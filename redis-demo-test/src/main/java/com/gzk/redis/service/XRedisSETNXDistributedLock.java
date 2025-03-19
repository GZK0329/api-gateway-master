package com.gzk.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;
import java.util.UUID;

/**
 * @className: XRedisDistributedLock
 * @description: SETNX 分布式锁
 * @author: gzk
 * @since: 2025/3/19
 **/
@Component
public class XRedisSETNXDistributedLock {

    public static final String LOCK_PREFIX = "redis_lock";
    private static final int LOCK_EXPIRE = 30;

    @Autowired
    private Jedis jedis;

    /**
     * 获取分布式锁 SETNX
     * @param lockKey 锁的名字（每个锁的名字应该唯一）
     * @return 如果获得锁，则返回锁的标识；否则返回null
     */
    public String acquireLock(String lockKey) {
        String lockValue = UUID.randomUUID().toString();
        SetParams setParams = new SetParams().nx().ex(LOCK_EXPIRE);  // NX 表示只有在键不存在时才设置值，EX 设置过期时间
        String result = jedis.set(LOCK_PREFIX + lockKey, lockValue, setParams);

        if ("OK".equals(result)) {
            // 获得锁
            return lockValue;
        }
        return null;
    }

    /**
     * 释放分布式锁 SETNX   lua脚本保证锁释放的原子性
     * @param lockKey 锁的名字
     * @param lockValue 锁的标识（每次加锁时返回的UUID）
     * @return 如果锁释放成功，返回true；否则返回false
     */
    public boolean releaseLock(String lockKey, String lockValue) {
        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "return redis.call('del', KEYS[1]) " +
                        "else " +
                        "return 0 end";

        Object result = jedis.eval(script, 1, LOCK_PREFIX + lockKey, lockValue);
        // 如果返回1，表示释放锁成功
        return result.equals(1L);
    }




}
