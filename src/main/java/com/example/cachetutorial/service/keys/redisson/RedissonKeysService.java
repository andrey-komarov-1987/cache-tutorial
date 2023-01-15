package com.example.cachetutorial.service.keys.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedissonKeysService {

    private final RedissonClient redissonClient;

    public List<String> getAll(String prefix) {
        RKeys rKeys = redissonClient.getKeys();
        Iterable<String> iterable = rKeys.getKeysByPattern(keyName(prefix, "*"));
        List<String> result = new ArrayList<>();
        iterable.forEach(result::add);
        return Collections.unmodifiableList(result);
    }

    public boolean set(String prefix, String name, Object value, long ttl, boolean ifExists) {
        RBucket<Object> rBucket = redissonClient.getBucket(keyName(prefix, name));
        boolean result = true;
        if (ifExists) {
            result = rBucket.setIfExists(value, ttl, TimeUnit.SECONDS);
        } else {
            rBucket.set(value, ttl, TimeUnit.SECONDS);
        }
        return result;
    }

    public Object get(String prefix, String name, boolean remove) {
        RBucket<Object> rBucket = redissonClient.getBucket(keyName(prefix, name));
        return remove ? rBucket.getAndDelete() : rBucket.get();
    }

    public boolean exists(String prefix, String name) {
        RBucket<Object> rBucket = redissonClient.getBucket(keyName(prefix, name));
        return rBucket.isExists();
    }

    public boolean delete(String prefix, String name) {
        RBucket<Object> rBucket = redissonClient.getBucket(keyName(prefix, name));
        return rBucket.delete();
    }

    private String keyName(String prefix, String name) {
        return prefix + ":" + name;
    }
}
