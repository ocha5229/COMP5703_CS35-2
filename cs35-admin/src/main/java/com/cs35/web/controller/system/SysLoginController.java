package com.cs35.web.controller.system;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cs35.common.constant.Constants;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.entity.SysMenu;
import com.cs35.common.core.domain.entity.SysUser;
import com.cs35.common.core.domain.model.LoginBody;
import com.cs35.common.utils.SecurityUtils;
import com.cs35.framework.web.service.SysLoginService;
import com.cs35.framework.web.service.SysPermissionService;
import com.cs35.system.service.ISysMenuService;

/**
 * Login authentication
 * 
 * @author cs35
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * Login method
     * 
     * @param loginBody Login information
     * @return result
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // Generate token
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * Get user information
     * 
     * @return User information
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // Role set
        Set<String> roles = permissionService.getRolePermission(user);
        // Permission set
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * Get routing information
     * 
     * @return Routing information
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
