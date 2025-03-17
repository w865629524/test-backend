package com.zq.backend.cache;

class CacheValue<T> {
    private final T value;
    private final long expiryTime;

    public CacheValue(T value, long ttlInMillis) {
        this.value = value;
        this.expiryTime = ttlInMillis > 0 ? System.currentTimeMillis() + ttlInMillis : Long.MAX_VALUE;
    }

    public T getValue() {
        return value;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}