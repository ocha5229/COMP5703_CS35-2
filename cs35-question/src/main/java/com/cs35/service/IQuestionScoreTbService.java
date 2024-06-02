package com.cs35.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cs35.domain.QuestionScoreTb;

import java.util.List;

public interface IQuestionScoreTbService extends IService<QuestionScoreTb> {

    List<QuestionScoreTb> selectQuestionScoreTbList(QuestionScoreTb questionScoreTb);

    QuestionScoreTb selectQuestionScoreTbById(Long id);

    int insertQuestionScoreTb(QuestionScoreTb questionScoreTb);

    int updateQuestionScoreTb(QuestionScoreTb questionScoreTb);

    int deleteQuestionScoreTbByIds(Long[] ids);
}
