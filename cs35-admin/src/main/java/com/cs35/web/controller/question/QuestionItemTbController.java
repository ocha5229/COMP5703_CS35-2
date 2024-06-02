package com.cs35.web.controller.question;

import com.cs35.common.core.controller.BaseController;
import com.cs35.common.core.domain.AjaxResult;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.domain.QuestionItemTb;
import com.cs35.service.IQuestionItemTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/question/item")
public class QuestionItemTbController extends BaseController {
    @Autowired
    private IQuestionItemTbService questionItemTbService;


    @GetMapping("/list")
    public AjaxResult list(QuestionItemTb questionItemTb) {
        List<QuestionItemTb> list = questionItemTbService.selectQuestionItemTbList(questionItemTb);
        return AjaxResult.success(list);
    }


    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(questionItemTbService.selectQuestionItemTbById(id));
    }


    @PostMapping
    public AjaxResult add(@RequestBody QuestionItemTb questionItemTb) {
        return toAjax(questionItemTbService.insertQuestionItemTb(questionItemTb));
    }


    @PutMapping
    public AjaxResult edit(@RequestBody QuestionItemTb questionItemTb) {
        return toAjax(questionItemTbService.updateQuestionItemTb(questionItemTb));
    }


    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(questionItemTbService.deleteQuestionItemTbByIds(ids));
    }
}
