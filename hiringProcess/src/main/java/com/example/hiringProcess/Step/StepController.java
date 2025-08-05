package com.example.hiringProcess.Step;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
}
