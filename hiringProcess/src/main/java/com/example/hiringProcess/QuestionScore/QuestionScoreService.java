package com.example.hiringProcess.QuestionScore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionScoreService {
    private final QuestionScoreRepository questionScoreRepository;

    @Autowired
    public QuestionScoreService(QuestionScoreRepository questionScoreRepository) {
        this.questionScoreRepository = questionScoreRepository;
    }

    public List<QuestionScore> getQuestionScores() {
        return questionScoreRepository.findAll();
    }

    public Optional<QuestionScore> getQuestionScore(Integer questionScoreId) {
        return questionScoreRepository.findById(questionScoreId);
    }

    public void addNewQuestionScore(QuestionScore questionScore) {
        questionScoreRepository.save(questionScore);
    }

    public void deleteQuestionScore(Integer questionScoreId) {
        boolean exists = questionScoreRepository.existsById(questionScoreId);
        if (!exists) {
            throw new IllegalStateException("QuestionScore with id " + questionScoreId + " does not exist");
        }
        questionScoreRepository.deleteById(questionScoreId);
    }

//    @Transactional
//    public void updateQuestionScore(Integer questionScoreId, double score) {
//        QuestionScore questionScore = questionScoreRepository.findById(questionScoreId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "QuestionScore with id " + questionScoreId + " does not exist"));
//
//        questionScore.setScore(score);
//    }
}
