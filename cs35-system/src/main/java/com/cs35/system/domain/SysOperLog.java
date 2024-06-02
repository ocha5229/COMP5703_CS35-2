package com.cs35.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.cs35.common.annotation.Excel;
import com.cs35.common.annotation.Excel.ColumnType;
import com.cs35.common.core.domain.BaseEntity;

/**
 * Operation log record table oper_log
 * 
 * @author cs35
 */
public class SysOperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "Operation sequence number", cellType = ColumnType.NUMERIC)
    private Long operId;

    @Excel(name = "Operation module")
    private String title;

    @Excel(name = "Business type", readConverterExp = "0=Other,1=new,2=change,3=delete,4=authorization,5=export,6=import,7=strongly back,8=generated code,9=empty data")
    private Integer businessType;

    private Integer[] businessTypes;

    @Excel(name = "Request method")
    private String method;

    @Excel(name = "Request mode")
    private String requestMethod;

    @Excel(name = "Operation class", readConverterExp = "0=other,1=Background user,2=Mobile user")
    private Integer operatorType;

    @Excel(name = "operator")
    private String operName;

    @Excel(name = "Department name")
    private String deptName;

    @Excel(name = "Request address")
    private String operUrl;

    @Excel(name = "Operational address")
    private String operIp;

    @Excel(name = "Operating location")
    private String operLocation;

    @Excel(name = "Request parameter")
    private String operParam;

    @Excel(name = "Return parameter")
    private String jsonResult;

    @Excel(name = "status", readConverterExp = "0=normal,1=abnormal")
    private Integer status;

    @Excel(name = "Error message")
    private String errorMsg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Operating time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    @Excel(name = "Consume time", suffix = "ms")
    private Long costTime;

    public Long getOperId()
    {
        return operId;
    }

    public void setOperId(Long operId)
    {
        this.operId = operId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
    }

    public Integer[] getBusinessTypes()
    {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes)
    {
        this.businessTypes = businessTypes;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public Integer getOperatorType()
    {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType)
    {
        this.operatorType = operatorType;
    }

    public String getOperName()
    {
        return operName;
    }

    public void setOperName(String operName)
    {
        this.operName = operName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getOperUrl()
    {
        return operUrl;
    }

    public void setOperUrl(String operUrl)
    {
        this.operUrl = operUrl;
    }

    public String getOperIp()
    {
        return operIp;
    }

    public void setOperIp(String operIp)
    {
        this.operIp = operIp;
    }

    public String getOperLocation()
    {
        return operLocation;
    }

    public void setOperLocation(String operLocation)
    {
        this.operLocation = operLocation;
    }

    public String getOperParam()
    {
        return operParam;
    }

    public void setOperParam(String operParam)
    {
        this.operParam = operParam;
    }

    public String getJsonResult()
    {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult)
    {
        this.jsonResult = jsonResult;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public Date getOperTime()
    {
        return operTime;
    }

    public void setOperTime(Date operTime)
    {
        this.operTime = operTime;
    }

    public Long getCostTime()
    {
        return costTime;
    }

    public void setCostTime(Long costTime)
    {
        this.costTime = costTime;
    }
}
