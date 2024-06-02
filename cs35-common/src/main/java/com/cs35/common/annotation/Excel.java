package com.cs35.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import com.cs35.common.utils.poi.ExcelHandlerAdapter;

/**
 * Excel
 * 
 * @author cs35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel
{
    /**
     * excel
     */
    public int sort() default Integer.MAX_VALUE;

    /**
     * Excel.
     */
    public String name() default "";

    /**
     * , : yyyy-MM-dd
     */
    public String dateFormat() default "";

    /**
     * ，type (: sys_user_sex)
     */
    public String dictType() default "";

    /**
     *  (: 0=,1=,2=)
     */
    public String readConverterExp() default "";

    /**
     * ，
     */
    public String separator() default ",";

    /**
     * BigDecimal  :-1(BigDecimal)
     */
    public int scale() default -1;

    /**
     * BigDecimal  :BigDecimal.ROUND_HALF_EVEN
     */
    public int roundingMode() default BigDecimal.ROUND_HALF_EVEN;

    /**
     * excel
     */
    public double height() default 14;

    /**
     * excel
     */
    public double width() default 16;

    /**
     * ,% 90 90%
     */
    public String suffix() default "";

    /**
     * ,
     */
    public String defaultValue() default "";

    /**
     * 
     */
    public String prompt() default "";

    /**
     * .
     */
    public String[] combo() default {};

    /**
     * ,:list)
     */
    public boolean needMerge() default false;

    /**
     * ,:,.
     */
    public boolean isExport() default true;

    /**
     * ,,
     */
    public String targetAttr() default "";

    /**
     * ,
     */
    public boolean isStatistics() default false;

    /**
     * （0 1 2）
     */
    public ColumnType cellType() default ColumnType.STRING;

    /**
     * 
     */
    public IndexedColors headerBackgroundColor() default IndexedColors.GREY_50_PERCENT;

    /**
     * 
     */
    public IndexedColors headerColor() default IndexedColors.WHITE;

    /**
     * 
     */
    public IndexedColors backgroundColor() default IndexedColors.WHITE;

    /**
     * 
     */
    public IndexedColors color() default IndexedColors.BLACK;

    /**
     * 
     */
    public HorizontalAlignment align() default HorizontalAlignment.CENTER;

    /**
     * 
     */
    public Class<?> handler() default ExcelHandlerAdapter.class;

    /**
     * 
     */
    public String[] args() default {};

    /**
     * （0：；1：；2：）
     */
    Type type() default Type.ALL;

    public enum Type
    {
        ALL(0), EXPORT(1), IMPORT(2);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    public enum ColumnType
    {
        NUMERIC(0), STRING(1), IMAGE(2), TEXT(3);
        private final int value;

        ColumnType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}