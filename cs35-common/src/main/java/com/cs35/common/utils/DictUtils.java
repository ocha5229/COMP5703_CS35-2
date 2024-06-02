package com.cs35.common.utils;

import java.util.Collection;
import java.util.List;
import com.alibaba.fastjson2.JSONArray;
import com.cs35.common.constant.CacheConstants;
import com.cs35.common.core.domain.entity.SysDictData;
import com.cs35.common.core.redis.RedisCache;
import com.cs35.common.utils.spring.SpringUtils;

/**
 * 
 * 
 * @author cs35
 */
public class DictUtils
{
    /**
     * 
     */
    public static final String SEPARATOR = ",";

    /**
     * 
     * 
     * @param key 
     * @param dictDatas 
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        SpringUtils.getBean(RedisCache.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 
     * 
     * @param key 
     * @return dictDatas 
     */
    public static List<SysDictData> getDictCache(String key)
    {
        JSONArray arrayCache = SpringUtils.getBean(RedisCache.class).getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(arrayCache))
        {
            return arrayCache.toList(SysDictData.class);
        }
        return null;
    }

    /**
     * 
     * 
     * @param dictType 
     * @param dictValue 
     * @return 
     */
    public static String getDictLabel(String dictType, String dictValue)
    {
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 
     * 
     * @param dictType 
     * @param dictLabel 
     * @return 
     */
    public static String getDictValue(String dictType, String dictLabel)
    {
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 
     * 
     * @param dictType 
     * @param dictValue 
     * @param separator 
     * @return 
     */
    public static String getDictLabel(String dictType, String dictValue, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);

        if (StringUtils.isNotNull(datas))
        {
            if (StringUtils.containsAny(separator, dictValue))
            {
                for (SysDictData dict : datas)
                {
                    for (String value : dictValue.split(separator))
                    {
                        if (value.equals(dict.getDictValue()))
                        {
                            propertyString.append(dict.getDictLabel()).append(separator);
                            break;
                        }
                    }
                }
            }
            else
            {
                for (SysDictData dict : datas)
                {
                    if (dictValue.equals(dict.getDictValue()))
                    {
                        return dict.getDictLabel();
                    }
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 
     * 
     * @param dictType 
     * @param dictLabel 
     * @param separator 
     * @return 
     */
    public static String getDictValue(String dictType, String dictLabel, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);

        if (StringUtils.containsAny(separator, dictLabel) && StringUtils.isNotEmpty(datas))
        {
            for (SysDictData dict : datas)
            {
                for (String label : dictLabel.split(separator))
                {
                    if (label.equals(dict.getDictLabel()))
                    {
                        propertyString.append(dict.getDictValue()).append(separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict : datas)
            {
                if (dictLabel.equals(dict.getDictLabel()))
                {
                    return dict.getDictValue();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 
     * 
     * @param key 
     */
    public static void removeDictCache(String key)
    {
        SpringUtils.getBean(RedisCache.class).deleteObject(getCacheKey(key));
    }

    /**
     * 
     */
    public static void clearDictCache()
    {
        Collection<String> keys = SpringUtils.getBean(RedisCache.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisCache.class).deleteObject(keys);
    }

    /**
     * cache key
     * 
     * @param configKey 
     * @return key
     */
    public static String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}
