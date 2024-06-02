package com.cs35.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HistoryTb implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.INPUT)
    private Long id;
    private Long gameId;
    private Long userId;
    private Long gameType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String evaluate;

}
