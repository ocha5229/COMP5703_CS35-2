package com.cs35.common.exception.user;

import com.cs35.common.exception.base.BaseException;

/**
 * 
 * 
 * @author cs35
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
