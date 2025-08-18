package com.example.hiringProcess.Step;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/step")
public class StepController {

    private final StepService stepService;

    @Autowired
    public StepController(StepService stepService) { this.stepService = stepService; }

    /* ======= Υπάρχοντα ======= */
    @GetMapping(path = "/steps")
    public List<Step> getSteps() { return stepService.getSteps(); }

    @GetMapping(path = "/step")
    public Optional<Step> getStep(@RequestParam Integer stepId) { return stepService.getStep(stepId); }

    @PostMapping(path = "/newstep")
    public void addNewStep(@RequestBody Step step) { stepService.addNewStep(step); }

    @DeleteMapping("/{stepId}")
    public void deleteStep(@PathVariable("stepId") Integer stepId) { stepService.deleteStep(stepId); }

    @PutMapping("/{stepId}")
    public void updateStep(@PathVariable("stepId") Integer stepId, @RequestBody Step updatedStep) {
        stepService.updateStep(stepId, updatedStep);
    }

    /* ======= Skills για step ======= */
    @GetMapping("/{stepId}/skills")
    public List<StepSkillDTO> getStepSkills(@PathVariable Integer stepId) {
        return stepService.getSkillsForStep(stepId);
    }

    /* ======= Ordering API ======= */
    @GetMapping("/interviews/{interviewId}/steps")
    public List<Step> listByInterview(@PathVariable Integer interviewId) {
        return stepService.getStepsByInterviewSorted(interviewId);
    }

    @PostMapping("/interviews/{interviewId}/steps")
    public Step createAtEnd(@PathVariable Integer interviewId, @RequestBody StepCreateRequest req) {
        String title = req.getTitle() == null ? "" : req.getTitle().trim();
        String desc  = req.getDescription() == null ? "" : req.getDescription().trim();
        if (title.isBlank()) throw new IllegalStateException("title required");
        return stepService.createAtEnd(interviewId, title, desc);
    }

    @PatchMapping("/{stepId}/move")
    public void move(@PathVariable Integer stepId, @RequestParam String direction) {
        stepService.move(stepId, direction);
    }

    @PatchMapping("/interviews/{interviewId}/steps/reorder")
    public ResponseEntity<?> reorder(
            @PathVariable int interviewId,
            @RequestBody StepReorderRequest body) {
        try {
            stepService.reorder(interviewId, body.getStepIds());
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            // Επιστρέφουμε 400 με μήνυμα για να ΔΕΙΣ τι φταίει από το frontend
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    /* ======= Συμβατότητα ======= */
    @PostMapping
    public StepResponseDTO createStepFallback(@RequestBody StepCreateRequest req) {
        if (req.getInterviewId() == null || req.getTitle() == null || req.getTitle().isBlank())
            throw new IllegalStateException("interviewId and title required");
        var created = stepService.createAtEnd(req.getInterviewId(), req.getTitle().trim(),
                req.getDescription() == null ? "" : req.getDescription());
        return new StepResponseDTO(created.getId(), created.getTitle());
    }
}
