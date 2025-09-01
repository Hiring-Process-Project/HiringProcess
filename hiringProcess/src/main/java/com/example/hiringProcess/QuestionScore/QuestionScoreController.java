package com.example.hiringProcess.QuestionScore;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Candidate.CandidateRepository;
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
    private final CandidateRepository candidateRepository; // για το bridge endpoint με candidateId

    @Autowired
    public QuestionScoreController(QuestionScoreService questionScoreService,
                                   CandidateRepository candidateRepository) {
        this.questionScoreService = questionScoreService;
        this.candidateRepository = candidateRepository;
    }

    // ===== CRUD =====

    @GetMapping
    public List<QuestionScore> getAll() {
        return questionScoreService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionScore> getById(@PathVariable("id") Integer id) {
        return questionScoreService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<QuestionScore> create(@RequestBody QuestionScore questionScore) {
        QuestionScore saved = questionScoreService.create(questionScore);
        URI location = URI.create("/api/v1/question-scores/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionScore> update(
            @PathVariable("id") Integer id,
            @RequestBody QuestionScore updatedFields
    ) {
        QuestionScore updated = questionScoreService.update(id, updatedFields);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        questionScoreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Metrics (1) με interviewReportId =====
    // Παράδειγμα:
    // GET /api/v1/question-scores/metrics-by-report?interviewReportId=9&questionIds=13,14,15
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

    // ===== Metrics (2) bridge με candidateId -> βρίσκουμε εδώ το interviewReportId και καλούμε την ίδια service =====
    // Παράδειγμα:
    // GET /api/v1/question-scores/metrics?candidateId=10&questionIds=13,14,15
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

        Candidate cand = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException("Candidate " + candidateId + " does not exist"));

        Integer interviewReportId = (cand.getInterviewReport() != null) ? cand.getInterviewReport().getId() : null;

        return ResponseEntity.ok(
                questionScoreService.getQuestionMetricsByReport(interviewReportId, qids)
        );
    }
}
