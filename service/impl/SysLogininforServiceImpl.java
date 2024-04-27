package com.cs35.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cs35.system.domain.SysLogininfor;
import com.cs35.system.mapper.SysLogininforMapper;
import com.cs35.system.service.ISysLogininforService;

/**
 *
 * @author cs35
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService
{

    @Autowired
    private SysLogininforMapper logininforMapper;

    @Override
    public void insertLogininfor(SysLogininfor logininfor)
    {
        logininforMapper.insertLogininfor(logininfor);
    }

    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor)
    {
        return logininforMapper.selectLogininforList(logininfor);
    }

    @Override
    public int deleteLogininforByIds(Long[] infoIds)
    {
        return logininforMapper.deleteLogininforByIds(infoIds);
    }

    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
