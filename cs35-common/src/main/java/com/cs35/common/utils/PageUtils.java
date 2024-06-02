package com.cs35.common.utils;

import com.github.pagehelper.PageHelper;
import com.cs35.common.core.page.PageDomain;
import com.cs35.common.core.page.TableSupport;
import com.cs35.common.utils.sql.SqlUtil;

/**
 * 
 * 
 * @author cs35
 */
public class PageUtils extends PageHelper
{
    /**
     * 
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
