package com.cs35.common.utils.sql;

import com.cs35.common.exception.UtilException;
import com.cs35.common.utils.StringUtils;

/**
 * sql
 * 
 * @author cs35
 */
public class SqlUtil
{
    /**
     *  sql
     */
    public static String SQL_REGEX = "and |extractvalue|updatexml|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |+|user()";

    /**
     * 、、、、、（）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * orderBy
     */
    private static final int ORDER_BY_MAX_LENGTH = 500;

    /**
     * ，
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new UtilException("，");
        }
        if (StringUtils.length(value) > ORDER_BY_MAX_LENGTH)
        {
            throw new UtilException("，");
        }
        return value;
    }

    /**
     *  order by
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQL
     */
    public static void filterKeyword(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return;
        }
        String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords)
        {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1)
            {
                throw new UtilException("SQL");
            }
        }
    }
}
