package com.cs35.common.exception.user;

/**
 * 
 * 
 * @author cs35
 */
public class UserNotExistsException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserNotExistsException()
    {
        super("user.not.exists", null);
    }
}
