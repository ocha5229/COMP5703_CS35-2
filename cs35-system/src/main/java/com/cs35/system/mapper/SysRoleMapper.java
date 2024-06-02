package com.cs35.system.mapper;

import java.util.List;
import com.cs35.common.core.domain.entity.SysRole;

/**
 *
 * @author cs35
 */
public interface SysRoleMapper
{
    public List<SysRole> selectRoleList(SysRole role);

    public List<SysRole> selectRolePermissionByUserId(Long userId);

    public List<SysRole> selectRoleAll();

    public List<Long> selectRoleListByUserId(Long userId);

    public SysRole selectRoleById(Long roleId);
    public SysRole selectRoleByCode(String roleCode);

    public List<SysRole> selectRolesByUserName(String userName);

    public SysRole checkRoleNameUnique(String roleName);

    public SysRole checkRoleKeyUnique(String roleKey);

    public int updateRole(SysRole role);

    public int insertRole(SysRole role);

    public int deleteRoleById(Long roleId);

    public int deleteRoleByIds(Long[] roleIds);
}
