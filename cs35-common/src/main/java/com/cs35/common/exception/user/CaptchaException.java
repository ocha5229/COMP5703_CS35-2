package com.cs35.common.exception.user;

/**
 *
 * 
 * @author cs35
 */
public class CaptchaException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaException()
    {
        super("user.jcaptcha.error", null);
    }
}
