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

    // Επιστροφή CandidateDTOs για το tab
    public List<CandidateDTO> getCandidateDTOs() {
        return candidateRepository.findAll()
                .stream()
                .map(c -> new CandidateDTO(
                        c.getId(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getEmail(),
                        c.getStatus(),
                        c.getCvPath()
                ))
                .toList();
    }

    public List<Candidate> getCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidate(Integer candidateId) {
        return candidateRepository.findById(candidateId);
    }

    public void addNewCandidate(Candidate candidate) {
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

        if (updatedFields.getCvPath() != null &&
                !updatedFields.getCvPath().isEmpty() &&
                !Objects.equals(candidate.getCvPath(), updatedFields.getCvPath())) {
            candidate.setCvPath(updatedFields.getCvPath());
        }

        if (updatedFields.getInfo() != null &&
                !updatedFields.getInfo().isEmpty() &&
                !Objects.equals(candidate.getInfo(), updatedFields.getInfo())) {
            candidate.setInfo(updatedFields.getInfo());
        }

        if (updatedFields.getStatus() != null &&
                !Objects.equals(candidate.getStatus(), updatedFields.getStatus())) {
            candidate.setStatus(updatedFields.getStatus());
        }

        if (updatedFields.getComments() != null &&
                !updatedFields.getComments().isEmpty() &&
                !Objects.equals(candidate.getComments(), updatedFields.getComments())) {
            candidate.setComments(updatedFields.getComments());
        }

        return candidate; // ενημερώνεται λόγω @Transactional
    }
}
