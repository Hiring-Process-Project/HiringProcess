// src/main/java/com/example/hiringProcess/Question/QuestionService.java
package com.example.hiringProcess.Question;

import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Skill.SkillRepository;
import com.example.hiringProcess.Step.Step;
import com.example.hiringProcess.Step.StepRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final StepRepository stepRepository;
    private final QuestionMapper questionMapper;
    private final SkillRepository skillRepository;

    public QuestionService(QuestionRepository questionRepository,
                           StepRepository stepRepository,
                           QuestionMapper questionMapper,
                           SkillRepository skillRepository) {
        this.questionRepository = questionRepository;
        this.stepRepository = stepRepository;
        this.questionMapper = questionMapper;
        this.skillRepository = skillRepository;
    }

    // ============ legacy ============
    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestion(Integer questionId) {
        return questionRepository.findById(questionId);
    }

    public void addNewQuestion(Question question) {
        if (question.getId() != 0 && questionRepository.findById(question.getId()).isPresent()) {
            throw new IllegalStateException("ID already exists");
        }
        // Αν δεν έχει οριστεί position, βάλε την στο τέλος του step (αν έχει step)
        if (question.getStep() != null && question.getPosition() == null) {
            long count = questionRepository.countByStep_Id(question.getStep().getId());
            question.setPosition((int) count);
        }
        questionRepository.save(question);
    }

    public void deleteQuestion(Integer questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new IllegalStateException("Question with id " + questionId + " does not exist");
        }
        questionRepository.deleteById(questionId);
    }

    @Transactional
    public void updateQuestion(Integer questionId, Question updatedQuestion) {
        Question existing = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("Question with id " + questionId + " does not exist"));

        if (updatedQuestion.getTitle() != null) existing.setTitle(updatedQuestion.getTitle());
        if (updatedQuestion.getDescription() != null) existing.setDescription(updatedQuestion.getDescription());
        if (updatedQuestion.getStep() != null) existing.setStep(updatedQuestion.getStep());

        if (updatedQuestion.getSkills() != null) {
            existing.getSkills().clear();
            existing.getSkills().addAll(updatedQuestion.getSkills());
        }
        if (updatedQuestion.getQuestionScore() != null) {
            existing.getQuestionScore().clear();
            existing.getQuestionScore().addAll(updatedQuestion.getQuestionScore());
        }
    }

    // ============ UI helpers ============
    public List<QuestionLiteDTO> getQuestionsForStep(Integer stepId) {
        var list = questionRepository.findByStep_IdOrderByPositionAsc(stepId);
        return questionMapper.toLite(list);
    }

    @Transactional
    public Question createUnderStep(Integer stepId, String name, String description) {
        Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new EntityNotFoundException("Step " + stepId + " not found"));

        Question q = new Question();
        q.setStep(step);
        q.setTitle(name);
        q.setDescription(description);

        // set position στο τέλος
        long count = questionRepository.countByStep_Id(stepId);
        q.setPosition((int) count);

        return questionRepository.save(q);
    }

    public QuestionDetailsDTO getQuestionDetails(Integer questionId) {
        Question q = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question " + questionId + " not found"));
        return questionMapper.toDetails(q);
    }

    @Transactional
    public void updateDescriptionAndSkills(Integer questionId, String description, List<String> skillNames) {
        var q = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question " + questionId + " not found"));

        q.setDescription(description == null ? "" : description.trim());

        if (skillNames == null) {
            q.getSkills().clear();
            return;
        }

        var wanted = skillNames.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(String::trim)
                .distinct()
                .toList();

        var existing = skillRepository.findByTitleIn(wanted);

        var existingTitles = existing.stream().map(Skill::getTitle).toList();
        for (String title : wanted) {
            if (!existingTitles.contains(title)) {
                Skill s = new Skill();
                s.setTitle(title);
                existing.add(skillRepository.save(s));
            }
        }

        q.getSkills().clear();
        q.getSkills().addAll(existing);
    }

    /* ========= ΝΕΟ: Reorder μέσα στο ίδιο step ========= */
    @Transactional
    public void reorderInStep(Integer stepId, List<Integer> questionIdsInNewOrder) {
        if (questionIdsInNewOrder == null) return;

        // Τωρινές ερωτήσεις του step ταξινομημένες
        List<Question> current = questionRepository.findByStep_IdOrderByPositionAsc(stepId);

        // id -> Question
        Map<Integer, Question> byId = current.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        // Θέσε position με βάση το νέο Order
        int pos = 0;
        for (Integer qid : questionIdsInNewOrder) {
            Question q = byId.get(qid);
            if (q != null) q.setPosition(pos++);
        }

        // Όποιες δεν μπήκαν (π.χ. λάθος client), πάνε στο τέλος
        for (Question q : current) {
            if (q.getPosition() == null) q.setPosition(pos++);
        }
        // @Transactional -> flush αυτόματα
    }

    /* ========= ΝΕΟ: Μετακίνηση ερώτησης σε άλλο step (και θέση) ========= */
    @Transactional
    public void moveQuestion(Integer questionId, Integer toStepId, Integer toIndex) {
        if (toStepId == null) throw new IllegalArgumentException("toStepId is required");

        Question q = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question " + questionId + " not found"));

        Step targetStep = stepRepository.findById(toStepId)
                .orElseThrow(() -> new EntityNotFoundException("Step " + toStepId + " not found"));

        // Αν αλλάζει step
        boolean stepChanged = (q.getStep() == null) || (q.getStep().getId() != toStepId);
        if (stepChanged) {
            q.setStep(targetStep);
        }

        // Φέρε όλες τις ερωτήσεις του target step
        List<Question> target = questionRepository.findByStep_IdOrderByPositionAsc(toStepId);

        // Αφαίρεσε αν υπάρχει ήδη
        target.removeIf(it -> Objects.equals(it.getId(), q.getId()));
        // Πρόσθεσε προσωρινά στο τέλος και μετά στο ζητούμενο index
        target.add(q);

        long count = target.size();
        int safeIndex = Math.max(0, Math.min(toIndex == null ? (int) (count - 1) : toIndex, (int) (count - 1)));

        Question moved = target.remove(target.size() - 1);
        target.add(safeIndex, moved);

        // Επανυπολόγισε positions
        for (int i = 0; i < target.size(); i++) {
            Question qi = target.get(i);
            qi.setPosition(i);
            qi.setStep(targetStep);
        }
    }
}
