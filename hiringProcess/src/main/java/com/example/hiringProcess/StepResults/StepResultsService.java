package com.example.hiringProcess.StepResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepResultsService {
    private final StepResultsRepository stepResultsRepository;

    @Autowired
    public StepResultsService(StepResultsRepository stepResultsRepository) {
        this.stepResultsRepository = stepResultsRepository;
    }

    public List<StepResults> getStepResults() {
        return stepResultsRepository.findAll();
    }

    public Optional<StepResults> getStepResult(Integer stepResultId) {
        return stepResultsRepository.findById(stepResultId);
    }

    public void addNewStepResult(StepResults stepResults) {
        stepResultsRepository.save(stepResults);
    }

    public void deleteStepResult(Integer stepResultId) {
        boolean exists = stepResultsRepository.existsById(stepResultId);
        if (!exists) {
            throw new IllegalStateException("StepResult with id " + stepResultId + " does not exist");
        }
        stepResultsRepository.deleteById(stepResultId);
    }

//    @Transactional
//    public void updateStepResult(Integer stepResultId, ...) {
//        StepResults stepResults = stepResultsRepository.findById(stepResultId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "StepResult with id " + stepResultId + " does not exist"));
//
//        // update logic here
//    }
}
