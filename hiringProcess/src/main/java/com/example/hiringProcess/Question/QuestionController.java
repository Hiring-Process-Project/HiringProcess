package com.example.hiringProcess.Question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping(path = "/questions")
    public List<Question> getQuestions() {
        return questionService.getQuestions();
    }

    @GetMapping(path = "/question")
    public Optional<Question> getQuestion(@RequestParam Integer questionId) {
        return questionService.getQuestion(questionId);
    }

    @PostMapping(path = "/newQuestion")
    public void addNewQuestion(@RequestBody Question question) {
        questionService.addNewQuestion(question);
    }

    @DeleteMapping(path = "/question")
    public void deleteQuestion(@RequestParam Integer questionId) {
        questionService.deleteQuestion(questionId);
    }

    // Αν χρειάζεσαι update endpoint:
    /*
    @PutMapping(path = "/question")
    public void updateQuestion(@RequestParam Integer questionId, @RequestParam(required = false) String name) {
        questionService.updateQuestion(questionId, name);
    }
    */
}
