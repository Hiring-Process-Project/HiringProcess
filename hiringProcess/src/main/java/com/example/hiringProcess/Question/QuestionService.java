// QuestionService.java
package com.example.hiringProcess.Question;

import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Skill.SkillRepository;   // <-- ΠΡΟΣΘΗΚΗ
import com.example.hiringProcess.Step.Step;
import com.example.hiringProcess.Step.StepRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final StepRepository stepRepository;
    private final QuestionMapper questionMapper;
    private final SkillRepository skillRepository;        // <-- ΠΡΟΣΘΗΚΗ

    public QuestionService(QuestionRepository questionRepository,
                           StepRepository stepRepository,
                           QuestionMapper questionMapper,
                           SkillRepository skillRepository) {          // <-- ΠΡΟΣΘΗΚΗ
        this.questionRepository = questionRepository;
        this.stepRepository = stepRepository;
        this.questionMapper = questionMapper;
        this.skillRepository = skillRepository;           // <-- ΠΡΟΣΘΗΚΗ
    }

    // ============ legacy ============
    public List<Question> getQuestions() { return questionRepository.findAll(); }

    public Optional<Question> getQuestion(Integer questionId) {
        return questionRepository.findById(questionId);
    }

    public void addNewQuestion(Question question) {
        if (question.getId() != 0 && questionRepository.findById(question.getId()).isPresent()) {
            throw new IllegalStateException("ID already exists");
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

        if (updatedQuestion.getName() != null)        existing.setName(updatedQuestion.getName());
        if (updatedQuestion.getDescription() != null) existing.setDescription(updatedQuestion.getDescription());
        if (updatedQuestion.getStep() != null)        existing.setStep(updatedQuestion.getStep());

        if (updatedQuestion.getSkills() != null && !updatedQuestion.getSkills().isEmpty()) {
            existing.getSkills().clear();
            existing.getSkills().addAll(updatedQuestion.getSkills());
        }
        if (updatedQuestion.getQuestionScore() != null && !updatedQuestion.getQuestionScore().isEmpty()) {
            existing.getQuestionScore().clear();
            existing.getQuestionScore().addAll(updatedQuestion.getQuestionScore());
        }
    }

    // ============ UI helpers ============
    public List<QuestionLiteDTO> getQuestionsForStep(Integer stepId) {
        return questionMapper.toLite(questionRepository.findByStep_Id(stepId));
    }

    @Transactional
    public Question createUnderStep(Integer stepId, String name, String description) {
        Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new EntityNotFoundException("Step " + stepId + " not found"));

        Question q = new Question();
        q.setStep(step);
        q.setName(name);
        q.setDescription(description);

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

        // description
        q.setDescription(description == null ? "" : description.trim());

        // skills
        if (skillNames == null) {
            q.getSkills().clear();
            return;
        }

        var wanted = skillNames.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(String::trim)
                .distinct()
                .toList();

        var existing = skillRepository.findByNameIn(wanted);

        // Δημιούργησε όσα λείπουν (αν δεν το θες, αφαίρεσέ το μπλοκ)
        var existingNames = existing.stream().map(Skill::getName).toList();
        for (String name : wanted) {
            if (!existingNames.contains(name)) {
                Skill s = new Skill();
                s.setName(name);
                existing.add(skillRepository.save(s));
            }
        }

        q.getSkills().clear();
        q.getSkills().addAll(existing);
    }
}
