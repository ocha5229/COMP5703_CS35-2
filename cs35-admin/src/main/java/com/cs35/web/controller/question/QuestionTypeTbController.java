package com.cs35.web.controller.question;

import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.domain.QuestionTypeTb;
import com.cs35.service.IQuestionTypeTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/question/type")
public class QuestionTypeTbController extends BaseController {
    @Autowired
    private IQuestionTypeTbService questionTypeTbService;


    @GetMapping("/list")
    public TableDataInfo list(QuestionTypeTb questionTypeTb) {
        startPage();
        List<QuestionTypeTb> list = questionTypeTbService.selectQuestionTypeTbList(questionTypeTb);
        return getDataTable(list);
    }
    @GetMapping("/allList")
    public AjaxResult allList(QuestionTypeTb questionTypeTb) {
        List<QuestionTypeTb> list = questionTypeTbService.selectQuestionTypeTbList(questionTypeTb);
        return AjaxResult.success(list);
    }


    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(questionTypeTbService.selectQuestionTypeTbById(id));
    }


    @PostMapping
    public AjaxResult add(@RequestBody QuestionTypeTb questionTypeTb) {
        return toAjax(questionTypeTbService.insertQuestionTypeTb(questionTypeTb));
    }


    @PutMapping
    public AjaxResult edit(@RequestBody QuestionTypeTb questionTypeTb) {
        return toAjax(questionTypeTbService.updateQuestionTypeTb(questionTypeTb));
    }


    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Long id) {
        return toAjax(questionTypeTbService.deleteQuestionTypeTbById(id));
    }
}
