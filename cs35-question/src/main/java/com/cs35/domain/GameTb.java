package com.cs35.domain;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GameTb implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long questionTypeId;

    private String gameTitle;

    private String remark;
    private Integer sort;

    @TableField(exist = false)
    private String fields;
    @TableField(exist = false)
    private String questionTypeNm;
    @TableField(exist = false)
    private String nickName;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @TableField(exist = false)
    private Long historyId;
    @TableField(exist = false)
    private String evaluate;
    @TableField(exist = false)
    private String answerTime;

}
