package com.example.hiringProcess.Interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InterviewService {
    private final InterviewRepository interviewRepository;

    @Autowired
    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public List<Interview> getInterviews() {
        return interviewRepository.findAll();
    }

    public Optional<Interview> getInterview(Integer interviewId) {
        return interviewRepository.findById(interviewId);
    }

    public void addNewInterview(Interview interview) {
        interviewRepository.save(interview);
    }

    public void deleteInterview(Integer interviewId) {
        boolean exists = interviewRepository.existsById(interviewId);
        if (!exists) {
            throw new IllegalStateException("Interview with id " + interviewId + " does not exist");
        }
        interviewRepository.deleteById(interviewId);
    }

//    @Transactional
//    public void updateInterview(Integer interviewId, ...) {
//        Interview interview = interviewRepository.findById(interviewId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Interview with id " + interviewId + " does not exist"));
//        // Ενημέρωση πεδίων εδώ
//    }
}
