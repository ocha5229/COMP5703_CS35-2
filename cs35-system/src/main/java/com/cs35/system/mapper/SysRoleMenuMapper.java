package com.cs35.system.mapper;

import java.util.List;
import com.cs35.system.domain.SysRoleMenu;

/**
 *
 * @author cs35
 */
public interface SysRoleMenuMapper
{
    public int checkMenuExistRole(Long menuId);

    public int deleteRoleMenuByRoleId(Long roleId);

    public int deleteRoleMenu(Long[] ids);

    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);
}
