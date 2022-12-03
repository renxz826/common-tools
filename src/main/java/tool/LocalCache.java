package tool;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存 builder 模式
 *
 * @param <K>
 * @param <V>
 * @author renxz
 * @date 2022/11/25 10:33 上午
 */
public class LocalCache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(LocalCache.class);
    private static volatile Cache cache;
    /**
     * 缓存过期时间 default 30 分
     */
    private Integer expireTime = 30;
    /**
     * default 单位 分
     */
    private TimeUnit expireTimeUnit = TimeUnit.MINUTES;

    private LocalCache() {
        cache = CacheBuilder.newBuilder().build();
    }

    private LocalCache(Integer expireTime, TimeUnit expireTimeUnit) {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    this.expireTime = expireTime;
                    this.expireTimeUnit = expireTimeUnit;
                    cache = CacheBuilder.newBuilder().expireAfterWrite(expireTime, expireTimeUnit).build();
                }
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public V getIfPresent(Object o) {
        return (V) cache.getIfPresent(o);
    }

    public V get(K k, Callable<? extends V> callable) throws ExecutionException {
        return (V) cache.get(k, callable);
    }

    public ImmutableMap<K, V> getAllPresent(Iterable<?> iterable) {
        return cache.getAllPresent(iterable);
    }

    public void put(K k, V v) {
        cache.put(k, v);

    }

    public void putAll(Map<? extends K, ? extends V> map) {
        cache.putAll(map);
    }

    public void invalidate(Object o) {
        cache.invalidate(o);
    }

    public void invalidateAll(Iterable<?> iterable) {
        cache.invalidateAll(iterable);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public long size() {
        return cache.size();
    }

    public CacheStats stats() {
        return cache.stats();
    }

    public ConcurrentMap<K, V> asMap() {
        return cache.asMap();
    }

    public void cleanUp() {
        cache.cleanUp();
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public TimeUnit getTimeUnit() {
        return expireTimeUnit;
    }

    @Override
    public String toString() {
        return "LocalCache{" +
                "expireTime=" + expireTime +
                ", expireTimeUnit=" + expireTimeUnit +
                ", cache=" + cache +
                '}';
    }

    public static class Builder<K, V> {

        private Integer expireTime = 30;
        private TimeUnit timeUnit = TimeUnit.MINUTES;


        public Builder expireTime(Integer expireTime, TimeUnit time) {
            this.expireTime = expireTime;
            this.timeUnit = time;
            return this;
        }

        public LocalCache<K, V> build() {
            LocalCache<K, V> localCache = new LocalCache<>(expireTime, timeUnit);
            return localCache;
        }

    }
}
