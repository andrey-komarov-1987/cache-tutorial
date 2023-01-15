package com.example.cachetutorial.service.cache.redisson;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

@RequiredArgsConstructor
public abstract class AbstractRedissonCacheService<T extends RMap<Object, Object>> {

    protected final RedissonClient redissonClient;

    public Object put(String name, String key, Object value) {
        return getMap(name).put(key, value);
    }

    public Object get(String name, String key) {
        return getMap(name).get(key);
    }

    public Object remove(String name, String key) {
        return getMap(name).remove(key);
    }

    public long fastRemove(String name, String[] keys) {
        return getMap(name).fastRemove(keys);
    }

    protected abstract T getMap(String name);
}
