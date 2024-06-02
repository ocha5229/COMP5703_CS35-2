package com.cs35.common.exception.user;

/**
 * IP
 * 
 * @author cs35
 */
public class BlackListException extends UserException
{
    private static final long serialVersionUID = 1L;

    public BlackListException()
    {
        super("login.blocked", null);
    }
}
