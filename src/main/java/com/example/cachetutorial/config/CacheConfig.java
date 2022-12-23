package com.example.cachetutorial.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.jcache.JCachingProvider;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    private static final TimeUnit EXPIRE_TIME_UNIT = TimeUnit.SECONDS;
    private static final long EXPIRE_DURATION = 30;

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(EXPIRE_DURATION, EXPIRE_TIME_UNIT)
                .evictionListener(((key, value, cause) -> log.info("evict() key = {}, value = {}, cause = {}", key, value, cause)));
    }

    @Primary
    @Bean
    public CaffeineCacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

    @Bean
    public CaffeineCachingProvider caffeineCachingProvider() {
        return new CaffeineCachingProvider();
    }


    @Bean(destroyMethod="shutdown")
    RedissonClient redissonClient(@Value("classpath:/redisson-jcache.yaml") Resource configFile) throws IOException {
        try (InputStream inputStream = configFile.getInputStream()) {
            return Redisson.create(Config.fromYAML(inputStream));
        }
    }

    @Bean
    RedissonSpringCacheManager redisCacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient) {

            @Override
            protected org.redisson.spring.cache.CacheConfig createDefaultConfig() {
                long millis = EXPIRE_TIME_UNIT.toMillis(EXPIRE_DURATION);
                return new org.redisson.spring.cache.CacheConfig(millis, millis);
            }
        };
    }

    @Bean
    JCachingProvider jCachingProvider() {
        return new JCachingProvider();
    }
}
