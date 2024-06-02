package com.cs35.common.core.controller;

import com.cs35.common.constant.HttpStatus;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.common.core.page.PageDomain;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.common.core.page.TableSupport;
import com.cs35.common.utils.DateUtils;
import com.cs35.common.utils.PageUtils;
import com.cs35.common.utils.SecurityUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web
 *
 * @author cs35
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ，Date
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    /**
     * 
     */
    protected void startOrderBy() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 
     */
    protected void clearPage() {
        PageUtils.clearPage();
    }

    /**
     * 
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("query was successful");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 
     */
    public AjaxResult success(Object data) {
        return AjaxResult.success(data);
    }

    /**
     * 
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    /**
     * 
     */
    public AjaxResult warn(String message) {
        return AjaxResult.warn(message);
    }

    /**
     * 
     *
     * @param rows 
     * @return 
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 
     *
     * @param result 
     * @return 
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 
     */
    public LoginUser getLoginUser() {
        return SecurityUtils.getLoginUser();
    }

    /**
     * id
     */
    public Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * id
     */
    public Long getDeptId() {
        return getLoginUser().getDeptId();
    }

    /**
     * 
     */
    public String getUsername() {
        return getLoginUser().getUsername();
    }
}
