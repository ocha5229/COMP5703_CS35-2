package com.cs35.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.cs35.system.domain.SysUserRole;

/**
 *
 * @author cs35
 */
public interface SysUserRoleMapper
{
    public int deleteUserRoleByUserId(Long userId);

    public int deleteUserRole(Long[] ids);

    public int countUserRoleByRoleId(Long roleId);

    public int batchUserRole(List<SysUserRole> userRoleList);

    public int deleteUserRoleInfo(SysUserRole userRole);

    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
