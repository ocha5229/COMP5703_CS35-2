package com.cs35.framework.web.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.cs35.common.core.domain.entity.SysRole;
import com.cs35.common.core.domain.entity.SysUser;
import com.cs35.system.service.ISysMenuService;
import com.cs35.system.service.ISysRoleService;

/**
 *
 * 
 * @author cs35
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     *
     * 
     * @param user
     * @return
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<String>();
        //
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     *
     * 
     * @param user
     * @return
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        //
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else
        {
            List<SysRole> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles))
            {
                // permissions，
                for (SysRole role : roles)
                {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            }
            else
            {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
