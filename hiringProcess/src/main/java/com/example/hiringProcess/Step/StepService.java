package com.example.hiringProcess.Step;

import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Question.Question;
import com.example.hiringProcess.Question.QuestionRepository;
import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Interview.InterviewRepository;
import com.example.hiringProcess.Step.StepSkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StepService {

    private final StepRepository stepRepository;
    private final QuestionRepository questionRepository;
    private final InterviewRepository interviewRepository;


    @Autowired
    public StepService(StepRepository stepRepository,
                       QuestionRepository questionRepository,
                       InterviewRepository interviewRepository) {
        this.stepRepository = stepRepository;
        this.questionRepository = questionRepository;
        this.interviewRepository = interviewRepository;
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

    @Transactional
    public void updateStep(Integer stepId, Step updatedStep) {
        Step existingStep = stepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalStateException("Step with id " + stepId + " does not exist"));

        if (updatedStep.getTitle() != null) {
            existingStep.setTitle(updatedStep.getTitle());
        }
        if (updatedStep.getDescription() != null) {
            existingStep.setDescription(updatedStep.getDescription());
        }
        if (updatedStep.getInterview() != null) {
            existingStep.setInterview(updatedStep.getInterview());
        }
        if (updatedStep.getQuestions() != null && !updatedStep.getQuestions().isEmpty()) {
            existingStep.getQuestions().clear();
            for (Question question : updatedStep.getQuestions()) {
                existingStep.addQuestion(question);
            }
        }
    }

    /**
     * Επιστρέφει τα skills των ερωτήσεων που ανήκουν στο συγκεκριμένο step (distinct ανά skill id).
     */
    public List<StepSkillDTO> getSkillsForStep(Integer stepId) {
        // Validate ότι υπάρχει το step (προαιρετικά, αλλά καλό για 404)
        stepRepository.findById(stepId)
                .orElseThrow(() -> new EntityNotFoundException("Step " + stepId + " not found"));

        List<Skill> skills = questionRepository.findDistinctSkillsByStepId(stepId);

        return skills.stream()
                .map(s -> new StepSkillDTO(stepId, s.getId(), s.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Step createStep(Integer interviewId, String title) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalStateException("Interview " + interviewId + " not found"));
        Step s = new Step();
        s.setTitle(title);
        s.setInterview(interview);
        return stepRepository.save(s);
    }


}

