package com.cs35.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cs35.common.annotation.Excel;
import com.cs35.common.annotation.Excel.ColumnType;
import com.cs35.common.core.domain.BaseEntity;

/**
 *  sys_dict_type
 * 
 * @author cs35
 */
public class SysDictType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    @Excel(name = "", cellType = ColumnType.NUMERIC)
    private Long dictId;

    /**  */
    @Excel(name = "")
    private String dictName;

    /**  */
    @Excel(name = "")
    private String dictType;

    /** （0 1） */
    @Excel(name = "", readConverterExp = "0=,1=")
    private String status;

    public Long getDictId()
    {
        return dictId;
    }

    public void setDictId(Long dictId)
    {
        this.dictId = dictId;
    }

    @NotBlank(message = "")
    @Size(min = 0, max = 100, message = "100")
    public String getDictName()
    {
        return dictName;
    }

    public void setDictName(String dictName)
    {
        this.dictName = dictName;
    }

    @NotBlank(message = "")
    @Size(min = 0, max = 100, message = "100")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "，（，，）")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
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
            .append("dictId", getDictId())
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
