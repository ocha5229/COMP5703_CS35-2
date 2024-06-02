package com.cs35.common.core.redis;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * spring redis 
 *
 * @author cs35
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * ，Integer、String、
     *
     * @param key 
     * @param value 
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * ，Integer、String、
     *
     * @param key 
     * @param value 
     * @param timeout 
     * @param timeUnit 
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 
     *
     * @param key Redis
     * @param timeout 
     * @return true=；false=
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 
     *
     * @param key Redis
     * @param timeout 
     * @param unit 
     * @return true=；false=
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 
     *
     * @param key Redis
     * @return 
     */
    public long getExpire(final String key)
    {
        return redisTemplate.getExpire(key);
    }

    /**
     *  key
     *
     * @param key 
     * @return true  false
     */
    public Boolean hasKey(String key)
    {
        return redisTemplate.hasKey(key);
    }

    /**
     * 。
     *
     * @param key 
     * @return 
     */
    public <T> T getCacheObject(final String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return redisTemplate.delete(key);
    }

    /**
     * 
     *
     * @param collection 
     * @return
     */
    public boolean deleteObject(final Collection collection)
    {
        return redisTemplate.delete(collection) > 0;
    }

    /**
     * List
     *
     * @param key 
     * @param dataList List
     * @return 
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * list
     *
     * @param key 
     * @return 
     */
    public <T> List<T> getCacheList(final String key)
    {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * Set
     *
     * @param key 
     * @param dataSet 
     * @return 
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * Hash
     *
     * @param key Redis
     * @param hKey Hash
     * @param value 
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * Hash
     *
     * @param key Redis
     * @param hKey Hash
     * @return Hash
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * Hash
     *
     * @param key Redis
     * @param hKeys Hash
     * @return Hash
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * Hash
     *
     * @param key Redis
     * @param hKey Hash
     * @return 
     */
    public boolean deleteCacheMapValue(final String key, final String hKey)
    {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 
     *
     * @param pattern 
     * @return 
     */
    public Collection<String> keys(final String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
