package com.cs35.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import com.cs35.common.utils.spring.SpringUtils;

/**
 * i18n
 * 
 * @author cs35
 */
public class MessageUtils
{
    /**
     *   spring messageSource
     *
     * @param code 
     * @param args 
     * @return 
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
