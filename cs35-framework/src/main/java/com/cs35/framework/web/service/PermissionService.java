package com.cs35.framework.web.service;

import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.cs35.common.constant.Constants;
import com.cs35.common.core.domain.entity.SysRole;
import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.common.utils.SecurityUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.framework.security.context.PermissionContextHolder;

/**
 *
 * @author cs35
 */
@Service("ss")
public class PermissionService
{
    /**
     *
     * 
     * @param permission
     * @return
     */
    public boolean hasPermi(String permission)
    {
        if (StringUtils.isEmpty(permission))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * ， hasPermi
     *
     * @param permission
     * @return
     */
    public boolean lacksPermi(String permission)
    {
        return hasPermi(permission) != true;
    }

    /**
     *
     *
     * @param permissions  PERMISSION_DELIMETER
     * @return
     */
    public boolean hasAnyPermi(String permissions)
    {
        if (StringUtils.isEmpty(permissions))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        PermissionContextHolder.setContext(permissions);
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(Constants.PERMISSION_DELIMETER))
        {
            if (permission != null && hasPermissions(authorities, permission))
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * 
     * @param role
     * @return
     */
    public boolean hasRole(String role)
    {
        if (StringUtils.isEmpty(role))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (SysRole sysRole : loginUser.getUser().getRoles())
        {
            String roleKey = sysRole.getRoleKey();
            if (Constants.SUPER_ADMIN.equals(roleKey) || roleKey.equals(StringUtils.trim(role)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * ， isRole。
     *
     * @param role
     * @return
     */
    public boolean lacksRole(String role)
    {
        return hasRole(role) != true;
    }

    /**
     *
     *
     * @param roles  ROLE_NAMES_DELIMETER
     * @return
     */
    public boolean hasAnyRoles(String roles)
    {
        if (StringUtils.isEmpty(roles))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (String role : roles.split(Constants.ROLE_DELIMETER))
        {
            if (hasRole(role))
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * 
     * @param permissions
     * @param permission
     * @return
     */
    private boolean hasPermissions(Set<String> permissions, String permission)
    {
        return permissions.contains(Constants.ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
