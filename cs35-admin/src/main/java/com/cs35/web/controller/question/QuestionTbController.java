package com.cs35.web.controller.question;

import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.domain.*;
import com.cs35.service.IQuestionTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/question")
public class QuestionTbController extends BaseController {


    @Autowired
    private IQuestionTbService questionTbService;


    @GetMapping("/list")
    public TableDataInfo list(QuestionTb questionTb) {
        startPage();
        List<QuestionTb> list = questionTbService.selectQuestionTbList(questionTb);
        return getDataTable(list);
    }


    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(questionTbService.selectQuestionTbById(id));
    }


    @PutMapping
    public AjaxResult edit(@RequestBody QuestionTb questionTb) {
        return toAjax(questionTbService.updateQuestionTb(questionTb));
    }


    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(questionTbService.deleteQuestionTbByIds(ids));
    }


    @PostMapping("/option")
    public AjaxResult option(@RequestBody QuestionTb questionTb) {
        return toAjax(questionTbService.insertQuestionOption(questionTb));
    }


    @PostMapping
    public AjaxResult add(@RequestBody QuestionTb questionTb) {
        return toAjax(questionTbService.insertQuestionTb(questionTb));
    }


    @PostMapping("/createGame")
    public AjaxResult createGame(@RequestBody GameTb gameTb) {
        return toAjax(questionTbService.createGame(gameTb));
    }


    @GetMapping("/game/list")
    public TableDataInfo gameList(GameTb gameTb) {
        startPage();
        List<GameTb> list = questionTbService.gameList(gameTb);
        return getDataTable(list);
    }


    @GetMapping("/game/{gameId}")
    public AjaxResult gameList(@PathVariable("gameId") Long gameId) {
        GameTb info = questionTbService.gameInfo(gameId);
        return AjaxResult.success(info);
    }

    @PostMapping(value = "/answer")
    public AjaxResult startAnswer(@RequestBody QuestionTb questionTb) {
        return AjaxResult.success(questionTbService.startAnswer(questionTb));
    }


    @GetMapping("/answer/cancel")
    public AjaxResult answerCancel(AnswerTb answerTb) {
        return AjaxResult.success(questionTbService.answerCancel(answerTb));
    }


    @GetMapping(value = "/historyList")
    public TableDataInfo historyList(Integer pageNum, Integer pageSize) {
        TableDataInfo gameTbs = questionTbService.historyList(pageNum, pageSize);
        return gameTbs;
    }


    @PostMapping("/history/evaluate")
    public AjaxResult historyEvaluate(@RequestBody HistoryTb historyTb) {
        return AjaxResult.success(questionTbService.historyEvaluate(historyTb));
    }


    @GetMapping("/history/info")
    public AjaxResult gameHistoryInfo(GameTb gameTb) {
        GameTb info = questionTbService.historyInfo(gameTb);
        return AjaxResult.success(info);
    }


    @DeleteMapping("/game/{gameId}")
    public AjaxResult deleteGame(@PathVariable Long gameId) {
        return toAjax(questionTbService.deleteGame(gameId));
    }


    @GetMapping("/history/{id}")
    public AjaxResult getEvaluate(@PathVariable("id") Long id) {
        HistoryTb info = questionTbService.getEvaluate(id);
        return AjaxResult.success(info);
    }


    @GetMapping("/integral/list")
    public TableDataInfo integralList() {
        List<Map<String, Object>> result = questionTbService.integralList();
        return getDataTable(result);
    }


    @PostMapping("/game/comment")
    public AjaxResult gameComment(@RequestBody GameCommentTb gameCommentTb) {
        return AjaxResult.success(questionTbService.gameComment(gameCommentTb));
    }


    @GetMapping("/game/comment")
    public AjaxResult getGameComment(GameCommentTb gameCommentTb) {
        return AjaxResult.success(questionTbService.getGameComment(gameCommentTb));
    }
}
