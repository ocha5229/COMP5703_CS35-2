package com.cs35.framework.web.service;

import com.cs35.common.constant.CacheConstants;
import com.cs35.common.constant.Constants;
import com.cs35.common.constant.UserConstants;
import com.cs35.common.core.domain.entity.SysUser;
import com.cs35.common.core.domain.model.RegisterBody;
import com.cs35.common.core.redis.RedisCache;
import com.cs35.common.exception.user.CaptchaException;
import com.cs35.common.exception.user.CaptchaExpireException;
import com.cs35.common.utils.MessageUtils;
import com.cs35.common.utils.SecurityUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.framework.manager.AsyncManager;
import com.cs35.framework.manager.factory.AsyncFactory;
import com.cs35.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author cs35
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;


    @Autowired
    private RedisCache redisCache;

    /**
     * 
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 
        boolean captchaEnabled = true;
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username)) {
            msg = "The username cannot be empty";
        } else if (StringUtils.isEmpty(password)) {
            msg = "User password cannot be empty";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "The account length must be between 2 and 20 characters";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "The password length must be between 5 and 20 characters";
        } else if (!userService.checkUserNameUnique(sysUser)) {
            msg = "Failed to save user " + username + ", registered account already exists";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "Registration failed, please contact the system administrator";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, ""));
            }
        }
        return msg;
    }

    /**
     * 
     *
     * @param username 
     * @param code     
     * @param uuid     
     * @return 
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
