package com.cs35.system.mapper;

import java.util.List;
import com.cs35.system.domain.SysLogininfor;

/**
 *
 * @author cs35
 */
public interface SysLogininforMapper
{
    public void insertLogininfor(SysLogininfor logininfor);

    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    public int deleteLogininforByIds(Long[] infoIds);

    public int cleanLogininfor();
}
