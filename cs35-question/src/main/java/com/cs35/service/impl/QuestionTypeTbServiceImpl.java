package com.cs35.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs35.domain.QuestionTb;
import com.cs35.domain.QuestionTypeTb;
import com.cs35.mapper.QuestionTypeTbMapper;
import com.cs35.service.IQuestionItemTbService;
import com.cs35.service.IQuestionTbService;
import com.cs35.service.IQuestionTypeTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class QuestionTypeTbServiceImpl extends ServiceImpl<QuestionTypeTbMapper, QuestionTypeTb> implements IQuestionTypeTbService {

    @Autowired
    private IQuestionTbService questionTbService;

    @Override
    public List<QuestionTypeTb> selectQuestionTypeTbList(QuestionTypeTb questionTypeTb) {
        return this.list();
    }

    @Override
    public QuestionTypeTb selectQuestionTypeTbById(Long id) {
        return this.getById(id);
    }

    @Override
    public int insertQuestionTypeTb(QuestionTypeTb questionTypeTb) {
        return this.save(questionTypeTb) ? 1 : 0;
    }

    @Override
    public int updateQuestionTypeTb(QuestionTypeTb questionTypeTb) {
        return this.updateById(questionTypeTb) ? 1 : 0;
    }

    @Override
    @Transactional
    public int deleteQuestionTypeTbById(Long id) {
        boolean b = this.removeById(id);
        questionTbService.remove(Wrappers.lambdaQuery(QuestionTb.class)
                .eq(QuestionTb::getGameId, id)
        );
        return b ? 1 : 0;
    }
}
