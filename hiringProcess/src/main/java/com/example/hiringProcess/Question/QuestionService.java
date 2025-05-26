package com.example.hiringProcess.Question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /*
    @Transactional
    public void updateQuestion(Integer questionId, String name) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new IllegalStateException("Question with id " + questionId + " does not exist"));

        if (name != null && !name.isEmpty() && !Objects.equals(question.getName(), name)) {
            question.setName(name);
        }
    }
    */
}
