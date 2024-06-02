package com.cs35.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cs35.domain.QuestionTypeTb;

import java.util.List;

public interface IQuestionTypeTbService extends IService<QuestionTypeTb> {

    List<QuestionTypeTb> selectQuestionTypeTbList(QuestionTypeTb questionTypeTb);

    QuestionTypeTb selectQuestionTypeTbById(Long id);

    int insertQuestionTypeTb(QuestionTypeTb questionTypeTb);

    int updateQuestionTypeTb(QuestionTypeTb questionTypeTb);

    int deleteQuestionTypeTbById(Long id);
}
