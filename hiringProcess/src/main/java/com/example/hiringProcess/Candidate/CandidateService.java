package com.example.hiringProcess.Candidate;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<CandidateDTO> getCandidateDTOs() {
        return candidateRepository.findAll()
                .stream()
                .map(candidate -> new CandidateDTO(
                        candidate.getFirstName(),
                        candidate.getLastName(),
                        candidate.getEmail()))
                .toList();
    }


    public List<Candidate> getCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidate(Integer candidateId) {
        return candidateRepository.findById(candidateId);
    }

    public void addNewCandidate(Candidate candidate) {
//        Optional<Candidate> candidateByName = candidateRepository.findCandidateByFirstName(candidate.getFirstName());
//        if (candidateByName.isPresent()) {
//            throw new IllegalStateException("firstname taken");
//        }
        System.out.println("Saving candidate: " + candidate);
        candidateRepository.save(candidate);
    }




    public void deleteCandidate(Integer candidateId) {
        boolean exists = candidateRepository.existsById(candidateId);
        if (!exists) {
            throw new IllegalStateException("candidate with id " + candidateId + " does not exist");
        }
        candidateRepository.deleteById(candidateId);
    }

    @Transactional
    public void updateCandidate(Integer candidateId, String firstname) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException("candidate with id " + candidateId + " does not exist"));

        if (firstname != null && !firstname.isEmpty() && !Objects.equals(candidate.getFirstName(), firstname)) {
            candidate.setFirstName(firstname);
        }
    }
}
