package com.example.hiringProcess.Candidate;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

   @GetMapping(path="/candidates")//χρειαζεται??
    public List<Candidate> getCandidates(){
        return candidateRepository.findAll();
    }

    @GetMapping(path="/candidate")
    public Optional<Candidate> getCandidate(Integer candidateId) {
        return candidateRepository.findById(candidateId);
    }

    public void addNewCandidate(Candidate candidate) {
       Optional<Candidate> candidateByName = candidateRepository.findCandidateByName(candidate.getName());
       if (candidateByName.isPresent()){
           throw new IllegalStateException("name taken");
        }
       candidateRepository.save(candidate);
    }

    public void deleteCandidate(Integer candidateId) {
        boolean exists = candidateRepository.existsById(candidateId);

        if(!exists){
            throw  new IllegalStateException("candidate with id" + candidateId + "does not exists");
        }
        candidateRepository.deleteById(candidateId);
    }

    @Transactional
    public void updateCandidate(Integer candidateId, String name) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new IllegalStateException(
                "candidate with id" + candidateId + "does not exists"));

        if(name != null && !name.isEmpty() && !Objects.equals(candidate.getName(),name)){
            candidate.setFirstName(name);
        }
    }
}
