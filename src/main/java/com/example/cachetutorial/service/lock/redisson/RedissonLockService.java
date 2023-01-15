package com.example.cachetutorial.service.lock.redisson;

import com.example.cachetutorial.dto.LockStatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedissonLockService {

    private final RedissonClient redissonClient;

    public LockStatDto lock(String name, int leaseTime, int durationTime) {
        LockStatDto result = new LockStatDto();
        RLock lock = redissonClient.getLock("lock:" + name);
        long startTime = System.currentTimeMillis();
        lock.lock(leaseTime, TimeUnit.SECONDS);
        log.info("Acquiring lock {} by {}", name, Thread.currentThread().getName());
        try {
            log.info("Lock {} acquired by {}", name, Thread.currentThread().getName());
            long lockTime = System.currentTimeMillis();
            result.setWait(TimeUnit.MILLISECONDS.toSeconds(lockTime - startTime));
            TimeUnit.SECONDS.sleep(durationTime);
            result.setDuration(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lockTime));
        } catch (InterruptedException e) {
            log.error("Sleep interrupted", e);
            result.setInterrupted(true);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            log.info("Lock {} released by {}", name, Thread.currentThread().getName());
        }
        return result;
    }

    public LockStatDto multiLock(List<String> names, int leaseTime, int durationTime) {
        Set<String> uniqueNames = new LinkedHashSet<>(names);
        LockStatDto result = new LockStatDto();
        RLock[] locks = uniqueNames.stream()
                .distinct()
                .map(name -> redissonClient.getLock("lock:" + name))
                .toArray(RLock[]::new);
        RLock multiLock = redissonClient.getMultiLock(locks);
        long startTime = System.currentTimeMillis();
        multiLock.lock(leaseTime, TimeUnit.SECONDS);
        String name = String.join(", ", uniqueNames);
        log.info("Acquiring multi-lock {} by {}", name, Thread.currentThread().getName());
        try {
            log.info("Multi-lock {} acquired by {}", name, Thread.currentThread().getName());
            long lockTime = System.currentTimeMillis();
            result.setWait(TimeUnit.MILLISECONDS.toSeconds(lockTime - startTime));
            TimeUnit.SECONDS.sleep(durationTime);
            result.setDuration(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lockTime));
        } catch (InterruptedException e) {
            log.error("Sleep interrupted", e);
            result.setInterrupted(true);
            Thread.currentThread().interrupt();
        } finally {
            multiLock.unlock();
            log.info("Multi-lock {} released by {}", name, Thread.currentThread().getName());
        }
        return result;
    }
}
