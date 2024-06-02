package com.cs35.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.cs35.common.annotation.Excel;
import com.cs35.common.annotation.Excel.ColumnType;
import com.cs35.common.core.domain.BaseEntity;

/**
 * System access record table sys_logininfor
 * 
 * @author cs35
 */
public class SysLogininfor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "Serial number", cellType = ColumnType.NUMERIC)
    private Long infoId;

    @Excel(name = "User account")
    private String userName;

    @Excel(name = "Login status", readConverterExp = "0=success,1=failure")
    private String status;

    @Excel(name = "Login address")
    private String ipaddr;

    @Excel(name = "Login location")
    private String loginLocation;

    @Excel(name = "Browser")
    private String browser;

    @Excel(name = "Operating system")
    private String os;

    @Excel(name = "Prompt message")
    private String msg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Access time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    public Long getInfoId()
    {
        return infoId;
    }

    public void setInfoId(Long infoId)
    {
        this.infoId = infoId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Date getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Date loginTime)
    {
        this.loginTime = loginTime;
    }
}
