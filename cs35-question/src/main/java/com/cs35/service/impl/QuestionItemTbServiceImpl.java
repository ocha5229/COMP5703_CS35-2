package com.cs35.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs35.domain.QuestionItemTb;
import com.cs35.mapper.QuestionItemTbMapper;
import com.cs35.service.IQuestionItemTbService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestionItemTbServiceImpl extends ServiceImpl<QuestionItemTbMapper, QuestionItemTb> implements IQuestionItemTbService {

    @Override
    public List<QuestionItemTb> selectQuestionItemTbList(QuestionItemTb questionItemTb) {
        LambdaQueryWrapper<QuestionItemTb> wrapper = Wrappers.lambdaQuery(QuestionItemTb.class);
        if (questionItemTb.getQuestionId() != null) {
            wrapper.eq(QuestionItemTb::getQuestionId, questionItemTb.getQuestionId());
        }
        return list(wrapper);
    }

    @Override
    public QuestionItemTb selectQuestionItemTbById(Long id) {
        return this.getById(id);
    }

    @Override
    public int insertQuestionItemTb(QuestionItemTb questionItemTb) {
        return this.save(questionItemTb) ? 1 : 0;
    }

    @Override
    public int updateQuestionItemTb(QuestionItemTb questionItemTb) {
        return this.updateById(questionItemTb) ? 1 : 0;
    }

    @Override
    public int deleteQuestionItemTbByIds(Long[] ids) {
        return 0;
    }
}
