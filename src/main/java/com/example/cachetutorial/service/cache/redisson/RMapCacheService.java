package com.example.cachetutorial.service.cache.redisson;

import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RMapCacheService extends AbstractRedissonCacheService<RMap<Object, Object>> {

    public RMapCacheService(RedissonClient redissonClient) {
        super(redissonClient);
    }

    public Object put(String name, String key, Object value, long ttl) {
        return getMap(name).put(key, value, ttl, TimeUnit.SECONDS);
    }

    @Override
    protected RMapCache<Object, Object> getMap(String name) {
        return redissonClient.getMapCache(name);
    }
}
