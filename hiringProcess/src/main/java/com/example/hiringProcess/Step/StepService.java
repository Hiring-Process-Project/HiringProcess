package com.example.hiringProcess.Step;

import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Interview.InterviewRepository;
import com.example.hiringProcess.Question.Question;
import com.example.hiringProcess.Question.QuestionRepository;
import com.example.hiringProcess.Skill.Skill;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;
    private final QuestionRepository questionRepository;
    private final InterviewRepository interviewRepository;

    /* ===== order helpers ===== */

    public List<Step> getStepsByInterviewSorted(int interviewId) {
        return stepRepository.findByInterviewIdOrderByPositionAsc(interviewId);
    }

    @Transactional
    public Step createAtEnd(int interviewId, String title, String description) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalStateException("Interview " + interviewId + " not found"));

        int max = stepRepository.findMaxPositionByInterviewId(interviewId);

        Step s = new Step();
        s.setTitle(title);
        s.setDescription(description);
        s.setInterview(interview);
        s.setPosition(max + 1);   // στο τέλος

        // ΜΗΝ ελέγχεις για null όταν είναι primitive – δώσε ρητά default
        s.setScore(0);

        return stepRepository.save(s);
    }

    @Transactional
    public void move(int stepId, String direction) {
        Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalStateException("Step " + stepId + " not found"));

        int interviewId = step.getInterview().getId();
        int from = step.getPosition();
        int to = "up".equalsIgnoreCase(direction) ? from - 1 : from + 1;
        if (to < 0) return;

        Optional<Step> otherOpt = stepRepository.findByInterviewIdAndPosition(interviewId, to);
        if (otherOpt.isEmpty()) return;

        Step other = otherOpt.get();

        // ΔΙΟΡΘΩΣΗ: getId() είναι primitive -> σύγκριση με '==', όχι equals()
        if (other.getId() == step.getId()) return;

        step.setPosition(to);
        other.setPosition(from);
        stepRepository.save(step);
        stepRepository.save(other);
    }

    @Transactional
    public void deleteAndCompact(int stepId) {
        Step s = stepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalStateException("Step " + stepId + " not found"));

        int interviewId = s.getInterview().getId();
        int deletedPos = s.getPosition();

        stepRepository.delete(s);

        List<Step> rest = stepRepository.findByInterviewIdOrderByPositionAsc(interviewId);
        for (Step st : rest) {
            if (st.getPosition() > deletedPos) {
                st.setPosition(st.getPosition() - 1);
            }
        }
        stepRepository.saveAll(rest);
    }

    /** Batch reorder με δύο φάσεις για αποφυγή unique conflicts */
    @Transactional
    public void reorder(int interviewId, List<Integer> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalStateException("stepIds is empty");
        }

        List<Step> steps = stepRepository.findByInterviewIdOrderByPositionAsc(interviewId);
        if (steps.isEmpty()) {
            throw new IllegalStateException("no steps for interview " + interviewId);
        }

        if (steps.size() != orderedIds.size()) {
            throw new IllegalStateException("mismatch size: db=" + steps.size() + " req=" + orderedIds.size());
        }

        Set<Integer> current = steps.stream().map(Step::getId).collect(Collectors.toSet());
        Set<Integer> incoming = new HashSet<>(orderedIds);
        if (!current.equals(incoming)) {
            throw new IllegalStateException("ids don't match interview steps: db=" + current + " req=" + incoming);
        }

        Map<Integer, Step> byId = steps.stream()
                .collect(Collectors.toMap(Step::getId, Function.identity()));

        // 1η φάση: προσωρινές θέσεις (αρνητικές)
        int tempPos = -orderedIds.size();
        for (Step s : steps) {
            s.setPosition(tempPos++);
        }
        stepRepository.saveAll(steps);
        stepRepository.flush();

        // 2η φάση: κανονικές θέσεις 0..N-1
        for (int i = 0; i < orderedIds.size(); i++) {
            Step s = byId.get(orderedIds.get(i));
            s.setPosition(i);
        }
        stepRepository.saveAll(steps);
    }

    /* ===== existing ===== */

    public List<Step> getSteps() { return stepRepository.findAll(); }

    public Optional<Step> getStep(Integer stepId) { return stepRepository.findById(stepId); }

    @Transactional
    public void addNewStep(Step step) {
        if (step.getInterview() != null && step.getInterview().getId() != 0) {
            int interviewId = step.getInterview().getId();
            int max = stepRepository.findMaxPositionByInterviewId(interviewId);
            step.setPosition(max + 1);
        }

        // primitive -> δώσε ρητά default
        step.setScore(0);

        stepRepository.save(step);
    }

    public void deleteStep(Integer stepId) { deleteAndCompact(stepId); }

    @Transactional
    public void updateStep(Integer stepId, Step updatedStep) {
        Step existingStep = stepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalStateException("Step with id " + stepId + " does not exist"));

        if (updatedStep.getTitle() != null) existingStep.setTitle(updatedStep.getTitle());
        if (updatedStep.getDescription() != null) existingStep.setDescription(updatedStep.getDescription());

        if (updatedStep.getInterview() != null && updatedStep.getInterview().getId() != 0) {
            int newInterviewId = updatedStep.getInterview().getId();
            Interview newInterview = interviewRepository.findById(newInterviewId)
                    .orElseThrow(() -> new IllegalStateException("Interview " + newInterviewId + " not found"));
            existingStep.setInterview(newInterview);
            int max = stepRepository.findMaxPositionByInterviewId(newInterviewId);
            existingStep.setPosition(max + 1);
        }

        if (updatedStep.getQuestions() != null) {
            existingStep.getQuestions().clear();
            for (Question q : updatedStep.getQuestions()) {
                existingStep.addQuestion(q);
            }
        }

        // Αν το Step.score είναι primitive (double/int), δεν κάνουμε null-check.
        // Αν θέλεις να επιτρέπεις update του score, απλώς πάρε την τιμή που έρχεται.
        // Προσοχή: αυτό θα γράψει 0 αν ο caller δεν στείλει κάτι «ειδικό».
        existingStep.setScore(updatedStep.getScore());

        stepRepository.save(existingStep);
    }

    public List<StepSkillDTO> getSkillsForStep(Integer stepId) {
        stepRepository.findById(stepId).orElseThrow(() ->
                new EntityNotFoundException("Step " + stepId + " not found"));
        List<Skill> skills = questionRepository.findDistinctSkillsByStepId(stepId);
        return skills.stream()
                .map(s -> new StepSkillDTO(stepId, s.getId(), s.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Step createStep(Integer interviewId, String title) {
        return createAtEnd(interviewId, title, "");
    }
}
