package com.example.hiringProcess.StepResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "api/v1/StepResults")
public class StepResultsController {

    private final StepResultsService stepResultsService;

    @Autowired
    public StepResultsController(StepResultsService stepResultsService) {
        this.stepResultsService = stepResultsService;
    }

    @GetMapping(path = "/stepResults")
    public List<StepResults> getStepResults() {
        return stepResultsService.getStepResults();
    }

    @GetMapping(path = "/stepResult")
    public Optional<StepResults> getStepResult(Integer stepResultId) {
        return stepResultsService.getStepResult(stepResultId);
    }

    @PostMapping(path = "/newStepResult")
    public void addNewStepResult(StepResults stepResults) {
        stepResultsService.addNewStepResult(stepResults);
    }

//    @DeleteMapping(path = "{stepResultId}")
//    public void deleteStepResult(@PathVariable("stepResultId") Integer id) {
//        stepResultsService.deleteStepResult(id);
//    }

//    @PutMapping(path = "{stepResultId}")
//    public void updateStepResult(@PathVariable("stepResultId") Integer stepResultId,
//                                 @RequestParam(required = false) ...) {
//        stepResultsService.updateStepResult(stepResultId, ...);
//    }
}
