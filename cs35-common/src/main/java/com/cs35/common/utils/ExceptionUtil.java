package com.cs35.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 。
 *
 * @author cs35
 */
public class ExceptionUtil
{
    /**
     * exception。
     */
    public static String getExceptionMessage(Throwable e)
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    public static String getRootErrorMessage(Exception e)
    {
        Throwable root = ExceptionUtils.getRootCause(e);
        root = (root == null ? e : root);
        if (root == null)
        {
            return "";
        }
        String msg = root.getMessage();
        if (msg == null)
        {
            return "null";
        }
        return StringUtils.defaultString(msg);
    }
}
