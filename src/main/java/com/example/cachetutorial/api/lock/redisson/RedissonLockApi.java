package com.example.cachetutorial.api.lock.redisson;

import com.example.cachetutorial.dto.LockStatDto;
import com.example.cachetutorial.service.lock.redisson.RedissonLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("lock/redisson")
@RequiredArgsConstructor
@Slf4j
public class RedissonLockApi {

    private final RedissonLockService redissonLockService;

    @PostMapping("/{name}")
    public ResponseEntity<LockStatDto> lock(
            @PathVariable String name,
            @RequestParam(value = "lease", defaultValue = "30") int leaseTime,
            @RequestParam(value = "duration", defaultValue = "30") int durationTime) {
        log.info("lock() - start, name = {}, leaseTime = {}, durationTime = {}", name, leaseTime, durationTime);
        LockStatDto lockStat = redissonLockService.lock(name, leaseTime, durationTime);
        log.info("lock() - end, result = {}", lockStat);
        return ResponseEntity.ok(lockStat);
    }

    @PostMapping
    public ResponseEntity<LockStatDto> multiLock(
            @RequestBody List<String> names,
            @RequestParam(value = "lease", defaultValue = "30") int leaseTime,
            @RequestParam(value = "duration", defaultValue = "30") int durationTime) {
        log.info("multiLock() - start, name = {}, leaseTime = {}, durationTime = {}", String.join(",", names), leaseTime, durationTime);
        LockStatDto lockStat = redissonLockService.multiLock(names, leaseTime, durationTime);
        log.info("multiLock() - end, result = {}", lockStat);
        return ResponseEntity.ok(lockStat);
    }
}
