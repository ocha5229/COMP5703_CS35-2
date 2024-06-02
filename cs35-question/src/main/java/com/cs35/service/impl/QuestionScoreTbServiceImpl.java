package com.cs35.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs35.domain.QuestionScoreTb;
import com.cs35.mapper.QuestionScoreTbMapper;
import com.cs35.service.IQuestionScoreTbService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestionScoreTbServiceImpl extends ServiceImpl<QuestionScoreTbMapper, QuestionScoreTb> implements IQuestionScoreTbService {


    @Override
    public List<QuestionScoreTb> selectQuestionScoreTbList(QuestionScoreTb questionScoreTb) {
        return this.list();
    }

    @Override
    public QuestionScoreTb selectQuestionScoreTbById(Long id) {
        return this.getById(id);
    }

    @Override
    public int insertQuestionScoreTb(QuestionScoreTb questionScoreTb) {
        return this.save(questionScoreTb) ? 1 : 0;
    }

    @Override
    public int updateQuestionScoreTb(QuestionScoreTb questionScoreTb) {
        return this.updateById(questionScoreTb) ? 1 : 0;
    }

    @Override
    public int deleteQuestionScoreTbByIds(Long[] ids) {
        return 0;
    }
}
