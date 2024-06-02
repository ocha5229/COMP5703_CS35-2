package com.cs35.web.controller.system;

import com.cs35.common.annotation.Log;
import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.entity.SysRole;
import com.cs35.common.core.domain.entity.SysUser;
import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.common.enums.BusinessType;
import com.cs35.common.utils.StringUtils;
import com.cs35.framework.web.service.SysPermissionService;
import com.cs35.framework.web.service.TokenService;
import com.cs35.system.domain.SysUserRole;
import com.cs35.system.service.ISysRoleService;
import com.cs35.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Role information
 *
 * @author cs35
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysUserService userService;


    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }


    /**
     * Get details based on the role number
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * New role
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "Role management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return error("New role'" + role.getRoleName() + "'Failed. The role name already exists");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("New role'" + role.getRoleName() + "'Failed. The role permission already exists");
        }
        role.setCreateBy(getUsername());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * Modify save role
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role)) {
            return error("Modify role'" + role.getRoleName() + "'Failed. The role name already exists");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("Modify role'" + role.getRoleName() + "'Failed. The role permission already exists");
        }
        role.setUpdateBy(getUsername());

        if (roleService.updateRole(role) > 0) {
            // Update cache user permissions
            LoginUser loginUser = getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }
            return success();
        }
        return error("Modify role'" + role.getRoleName() + "'Failed. Please contact the administrator");
    }

    /**
     * Modify the permission to save data
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role management", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * Status modification
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role management", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * Deleting a role
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "Role management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * Gets a list of role selection boxes
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return success(roleService.selectRoleAll());
    }

    /**
     * Example Query the list of assigned user roles
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * Example Query the list of unassigned user roles
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * Unauthorize user
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role management", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * Cancel authorized users in batch
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role management", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * Select user authorization in batches
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role management", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }


}
