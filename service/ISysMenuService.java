package com.cs35.system.service;

import java.util.List;
import java.util.Set;
import com.cs35.common.core.domain.TreeSelect;
import com.cs35.common.core.domain.entity.SysMenu;
import com.cs35.system.domain.vo.RouterVo;

/**
 *
 * @author cs35
 */
public interface ISysMenuService
{
    public List<SysMenu> selectMenuList(Long userId);

    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    public Set<String> selectMenuPermsByUserId(Long userId);

    public Set<String> selectMenuPermsByRoleId(Long roleId);

    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    public List<Long> selectMenuListByRoleId(Long roleId);

    public List<RouterVo> buildMenus(List<SysMenu> menus);

    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    public SysMenu selectMenuById(Long menuId);

    public boolean hasChildByMenuId(Long menuId);

    public boolean checkMenuExistRole(Long menuId);

    public int insertMenu(SysMenu menu);

    public int updateMenu(SysMenu menu);

    public int deleteMenuById(Long menuId);

    public boolean checkMenuNameUnique(SysMenu menu);
}
