package com.example.cachetutorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class Jsr107CacheService {

    private final CacheManagerResolver resolver;

    private final Map<String, Cache<Object, Object>> cashes = new ConcurrentHashMap<>(16);
    
    public void put(String provider, String name, String key, Object value) {
        Cache<Object, Object> cache = getCache(provider, name);
        cache.put(key, value);
    }

    public Object get(String provider, String name, String key) {
        Cache<Object, Object> cache = getCache(provider, name);
        return cache.get(key);
    }

    public boolean remove(String provider, String name, String key) {
        Cache<Object, Object> cache = getCache(provider, name);
        return cache.remove(key);
    }

    public Object getAndRemove(String provider, String name, String key) {
        Cache<Object, Object> cache = getCache(provider, name);
        return cache.getAndRemove(key);
    }

    private Cache<Object, Object> getCache(String provider, String name) {
        String cacheKey = provider + ":" + name;
        return cashes.computeIfAbsent(cacheKey,
                k -> resolver.resolveProvider(provider).getCacheManager().createCache(name, configuration()));
    }

    private MutableConfiguration<Object, Object> configuration() {
        Factory<ExpiryPolicy> expiryPolicyFactory = CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 30));
        MutableConfiguration<Object, Object> result = new MutableConfiguration<>();
        result.setExpiryPolicyFactory(expiryPolicyFactory);
        return result;
    }
}
