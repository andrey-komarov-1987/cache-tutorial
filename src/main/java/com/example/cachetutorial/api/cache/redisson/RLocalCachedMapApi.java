package com.example.cachetutorial.api.cache.redisson;

import com.example.cachetutorial.dto.LocalCachedMapOptionsDto;
import com.example.cachetutorial.service.cache.redisson.RLocalCachedMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@RequestMapping("cache/redisson/r-local-cached-map")
@Slf4j
public class RLocalCachedMapApi extends AbstractRedissonCacheApi<RLocalCachedMapService>  {

    public RLocalCachedMapApi(RLocalCachedMapService redissonCacheService) {
        super(redissonCacheService);
    }

    @PostMapping("/{cache}/{key}")
    public ResponseEntity<Object> put(
            @PathVariable String cache,
            @PathVariable String key,
            @RequestBody(required = false) Object value) {
        log.info("put() - start, cache = {}, key = {}, value = {}", cache, key, value);
        Object prevValue = redissonCacheService.put(cache, key, value);
        log.info("put() - end, prevValue = {}", prevValue);
        return ResponseEntity.ok(prevValue);
    }

    @GetMapping("/options")
    public ResponseEntity<LocalCachedMapOptionsDto> getOptions() {
        log.info("getOptions() - start");
        LocalCachedMapOptionsDto result = redissonCacheService.getOptions();
        log.info("getOptions() - end");
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/options")
    public ResponseEntity<LocalCachedMapOptionsDto> updateOptions(@RequestBody @NotNull LocalCachedMapOptionsDto options) {
        log.info("updateOptions() - start, options = {}", options);
        LocalCachedMapOptionsDto result = redissonCacheService.updateOptions(options);
        log.info("updateOptions() - end");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{cache}/local")
    public ResponseEntity<Void> preloadCache(@PathVariable String cache) {
        log.info("preloadCache() - start, cache = {}", cache);
        redissonCacheService.preloadCache(cache);
        log.info("preloadCache() - end");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cache}/local")
    public ResponseEntity<Map<Object, Object>> getLocalCache(@PathVariable String cache) {
        log.info("getLocalCache() - start, cache = {}", cache);
        Map<Object, Object> result = redissonCacheService.getLocalCache(cache);
        log.info("preloadCache() - end, count = {}", result.size());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{cache}/local")
    public ResponseEntity<Void> clearLocalCache(@PathVariable String cache) {
        log.info("clearLocalCache() - start, cache = {}", cache);
        redissonCacheService.clearLocalCache(cache);
        log.info("clearLocalCache() - end");
        return ResponseEntity.ok().build();
    }


}
