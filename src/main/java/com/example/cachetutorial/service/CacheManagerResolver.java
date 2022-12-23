package com.example.cachetutorial.service;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import lombok.RequiredArgsConstructor;
import org.redisson.jcache.JCachingProvider;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.cache.spi.CachingProvider;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CacheManagerResolver {

    public static final String CAFFEINE = "caffeine";
    public static final String REDIS = "redis";

    private final CaffeineCacheManager caffeineCacheManager;
    private final CaffeineCachingProvider caffeineCachingProvider;

    private final RedissonSpringCacheManager redisCacheManager;
    private final JCachingProvider jCachingProvider;

    private final Map<String, CacheManager> managers = new HashMap<>();
    private final Map<String, CachingProvider> providers = new HashMap<>();

    @PostConstruct
    private void init() {
        managers.put(CAFFEINE, caffeineCacheManager);
        managers.put(REDIS, redisCacheManager);

        providers.put(CAFFEINE, caffeineCachingProvider);
        providers.put(REDIS, jCachingProvider);
    }

    public CacheManager resolveManager(String name) {
        CacheManager result = managers.get(name);
        if (result == null) {
            throw new IllegalArgumentException("Manager " + name + " not found");
        }
        return result;
    }

    public CachingProvider resolveProvider(String name) {
        CachingProvider result = providers.get(name);
        if (result == null) {
            throw new IllegalArgumentException("Provider " + name + " not found");
        }
        return result;
    }
}
