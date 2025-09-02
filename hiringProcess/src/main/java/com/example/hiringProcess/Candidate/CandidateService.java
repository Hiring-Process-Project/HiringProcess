package com.example.hiringProcess.Candidate;

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

    /* ===================== READS ===================== */

    /** Επιστρέφει οντότητες (αν χρειάζεται αλλού) */
    public List<Candidate> getCandidatesEntities() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidate(Integer candidateId) {
        return candidateRepository.findById(candidateId);
    }

    /** Λίστα για το front (DTOs) — MapStruct mapping */
    public List<CandidateDTO> getCandidateDTOs() {
        return candidateRepository.findAll()
                .stream()
                .map(candidateMapper::toListDto)
                .toList();
    }

    /** Λίστα υποψηφίων (DTOs) για συγκεκριμένο Job Ad */
    public List<CandidateDTO> getCandidateDTOsByJobAd(Integer jobAdId) {
        return candidateRepository.findByJobAd_Id(jobAdId)
                .stream()
                .map(candidateMapper::toListDto)
                .toList();
    }

    /** Read comments (DTO) — MapStruct mapping */
    public CandidateCommentDTO getCandidateComments(Integer candidateId) {
        Candidate c = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));
        return candidateMapper.toCommentDto(c);
    }

    /* ===================== WRITES ===================== */

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

        return candidate; // managed entity, ενημερώνεται λόγω @Transactional
    }

    /** Update μόνο των comments (String) */
    @Transactional
    public void updateComments(Integer candidateId, String comments) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));
        candidate.setComments(comments);
    }

    /** Update comments μέσω DTO (Mapper-based) */
    @Transactional
    public void updateComments(Integer candidateId, CandidateCommentDTO dto) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));

        if (dto != null) {
            candidateMapper.updateCommentsFromDto(dto, candidate);
        }
        // @Transactional -> no explicit save() needed
    }

    @Transactional
    public void updateStatus(Integer candidateId, CandidateStatusDTO dto) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));

        if (dto != null) {
            candidateMapper.updateStatusFromDto(dto, candidate); // μόνο status
        }
        // @Transactional -> δεν χρειάζεται ρητό save()
    }


    /**
     * Επιστρέφει όλους τους υποψηφίους του jobAd μαζί με την τελική τους βαθμολογία,
     * ταξινομημένους από τον καλύτερο προς τον χειρότερο.
     * Το repository query μπορεί ήδη να έχει ORDER BY· αν όχι, υπάρχει fallback ταξινόμηση εδώ.
     */
    @Transactional(readOnly = true)
    public List<CandidateFinalScoreDTO> getCandidateFinalScoresForJobAd(Integer jobAdId) {
        List<CandidateFinalScoreDTO> list = candidateRepository.findFinalScoresByJobAd(jobAdId);

        // --- Fallback ταξινόμηση στο service (αν το SQL ΔΕΝ έχει ORDER BY):
        // list.sort(
        //     Comparator.comparing(
        //         CandidateFinalScoreDTO::getAverageScore,   // ή getAvgScore(), ανάλογα με το DTO σου
        //         Comparator.nullsLast(Comparator.naturalOrder())
        //     ).reversed()
        // );

        return list;
    }

}
