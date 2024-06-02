package com.cs35.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 
 * @author cs35
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope
{
    /**
     * 
     */
    public String deptAlias() default "";

    /**
     * 
     */
    public String userAlias() default "";

    /**
     * （）@ss，
     */
    public String permission() default "";
}
