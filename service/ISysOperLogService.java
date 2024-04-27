package com.cs35.system.service;

import java.util.List;
import com.cs35.system.domain.SysOperLog;

/**
 *
 * @author cs35
 */
public interface ISysOperLogService
{
    public void insertOperlog(SysOperLog operLog);

    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    public int deleteOperLogByIds(Long[] operIds);

    public SysOperLog selectOperLogById(Long operId);

    public void cleanOperLog();
}
