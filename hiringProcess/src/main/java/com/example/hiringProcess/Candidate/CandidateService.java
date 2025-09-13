package com.example.hiringProcess.Candidate;

import com.example.hiringProcess.JobAd.JobAd;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository,
                            CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    // Επιστρέφει έναν υποψήφιο
    public Optional<Candidate> getCandidate(Integer candidateId) {
        return candidateRepository.findById(candidateId);
    }

    //Επιστρέφει λίστα όλων των υποψηφίων
    public List<CandidateDTO> getCandidateDTOs() {
        return candidateRepository.findAll()
                .stream()
                .map(candidateMapper::toListDto)
                .toList();
    }

    //Επιστρέφει λίστα όλων των υποψηφίων για ένα συγκεκριμένο job ad
    public List<CandidateDTO> getCandidateDTOsByJobAd(Integer jobAdId) {
        return candidateRepository.findByJobAd_Id(jobAdId)
                .stream()
                .map(candidateMapper::toListDto)
                .toList();
    }

    //Αποθηκεύει νέο candidate στη βάση
    public void addNewCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public void deleteCandidate(Integer candidateId) {
        boolean exists = candidateRepository.existsById(candidateId);
        if (!exists) {
            throw new IllegalStateException("candidate with id " + candidateId + " does not exist");
        }
        candidateRepository.deleteById(candidateId);
    }

    // Ενημερώνει πεδία ενός υποψηφίου (firstName, lastName, email, cvPath, status, comments, cvOriginalName)
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

        if (updatedFields.getStatus() != null &&
                !Objects.equals(candidate.getStatus(), updatedFields.getStatus())) {
            candidate.setStatus(updatedFields.getStatus());
        }

        if (updatedFields.getComments() != null &&
                !updatedFields.getComments().isEmpty() &&
                !Objects.equals(candidate.getComments(), updatedFields.getComments())) {
            candidate.setComments(updatedFields.getComments());
        }
        if (updatedFields.getCvOriginalName() != null &&
                !updatedFields.getCvOriginalName().isEmpty() &&
                !Objects.equals(candidate.getCvOriginalName(), updatedFields.getCvOriginalName())) {
            candidate.setCvOriginalName(updatedFields.getCvOriginalName());
        }

        return candidate;
    }

    // Update μόνο των comments
    @Transactional
    public void updateComments(Integer candidateId, String comments) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));
        candidate.setComments(comments);
    }

    // Update μόνο του status
    @Transactional
    public void updateStatus(Integer candidateId, CandidateStatusDTO dto) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));

        if (dto != null) {
            candidateMapper.updateStatusFromDto(dto, candidate); // μόνο status
        }
    }

    // Κάνει hire έναν υποψήφιο (μόνο αν είναι σε status Approved).
    // Επιστρέφει DTO με την κατάσταση του υποψηφίου και του job ad.
    @Transactional
    public CandidateAndJobAdStatusDTO hireCandidate(Integer candidateId) {
        Candidate cand = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException("Candidate not found"));

        JobAd job = cand.getJobAd();
        if (job == null) {
            throw new IllegalStateException("Candidate not linked to a JobAd");
        }

        // Idempotent ανά υποψήφιο
        if ("Hired".equalsIgnoreCase(cand.getStatus())) {
            long hiredCountNow = candidateRepository.countByJobAd_IdAndStatusIgnoreCase(job.getId(), "Hired");
            return new CandidateAndJobAdStatusDTO(
                    cand.getId(),
                    cand.getStatus(),
                    job.getId(),
                    job.getStatus(),
                    hiredCountNow
            );
        }

        // Επιτρέπουμε hire μόνο από Approved
        if (!"Approved".equalsIgnoreCase(cand.getStatus())) {
            throw new IllegalStateException("Only Approved candidates can be hired");
        }

        cand.setStatus("Hired");

        long hiredCountAfter = candidateRepository.countByJobAd_IdAndStatusIgnoreCase(job.getId(), "Hired");

        return new CandidateAndJobAdStatusDTO(
                cand.getId(),
                cand.getStatus(),
                job.getId(),
                job.getStatus(),
                hiredCountAfter
        );
    }

     // Επιστρέφει όλους τους υποψηφίους του jobAd  με την τελική τους βαθμολογία,
     // ταξινομημένους από τον καλύτερο προς τον χειρότερο.
    @Transactional(readOnly = true)
    public List<CandidateFinalScoreDTO> getCandidateFinalScoresForJobAd(Integer jobAdId) {
        List<CandidateFinalScoreDTO> list = candidateRepository.findFinalScoresByJobAd(jobAdId);
        return list;
    }
}
