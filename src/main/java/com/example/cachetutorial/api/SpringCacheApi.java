package com.example.cachetutorial.api;

import com.example.cachetutorial.service.SpringCacheService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("cache/spring")
@RequiredArgsConstructor
@Slf4j
public class SpringCacheApi {

    private final SpringCacheService springCacheService;

    @PostMapping("/{manager}/{cache}/{key}")
    public ResponseEntity<Void> put(
            @PathVariable String manager,
            @PathVariable String cache,
            @PathVariable String key,
            @RequestBody(required = false) Object value,
            @RequestParam(defaultValue = "false") boolean ifAbsent) {
        log.info("put() - start, manager = {}, cache = {}, key = {}, value = {}, ifAbsent = {}", manager, cache, key, value, ifAbsent);
        if (ifAbsent) {
            Object prevValue = springCacheService.putIfAbsent(manager, cache, key, value);
            log.info("prevValue = {}", prevValue);
        } else {
            springCacheService.put(manager, cache, key, value);
        }
        log.info("put() - end");
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{manager}/{cache}/{key}")
    @SneakyThrows
    public ResponseEntity<Object> get(
            @PathVariable String manager,
            @PathVariable String cache,
            @PathVariable String key,
            @RequestParam(defaultValue = "0") Integer timeout) {
        log.info("get() - start, manager = {}, cache = {}, key = {}, timeout = {}", manager, cache, key, timeout);
        if (timeout != null && timeout > 0) {
            log.info("Sleep {} seconds", timeout);
            TimeUnit.SECONDS.sleep(timeout);
        }
        Object value = springCacheService.get(manager, cache, key);
        log.info("get() - end, value = {}", value);
        return Optional.ofNullable(value)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{manager}/{cache}/{key}")
    public ResponseEntity<Void> delete(
            @PathVariable String manager,
            @PathVariable String cache,
            @PathVariable String key,
            @RequestParam(defaultValue = "false") boolean ifPresent) {
        log.info("delete() - start, manager = {}, cache = {}, key = {}, ifPresent = {}", manager, cache, key, ifPresent);
        if (ifPresent) {
            boolean present = springCacheService.evictIfPresent(manager, cache, key);
            log.info("present = {}", present);
        } else {
            springCacheService.evict(manager, cache, key);
        }
        log.info("delete() - end");
        return ResponseEntity.noContent().build();
    }
}
