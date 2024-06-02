package com.cs35.web.controller.question;

import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.domain.QuestionScoreTb;
import com.cs35.service.IQuestionScoreTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/question/score")
public class QuestionScoreTbController extends BaseController {
    @Autowired
    private IQuestionScoreTbService questionScoreTbService;


    @GetMapping("/list")
    public TableDataInfo list(QuestionScoreTb questionScoreTb) {
        startPage();
        List<QuestionScoreTb> list = questionScoreTbService.selectQuestionScoreTbList(questionScoreTb);
        return getDataTable(list);
    }


    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(questionScoreTbService.selectQuestionScoreTbById(id));
    }


    @PostMapping
    public AjaxResult add(@RequestBody QuestionScoreTb questionScoreTb) {
        return toAjax(questionScoreTbService.insertQuestionScoreTb(questionScoreTb));
    }


    @PutMapping
    public AjaxResult edit(@RequestBody QuestionScoreTb questionScoreTb) {
        return toAjax(questionScoreTbService.updateQuestionScoreTb(questionScoreTb));
    }


    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(questionScoreTbService.deleteQuestionScoreTbByIds(ids));
    }
}
