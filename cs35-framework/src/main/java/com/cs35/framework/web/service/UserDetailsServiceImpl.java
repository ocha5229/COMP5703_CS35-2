package com.cs35.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.cs35.common.core.domain.entity.SysUser;
import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.common.enums.UserStatus;
import com.cs35.common.exception.ServiceException;
import com.cs35.common.utils.MessageUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.system.service.ISysUserService;

/**
 *
 *
 * @author cs35
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
