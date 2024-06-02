package com.cs35.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.cs35.common.enums.DataSourceType;

/**
 * 
 *
 * ：，，，，
 *
 * @author cs35
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource
{
    /**
     * 
     */
    public DataSourceType value() default DataSourceType.MASTER;
}
