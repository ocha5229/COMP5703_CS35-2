package com.cs35.framework.web.service;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.cs35.common.constant.CacheConstants;
import com.cs35.common.constant.Constants;
import com.cs35.common.constant.UserConstants;
import com.cs35.common.core.domain.entity.SysUser;
import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.common.core.redis.RedisCache;
import com.cs35.common.exception.ServiceException;
import com.cs35.common.exception.user.BlackListException;
import com.cs35.common.exception.user.CaptchaException;
import com.cs35.common.exception.user.CaptchaExpireException;
import com.cs35.common.exception.user.UserNotExistsException;
import com.cs35.common.exception.user.UserPasswordNotMatchException;
import com.cs35.common.utils.DateUtils;
import com.cs35.common.utils.MessageUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.common.utils.ip.IpUtils;
import com.cs35.framework.manager.AsyncManager;
import com.cs35.framework.manager.factory.AsyncFactory;
import com.cs35.framework.security.context.AuthenticationContextHolder;
import com.cs35.system.service.ISysUserService;

/**
 *
 * 
 * @author cs35
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysUserService userService;



    /**
     *
     * 
     * @param username
     * @param password
     * @param code
     * @param uuid
     * @return
     */
    public String login(String username, String password, String code, String uuid)
    {
        //
        validateCaptcha(username, code, uuid);
        //
        loginPreCheck(username, password);
        //
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // token
        return tokenService.createToken(loginUser);
    }

    /**
     *
     * 
     * @param username
     * @param code
     * @param uuid
     * @return
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        boolean captchaEnabled = true;
        if (captchaEnabled)
        {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     *
     * @param username
     * @param password
     */
    public void loginPreCheck(String username, String password)
    {
        //
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        //
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        //
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP
//        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
//        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
//            throw new BlackListException();
//        }
    }

    /**
     *
     *
     * @param userId ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
