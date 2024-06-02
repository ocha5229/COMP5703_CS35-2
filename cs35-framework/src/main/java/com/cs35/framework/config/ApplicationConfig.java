package com.cs35.framework.config;

import java.util.TimeZone;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 *
 *
 * @author cs35
 */
@Configuration
// aop,AopContext
@EnableAspectJAutoProxy(exposeProxy = true)
// Mapper
@MapperScan("com.cs35.**.mapper")
public class ApplicationConfig
{
    /**
     *
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization()
    {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }
}
