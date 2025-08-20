////// src/main/java/com/example/hiringProcess/Candidate/CandidateService.java
////package com.example.hiringProcess.Candidate;
////
////import jakarta.transaction.Transactional;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////import org.springframework.util.Assert;
////
////import com.example.hiringProcess.Skill.Skill;
////import com.example.hiringProcess.Skill.SkillRepository;
////
////import java.util.List;
////import java.util.Objects;
////import java.util.Optional;
////
////@Service
////public class CandidateService {
////
////    private final CandidateRepository candidateRepository;
////    private final SkillRepository skillRepository;
////    private final CandidateMapper candidateMapper;
////
////    @Autowired
////    public CandidateService(CandidateRepository candidateRepository,
////                            SkillRepository skillRepository,
////                            CandidateMapper candidateMapper) {
////        this.candidateRepository = candidateRepository;
////        this.skillRepository = skillRepository;
////        this.candidateMapper = candidateMapper;
////    }
////
////    // -------- READS --------
////
////    public List<Candidate> getCandidates() {
////        return candidateRepository.findAll();
////    }
////
////    public Optional<Candidate> getCandidate(Integer candidateId) {
////        return candidateRepository.findById(candidateId);
////    }
////
////    // Λίστα για το front (DTOs)
////    public List<CandidateDTO> getCandidateDTOs() {
////        return candidateRepository.findAll()
////                .stream()
////                .map(candidateMapper::toListDto)
////                .toList();
////    }
////
////    // Read comments (αν το χρειαστείς)
////    public CandidateCommentDTO getCandidateComments(Integer candidateId) {
////        Candidate c = candidateRepository.findById(candidateId)
////                .orElseThrow(() -> new IllegalStateException("Candidate with id " + candidateId + " does not exist"));
////        return candidateMapper.toCommentDto(c);
////    }
////
////    // -------- WRITES --------
////
////    public void addNewCandidate(Candidate candidate) {
////        candidateRepository.save(candidate);
////    }
////
////    public void deleteCandidate(Integer candidateId) {
////        boolean exists = candidateRepository.existsById(candidateId);
////        if (!exists) {
////            throw new IllegalStateException("candidate with id " + candidateId + " does not exist");
////        }
////        candidateRepository.deleteById(candidateId);
////    }
////
////    @Transactional
////    public Candidate updateCandidate(Integer candidateId, Candidate updatedFields) {
////        Candidate candidate = candidateRepository.findById(candidateId)
////                .orElseThrow(() -> new IllegalStateException(
////                        "Candidate with id " + candidateId + " does not exist"));
////
////        if (updatedFields.getFirstName() != null &&
////                !updatedFields.getFirstName().isEmpty() &&
////                !Objects.equals(candidate.getFirstName(), updatedFields.getFirstName())) {
////            candidate.setFirstName(updatedFields.getFirstName());
////        }
////
////        if (updatedFields.getLastName() != null &&
////                !updatedFields.getLastName().isEmpty() &&
////                !Objects.equals(candidate.getLastName(), updatedFields.getLastName())) {
////            candidate.setLastName(updatedFields.getLastName());
////        }
////
////        if (updatedFields.getEmail() != null &&
////                !updatedFields.getEmail().isEmpty() &&
////                !Objects.equals(candidate.getEmail(), updatedFields.getEmail())) {
////            candidate.setEmail(updatedFields.getEmail());
////        }
////
////        if (updatedFields.getCvPath() != null &&
////                !updatedFields.getCvPath().isEmpty() &&
////                !Objects.equals(candidate.getCvPath(), updatedFields.getCvPath())) {
////            candidate.setCvPath(updatedFields.getCvPath());
////        }
////
////        if (updatedFields.getInfo() != null &&
////                !updatedFields.getInfo().isEmpty() &&
////                !Objects.equals(candidate.getInfo(), updatedFields.getInfo())) {
////            candidate.setInfo(updatedFields.getInfo());
////        }
////
////        if (updatedFields.getStatus() != null &&
////                !Objects.equals(candidate.getStatus(), updatedFields.getStatus())) {
////            candidate.setStatus(updatedFields.getStatus());
////        }
////
////        if (updatedFields.getComments() != null &&
////                !updatedFields.getComments().isEmpty() &&
////                !Objects.equals(candidate.getComments(), updatedFields.getComments())) {
////            candidate.setComments(updatedFields.getComments());
////        }
////
////        return candidate; // managed entity, ενημερώνεται λόγω @Transactional
////    }
////
////    // Update μόνο των comments (καθαρό)
////    @Transactional
////    public void updateComments(Integer candidateId, String comments) {
////        Candidate candidate = candidateRepository.findById(candidateId)
////                .orElseThrow(() -> new IllegalStateException(
////                        "Candidate with id " + candidateId + " does not exist"));
////        candidate.setComments(comments);
////    }
////
////    // Εναλλακτικά, με mapper merge από DTO:
////    @Transactional
////    public void updateComments(Integer candidateId, CandidateCommentDTO dto) {
////        Candidate candidate = candidateRepository.findById(candidateId)
////                .orElseThrow(() -> new IllegalStateException(
////                        "Candidate with id " + candidateId + " does not exist"));
////        candidateMapper.updateCommentsFromDto(dto, candidate);
////    }
////
////    // Αποθήκευση αξιολόγησης Skill → γράφει score στο Skill
////    @Transactional
////    public void saveSkillEvaluation(SkillEvaluationDTO dto) {
////        if (dto == null) throw new IllegalArgumentException("SkillEvaluationDTO is null");
////
////        Integer candidateId = dto.getCandidateId();
////        Integer skillId     = dto.getSkillId();
////        Integer rating      = dto.getRating(); // 0–100
////
////        Assert.notNull(candidateId, "candidateId is required");
////        Assert.notNull(skillId, "skillId is required");
////        Assert.notNull(rating, "rating is required");
////
////        if (rating < 0 || rating > 100) {
////            throw new IllegalArgumentException("rating must be between 0 and 100");
////        }
////
////        // check candidate existence
////        candidateRepository.findById(candidateId)
////                .orElseThrow(() -> new IllegalStateException(
////                        "Candidate with id " + candidateId + " does not exist"));
////
////        // find Skill and write score
////        Skill skill = skillRepository.findById(skillId)
////                .orElseThrow(() -> new IllegalStateException(
////                        "Skill with id " + skillId + " does not exist"));
////
////        skill.setScore(rating);
////        // if (dto.getComments() != null) skill.setComments(dto.getComments());
////
////        skillRepository.save(skill);
////    }
////}
//// src/main/java/com/example/hiringProcess/Candidate/CandidateService.java
//package com.example.hiringProcess.Candidate;
//
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Assert;
//
//import com.example.hiringProcess.Skill.Skill;
//import com.example.hiringProcess.Skill.SkillRepository;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//@Service
//public class CandidateService {
//
//    private final CandidateRepository candidateRepository;
//    private final SkillRepository skillRepository;
//
//    @Autowired
//    public CandidateService(CandidateRepository candidateRepository,
//                            SkillRepository skillRepository
//            /* CandidateMapper candidateMapper -- ΔΕΝ ΤΟ ΧΡΕΙΑΖΟΜΑΣΤΕ */) {
//        this.candidateRepository = candidateRepository;
//        this.skillRepository = skillRepository;
//    }
//
//    // -------- READS --------
//
//    public List<Candidate> getCandidates() {
//        return candidateRepository.findAll();
//    }
//
//    public Optional<Candidate> getCandidate(Integer candidateId) {
//        return candidateRepository.findById(candidateId);
//    }
//
//    // Λίστα για το front (DTOs) — ΧΕΙΡΟΚΙΝΗΤΟ MAPPING
//    public List<CandidateDTO> getCandidateDTOs() {
//        return candidateRepository.findAll()
//                .stream()
//                .map(this::toListDto)   // χειροκίνητος mapper
//                .toList();
//    }
//
//    // Read comments (αν το χρειαστείς) — ΧΕΙΡΟΚΙΝΗΤΟ MAPPING
//    public CandidateCommentDTO getCandidateComments(Integer candidateId) {
//        Candidate c = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new IllegalStateException("Candidate with id " + candidateId + " does not exist"));
//        return toCommentDto(c); // χειροκίνητος mapper
//    }
//
//    // -------- WRITES --------
//
//    public void addNewCandidate(Candidate candidate) {
//        candidateRepository.save(candidate);
//    }
//
//    public void deleteCandidate(Integer candidateId) {
//        boolean exists = candidateRepository.existsById(candidateId);
//        if (!exists) {
//            throw new IllegalStateException("candidate with id " + candidateId + " does not exist");
//        }
//        candidateRepository.deleteById(candidateId);
//    }
//
//    @Transactional
//    public Candidate updateCandidate(Integer candidateId, Candidate updatedFields) {
//        Candidate candidate = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Candidate with id " + candidateId + " does not exist"));
//
//        if (updatedFields.getFirstName() != null &&
//                !updatedFields.getFirstName().isEmpty() &&
//                !Objects.equals(candidate.getFirstName(), updatedFields.getFirstName())) {
//            candidate.setFirstName(updatedFields.getFirstName());
//        }
//
//        if (updatedFields.getLastName() != null &&
//                !updatedFields.getLastName().isEmpty() &&
//                !Objects.equals(candidate.getLastName(), updatedFields.getLastName())) {
//            candidate.setLastName(updatedFields.getLastName());
//        }
//
//        if (updatedFields.getEmail() != null &&
//                !updatedFields.getEmail().isEmpty() &&
//                !Objects.equals(candidate.getEmail(), updatedFields.getEmail())) {
//            candidate.setEmail(updatedFields.getEmail());
//        }
//
//        if (updatedFields.getCvPath() != null &&
//                !updatedFields.getCvPath().isEmpty() &&
//                !Objects.equals(candidate.getCvPath(), updatedFields.getCvPath())) {
//            candidate.setCvPath(updatedFields.getCvPath());
//        }
//
//        if (updatedFields.getInfo() != null &&
//                !updatedFields.getInfo().isEmpty() &&
//                !Objects.equals(candidate.getInfo(), updatedFields.getInfo())) {
//            candidate.setInfo(updatedFields.getInfo());
//        }
//
//        if (updatedFields.getStatus() != null &&
//                !Objects.equals(candidate.getStatus(), updatedFields.getStatus())) {
//            candidate.setStatus(updatedFields.getStatus());
//        }
//
//        if (updatedFields.getComments() != null &&
//                !updatedFields.getComments().isEmpty() &&
//                !Objects.equals(candidate.getComments(), updatedFields.getComments())) {
//            candidate.setComments(updatedFields.getComments());
//        }
//
//        return candidate; // managed entity, ενημερώνεται λόγω @Transactional
//    }
//
//    // Update μόνο των comments (καθαρό)
//    @Transactional
//    public void updateComments(Integer candidateId, String comments) {
//        Candidate candidate = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Candidate with id " + candidateId + " does not exist"));
//        candidate.setComments(comments);
//    }
//
//    // Εναλλακτικά από DTO -> Entity (ΧΕΙΡΟΚΙΝΗΤΟ merge από DTO)
//    @Transactional
//    public void updateComments(Integer candidateId, CandidateCommentDTO dto) {
//        Candidate candidate = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Candidate with id " + candidateId + " does not exist"));
//
//        // manual "mapper merge"
//        if (dto != null && dto.getComments() != null) {
//            candidate.setComments(dto.getComments());
//        }
//        // δεν χρειάζεται save() λόγω @Transactional
//    }
//
//    // Αποθήκευση αξιολόγησης Skill → γράφει score στο Skill
//    @Transactional
//    public void saveSkillEvaluation(SkillEvaluationDTO dto) {
//        if (dto == null) throw new IllegalArgumentException("SkillEvaluationDTO is null");
//
//        Integer candidateId = dto.getCandidateId();
//        Integer skillId     = dto.getSkillId();
//        Integer rating      = dto.getRating(); // 0–100
//
//        Assert.notNull(candidateId, "candidateId is required");
//        Assert.notNull(skillId, "skillId is required");
//        Assert.notNull(rating, "rating is required");
//
//        if (rating < 0 || rating > 100) {
//            throw new IllegalArgumentException("rating must be between 0 and 100");
//        }
//
//        // check candidate existence
//        candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Candidate with id " + candidateId + " does not exist"));
//
//        // find Skill and write score
//        Skill skill = skillRepository.findById(skillId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Skill with id " + skillId + " does not exist"));
//
//        skill.setScore(rating);
//        // if (dto.getComments() != null) skill.setComments(dto.getComments());
//
//        skillRepository.save(skill);
//    }
//
//    // =========================
//    //  ΧΕΙΡΟΚΙΝΗΤΟΙ MAPPERS
//    // =========================
//
//    /** Entity -> List DTO (Candidates tab) */
//    private CandidateDTO toListDto(Candidate c) {
//        if (c == null) return null;
//        return new CandidateDTO(
//                c.getId(),
//                c.getFirstName(),
//                c.getLastName(),
//                c.getEmail(),
//                c.getStatus(),
//                c.getCvPath()
//        );
//    }
//
//    /** Entity -> Comment DTO (read) */
//    private CandidateCommentDTO toCommentDto(Candidate c) {
//        if (c == null) return null;
//        CandidateCommentDTO dto = new CandidateCommentDTO();
//        dto.setCandidateId(c.getId());
//        dto.setComments(c.getComments());
//        return dto;
//    }
//}
// src/main/java/com/example/hiringProcess/Candidate/CandidateService.java
// src/main/java/com/example/hiringProcess/Candidate/CandidateService.java
package com.example.hiringProcess.Candidate;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Skill.SkillRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;
    private final CandidateMapper candidateMapper;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository,
                            SkillRepository skillRepository,
                            CandidateMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
        this.candidateMapper = candidateMapper;
    }

    // -------- READS --------

    /** Επιστρέφει οντότητες (αν χρειάζεται αλλού) */
    public List<Candidate> getCandidates() {
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

    /** Read comments (DTO) — MapStruct mapping */
    public CandidateCommentDTO getCandidateComments(Integer candidateId) {
        Candidate c = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));
        return candidateMapper.toCommentDto(c);
    }

    // -------- WRITES --------

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

    // Update μόνο των comments (κρατάμε και την εκδοχή με String για συμμόρφωση με Controller)
    @Transactional
    public void updateComments(Integer candidateId, String comments) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));
        candidate.setComments(comments);
    }

    // Εναλλακτικά, από DTO (Mapper-based, χωρίς write στο id)
    @Transactional
    public void updateComments(Integer candidateId, CandidateCommentDTO dto) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));

        if (dto != null) {
            // Χρησιμοποιούμε MapStruct για in-place ενημέρωση ΜΟΝΟ των comments.
            candidateMapper.updateCommentsFromDto(dto, candidate);
        }
        // δεν χρειάζεται save() λόγω @Transactional
    }

    // Αποθήκευση αξιολόγησης Skill → γράφει score στο Skill
    @Transactional
    public void saveSkillEvaluation(SkillEvaluationDTO dto) {
        if (dto == null) throw new IllegalArgumentException("SkillEvaluationDTO is null");

        Integer candidateId = dto.getCandidateId();
        Integer skillId     = dto.getSkillId();
        Integer rating      = dto.getRating(); // 0–100

        Assert.notNull(candidateId, "candidateId is required");
        Assert.notNull(skillId, "skillId is required");
        Assert.notNull(rating, "rating is required");

        if (rating < 0 || rating > 100) {
            throw new IllegalArgumentException("rating must be between 0 and 100");
        }

        // check candidate existence
        candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException(
                        "Candidate with id " + candidateId + " does not exist"));

        // find Skill and write score
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalStateException(
                        "Skill with id " + skillId + " does not exist"));

        skill.setScore(rating);
        // if (dto.getComments() != null) skill.setComments(dto.getComments());

        skillRepository.save(skill);
    }
}
