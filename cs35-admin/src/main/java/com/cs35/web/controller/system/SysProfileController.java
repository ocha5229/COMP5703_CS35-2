package com.cs35.web.controller.system;

import com.cs35.common.annotation.Log;
import com.cs35.common.config.CS35Config;
import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.entity.SysUser;
import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.common.enums.BusinessType;
import com.cs35.common.utils.SecurityUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.common.utils.file.FileUploadUtils;
import com.cs35.common.utils.file.MimeTypeUtils;
import com.cs35.framework.web.service.TokenService;
import com.cs35.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Business processing of personal information
 *
 * @author cs35
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * Personal information
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * Modify user
     */
    @Log(title = "Personal information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser)) {
            return error("Modify user'" + loginUser.getUsername() + "'Failed. Cell phone number already exists");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser)) {
            return error("Modify user'" + loginUser.getUsername() + "'Failed, the email account already exists");
        }
        if (userService.updateUserProfile(currentUser) > 0) {
            // Update cached user information
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("If the personal information cannot be modified, contact the administrator");
    }

    /**
     * Reset password
     */
    @Log(title = "Personal information", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return error("Failed to change the password because the old password is incorrect");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return error("The new password cannot be the same as the old password");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userName, newPassword) > 0) {
            // Update the cache user password
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("If the password cannot be changed, contact the administrator");
    }

    /**
     * Profile picture upload
     */
    @Log(title = "User profile picture", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(CS35Config.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return error("The uploaded image is abnormal. Please contact the administrator");
    }
}
