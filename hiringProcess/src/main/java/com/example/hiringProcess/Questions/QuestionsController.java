package com.example.hiringProcess.Questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "api/v1/Questions")
public class QuestionsController {

    private final QuestionsService questionsService;

    @Autowired
    public QuestionsController(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @GetMapping(path = "/questions")
    public List<Questions> getQuestions() {
        return questionsService.getQuestions();
    }

    @GetMapping(path = "/question")
    public Optional<Questions> getQuestion(Integer questionId) {
        return questionsService.getQuestion(questionId);
    }

    @PostMapping(path = "/newquestion")
    public void addNewQuestion(Questions question) {
        questionsService.addNewQuestion(question);
    }

//    @DeleteMapping(path = "{questionId}")
//    public void deleteQuestion(@PathVariable("questionId") Integer id) {
//        questionsService.deleteQuestion(id);
//    }

//    @PutMapping(path = "{questionId}")
//    public void updateQuestion(@PathVariable("questionId") Integer questionId,
//                               @RequestParam(required = false) String name) {
//        questionsService.updateQuestion(questionId, name);
//    }
}
