package com.example.hiringProcess.Interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void updateInterview(Integer interviewId, Interview updatedInterview) {
        Interview existing = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalStateException("Interview with id " + interviewId + " does not exist"));

        // Primitive/simple fields
        if (updatedInterview.getTitle() != null) {
            existing.setTitle(updatedInterview.getTitle());
        }
        if (updatedInterview.getDescription() != null) {
            existing.setDescription(updatedInterview.getDescription());
        }

        // Steps: αντικαθιστούμε πλήρως μόνο αν δόθηκαν (το setSteps φροντίζει το bidirectional link)
        if (updatedInterview.getSteps() != null) {
            existing.setSteps(updatedInterview.getSteps());
        }

        // InterviewReport: αντικαθιστούμε μόνο αν δόθηκε νέο
        if (updatedInterview.getInterviewReport() != null) {
            existing.setInterviewReport(updatedInterview.getInterviewReport());
        }

        // Σημείωση: το jobAd δεν το αλλάζουμε εδώ. Αν θες να το αλλάξεις,
        // μπορείς να προσθέσεις παρόμοια λογική με conditional set.
        // Δεν χρειάζεται explicit save επειδή είσαι σε @Transactional context.
    }

    public void deleteInterview(Integer interviewId) {
        boolean exists = interviewRepository.existsById(interviewId);

        if (!exists) {
            throw new IllegalStateException("Interview with id " + interviewId + " does not exist");
        }
        interviewRepository.deleteById(interviewId);
    }


}
