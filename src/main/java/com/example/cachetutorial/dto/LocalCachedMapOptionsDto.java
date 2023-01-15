package com.example.cachetutorial.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.redisson.api.LocalCachedMapOptions;

@Accessors(chain = true)
@Getter
@Setter
@ToString
public class LocalCachedMapOptionsDto {

    private LocalCachedMapOptions.ReconnectionStrategy reconnectionStrategy;
    private LocalCachedMapOptions.SyncStrategy syncStrategy;
    private LocalCachedMapOptions.EvictionPolicy evictionPolicy;
    private Integer cacheSize;
    private Long timeToLive;
    private Long maxIdle;
    private LocalCachedMapOptions.CacheProvider cacheProvider;
    private LocalCachedMapOptions.StoreMode storeMode;
    private Boolean storeCacheMiss;
}
