package com.gzk.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @className: XRedisAdvanceFeatureTest
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/19
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class XRedisAdvanceFeatureTest {

    @Resource
    private XRedisPublisher xRedisPublisher;

    @Resource
    private XRedisSubscriber xRedisSubscriber;

    @Resource
    private XRedisHelper xRedisHelper;

    @Resource
    private XRedisSETNXDistributedLock xRedisSETNXDistributedLock;

//    @Resource
//    private XRedisRedlock xRedisRedlock;

    @Autowired
    private Jedis jedis;

    public String getRedisKey() {
        return "{redis_key}_" + UUID.randomUUID().toString();
    }

    public String getRedisKeyNotFixedSlot() {
        return "redis_key_" + UUID.randomUUID().toString();
    }

    @Test
    public void testPubAndSub() throws InterruptedException {
        String redisKey = getRedisKey();
        log.info("测试发布订阅，生成channel:{}", redisKey);
        new Thread(() -> {
            xRedisSubscriber.subscribe(redisKey);
        }).start();

        new Thread(() -> {
            xRedisSubscriber.subscribe(redisKey);
        }).start();

        new Thread(() -> {
            xRedisSubscriber.subscribe(redisKey);
        }).start();

        Thread.sleep(3000);

        String message = "测试消息123";
        xRedisPublisher.publish(redisKey, message);
    }

    /**
     * 测试SETNX分布式锁
     * @throws InterruptedException
     */
    @Test
    public void testSETNXDistributedLock() throws InterruptedException {
        String redisKey = getRedisKey();
        Thread thread1 = new Thread(() -> {
            String value = null;
            try{
                value = xRedisSETNXDistributedLock.acquireLock(redisKey);
                if(value != null){
                    log.info("thread 1 获取锁");
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(value != null){
                    log.info("thread 1 释放锁");
                    xRedisSETNXDistributedLock.releaseLock(redisKey, value);
                }
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            String value = null;
            try{
                value = xRedisSETNXDistributedLock.acquireLock(redisKey);
                if(value != null){
                    log.info("thread 2 获取锁");
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(value != null){
                    log.info("thread 2 释放锁");
                    xRedisSETNXDistributedLock.releaseLock(redisKey, value);
                }
            }
        });
        thread2.start();

        thread1.join();
        thread2.join();
    }

    /**
     * 测试RedLock分布式锁 TODO有点问题，可以使用Redisson来实现
     * @throws InterruptedException
     */
//    @Test
//    public void testRedLock() throws InterruptedException {
//        String redisKey = getRedisKeyNotFixedSlot();
//        String lockValue = UUID.randomUUID().toString();
//        Thread thread1 = new Thread(() -> {
//            Boolean value = false;
//            try{
//                value = xRedisRedlock.acquireLock(redisKey, lockValue);
//                if(value){
//                    log.info("thread 1 获取锁");
//                    Thread.sleep(5000);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                if(value){
//                    log.info("thread 1 释放锁");
//                    xRedisRedlock.releaseLock(redisKey, lockValue);
//                }
//            }
//        });
//        thread1.start();
//
//        Thread thread2 = new Thread(() -> {
//            Boolean value = false;
//            try{
//                value = xRedisRedlock.acquireLock(redisKey, lockValue);
//                if(value){
//                    log.info("thread 2 获取锁");
//                    Thread.sleep(5000);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                if(value){
//                    log.info("thread 2 释放锁");
//                    xRedisRedlock.releaseLock(redisKey, lockValue);
//                }
//            }
//        });
//        thread2.start();
//
//        thread1.join();
//        thread2.join();
//    }


    /**
     * 测试管道批量执行
     * @throws InterruptedException
     */
    @Test
    public void testPipeline() {
        Pipeline pipeline = jedis.pipelined();
        String redisKey = getRedisKey();
        for (int i = 0; i < 2; i++) {
            pipeline.setbit(redisKey, i, true);
        }

        pipeline.sync();

        Assert.assertEquals(2, xRedisHelper.bitCount(redisKey));
    }


    /**
     * 测试multi 和 exec 事务机制
     */
    @Test
    public void testMultiExec() {
        // 开始事务
        Transaction transaction = jedis.multi();

        // 向事务中添加命令
        String redisKey1 = getRedisKey();
        transaction.set(redisKey1, "testValue1");
        String redisKey2 = getRedisKey();
        transaction.set(redisKey2, "testValue2");

        // 提交事务
        transaction.exec();

        // 验证结果
        Assert.assertEquals("testValue1", jedis.get(redisKey1));
        Assert.assertEquals("testValue2", jedis.get(redisKey2));
    }


    /**
     * 测试watch 监视某个key，当那个key发生改变时，终止事务
     */
    @Test(expected = IllegalStateException.class)
    public void testWatch() {
        String redisKey1 = getRedisKey();
        String redisKey2 = getRedisKey();
        jedis.set(redisKey1, "initialValue");

        // 使用 WATCH
        jedis.watch(redisKey1);

        // 启动事务
        Transaction transaction = jedis.multi();

        // 向事务中添加命令
        transaction.set(redisKey1, "testValue1");
        transaction.set(redisKey2, "testValue2");

        // 在事务执行前，修改监视的键 key1 的值
        jedis.set(redisKey1, "modifiedValue");

        // 提交事务
        // 事务将返回 null，因为 key1 的值已经被修改
        List<Object> result = transaction.exec();

        // 验证事务未被执行
        Assert.assertNull(result);
        Assert.assertEquals("modifiedValue", jedis.get(redisKey1));
        Assert.assertNull(jedis.get(redisKey2));
    }

    @Test
    public void testDiscard() {
        String redisKey1 = getRedisKey();
        String redisKey2 = getRedisKey();
        // 设置初始值
        jedis.set(redisKey1, "initialValue");

        // 开始事务
        Transaction transaction = jedis.multi();

        // 向事务中添加命令
        transaction.set(redisKey1, "newValue");
        transaction.set(redisKey2, "value2");

        // 使用 DISCARD 放弃事务
        transaction.discard();

        // 验证事务是否被丢弃
        Assert.assertEquals("initialValue", jedis.get(redisKey1));
        Assert.assertNull(jedis.get(redisKey2));
    }
}
