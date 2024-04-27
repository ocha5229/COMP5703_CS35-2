package com.cs35.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cs35.system.domain.SysOperLog;
import com.cs35.system.mapper.SysOperLogMapper;
import com.cs35.system.service.ISysOperLogService;

/**
 *
 * @author cs35
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
