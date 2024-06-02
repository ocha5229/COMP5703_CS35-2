package com.cs35.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class QuestionTb implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long gameId;

    private String questionTitle;

    private String type;
    private String hint;
    private String answer;



    @TableField(exist = false)
    private String typeValue;

    @TableField(exist = false)
    private String questionTypeNm;


    @TableField(exist = false)
    private List<QuestionItemTb> itemArr;
    @TableField(exist = false)
    private List<QuestionItemTb> questionItem;

    @TableField(exist = false)
    private String title;
    @TableField(exist = false)
    private Boolean require;
    @TableField(exist = false)
    private List<Map<String, Object>> relationItems;

    @TableField(exist = false)
    private Integer sort;
    @TableField(exist = false)
    private Long historyId;

    @TableField(exist = false)
    private Boolean end;


    @TableField(exist = false)
    private String choice;
    @TableField(exist = false)
    private String options;
    @TableField(exist = false)
    private String answerTime;



}
