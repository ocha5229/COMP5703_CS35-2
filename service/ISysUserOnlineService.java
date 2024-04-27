package com.cs35.system.service;

import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.system.domain.SysUserOnline;

/**
 *
 * @author cs35
 */
public interface ISysUserOnlineService
{
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    public SysUserOnline loginUserToUserOnline(LoginUser user);
}
