package com.example.cachetutorial.api.cache.redisson;

import com.example.cachetutorial.service.cache.redisson.RMapCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("cache/redisson/r-map-cache")
@Slf4j
public class RMapCacheApi extends AbstractRedissonCacheApi<RMapCacheService>  {

    public RMapCacheApi(RMapCacheService redissonCacheService) {
        super(redissonCacheService);
    }

    @PostMapping("/{cache}/{key}")
    public ResponseEntity<Object> put(
            @PathVariable String cache,
            @PathVariable String key,
            @RequestBody(required = false) Object value,
            @RequestParam(required = false) Long ttl) {
        log.info("put() - start, cache = {}, key = {}, value = {}, ttl = {}", cache, key, value, ttl);
        Object prevValue = Optional.ofNullable(ttl)
                .map(t -> redissonCacheService.put(cache, key, value, t))
                .orElseGet(() -> redissonCacheService.put(cache, key, value));
        log.info("put() - end, prevValue = {}", prevValue);
        return ResponseEntity.ok(prevValue);
    }
}
