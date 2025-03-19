package com.gzk.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.args.GeoUnit;

import javax.annotation.Resource;
import javax.management.monitor.StringMonitor;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @className: XRedisHelper
 * @description: TODO
 * @author: gzk
 * @since: 2025/3/18
 **/
@Component
public class XRedisHelper {

    @Resource
    private Jedis jedis;

//    @Autowired
//    private JedisCluster jedis;

    public boolean setString(String key, String value) {
        jedis.set(key, value);
        return true;
    }

    public String getString(String key) {
        return jedis.get(key);
    }


    // List 类型
    public boolean setList(String key, String... values) {
        jedis.lpush(key, values);  // 从左侧插入值
        return true;
    }

    public List<String> getList(String key) {
        return jedis.lrange(key, 0, -1);  // 获取整个列表
    }

    // Set 类型
    public boolean setSet(String key, String... values) {
        jedis.sadd(key, values);  // 向 Set 中添加元素
        return true;
    }

    public Set<String> getSet(String key) {
        return jedis.smembers(key);  // 获取 Set 中所有的元素
    }

    // Hash 类型
    public boolean setHash(String key, String field, String value) {
        jedis.hset(key, field, value);  // 设置 Hash 的字段值
        return true;
    }

    public String getHash(String key, String field) {
        return jedis.hget(key, field);  // 获取 Hash 中指定字段的值
    }

    public boolean setHash(String key, Map<String, String> values) {
        jedis.hmset(key, values);  // 批量设置 Hash 中的字段
        return true;
    }

    public Map<String, String> getHash(String key) {
        return jedis.hgetAll(key);  // 获取 Hash 中所有的字段和值
    }

    // Sorted Set 类型
    public boolean setSortedSet(String key, double score, String member) {
        jedis.zadd(key, score, member);  // 向 Sorted Set 中添加元素，并指定分数
        return true;
    }

    public List<String> getSortedSet(String key) {
        return jedis.zrange(key, 0, -1);  // 获取 Sorted Set 中按分数排序后的所有元素
    }

    public List<String> getSortedSetByScore(String key, double minScore, double maxScore) {
        return jedis.zrangeByScore(key, minScore, maxScore);  // 获取指定分数范围的元素
    }

    // 设置 Bitmap 指定位的值（0 或 1）
    public boolean setBit(String key, long offset, boolean value) {
        jedis.setbit(key, offset, value);
        return true;
    }

    // 获取 Bitmap 指定位的值
    public boolean getBit(String key, long offset) {
        return jedis.getbit(key, offset);
    }

    // 统计 Bitmap 中值为 1 的位数（用于统计活跃用户数等）
    public long bitCount(String key) {
        return jedis.bitcount(key);
    }

    // 添加元素到 HyperLogLog
    public boolean addHyperLogLog(String key, String... values) {
        jedis.pfadd(key, values);
        return true;
    }

    // 获取 HyperLogLog 统计的基数（近似去重计数）
    public long countHyperLogLog(String key) {
        return jedis.pfcount(key);
    }

    // 合并多个 HyperLogLog 计数
    public boolean mergeHyperLogLog(String destKey, String... sourceKeys) {
        jedis.pfmerge(destKey, sourceKeys);
        return true;
    }

    // 添加地理位置信息（key: 地图名, longitude: 经度, latitude: 纬度, member: 地点名称）
    public boolean addGeo(String key, double longitude, double latitude, String member) {
        jedis.geoadd(key, longitude, latitude, member);
        return true;
    }

    // 获取某个位置的经纬度（key: 地图名, longitude: 经度, latitude: 纬度, member: 地点名称）
    public GeoCoordinate getGeo(String key, String member) {
        List<GeoCoordinate> positions = jedis.geopos(key, member);
        return positions.get(0);
    }

    // 计算两个地点之间的距离
    public Double geoDistKM(String key, String member1, String member2) {
        return jedis.geodist(key, member1, member2, GeoUnit.KM);
    }

    public void del(String key) {
        jedis.del(key);
    }
}
