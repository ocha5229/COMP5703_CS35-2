package com.cs35.common.exception.file;

import com.cs35.common.exception.base.BaseException;

/**
 *
 * 
 * @author cs35
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
