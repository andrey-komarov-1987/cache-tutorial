package com.example.cachetutorial.api.keys.redisson;

import com.example.cachetutorial.service.keys.redisson.RedissonKeysService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("keys/redisson")
@RequiredArgsConstructor
@Slf4j
public class RedissonKeyApi {

    private final RedissonKeysService redissonKeysService;

    @PostMapping("/{prefix}/{name}")
    public ResponseEntity<Void> set(
            @PathVariable String prefix,
            @PathVariable String name,
            @RequestBody Object value,
            @RequestParam(defaultValue = "30") long ttl,
            @RequestParam(defaultValue = "false") boolean ifExists) {
        log.info("set() - start, prefix = {}, name = {}, value = {}, ttl = {}, ifExists = {}",
                prefix, name, value, ttl, ifExists);
        boolean result = redissonKeysService.set(prefix, name, value, ttl, ifExists);
        log.info("set() - end, result = {}", result);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/{prefix}/{name}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> exists(
            @PathVariable String prefix,
            @PathVariable String name) {
        log.info("exists() - start, prefix = {}, name = {}", prefix, name);
        boolean result = redissonKeysService.exists(prefix, name);
        log.info("exists() - end, result = {}", result);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{prefix}")
    public ResponseEntity<List<String>> getAll(@PathVariable String prefix) {
        log.info("getAll() - start, prefix = {}", prefix);
        List<String> result = redissonKeysService.getAll(prefix);
        log.info("getAll() - end, count = {}", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{prefix}/{name}")
    public ResponseEntity<Object> get(
            @PathVariable String prefix,
            @PathVariable String name,
            @RequestParam(defaultValue = "false") boolean remove) {
        log.info("get() - start, prefix = {}, name = {}", prefix, name);
        Object result = redissonKeysService.get(prefix, name, remove);
        log.info("get() - end");
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{prefix}/{name}")
    public ResponseEntity<Boolean> delete(
            @PathVariable String prefix,
            @PathVariable String name) {
        log.info("delete() - start, prefix = {}, name = {}", prefix, name);
        boolean result = redissonKeysService.delete(prefix, name);
        log.info("delete() - end, result = {}", result);
        return ResponseEntity.ok(result);
    }
}
