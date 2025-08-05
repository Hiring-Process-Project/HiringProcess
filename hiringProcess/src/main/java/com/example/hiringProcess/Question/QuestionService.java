package com.example.hiringProcess.Question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestion(Integer questionId) {
        return questionRepository.findById(questionId);
    }

    public void addNewQuestion(Question question) {
        Optional<Question> questionById = questionRepository.findQuestionById(question.getId());
        if (questionById.isPresent()) {
            throw new IllegalStateException("ID already exists");
        }
        questionRepository.save(question);
    }

    public void deleteQuestion(Integer questionId) {
        boolean exists = questionRepository.existsById(questionId);
        if (!exists) {
            throw new IllegalStateException("Question with id " + questionId + " does not exist");
        }
        questionRepository.deleteById(questionId);
    }

    @Transactional
    public void updateQuestion(Integer questionId, Question updatedQuestion) {
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalStateException("Question with id " + questionId + " does not exist"));

        // Απλά πεδία
        if (updatedQuestion.getName() != null) {
            existingQuestion.setName(updatedQuestion.getName());
        }

        if (updatedQuestion.getDescription() != null) {
            existingQuestion.setDescription(updatedQuestion.getDescription());
        }

        // Σχέση με Step
        if (updatedQuestion.getStep() != null) {
            existingQuestion.setStep(updatedQuestion.getStep());
        }

        // Σχέση με Skills - Αν θέλεις πλήρη αντικατάσταση
        if (updatedQuestion.getSkills() != null && !updatedQuestion.getSkills().isEmpty()) {
            existingQuestion.getSkills().clear();
            existingQuestion.getSkills().addAll(updatedQuestion.getSkills());
        }

        // Σχέση με QuestionScore - Αν θέλεις πλήρη αντικατάσταση
        if (updatedQuestion.getQuestionScore() != null && !updatedQuestion.getQuestionScore().isEmpty()) {
            existingQuestion.getQuestionScore().clear();
            existingQuestion.getQuestionScore().addAll(updatedQuestion.getQuestionScore());
        }

        // Δεν χρειάζεται save() αν είσαι σε @Transactional (και είναι managed το entity)
    }


}
