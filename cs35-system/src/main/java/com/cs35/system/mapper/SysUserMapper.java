package com.cs35.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.cs35.common.core.domain.entity.SysUser;

/**
 *
 * @author cs35
 */
public interface SysUserMapper
{
    public List<SysUser> selectUserList(SysUser sysUser);

    public List<SysUser> selectAllocatedList(SysUser user);

    public List<SysUser> selectUnallocatedList(SysUser user);

    public SysUser selectUserByUserName(String userName);

    public SysUser selectUserById(Long userId);

    public int insertUser(SysUser user);

    public int updateUser(SysUser user);

    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    public int deleteUserById(Long userId);

    public int deleteUserByIds(Long[] userIds);

    public SysUser checkUserNameUnique(String userName);

    public SysUser checkPhoneUnique(String phonenumber);

    public SysUser checkEmailUnique(String email);
}
