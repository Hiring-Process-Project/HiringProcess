package com.example.hiringProcess.Questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionsService {
    private final QuestionsRepository questionsRepository;

    @Autowired
    public QuestionsService(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    public List<Questions> getQuestions() {
        return questionsRepository.findAll();
    }

    public Optional<Questions> getQuestion(Integer questionId) {
        return questionsRepository.findById(questionId);
    }

    public void addNewQuestion(Questions question) {
        questionsRepository.save(question);
    }

    public void deleteQuestion(Integer questionId) {
        boolean exists = questionsRepository.existsById(questionId);
        if (!exists) {
            throw new IllegalStateException("Question with id " + questionId + " does not exist");
        }
        questionsRepository.deleteById(questionId);
    }

//    @Transactional
//    public void updateQuestion(Integer questionId, String name) {
//        Questions question = questionsRepository.findById(questionId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Question with id " + questionId + " does not exist"));
//
//        if (name != null && !name.isEmpty() && !Objects.equals(question.getName(), name)) {
//            question.setName(name);
//        }
//    }
}
