package com.example.hiringProcess.QuestionScore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "api/v1/QuestionScore")
public class QuestionScoreController {

    private final QuestionScoreService questionScoreService;

    @Autowired
    public QuestionScoreController(QuestionScoreService questionScoreService) {
        this.questionScoreService = questionScoreService;
    }

    @GetMapping(path = "/questionScores")
    public List<QuestionScore> getQuestionScores() {
        return questionScoreService.getQuestionScores();
    }

    @GetMapping(path = "/questionScore")
    public Optional<QuestionScore> getQuestionScore(Integer questionScoreId) {
        return questionScoreService.getQuestionScore(questionScoreId);
    }

    @PostMapping(path = "/newQuestionScore")
    public void addNewQuestionScore(QuestionScore questionScore) {
        questionScoreService.addNewQuestionScore(questionScore);
    }

//    @DeleteMapping(path = "{questionScoreId}")
//    public void deleteQuestionScore(@PathVariable("questionScoreId") Integer id) {
//        questionScoreService.deleteQuestionScore(id);
//    }

//    @PutMapping(path = "{questionScoreId}")
//    public void updateQuestionScore(@PathVariable("questionScoreId") Integer questionScoreId,
//                                    @RequestParam(required = false) double score) {
//        questionScoreService.updateQuestionScore(questionScoreId, score);
//    }
}
