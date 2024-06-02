package com.cs35.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cs35.common.annotation.Excel;
import com.cs35.common.annotation.Excel.ColumnType;
import com.cs35.common.constant.UserConstants;
import com.cs35.common.core.domain.BaseEntity;

/**
 *  sys_dict_data
 * 
 * @author cs35
 */
public class SysDictData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    @Excel(name = "", cellType = ColumnType.NUMERIC)
    private Long dictCode;

    /**  */
    @Excel(name = "", cellType = ColumnType.NUMERIC)
    private Long dictSort;

    /**  */
    @Excel(name = "")
    private String dictLabel;

    /**  */
    @Excel(name = "")
    private String dictValue;

    /**  */
    @Excel(name = "")
    private String dictType;

    /** （） */
    private String cssClass;

    /**  */
    private String listClass;

    /** （Y N） */
    @Excel(name = "", readConverterExp = "Y=,N=")
    private String isDefault;

    /** （0 1） */
    @Excel(name = "", readConverterExp = "0=,1=")
    private String status;

    public Long getDictCode()
    {
        return dictCode;
    }

    public void setDictCode(Long dictCode)
    {
        this.dictCode = dictCode;
    }

    public Long getDictSort()
    {
        return dictSort;
    }

    public void setDictSort(Long dictSort)
    {
        this.dictSort = dictSort;
    }

    @NotBlank(message = "")
    @Size(min = 0, max = 100, message = "100")
    public String getDictLabel()
    {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel)
    {
        this.dictLabel = dictLabel;
    }

    @NotBlank(message = "")
    @Size(min = 0, max = 100, message = "100")
    public String getDictValue()
    {
        return dictValue;
    }

    public void setDictValue(String dictValue)
    {
        this.dictValue = dictValue;
    }

    @NotBlank(message = "")
    @Size(min = 0, max = 100, message = "100")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    @Size(min = 0, max = 100, message = "100")
    public String getCssClass()
    {
        return cssClass;
    }

    public void setCssClass(String cssClass)
    {
        this.cssClass = cssClass;
    }

    public String getListClass()
    {
        return listClass;
    }

    public void setListClass(String listClass)
    {
        this.listClass = listClass;
    }

    public boolean getDefault()
    {
        return UserConstants.YES.equals(this.isDefault);
    }

    public String getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(String isDefault)
    {
        this.isDefault = isDefault;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictCode", getDictCode())
            .append("dictSort", getDictSort())
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("dictType", getDictType())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
