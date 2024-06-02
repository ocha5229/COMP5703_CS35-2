package com.cs35.common.utils.uuid;

/**
 * ID
 * 
 * @author cs35
 */
public class IdUtils
{
    /**
     * UUID
     * 
     * @return UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * UUID，
     * 
     * @return UUID，
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * UUID，ThreadLocalRandomUUID
     * 
     * @return UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * UUID，，ThreadLocalRandomUUID
     * 
     * @return UUID，
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }
}
