package com.cs35.common.utils.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean 
 * 
 * @author cs35
 */
public class BeanUtils extends org.springframework.beans.BeanUtils
{
    /** Bean */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /** * getter */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /** * setter */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean。
     * 
     * @param dest 
     * @param src 
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        try
        {
            copyProperties(src, dest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * setter。
     * 
     * @param obj 
     * @return setter
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        // setter
        List<Method> setterMethods = new ArrayList<Method>();

        // 
        Method[] methods = obj.getClass().getMethods();

        // setter

        for (Method method : methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        // setter
        return setterMethods;
    }

    /**
     * getter。
     * 
     * @param obj 
     * @return getter
     */

    public static List<Method> getGetterMethods(Object obj)
    {
        // getter
        List<Method> getterMethods = new ArrayList<Method>();
        // 
        Method[] methods = obj.getClass().getMethods();
        // getter
        for (Method method : methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        // getter
        return getterMethods;
    }

    /**
     * Bean。<br>
     * getName()setName()，getName()setAge()。
     * 
     * @param m1 1
     * @param m2 2
     * @return true，false
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }
}
