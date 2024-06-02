package com.cs35.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cs35.common.annotation.Log;
import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.domain.entity.SysDictType;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.common.enums.BusinessType;
import com.cs35.common.utils.poi.ExcelUtil;
import com.cs35.system.service.ISysDictTypeService;

/**
 * Data dictionary information
 * 
 * @author cs35
 */
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController
{
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDictType dictType)
    {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    @Log(title = "Dictionary type", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType dictType)
    {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        util.exportExcel(response, list, "Dictionary type");
    }

    /**
     * Query the details of the dictionary type
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId)
    {
        return success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * New dictionary type
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "Dictionary type", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict)
    {
        if (!dictTypeService.checkDictTypeUnique(dict))
        {
            return error("Add dictionary'" + dict.getDictName() + "'Failed. The dictionary type already exists");
        }
        dict.setCreateBy(getUsername());
        return toAjax(dictTypeService.insertDictType(dict));
    }

    /**
     * Modify dictionary type
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "Dictionary type", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict)
    {
        if (!dictTypeService.checkDictTypeUnique(dict))
        {
            return error("Modify dictionary'" + dict.getDictName() + "'Failed. The dictionary type already exists");
        }
        dict.setUpdateBy(getUsername());
        return toAjax(dictTypeService.updateDictType(dict));
    }

    /**
     * Delete dictionary type
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "Dictionary type", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds)
    {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    /**
     * Refresh dictionary cache
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "Dictionary type", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        dictTypeService.resetDictCache();
        return success();
    }

    /**
     * Gets a list of dictionary selection boxes
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return success(dictTypes);
    }
}
