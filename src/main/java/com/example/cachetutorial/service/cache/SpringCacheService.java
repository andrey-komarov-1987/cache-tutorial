package com.example.cachetutorial.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpringCacheService {

    private final CacheManagerResolver resolver;

    public void put(String manager, String name, String key, Object value) {
        Cache cache = Objects.requireNonNull(resolver.resolveManager(manager).getCache(name));
        cache.put(key, value);
    }

    public Object putIfAbsent(String manager, String name, String key, Object value) {
        Cache cache = Objects.requireNonNull(resolver.resolveManager(manager).getCache(name));
        Cache.ValueWrapper result = cache. putIfAbsent(key, value);
        return Optional.ofNullable(result).map(Cache.ValueWrapper::get).orElse(null);
    }

    public String get(String manager, String name, String key) {
        Cache cache = Objects.requireNonNull(resolver.resolveManager(manager).getCache(name));
        return cache.get(key, String.class);
    }

    public void evict(String manager, String name, String key) {
        Cache cache = Objects.requireNonNull(resolver.resolveManager(manager).getCache(name));
        cache.evict(key);
    }

    public boolean evictIfPresent(String manager, String name, String key) {
        Cache cache = Objects.requireNonNull(resolver.resolveManager(manager).getCache(name));
        return cache.evictIfPresent(key);
    }
}
