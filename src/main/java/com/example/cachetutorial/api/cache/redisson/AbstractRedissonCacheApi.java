package com.example.cachetutorial.api.cache.redisson;

import com.example.cachetutorial.service.cache.redisson.AbstractRedissonCacheService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRedissonCacheApi<T extends AbstractRedissonCacheService<?>> {

    protected final T redissonCacheService;

    @GetMapping("/{cache}/{key}")
    @SneakyThrows
    public ResponseEntity<Object> get(
            @PathVariable String cache,
            @PathVariable String key,
            @RequestParam(defaultValue = "0") Integer timeout) {
        log.info("get() - start, cache = {}, key = {}, timeout = {}", cache, key, timeout);
        if (timeout != null && timeout > 0) {
            log.info("Sleep {} seconds", timeout);
            TimeUnit.SECONDS.sleep(timeout);
        }
        Object value = redissonCacheService.get(cache, key);
        log.info("get() - end, value = {}", value);
        return Optional.ofNullable(value)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cache}/{key}")
    @SneakyThrows
    public ResponseEntity<Object> remove(
            @PathVariable String cache,
            @PathVariable String key) {
        log.info("remove() - start, cache = {}, key = {}", cache, key);
        Object value = redissonCacheService.remove(cache, key);
        log.info("remove() - end, value = {}", value);
        return Optional.ofNullable(value)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cache}")
    @SneakyThrows
    public ResponseEntity<Long> fastRemove(
            @PathVariable String cache,
            @RequestParam @NotNull @NotEmpty String[] keys) {
        log.info("remove() - start, cache = {}, keys = {}", cache, StringUtils.join(keys, ", ") );
        long count = redissonCacheService.fastRemove(cache, keys);
        log.info("remove() - end, count = {}", count);
        return ResponseEntity.ok(count);
    }
}
