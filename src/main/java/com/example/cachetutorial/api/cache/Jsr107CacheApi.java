package com.example.cachetutorial.api.cache;

import com.example.cachetutorial.service.cache.Jsr107CacheService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("cache/jsr107")
@RequiredArgsConstructor
@Slf4j
public class Jsr107CacheApi {

    private final Jsr107CacheService jsr107CacheService;

    @PostMapping("/{provider}/{cache}/{key}")
    public ResponseEntity<Void> put(
            @PathVariable String provider,
            @PathVariable String cache,
            @PathVariable String key,
            @RequestBody(required = false) Object value) {
        log.info("put() - start, provider = {}, cache = {}, key = {}, value = {}", provider, cache, key, value);
        jsr107CacheService.put(provider, cache, key, value);
        log.info("put() - end");
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{provider}/{cache}/{key}")
    @SneakyThrows
    public ResponseEntity<Object> get(
            @PathVariable String provider,
            @PathVariable String cache,
            @PathVariable String key,
            @RequestParam(defaultValue = "0") Integer timeout) {
        log.info("get() - start, provider = {}, cache = {}, key = {}, timeout = {}", provider, cache, key, timeout);
        if (timeout != null && timeout > 0) {
            log.info("Sleep {} seconds", timeout);
            TimeUnit.SECONDS.sleep(timeout);
        }
        Object value = jsr107CacheService.get(provider, cache, key);
        log.info("get() - end, value = {}", value);
        return Optional.ofNullable(value)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{provider}/{cache}/{key}/get-and-remove")
    @SneakyThrows
    public ResponseEntity<Object> getAndRemove(
            @PathVariable String provider,
            @PathVariable String cache,
            @PathVariable String key) {
        log.info("getAndRemove() - start, provider = {}, cache = {}, key = {}", provider, cache, key);
        Object value = jsr107CacheService.getAndRemove(provider, cache, key);
        log.info("getAndRemove() - end, value = {}", value);
        return Optional.ofNullable(value)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{provider}/{cache}/{key}")
    public ResponseEntity<Boolean> remove(
            @PathVariable String provider,
            @PathVariable String cache,
            @PathVariable String key) {
        log.info("remove() - start, provider = {}, cache = {}, key = {}", provider, cache, key);
        boolean result = jsr107CacheService.remove(provider, cache, key);
        log.info("remove() - end");
        return ResponseEntity.ok(result);
    }
}
