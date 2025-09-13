package com.example.hiringProcess.QuestionScore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:5173"})
@RestController
@RequestMapping("/api/v1/question-scores")
public class QuestionScoreController {

    private final QuestionScoreService questionScoreService;

    @Autowired
    public QuestionScoreController(QuestionScoreService questionScoreService) {
        this.questionScoreService = questionScoreService;
    }

    // Επιστρέφει όλα τα QuestionScores
    @GetMapping
    public List<QuestionScore> getAll() {
        return questionScoreService.getAll();
    }

    // Επιστρέφει ένα QuestionScore με βάση το id
    @GetMapping("/{id}")
    public ResponseEntity<QuestionScore> getById(@PathVariable("id") Integer id) {
        return questionScoreService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Δημιουργεί νέο QuestionScore
    @PostMapping
    public ResponseEntity<QuestionScore> create(@RequestBody QuestionScore questionScore) {
        QuestionScore saved = questionScoreService.create(questionScore);
        URI location = URI.create("/api/v1/question-scores/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    // Κάνει update σε υπάρχον QuestionScore
    @PutMapping("/{id}")
    public ResponseEntity<QuestionScore> update(
            @PathVariable("id") Integer id,
            @RequestBody QuestionScore updatedFields
    ) {
        QuestionScore updated = questionScoreService.update(id, updatedFields);
        return ResponseEntity.ok(updated);
    }

    // Διαγράφει QuestionScore με βάση το id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        questionScoreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Επιστρέφει metrics (σύνολο skills, rated skills, average score) για ερωτήσεις ενός interviewReport
    @GetMapping("/metrics-by-report")
    public ResponseEntity<List<QuestionMetricsItemDTO>> getMetricsByReport(
            @RequestParam Integer interviewReportId,
            @RequestParam String questionIds
    ) {
        List<Integer> qids = Arrays.stream(questionIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::valueOf)
                .toList();

        return ResponseEntity.ok(
                questionScoreService.getQuestionMetricsByReport(interviewReportId, qids)
        );
    }

    // Επιστρέφει metrics για ερωτήσεις συγκεκριμένου candidate (μέσω του interviewReport του)
    @GetMapping("/metrics")
    public ResponseEntity<List<QuestionMetricsItemDTO>> getMetricsByCandidate(
            @RequestParam Integer candidateId,
            @RequestParam String questionIds
    ) {
        List<Integer> qids = Arrays.stream(questionIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::valueOf)
                .toList();

        return ResponseEntity.ok(
                questionScoreService.getQuestionMetricsByCandidate(candidateId, qids)
        );
    }
}
