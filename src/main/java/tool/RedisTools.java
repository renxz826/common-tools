package tool;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import constant.RedisConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 * @author renxz
 * @date 2022/11/3 4:24 下午
 */
@Component
public class RedisTools {

    private static final String LOCK_PREFIX = RedisConstant.LOCK;
    @Resource
    private RedisTemplate redisTemplate;
    private static final Logger log= LoggerFactory.getLogger(RedisTools.class);

    private Integer count = 0;
    private long LOCK_EXPIRE = 300; // ms
    /**
     * 是否有key
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间,单位是秒
     */
    public void expire(String key, Long expireTime) {
        if (expireTime != null && expireTime > 0L) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    //================String 操作===================

    public void vSet(String key, String value) {
        ValueOperations<String, String> sOpts = redisTemplate.opsForValue();
        sOpts.set(key, value);
    }

    public String vGet(String key) {
        ValueOperations<String, String> sOpts = redisTemplate.opsForValue();
        return sOpts.get(key);
    }

    public void vSetToExpireSecond(String key, String value, Long expire) {
        vSet(key, value);
        expire(key, expire);
    }

    public void zsetAdd(String key, String val, Long score) {
        redisTemplate.opsForZSet().add(key, val, Convert.toDouble(score, 0d));
        expire(key, (long) RandomUtil.randomInt(9999));
    }

    public void zsetAdd(String key, String val, Long score, Long expire) {
        redisTemplate.opsForZSet().add(key, val, Convert.toDouble(score, 0d));
        expire(key, expire);
    }

    public void zsetDel(String key, String value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    public Long zsetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public Double zsetAddScore(String key, String item, Long score, Long expireTime) {
        ZSetOperations<String, String> zsetOpts = redisTemplate.opsForZSet();
        Double scoreD = Convert.toDouble(score, 0d);
        zsetOpts.add(key, item, scoreD);
        expire(key, expireTime);
        return scoreD;
    }

    public Double zsetIncrScore(String key, String item, Integer score) {
        return zsetIncrScore(key, item, score, (long) RandomUtil.randomInt(9999));
    }

    public Double zsetIncrScore(String key, String item, Integer score, Long expireTime) {
        return zsetIncrScore(key, item, score.longValue());
    }

    public Double zsetIncrScore(String key, String item, Long score) {
        return zsetIncrScore(key, item, score, (long) RandomUtil.randomInt(9999));
    }

    public Double zsetIncrScore(String key, String item, Long score, Long expireTime) {
        ZSetOperations<String, String> zsetOpts = redisTemplate.opsForZSet();
        Double sumScore = zsetOpts.incrementScore(key, item, Convert.toDouble(score, 0d));
        expire(key, expireTime);
        return sumScore;
    }

    public Double getZSetScore(String key, String item) {
        Double score = redisTemplate.opsForZSet().score(key, item);
        return score != null ? score : 0;
    }

    public LinkedHashMap<Long, Long> zsetGetRankMap(String key, Long start, Long end) {
        Set<ZSetOperations.TypedTuple<String>> members = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        LinkedHashMap<Long, Long> resultMap = Maps.newLinkedHashMap();
        for (ZSetOperations.TypedTuple<String> score : members) {
            resultMap.put(Long.valueOf(score.getValue()), Convert.toBigDecimal(score.getScore()).longValue());
        }
        return resultMap;
    }

    public LinkedHashMap<String, Long> zsetGetCpRankMap(String key, Long start, Long end) {
        Set<ZSetOperations.TypedTuple<String>> members = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        LinkedHashMap<String, Long> resultMap = Maps.newLinkedHashMap();
        for (ZSetOperations.TypedTuple<String> score : members) {
            resultMap.put(score.getValue(), Convert.toBigDecimal(score.getScore()).longValue());
        }
        return resultMap;
    }

    public Long zsetRevRank(String key, String item) {
        ZSetOperations<String, String> zsetOpts = redisTemplate.opsForZSet();
        return zsetOpts.reverseRank(key, item);
    }

    public Long zsetRank(String key, String item){
        return redisTemplate.opsForZSet().rank(key,item);
    }

    public long vIcrement(String key, Integer delta) {
        return vIcrement(key, delta, null);
    }

    public long vIcrement(String key, Integer delta, Long expire) {
        ValueOperations<String, String> sOpts = redisTemplate.opsForValue();
        long n = sOpts.increment(key, delta);
        expire(key, expire);
        return n;
    }

    public long vDecrement(String key) {
        ValueOperations<String, String> sOpts = redisTemplate.opsForValue();
        return sOpts.decrement(key);
    }

    public void leftPush(String key, String value, Long expire) {
        redisTemplate.opsForList().leftPush(key, value);
        expire(key, expire);
    }

    public void rightPush(String key, String value, Long expire) {
        redisTemplate.opsForList().rightPush(key, value);
        expire(key, expire);
    }

    public List<String> listRange(String key, Long start, Long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public String rightPop(String key) {
        return (String) redisTemplate.opsForList().rightPop(key);
    }

    public String hget(String hashKey, String hashItem) {
        HashOperations<String, String, String> hashOpt = redisTemplate.opsForHash();
        return hashOpt.get(hashKey, hashItem);
    }

    public void hput(String hashKey, String hashItem, String value, Long expire) {
        HashOperations<String, String, String> hashOpt = redisTemplate.opsForHash();
        hashOpt.put(hashKey, hashItem, value);
        expire(hashKey, expire);
    }

    public void hputAll(String hashKey, Map<String, String> map, Long expire) {
        HashOperations<String, String, String> hashOpt = redisTemplate.opsForHash();
        hashOpt.putAll(hashKey, map);
        expire(hashKey, expire);
    }


    public boolean checkCritical(String key, Integer delta, long expire, long criticalCount) {
        ValueOperations<String, String> sOpts = redisTemplate.opsForValue();
        String countStr = sOpts.get(key);
        long count = ObjectUtil.isNull(countStr) ? 0 : Integer.valueOf(countStr);
        long nowCount = sOpts.increment(key, delta);
        expire(key, expire);
        if ((count < criticalCount) && (nowCount >= criticalCount)) {
            return true;
        } else {
            return false;
        }
    }
    public void vSet(String key, String value,long expire) {
        ValueOperations<String, String> sOpts = redisTemplate.opsForValue();
        sOpts.set(key, value);
        if (expire > 0L) {
            redisTemplate.expire(key,expire,TimeUnit.SECONDS);
        }
    }


    // lock
    public boolean lock(String key) {
        Boolean isLock = false;
        String lock = LOCK_PREFIX + key;
        long timeout = System.currentTimeMillis() + LOCK_EXPIRE;
        isLock = redisTemplate.opsForValue().setIfAbsent(lock, timeout + "");
        if (isLock) {
            return true;
        } else {
            String value = (String)redisTemplate.opsForValue().get(lock);
            if (value != null) {
                long currentTime = System.currentTimeMillis();
                // 防止死锁
                if (currentTime > Long.parseLong(value)) {
                    redisTemplate.opsForValue().set(lock, currentTime + LOCK_EXPIRE + "");
                    return currentTime > Long.parseLong(value);
                }
            }
            if (count++ < 2) {
                return this.lock(key);
            }
            log.error("RedisClientService | tryLock| failed lock:{}.", lock);
        }
        return isLock;
    }

    public boolean lock(String key , long expire) {
        Boolean isLock = false;
        String lock = LOCK_PREFIX + key;
        long timeout = System.currentTimeMillis() + expire;
        isLock = redisTemplate.opsForValue().setIfAbsent(lock, timeout + "");
        if (isLock) {
            return true;
        } else {
            String value = (String)redisTemplate.opsForValue().get(lock);
            if (value != null) {
                long currentTime = System.currentTimeMillis();
                // 防止死锁
                if (currentTime > Long.parseLong(value)) {
                    redisTemplate.opsForValue().set(lock, currentTime + expire + "");
                    return currentTime > Long.parseLong(value);
                }
            }
            if (count++ < 2) {
                return this.lock(key, expire);
            }
            log.error("RedisClientService | tryLock| failed lock:{}.", lock);
        }
        return isLock;
    }
    public boolean unLock(String key) {
        String lock = LOCK_PREFIX + key;
        Boolean isdel = redisTemplate.delete(lock);
        if (!isdel) {
            while (count++ < 2) {
                return this.unLock(lock);
            }
            log.error("RedisClientService | unLock| failed lock:{}.", lock);
        }
        return isdel;
    }

    public boolean setNx(String key, String value, long expire) {
        return redisTemplate.opsForValue().setIfAbsent(key,value,expire,TimeUnit.SECONDS);
    }

    /**
     * 批量hmget 管道 pipeline
     *
     * @param keys
     * @param <T>
     * @return
     */
    public <T> List<T> batchHmget(List<String> keys) {
        Assert.notNull(keys, "keys cannot be null .");
        List<T> result = (List<T>) redisTemplate.executePipelined(new SessionCallback<Map<String, Object>>() {
            @Override
            public Map<String, Object> execute(RedisOperations operations) throws DataAccessException {
                for (String key : keys) {
                    operations.opsForHash().entries(key);
                }
                return null;
            }
        }, redisTemplate.getHashKeySerializer());

        return result;
    }

    /**
     * 批量hmset 管道 pipeline
     *
     * @param key
     * @param values
     * @param expireTime
     * @return
     */
    public <T> boolean batchHmset(String key, List<T> values, long expireTime) {
        Assert.notNull(key, "key cannot be null .");
        Assert.notNull(values, "values cannot be null .");
        try {
            redisTemplate.executePipelined(new SessionCallback<String>() {
                @Override
                public String execute(RedisOperations operations) throws DataAccessException {
                    for (T t : values) {

                        Map<String, Object> map = Maps.newHashMap();
                        if (t instanceof Map) {
                            map = (Map<String, Object>) t;
                        } else {
                            map = (Map<String, Object>) JSON.toJSON(t);
                        }
                        map.entrySet().forEach(p -> p.setValue(String.valueOf(p.getValue())));
                        operations.opsForHash().putAll(key, map);
                        if (expireTime > 0) {
                            expire(key, expireTime);
                        }
                    }

                    return null;
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 批量get 管道 pipeline
     *
     * @param keys
     * @param <T>
     * @return
     */
    public <T> List<T> batchget(List<String> keys) {
        Assert.notNull(keys, "keys cannot be null .");
        List<T> result = (List<T>) redisTemplate.executePipelined(new SessionCallback<T>() {
            @Override
            public T execute(RedisOperations operations) throws DataAccessException {
                for (String key : keys) {
                    operations.opsForValue().get(key);
                }
                return null;
            }
        }, redisTemplate.getStringSerializer());

        return result;
    }

    /**
     * 批量set 管道 pipeline
     *
     * @param maps (k V)
     * @return
     */
    public <K, V> boolean batchset(List<Map<K, V>> maps, long expireTime) {
        Assert.notNull(maps, "maps cannot be null .");
        try {
            redisTemplate.executePipelined(new SessionCallback<String>() {
                @Override
                public String execute(RedisOperations operations) throws DataAccessException {
                    maps.forEach(p -> {
                        p.entrySet().forEach(map -> {
                            K key = map.getKey();
                            V value = map.getValue();
                            operations.opsForValue().set(key, JSON.toJSONString(value));
                            if (expireTime > 0) {
                                expire((String) key, expireTime);
                            }
                        });

                    });
                    return null;
                }
            }, redisTemplate.getStringSerializer());
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
