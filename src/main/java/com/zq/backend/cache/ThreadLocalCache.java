package com.zq.backend.cache;

import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import com.zq.backend.object.common.ParamChecker;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ThreadLocalCache implements Cache {

    private final String name;
    private final ThreadLocal<Map<Object, CacheValue>> threadLocalCache = ThreadLocal.withInitial(ConcurrentHashMap::new);

    public ThreadLocalCache(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Object getNativeCache() {
        return threadLocalCache.get();
    }

    @Override
    public ValueWrapper get(@NotNull Object key) {
        CacheValue cacheValue = threadLocalCache.get().get(key);
        if(Objects.nonNull(cacheValue) && !cacheValue.isExpired()) {
            return cacheValue::getValue;
        }
        threadLocalCache.get().remove(key);
        return null;
    }

    @Override
    public <T> T get(@NotNull Object key, Class<T> type) {
        ParamChecker.checkNotNull(type, "type");
        ValueWrapper valueWrapper = get(key);
        if(Objects.isNull(valueWrapper)) {
            return null;
        }
        return type.cast(valueWrapper.get());
    }

    @Override
    public <T> T get(@NotNull Object key, @NotNull Callable<T> valueLoader) {
        // 检查缓存中是否存在值
        Map<Object, CacheValue> cacheMap = threadLocalCache.get();
        CacheValue<T> cacheValue = cacheMap.get(key);
        if(Objects.nonNull(cacheValue) && !cacheValue.isExpired()) {
            return cacheValue.getValue();
        }

        // 缓存未命中，调用 valueLoader 加载值
        try {
            T loadedValue = valueLoader.call();
            if (loadedValue != null) {
                cacheMap.put(key, new CacheValue<T>(loadedValue, -1)); // 将加载的值存入缓存
            }
            return loadedValue;
        } catch (Exception e) {
            log.error("[ThreadLocalCache][get][logMsg: loadValue error][key:{}], e:", key, e);
            ExceptionUtil.throwException(ErrorEnum.UNKNOWN_ERROR);
            return null;
        }
    }

    @Override
    public void put(@NotNull Object key, Object value) {
        put(key, value, -1L);
    }

    public <T> void put(@NotNull Object key, T value, long ttlInMills) {
        threadLocalCache.get().put(key, new CacheValue<T>(value, ttlInMills));
    }

    @Override
    public void evict(@NotNull Object key) {
        threadLocalCache.get().remove(key);
    }

    @Override
    public void clear() {
        threadLocalCache.get().clear();
    }
}
