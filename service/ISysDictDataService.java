package com.cs35.system.service;

import java.util.List;
import com.cs35.common.core.domain.entity.SysDictData;

/**
 *
 * @author cs35
 */
public interface ISysDictDataService
{
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    public String selectDictLabel(String dictType, String dictValue);

    public SysDictData selectDictDataById(Long dictCode);

    public void deleteDictDataByIds(Long[] dictCodes);

    public int insertDictData(SysDictData dictData);

    public int updateDictData(SysDictData dictData);
}
