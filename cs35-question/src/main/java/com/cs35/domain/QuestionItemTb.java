package com.cs35.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionItemTb implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long questionId;

    private String item;

    private Integer result;

    private String calResult;

    private String textValue;

    private Long lastQuesId;
    private String score;

    @TableField(exist = false)
    private Boolean correct;
    @TableField(exist = false)
    private String content;
    @TableField(exist = false)
    private Boolean isShow;
    @TableField(exist = false)
    private Boolean isRepulsion;


}
