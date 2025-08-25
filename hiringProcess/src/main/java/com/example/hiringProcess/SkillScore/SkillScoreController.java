package com.example.hiringProcess.SkillScore;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/skill-scores")
@RequiredArgsConstructor
public class SkillScoreController {

    private final SkillScoreService skillScoreService;

    /**
     * Upsert βαθμολογίας για (candidate, question, skill).
     * - 201 Created όταν δημιουργείται νέα εγγραφή
     * - 200 OK όταν γίνεται ενημέρωση υπάρχουσας
     */
    @PostMapping
    public ResponseEntity<SkillScoreResponseDTO> upsert(@RequestBody SkillScoreUpsertRequestDTO req) {
        SkillScoreResponseDTO resp = skillScoreService.upsert(req);
        return ResponseEntity
                .status(resp.created() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(resp);
    }

    /** Λίστα βαθμολογιών ενός υποψηφίου για συγκεκριμένη ερώτηση (για προφόρτωση στο UI). */
    @GetMapping("/candidate/{candidateId}/question/{questionId}")
    public List<SkillScoreResponseDTO> listForCandidateQuestion(
            @PathVariable int candidateId,
            @PathVariable int questionId
    ) {
        return skillScoreService.listForCandidateQuestion(candidateId, questionId);
    }

    /** Διαγραφή με id */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        skillScoreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** Διαγραφή βάσει tuple (candidate,question,skill) */
    @DeleteMapping
    public ResponseEntity<Void> deleteTuple(
            @RequestParam int candidateId,
            @RequestParam int questionId,
            @RequestParam int skillId
    ) {
        skillScoreService.deleteTuple(candidateId, questionId, skillId);
        return ResponseEntity.noContent().build();
    }
}
