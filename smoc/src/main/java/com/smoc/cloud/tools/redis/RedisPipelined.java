package com.smoc.cloud.tools.redis;

import com.google.gson.Gson;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 高效批量存取
 */
public class RedisPipelined {

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 批量取hash数据
     *
     * @param key
     * @param keys hash key列表
     * @return
     */
    public List<Object> hashGetBatch(String key, List<String> keys) {
        List list = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                for (String keyValue : keys) {
                    connection.hGet(key.getBytes(), keyValue.getBytes());
                }
                return null;
            }
        });
        return list;
    }


    /**
     * set批量保存
     *
     * @param redisKey
     * @param list     要保存的数据，map、set也可以
     */
    public void setSaveBatch(String redisKey, List<String> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.sAdd(RedisSerializer.string().serialize(redisKey),
                        RedisSerializer.string().serialize(value));
            });
            connection.close();
            return null;
        });
    }

    /**
     * hash批量保存
     *
     * @param redisKey
     * @param map
     */
    public void hashSaveBatch(String redisKey, Map<String, Object> map) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            map.forEach((key, value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.hSet(RedisSerializer.string().serialize(redisKey),
                        RedisSerializer.string().serialize(key), RedisSerializer.string().serialize(new Gson().toJson(value)));
                // 设置过期时间 10天
                connection.expire(RedisSerializer.string().serialize(redisKey), TimeUnit.DAYS.toSeconds(1));
            });

            connection.close();
            return null;
        });
    }
}
