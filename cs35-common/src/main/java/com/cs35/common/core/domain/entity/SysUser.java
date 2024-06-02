package com.cs35.common.core.domain.entity;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cs35.common.annotation.Excel;
import com.cs35.common.annotation.Excel.ColumnType;
import com.cs35.common.annotation.Excel.Type;
import com.cs35.common.annotation.Excels;
import com.cs35.common.core.domain.BaseEntity;
import com.cs35.common.xss.Xss;

/**
 *  sys_user
 * 
 * @author cs35
 */
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "", cellType = ColumnType.NUMERIC, prompt = "")
    private Long userId;

    /** ID */
    @Excel(name = "", type = Type.IMPORT)
    private Long deptId;

    /**  */
    @Excel(name = "")
    private String userName;

    /**  */
    @Excel(name = "")
    private String nickName;

    /**  */
    @Excel(name = "")
    private String email;

    /**  */
    @Excel(name = "", cellType = ColumnType.TEXT)
    private String phonenumber;

    /**  */
    @Excel(name = "", readConverterExp = "0=,1=,2=")
    private String sex;

    /**  */
    private String avatar;

    /**  */
    private String password;

    /** （0 1） */
    @Excel(name = "", readConverterExp = "0=,1=")
    private String status;

    /** （0 2） */
    private String delFlag;

    /** IP */
    @Excel(name = "IP", type = Type.EXPORT)
    private String loginIp;

    /**  */
    @Excel(name = "", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /**  */
    @Excels({
        @Excel(name = "", targetAttr = "deptName", type = Type.EXPORT),
        @Excel(name = "", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDept dept;

    /**  */
    private List<SysRole> roles;

    /**  */
    private Long[] roleIds;

    /**  */
    private Long[] postIds;

    /** ID */
    private Long roleId;

    public SysUser()
    {

    }

    public SysUser(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @Xss(message = "")
    @Size(min = 0, max = 30, message = "30")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Xss(message = "")
    @NotBlank(message = "")
    @Size(min = 0, max = 30, message = "30")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Email(message = "")
    @Size(min = 0, max = 50, message = "50")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "11")
    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public Date getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public SysDept getDept()
    {
        return dept;
    }

    public void setDept(SysDept dept)
    {
        this.dept = dept;
    }

    public List<SysRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<SysRole> roles)
    {
        this.roles = roles;
    }

    public Long[] getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds)
    {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds()
    {
        return postIds;
    }

    public void setPostIds(Long[] postIds)
    {
        this.postIds = postIds;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("email", getEmail())
            .append("phonenumber", getPhonenumber())
            .append("sex", getSex())
            .append("avatar", getAvatar())
            .append("password", getPassword())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("dept", getDept())
            .toString();
    }
}
