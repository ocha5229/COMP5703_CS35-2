package com.cs35.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.domain.*;

import java.util.List;
import java.util.Map;

public interface IQuestionTbService extends IService<QuestionTb> {

    List<QuestionTb> selectQuestionTbList(QuestionTb questionTb);

    QuestionTb selectQuestionTbById(Long id);

    int insertQuestionTb(QuestionTb questionTb);

    int updateQuestionTb(QuestionTb questionTb);

    int deleteQuestionTbByIds(Long[] ids);

    int insertQuestionOption(QuestionTb questionTb);

    int createGame(GameTb gameTb);

    List<GameTb> gameList(GameTb gameTb);

    GameTb gameInfo(Long gameId);

    QuestionTb startAnswer(QuestionTb questionTb);

    TableDataInfo historyList(Integer pageNum, Integer pageSize);

    GameTb historyInfo(GameTb gameTb);

    int deleteGame(Long gameId);

    int historyEvaluate(HistoryTb historyTb);

    HistoryTb getEvaluate(Long id);

    List<Map<String, Object>> integralList();

    Integer gameComment(GameCommentTb gameCommentTb);

    List<GameCommentTb> getGameComment(GameCommentTb gameCommentTb);

    List<Map<String, Object>> answerCancel(AnswerTb answerTb);
}
