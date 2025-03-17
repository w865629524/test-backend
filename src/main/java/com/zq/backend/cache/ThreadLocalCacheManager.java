package com.zq.backend.cache;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadLocalCacheManager implements CacheManager {

    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Cache getCache(@NotNull String name) {
        return cacheMap.computeIfAbsent(name, ThreadLocalCache::new);
    }

    @Override
    public @NotNull Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }
}
