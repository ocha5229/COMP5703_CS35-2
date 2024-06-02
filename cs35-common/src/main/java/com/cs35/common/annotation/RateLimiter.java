package com.cs35.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.cs35.common.constant.CacheConstants;
import com.cs35.common.enums.LimitType;

/**
 *
 * 
 * @author cs35
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter
{
    /**
     * key
     */
    public String key() default CacheConstants.RATE_LIMIT_KEY;

    /**
     * ,
     */
    public int time() default 60;

    /**
     *
     */
    public int count() default 100;

    /**
     *
     */
    public LimitType limitType() default LimitType.DEFAULT;
}
