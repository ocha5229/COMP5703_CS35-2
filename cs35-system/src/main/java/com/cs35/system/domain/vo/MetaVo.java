package com.cs35.system.domain.vo;

import com.cs35.common.utils.StringUtils;

/**
 * Route display information
 * 
 * @author cs35
 */
public class MetaVo
{
    /**
     * Set the name of the route displayed in the sidebar and breadcrumbs
     */
    private String title;

    /**
     * Set the icon of the route, corresponding to the path src/assets/icons/svg
     */
    private String icon;

    /**
     * If set to true, it will not be cached by <keep-alive>
     */
    private boolean noCache;

    /**
     * Internal link address (http(s):// begins)
     */
    private String link;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public MetaVo(String title, String icon, boolean noCache)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }

    public MetaVo(String title, String icon, String link)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public MetaVo(String title, String icon, boolean noCache, String link)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        if (StringUtils.ishttp(link))
        {
            this.link = link;
        }
    }

    public boolean isNoCache()
    {
        return noCache;
    }

    public void setNoCache(boolean noCache)
    {
        this.noCache = noCache;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}
