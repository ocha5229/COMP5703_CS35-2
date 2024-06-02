package com.cs35.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cs35.common.annotation.Excel;
import com.cs35.common.annotation.Excel.ColumnType;
import com.cs35.common.core.domain.BaseEntity;

/**
 * Parameter configuration table sys_config
 * 
 * @author cs35
 */
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "Parameter primary key", cellType = ColumnType.NUMERIC)
    private Long configId;

    @Excel(name = "Parameter name")
    private String configName;

    @Excel(name = "Parameter key name")
    private String configKey;

    @Excel(name = "Parameter key value")
    private String configValue;

    @Excel(name = "System built-in", readConverterExp = "Y=yes,N=no")
    private String configType;

    public Long getConfigId()
    {
        return configId;
    }

    public void setConfigId(Long configId)
    {
        this.configId = configId;
    }

    public String getConfigName()
    {
        return configName;
    }

    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigType()
    {
        return configType;
    }

    public void setConfigType(String configType)
    {
        this.configType = configType;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
