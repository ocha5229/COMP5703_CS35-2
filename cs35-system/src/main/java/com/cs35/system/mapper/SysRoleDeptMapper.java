package com.cs35.system.mapper;

import java.util.List;
import com.cs35.system.domain.SysRoleDept;

/**
 *
 * @author cs35
 */
public interface SysRoleDeptMapper
{
    public int deleteRoleDeptByRoleId(Long roleId);

    public int deleteRoleDept(Long[] ids);

    public int selectCountRoleDeptByDeptId(Long deptId);

    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
