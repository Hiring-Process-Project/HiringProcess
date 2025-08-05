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
    public Candidate updateCandidate(Integer candidateId, Candidate updatedFields) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));

        if (updatedFields.getFirstName() != null &&
                !updatedFields.getFirstName().isEmpty() &&
                !Objects.equals(candidate.getFirstName(), updatedFields.getFirstName())) {
            candidate.setFirstName(updatedFields.getFirstName());
        }

        if (updatedFields.getLastName() != null &&
                !updatedFields.getLastName().isEmpty() &&
                !Objects.equals(candidate.getLastName(), updatedFields.getLastName())) {
            candidate.setLastName(updatedFields.getLastName());
        }

        if (updatedFields.getEmail() != null &&
                !updatedFields.getEmail().isEmpty() &&
                !Objects.equals(candidate.getEmail(), updatedFields.getEmail())) {
            candidate.setEmail(updatedFields.getEmail());
        }

        if (updatedFields.getInfo() != null &&
                !updatedFields.getInfo().isEmpty() &&
                !Objects.equals(candidate.getInfo(), updatedFields.getInfo())) {
            candidate.setInfo(updatedFields.getInfo());
        }

        if (updatedFields.getDecision() != null &&
                !Objects.equals(candidate.getDecision(), updatedFields.getDecision())) {
            candidate.setDecision(updatedFields.getDecision());
        }

        if (updatedFields.getReasoning() != null &&
                !updatedFields.getReasoning().isEmpty() &&
                !Objects.equals(candidate.getReasoning(), updatedFields.getReasoning())) {
            candidate.setReasoning(updatedFields.getReasoning());
        }

        return candidate; // το αντικείμενο είναι ήδη ενημερωμένο λόγω @Transactional
    }

}
