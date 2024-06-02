package com.cs35.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class AnswerTb implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long gameId;
    private Long questionId;
    private String options;
    private int sort;
    private int score;
    private Long userId;
    private Long historyId;
    private String answerTime;


}
