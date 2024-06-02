package com.cs35.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cs35.common.annotation.Log;
import com.cs35.common.constant.UserConstants;
import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.entity.SysMenu;
import com.cs35.common.enums.BusinessType;
import com.cs35.common.utils.StringUtils;
import com.cs35.system.service.ISysMenuService;

/**
 * Menu information
 * 
 * @author cs35
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    /**
     * Get menu list
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menus);
    }

    /**
     * Get details by menu number
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return success(menuService.selectMenuById(menuId));
    }

    /**
     * Gets a menu drop-down list
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu)
    {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * Load the menu list tree for the corresponding role
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * New menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "Menu management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return error("New menu'" + menu.getMenuName() + "'Failed, the menu name already exists");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return error("New menu'" + menu.getMenuName() + "'Failed, the address must start with http(s)://");
        }
        menu.setCreateBy(getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * Modify menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "Menu management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
        if (!menuService.checkMenuNameUnique(menu))
        {
            return error("Modify menu'" + menu.getMenuName() + "'Failed, the menu name already exists");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return error("Modify menu'" + menu.getMenuName() + "'Failed, the address must start with http(s)://");
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return error("Modify menu'" + menu.getMenuName() + "'Failed, parent menu could not select themselves");
        }
        menu.setUpdateBy(getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * Delete menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "Menu management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return warn("Submenus exist and cannot be deleted");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return warn("The menu has been assigned and cannot be deleted");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}