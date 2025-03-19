package com.gzk.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.GeoCoordinate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class XRedisServiceTest {

    @Resource
    private XRedisHelper xRedisHelper;

    public String getRedisKey() {
        return "{redis_key}_" + UUID.randomUUID().toString();
    }

    @Test
    public void testRedisString() {
        String redisKey = getRedisKey();

        log.info("测试String数据类型，key={}",redisKey);
        xRedisHelper.setString(redisKey, "hello world");
        Assert.assertEquals("hello world", xRedisHelper.getString(redisKey));
//        xRedisHelper.del(redisKey);
    }


    @Test
    public void testRedisList() {
        String redisKey = getRedisKey();
        log.info("测试List数据类型，key={}",redisKey);
        xRedisHelper.setList(redisKey, "hello world1", "hello world2", "hello world2");
        Assert.assertEquals(3, xRedisHelper.getList(redisKey).size());
//        xRedisHelper.del(redisKey);
    }

    @Test
    public void testRedisSet() {
        String redisKey = getRedisKey();
        log.info("测试Set数据类型，key={}",redisKey);
        xRedisHelper.setSet(redisKey, "hello world1", "hello world2", "hello world1");
        Assert.assertEquals(2, xRedisHelper.getSet(redisKey).size());
//        xRedisHelper.del(redisKey);
    }

    @Test
    public void testRedisHash() {
        String redisKey = getRedisKey();
        log.info("测试Hash数据类型，key={}",redisKey);
        Map<String, String> map = new HashMap<>();
        map.put("key1", "hello world1");
        map.put("key2", "hello world2");
        map.put("key3", "hello world3");
        xRedisHelper.setHash(redisKey, map);
        Assert.assertEquals(3, xRedisHelper.getHash(redisKey).entrySet().size());
//        xRedisHelper.del(redisKey);
    }

    @Test
    public void testRedisSortedSet() {
        String redisKey = getRedisKey();
        log.info("测试SortedSet数据类型，key={}",redisKey);
        xRedisHelper.setSortedSet(redisKey, 1, "hello world1");
        xRedisHelper.setSortedSet(redisKey, 3, "hello world3");
        xRedisHelper.setSortedSet(redisKey, 2, "hello world2");
        Assert.assertEquals(3, xRedisHelper.getSortedSet(redisKey).size());
        Assert.assertEquals(2, xRedisHelper.getSortedSetByScore(redisKey, 1,2).size());
//        xRedisHelper.del(redisKey);
    }


    @Test
    public void testRedisBitmap() {
        String redisKey = getRedisKey();
        log.info("测试Bitmap数据类型，key={}",redisKey);
        xRedisHelper.setBit(redisKey, 3, true);
        xRedisHelper.setBit(redisKey, 5, false);
        Assert.assertEquals(true, xRedisHelper.getBit(redisKey, 3));
        Assert.assertEquals(false, xRedisHelper.getBit(redisKey, 5));
        Assert.assertEquals(1, xRedisHelper.bitCount(redisKey));
//        xRedisHelper.del(redisKey);
    }

    @Test
    public void testRedisHyperLogLog() {
        String redisKey1 = getRedisKey();
        log.info("测试HyperLogLog数据类型，key1={}",redisKey1);
        xRedisHelper.addHyperLogLog(redisKey1, "user1", "user2", "user3");
        String redisKey2 = getRedisKey();
        log.info("测试HyperLogLog数据类型，key2={}",redisKey2);
        xRedisHelper.addHyperLogLog(redisKey2, "user4", "user5", "user6");

        Assert.assertEquals(3, xRedisHelper.countHyperLogLog(redisKey1));
        String redisKey3 = getRedisKey();
        log.info("测试HyperLogLog数据类型，key3={}",redisKey3);
        xRedisHelper.mergeHyperLogLog(redisKey3, redisKey1, redisKey2);
        Assert.assertEquals(6, xRedisHelper.countHyperLogLog(redisKey3));
//        xRedisHelper.del(redisKey);
    }


    @Test
    public void testRedisGeo() {
        String redisKey = getRedisKey();
        log.info("测试Geo数据类型，key1={}",redisKey);
        xRedisHelper.addGeo(redisKey, 100.111, 90.222, "beijing");
        xRedisHelper.addGeo(redisKey, 90.222,100.111, "shanghai");

        Double dist = xRedisHelper.geoDistKM(redisKey, "beijing", "shanghai");
        System.out.println(dist);

        GeoCoordinate beijing = xRedisHelper.getGeo(redisKey, "beijing");
        System.out.println(beijing);
//        xRedisHelper.del(redisKey);
    }
}