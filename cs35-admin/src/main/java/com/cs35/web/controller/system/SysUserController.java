package com.cs35.web.controller.system;

import com.cs35.common.annotation.Log;
import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.entity.SysRole;
import com.cs35.common.core.domain.entity.SysUser;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.common.enums.BusinessType;
import com.cs35.common.utils.SecurityUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.system.service.ISysRoleService;
import com.cs35.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cs35
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;


    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * Get details by user number
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * add user
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "User management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user)) {
            return error("Add a user'" + user.getUserName() + "'Failed. The login account already exists");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("Add a user'" + user.getUserName() + "'Failed. Cell phone number already exists");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("Add a user'" + user.getUserName() + "'Failed, the email account already exists");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * Modify user
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "User management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user)) {
            return error("Modify user'" + user.getUserName() + "'Failed. The login account already exists");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("Modify user'" + user.getUserName() + "'Failed. The login account already exists");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("Modify user'" + user.getUserName() + "'Failed. The login account already exists");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * Delete a user
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "User management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return error("The current user cannot be deleted");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * Reset password
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "User management", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * Status modification
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "User management", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * Obtain an authorization role based on the user number
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * User authorization role
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "User management", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

}
