package com.cs35.framework.config;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.cs35.common.config.CS35Config;
import com.cs35.common.constant.Constants;
import com.cs35.framework.interceptor.RepeatSubmitInterceptor;

/**
 * 
 * 
 * @author cs35
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /**  */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + CS35Config.getProfile() + "/");

        /** swagger */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());;
    }

    /**
     * 
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }

    /**
     * 
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 
        config.addAllowedOriginPattern("*");
        // 
        config.addAllowedHeader("*");
        // 
        config.addAllowedMethod("*");
        //  1800
        config.setMaxAge(1800L);
        // ，
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // CorsFilter
        return new CorsFilter(source);
    }
}