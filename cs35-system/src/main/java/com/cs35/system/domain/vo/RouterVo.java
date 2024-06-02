package com.cs35.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Route configuration information
 * 
 * @author cs35
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo
{
    /**
     * Route name
     */
    private String name;

    /**
     * Routing address
     */
    private String path;

    /**
     * Whether to hide a route. When true is set, the route will not appear in the sidebar
     */
    private boolean hidden;

    /**
     * Redirect address. This route cannot be clicked in the breadcrumbs navigation when the noRedirect is set
     */
    private String redirect;

    /**
     * Component address
     */
    private String component;

    /**
     * Route parameters: for example, {"id": 1, "name": "ry"}
     */
    private String query;

    /**
     * When you have more than one route declared by children under a route, it automatically becomes a nested pattern - such as component pages
     */
    private Boolean alwaysShow;

    /**
     * Other elements
     */
    private MetaVo meta;

    /**
     * subroute
     */
    private List<RouterVo> children;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public boolean getHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public String getRedirect()
    {
        return redirect;
    }

    public void setRedirect(String redirect)
    {
        this.redirect = redirect;
    }

    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public Boolean getAlwaysShow()
    {
        return alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow)
    {
        this.alwaysShow = alwaysShow;
    }

    public MetaVo getMeta()
    {
        return meta;
    }

    public void setMeta(MetaVo meta)
    {
        this.meta = meta;
    }

    public List<RouterVo> getChildren()
    {
        return children;
    }

    public void setChildren(List<RouterVo> children)
    {
        this.children = children;
    }
}
