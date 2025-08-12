// QuestionController.java
package com.example.hiringProcess.Question;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // ===== LEGACY (κρατάς αν χρειάζονται) =====
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

    // ΔΙΟΡΘΩΜΕΝΟ: update με JSON body (και path id)
    @PutMapping(path = "/question/{questionId}")
    public void updateQuestion(@PathVariable Integer questionId,
                               @RequestBody Question body) {
        questionService.updateQuestion(questionId, body);
    }

    // ===== ΝΕΑ ENDPOINTS ΓΙΑ ΤΟ UI =====

    /** Αριστερό panel: ερωτήσεις ανά step. */
    @GetMapping("/api/v1/step/{stepId}/questions")
    public List<QuestionLiteDTO> getQuestionsForStep(@PathVariable Integer stepId) {
        return questionService.getQuestionsForStep(stepId);
    }

    /** Create question κάτω από συγκεκριμένο step (το καλεί το AddQuestionModal). */
    @PostMapping("/api/v1/step/{stepId}/questions")
    public ResponseEntity<QuestionLiteDTO> createQuestionUnderStep(@PathVariable Integer stepId,
                                                                   @RequestBody QuestionCreateRequest req) {
        if (req == null || req.getName() == null || req.getName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        var created = questionService.createUnderStep(
                stepId,
                req.getName().trim(),
                req.getDescription() == null ? null : req.getDescription().trim()
        );
        var dto = new QuestionLiteDTO(created.getId(), created.getName());
        return ResponseEntity.created(URI.create("/api/v1/question/" + created.getId())).body(dto);
    }

    /** Δεξί panel: description + skills της επιλεγμένης ερώτησης. */
    @GetMapping("/api/v1/question/{questionId}/details")
    public QuestionDetailsDTO getQuestionDetails(@PathVariable Integer questionId) {
        return questionService.getQuestionDetails(questionId);
    }

    // QuestionController.java (πρόσθεσε)
    @PutMapping("/api/v1/question/{questionId}")
    public ResponseEntity<Void> updateQuestionDescAndSkills(@PathVariable Integer questionId,
                                                            @RequestBody QuestionUpdateRequest body) {
        questionService.updateDescriptionAndSkills(questionId,
                body.getDescription(),
                body.getSkillNames());
        return ResponseEntity.noContent().build();
    }

}
