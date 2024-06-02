package com.cs35.system.service;

import java.util.List;
import com.cs35.system.domain.SysLogininfor;

/**
 *
 * @author cs35
 */
public interface ISysLogininforService
{
    public void insertLogininfor(SysLogininfor logininfor);

    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    public int deleteLogininforByIds(Long[] infoIds);

    public void cleanLogininfor();
}
