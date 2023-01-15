package com.example.cachetutorial.service.cache.redisson;

import com.example.cachetutorial.dto.LocalCachedMapOptionsDto;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class RLocalCachedMapService extends AbstractRedissonCacheService<RLocalCachedMap<Object, Object>> {

    protected final Map<String, RLocalCachedMap<Object, Object>> cashedMaps = new ConcurrentHashMap<>(16);

    private final ReadWriteLock optionsLock = new ReentrantReadWriteLock();

    private final LocalCachedMapOptions<Object, Object> localCachedMapOptions = LocalCachedMapOptions.defaults();

    public RLocalCachedMapService(RedissonClient redissonClient) {
        super(redissonClient);
    }

    public LocalCachedMapOptionsDto getOptions() {
        Lock lock = optionsLock.readLock();
        lock.lock();
        try {
            return toDto(localCachedMapOptions);
        } finally {
            lock.unlock();
        }
    }

    public LocalCachedMapOptionsDto updateOptions(LocalCachedMapOptionsDto dto) {
        Lock lock = optionsLock.writeLock();
        lock.lock();
        try {
            if (dto.getReconnectionStrategy() != null) {
                localCachedMapOptions.reconnectionStrategy(dto.getReconnectionStrategy());
            }
            if (dto.getSyncStrategy() != null) {
                localCachedMapOptions.syncStrategy(dto.getSyncStrategy());
            }
            if (dto.getEvictionPolicy() != null) {
                localCachedMapOptions.evictionPolicy(dto.getEvictionPolicy());
            }
            if (dto.getCacheSize() != null) {
                localCachedMapOptions.cacheSize(dto.getCacheSize());
            }
            if (dto.getTimeToLive() != null) {
                localCachedMapOptions.timeToLive(dto.getTimeToLive(), TimeUnit.SECONDS);
            }
            if (dto.getMaxIdle() != null) {
                localCachedMapOptions.maxIdle(dto.getMaxIdle(), TimeUnit.SECONDS);
            }
            if (dto.getCacheProvider() != null) {
                localCachedMapOptions.cacheProvider(dto.getCacheProvider());
            }
            if (dto.getStoreMode() != null) {
                localCachedMapOptions.storeMode(dto.getStoreMode());
            }
            if (dto.getStoreCacheMiss() != null) {
                localCachedMapOptions.storeCacheMiss(dto.getStoreCacheMiss());
            }
            return toDto(localCachedMapOptions);
        } finally {
            lock.unlock();
        }
    }

    public void preloadCache(String name) {
        getMap(name).preloadCache();
    }

    public Map<Object, Object> getLocalCache(String name) {
        return Collections.unmodifiableMap(getMap(name).getCachedMap());
    }

    public void clearLocalCache(String name) {
        getMap(name).clearLocalCache();
    }

    @Override
    protected RLocalCachedMap<Object, Object> getMap(String name) {
        return cashedMaps.computeIfAbsent(name, k -> {
            Lock lock = optionsLock.readLock();
            lock.lock();
            try {
                return redissonClient.getLocalCachedMap(name, localCachedMapOptions);
            } finally {
                lock.unlock();
            }
        });
    }

    private LocalCachedMapOptionsDto toDto(LocalCachedMapOptions<Object, Object> localCachedMapOptions) {
        return new LocalCachedMapOptionsDto()
                .setReconnectionStrategy(localCachedMapOptions.getReconnectionStrategy())
                .setSyncStrategy(this.localCachedMapOptions.getSyncStrategy())
                .setEvictionPolicy(this.localCachedMapOptions.getEvictionPolicy())
                .setCacheSize(this.localCachedMapOptions.getCacheSize())
                .setTimeToLive(TimeUnit.MILLISECONDS.toSeconds(this.localCachedMapOptions.getTimeToLiveInMillis()))
                .setMaxIdle(TimeUnit.MILLISECONDS.toSeconds(this.localCachedMapOptions.getMaxIdleInMillis()))
                .setCacheProvider(this.localCachedMapOptions.getCacheProvider())
                .setStoreMode(this.localCachedMapOptions.getStoreMode())
                .setStoreCacheMiss(this.localCachedMapOptions.isStoreCacheMiss());
    }
}
