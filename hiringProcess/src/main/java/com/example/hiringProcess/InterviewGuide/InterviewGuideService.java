package com.example.hiringProcess.InterviewGuide;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Candidate.CandidateRepository;
import com.example.hiringProcess.Evaluation.EvaluationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InterviewGuideService {
    private final InterviewGuideRepository interviewGuideRepository;

    @Autowired
    public InterviewGuideService(InterviewGuideRepository interviewGuideRepository) {
        this.interviewGuideRepository = interviewGuideRepository;
    }

    @GetMapping
    public List<InterviewGuide> getCandidates(){
        return interviewGuideRepository.findAll();
    }

    public void addNewCandidate(InterviewGuide interviewGuide) {
        Optional<InterviewGuide> interviewGuideByName = interviewGuideRepository.findCandidateByName(interviewGuide.getName());
        if (candidateByName.isPresent()){
            throw new IllegalStateException("name taken");
        }
        interviewGuideRepository.save(interviewGuide);
    }

    public void deleteCandidate(Integer candidateId) {
        boolean exists = interviewGuideRepository.existsById(candidateId);

        if(!exists){
            throw  new IllegalStateException("candidate with id" + candidateId + "does not exists");
        }
        interviewGuideRepository.deleteById(candidateId);
    }

    @Transactional
    public void updateCandidate(Integer candidateId, String name) {
        InterviewGuide interviewGuide = interviewGuideRepository.findById(candidateId).orElseThrow(() -> new IllegalStateException(
                "candidate with id" + candidateId + "does not exists"));

        if(name != null && name.length()>0 && !Objects.equals(interviewGuide.getName(),name)){
            interviewGuide.setName(name);
        }
    }
}
