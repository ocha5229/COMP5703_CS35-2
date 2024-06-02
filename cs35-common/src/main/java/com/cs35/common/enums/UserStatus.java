package com.cs35.common.enums;

/**
 * 
 * 
 * @author cs35
 */
public enum UserStatus
{
    OK("0", ""), DISABLE("1", ""), DELETED("2", "");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
