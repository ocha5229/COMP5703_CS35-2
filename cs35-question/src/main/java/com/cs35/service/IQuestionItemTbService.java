package com.cs35.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cs35.domain.QuestionItemTb;

import java.util.List;

public interface IQuestionItemTbService extends IService<QuestionItemTb> {

    List<QuestionItemTb> selectQuestionItemTbList(QuestionItemTb questionItemTb);

    QuestionItemTb selectQuestionItemTbById(Long id);

    int insertQuestionItemTb(QuestionItemTb questionItemTb);

    int updateQuestionItemTb(QuestionItemTb questionItemTb);

    int deleteQuestionItemTbByIds(Long[] ids);
}
