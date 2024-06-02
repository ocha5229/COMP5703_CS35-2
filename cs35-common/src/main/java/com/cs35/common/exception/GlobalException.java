package com.cs35.common.exception;

/**
 * 
 * 
 * @author cs35
 */
public class GlobalException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String message;

    /**
     * ，
     *
     *  {@link CommonResult#getDetailMessage()} 
     */
    private String detailMessage;

    /**
     * ，
     */
    public GlobalException()
    {
    }

    public GlobalException(String message)
    {
        this.message = message;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public GlobalException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public GlobalException setMessage(String message)
    {
        this.message = message;
        return this;
    }
}