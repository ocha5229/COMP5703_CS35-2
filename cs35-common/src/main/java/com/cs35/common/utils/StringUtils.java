package com.cs35.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.util.AntPathMatcher;
import com.cs35.common.constant.Constants;
import com.cs35.common.core.text.StrFormatter;

/**
 *
 * 
 * @author cs35
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    /**  */
    private static final String NULLSTR = "";

    /**  */
    private static final char SEPARATOR = '_';

    /**
     *
     * 
     * @param value defaultValue value
     * @return value
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * * Collection， List，Set，Queue
     * 
     * @param coll Collection
     * @return true： false：
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * Collection，List，Set，Queue
     * 
     * @param coll Collection
     * @return true： false：
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * *
     * 
     * @param objects
     ** @return true： false：
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * *
     * 
     * @param objects
     * @return true： false：
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * Map
     * 
     * @param map Map
     * @return true： false：
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * Map
     * 
     * @param map Map
     * @return true： false：
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * *
     * 
     * @param str String
     * @return true： false：
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * *
     * 
     * @param str String
     * @return true： false：
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * *
     * 
     * @param object Object
     * @return true： false：
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * *
     * 
     * @param object Object
     * @return true： false：
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * （Java）
     * 
     * @param object
     * @return true： false：
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     *
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     *
     * 
     * @param str
     * @param start
     * @return
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     *
     * 
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * ，
     * 
     * @param str value
     * @return
     */
    public static boolean hasText(String str)
    {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str)
    {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * , {} <br>
     *  {} <br>
     *  {}  \\ { ， {}  \  \\\\ <br>
     * ：<br>
     * ：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * {}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * \： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     * 
     * @param template ， {}
     * @param params
     * @return
     */
    public static String format(String template, Object... params)
    {
        if (isEmpty(params) || isEmpty(template))
        {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * http(s)://
     * 
     * @param link
     * @return
     */
    public static boolean ishttp(String link)
    {
        return StringUtils.startsWithAny(link, Constants.HTTP, Constants.HTTPS);
    }

    /**
     * set
     * 
     * @param str
     * @param sep
     * @return set
     */
    public static final Set<String> str2Set(String str, String sep)
    {
        return new HashSet<String>(str2List(str, sep, true, false));
    }

    /**
     * list
     * 
     * @param str
     * @param sep
     * @param filterBlank
     * @param trim
     * @return list
     */
    public static final List<String> str2List(String str, String sep, boolean filterBlank, boolean trim)
    {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isEmpty(str))
        {
            return list;
        }

        //
        if (filterBlank && StringUtils.isBlank(str))
        {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split)
        {
            if (filterBlank && StringUtils.isBlank(string))
            {
                continue;
            }
            if (trim)
            {
                string = string.trim();
            }
            list.add(string);
        }

        return list;
    }

    /**
     * collectionarray arrayvalue
     *
     * @param collection
     * @param array
     * @return boolean
     */
    public static boolean containsAny(Collection<String> collection, String... array)
    {
        if (isEmpty(collection) || isEmpty(array))
        {
            return false;
        }
        else
        {
            for (String str : array)
            {
                if (collection.contains(str))
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     *
     *
     * @param cs
     * @param searchCharSequences
     * @return
     */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences)
    {
        if (isEmpty(cs) || isEmpty(searchCharSequences))
        {
            return false;
        }
        for (CharSequence testStr : searchCharSequences)
        {
            if (containsIgnoreCase(cs, testStr))
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //
        boolean preCharIsUpperCase = true;
        //
        boolean curreCharIsUpperCase = true;
        //
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     *
     * 
     * @param str
     * @param strs
     * @return true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 。，。 ：HELLO_WORLD->HelloWorld
     * 
     * @param name
     * @return
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        //
        if (name == null || name.isEmpty())
        {
            //
            return "";
        }
        else if (!name.contains("_"))
        {
            // ，
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        //
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            // 、
            if (camel.isEmpty())
            {
                continue;
            }
            //
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     *
     * ：user_name->userName
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }
        if (s.indexOf(SEPARATOR) == -1)
        {
            return s;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     *
     * 
     * @param str
     * @param strs
     * @return
     */
    public static boolean matches(String str, List<String> strs)
    {
        if (isEmpty(str) || isEmpty(strs))
        {
            return false;
        }
        for (String pattern : strs)
        {
            if (isMatch(pattern, str))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * url:
     * ? ;
     * * ，;
     * ** ;
     * 
     * @param pattern
     * @param url url
     * @return
     */
    public static boolean isMatch(String pattern, String url)
    {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }

    /**
     * 0，。，，size， size。
     * 
     * @param num
     * @param size
     * @return ，。
     */
    public static final String padl(final Number num, final int size)
    {
        return padl(num.toString(), size, '0');
    }

    /**
     * 。ssize，size。
     * 
     * @param s
     * @param size
     * @param c
     * @return ，。
     */
    public static final String padl(final String s, final int size, final char c)
    {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null)
        {
            final int len = s.length();
            if (s.length() <= size)
            {
                for (int i = size - len; i > 0; i--)
                {
                    sb.append(c);
                }
                sb.append(s);
            }
            else
            {
                return s.substring(len - size, len);
            }
        }
        else
        {
            for (int i = size; i > 0; i--)
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}