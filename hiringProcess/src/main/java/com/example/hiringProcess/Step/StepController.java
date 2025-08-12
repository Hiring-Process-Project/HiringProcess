package com.example.hiringProcess.Step;

import com.example.hiringProcess.Step.StepSkillDTO;
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
    public StepController(StepService stepService) {
        this.stepService = stepService;
    }

    @GetMapping(path = "/steps")
    public List<Step> getSteps() {
        return stepService.getSteps();
    }

    @GetMapping(path = "/step")
    public Optional<Step> getStep(@RequestParam Integer stepId) {
        return stepService.getStep(stepId);
    }

    @PostMapping(path = "/newstep")
    public void addNewStep(@RequestBody Step step) {
        stepService.addNewStep(step);
    }

    @DeleteMapping("/{stepId}")
    public void deleteStep(@PathVariable("stepId") Integer stepId) {
        stepService.deleteStep(stepId);
    }

    @PutMapping("/{stepId}")
    public void updateStep(@PathVariable("stepId") Integer stepId,
                           @RequestBody Step updatedStep) {
        stepService.updateStep(stepId, updatedStep);
    }



    // ΝΕΟ endpoint: λίστα από StepSkillDTO για το συγκεκριμένο step
    @GetMapping("/{stepId}/skills")
    public List<com.example.hiringProcess.Step.StepSkillDTO> getStepSkills(@PathVariable Integer stepId) {
        return stepService.getSkillsForStep(stepId);
    }

    @PostMapping
    public StepResponseDTO createStepFallback(@RequestBody StepCreateRequest req) {
        if (req.getInterviewId() == null || req.getTitle() == null || req.getTitle().isBlank()) {
            throw new IllegalStateException("interviewId and title required");
        }
        var created = stepService.createStep(req.getInterviewId(), req.getTitle().trim());
        return new StepResponseDTO(created.getId(), created.getTitle());
    }
}
