package com.cs35.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cs35.common.core.domain.entity.SysDictData;
import com.cs35.common.utils.DictUtils;
import com.cs35.system.mapper.SysDictDataMapper;
import com.cs35.system.service.ISysDictDataService;

/**
 *
 * @author cs35
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{
    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    @Override
    public void deleteDictDataByIds(Long[] dictCodes)
    {
        for (Long dictCode : dictCodes)
        {
            SysDictData data = selectDictDataById(dictCode);
            dictDataMapper.deleteDictDataById(dictCode);
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    @Override
    public int insertDictData(SysDictData data)
    {
        int row = dictDataMapper.insertDictData(data);
        if (row > 0)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    @Override
    public int updateDictData(SysDictData data)
    {
        int row = dictDataMapper.updateDictData(data);
        if (row > 0)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}
