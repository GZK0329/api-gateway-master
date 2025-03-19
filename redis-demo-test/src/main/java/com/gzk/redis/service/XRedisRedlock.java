//package com.gzk.redis.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.*;
//import redis.clients.jedis.params.SetParams;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
///**
// * @className: XRedisRedlock
// * @description: 1、获取当前时间 startTime。
// * 2、尝试获取多个 Redis 实例上的锁。每次获取一个实例上的锁，都会有一个时间限制（TTL）。
// * 3、如果获取的锁数目大于或等于 N / 2 + 1（假设你有 N 个 Redis 实例），则认为成功获得锁。如果没有足够的锁，释放已经获得的锁并返回失败。
// * 4、如果在锁定期间超过了锁的最大生存时间（TTL），则认为锁获取失败。
// * @author: gzk
// * @since: 2025/3/19
// **/
//
//@Component
//public class XRedisRedlock {
//
//    private static final int LOCK_EXPIRY_TIME = 10000; // 锁过期时间 10 秒
//    private static final int RETRY_TIME = 100; // 每次尝试之间的间隔时间 100 毫秒
//
//    @Autowired
//    private JedisCluster jedisCluster;
//
//    public boolean acquireLock(String lockKey, String lockValue) {
//        long startTime = System.currentTimeMillis();
//        Set<String> acquiredLocks = new HashSet<>();
//        boolean lockAcquired = false;
//
//        while (System.currentTimeMillis() - startTime < LOCK_EXPIRY_TIME) {
//            // 遍历集群中的所有节点
//
//            for (Map.Entry<String, ConnectionPool> entry : jedisCluster.getClusterNodes().entrySet()) {
//                // NX 是确保只有没有锁的情况下才能设置，PX 是设置过期时间
//                SetParams params = new SetParams().nx().px(LOCK_EXPIRY_TIME);
//                Connection connection = entry.getValue().getResource();
//                Jedis jedis = new Jedis(connection);
//                String result = jedis.set(lockKey, lockValue, params);
//                if ("OK".equals(result)) {
//                    acquiredLocks.add(entry.getKey());
//                }
//            }
//
//            // 如果成功获取了超过一半的锁，表示获取锁成功
//            if (acquiredLocks.size() > jedisCluster.getClusterNodes().size() / 2) {
//                lockAcquired = true;
//                break;
//            }
//
//            // 如果没有成功获取锁，释放已经获取的锁
//            if (acquiredLocks.size() > 0) {
//                for (String node : acquiredLocks) {
//                    try (Jedis jedis = new Jedis(jedisCluster.getClusterNodes().get(node).getResource())) {
//                        jedis.del(lockKey);
//                    }
//                }
//            }
//
//            // 如果没有获取足够的锁，休眠并重试
//            try {
//                Thread.sleep(RETRY_TIME);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return lockAcquired;
//    }
//
//    public void releaseLock(String lockKey, String lockValue) {
//        // 遍历所有节点，删除锁
//        for (Map.Entry<String, ConnectionPool> entry : jedisCluster.getClusterNodes().entrySet()) {
//            Jedis jedis = new Jedis(entry.getValue().getResource());
//            String value = jedis.get(lockKey);
//            if (lockValue.equals(value)) {
//                jedis.del(lockKey);
//                break;
//            }
//        }
//    }
//}
