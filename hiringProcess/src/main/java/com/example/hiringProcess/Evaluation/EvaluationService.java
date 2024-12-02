package com.example.hiringProcess.Evaluation;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Candidate.CandidateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public EcaluationService(CandidateRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    @GetMapping
    public List<Candidate> getCandidates(){
        return evaluationRepository.findAll();
    }

    public void addNewCandidate(Candidate candidate) {
        Optional<Candidate> candidateByName = evaluationRepository.findCandidateByName(candidate.getName());
        if (candidateByName.isPresent()){
            throw new IllegalStateException("name taken");
        }
        evaluationRepository.save(candidate);
    }

    public void deleteCandidate(Integer candidateId) {
        boolean exists = evaluationRepository.existsById(candidateId);

        if(!exists){
            throw  new IllegalStateException("candidate with id" + candidateId + "does not exists");
        }
        evaluationRepository.deleteById(candidateId);
    }

    @Transactional
    public void updateCandidate(Integer candidateId, String name) {
        Evaluation evaluation = evaluationRepository.findById(candidateId).orElseThrow(() -> new IllegalStateException(
                "candidate with id" + candidateId + "does not exists"));

        if(name != null && name.length()>0 && !Objects.equals(evaluation.getName(),name)){
            evaluation.setName(name);
        }
    }
}