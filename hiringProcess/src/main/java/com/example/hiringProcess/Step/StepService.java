package com.example.hiringProcess.Step;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepService {

    private final StepRepository stepRepository;

    @Autowired
    public StepService(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    public List<Step> getSteps() {
        return stepRepository.findAll();
    }

    public Optional<Step> getStep(Integer stepId) {
        return stepRepository.findById(stepId);
    }

    public void addNewStep(Step step) {
        stepRepository.save(step);
    }

    public void deleteStep(Integer stepId) {
        boolean exists = stepRepository.existsById(stepId);
        if (!exists) {
            throw new IllegalStateException("Step with id " + stepId + " does not exist");
        }
        stepRepository.deleteById(stepId);
    }

//    @Transactional
//    public void updateStep(Integer stepId, ...) {
//        Step step = stepRepository.findById(stepId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Step with id " + stepId + " does not exist"));
//        // Ενημέρωση πεδίων εδώ
//    }
}
