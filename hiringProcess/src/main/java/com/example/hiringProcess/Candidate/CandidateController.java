//package com.example.hiringProcess.Candidate;
//
//import com.example.hiringProcess.SkillScore.SkillScore;
//import com.example.hiringProcess.SkillScore.SkillScoreService;
//import com.example.hiringProcess.SkillScore.SkillScoreUpsertRequestDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//import com.example.hiringProcess.SkillScore.SkillScoreResponseDTO;
//import com.example.hiringProcess.SkillScore.SkillScoreUpsertRequestDTO;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/candidates")
//@CrossOrigin(origins = "http://localhost:3000") // dev
//public class CandidateController {
//
//    private final CandidateService candidateService;
//    private final SkillScoreService skillScoreService; // 👈 προσθήκη
//
//    @Value("${app.cv.dir:/opt/app/uploads/cv}")
//    private String cvBaseDir;
//
//    @Autowired
//    public CandidateController(CandidateService candidateService,
//                               SkillScoreService skillScoreService) { // 👈 προσθήκη
//        this.candidateService = candidateService;
//        this.skillScoreService = skillScoreService;
//    }
//
//    // -------- DETAILS (Entity) --------
//    @GetMapping("/{id}")
//    public Candidate getCandidate(@PathVariable Integer id) {
//        return candidateService.getCandidate(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));
//    }
//
//    // -------- UPDATE (Entity) --------
//    @PutMapping("/{id}")
//    public ResponseEntity<Candidate> updateCandidate(
//            @PathVariable("id") Integer id,
//            @RequestBody Candidate updatedCandidate) {
//        Candidate updated = candidateService.updateCandidate(id, updatedCandidate);
//        return ResponseEntity.ok(updated);
//    }
//
//    // -------- DELETE --------
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCandidate(@PathVariable Integer id) {
//        candidateService.deleteCandidate(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // -------- LIST (DTO) --------
//    @GetMapping
//    public List<CandidateDTO> getCandidates() {
//        return candidateService.getCandidateDTOs();
//    }
//
//    // -------- LIST BY JOB AD (DTO) --------
//    @GetMapping("/jobad/{jobAdId}")
//    public List<CandidateDTO> getCandidatesByJobAd(@PathVariable Integer jobAdId) {
//        return candidateService.getCandidateDTOsByJobAd(jobAdId);
//    }
//
//    // -------- COMMENTS (write) --------
//    @PatchMapping("/{id}/comments")
//    public ResponseEntity<Void> saveCandidateComment(@PathVariable Integer id,
//                                                     @RequestBody CandidateCommentDTO dto) {
//        if (dto == null || dto.getComments() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "comments is required");
//        }
//        candidateService.updateComments(id, dto.getComments());
//        return ResponseEntity.noContent().build();
//    }
//
//    // -------- EVALUATIONS (compat wrapper -> SkillScore upsert) --------
//    /**
//     * Συμβατότητα με παλιό front:
//     * POST /api/v1/candidates/{id}/evaluations
//     * Body: SkillEvaluationDTO { questionId, skillId, rating, comments }
//     *
//     * Μετατρέπουμε σε SkillScoreUpsertRequestDTO και κάνουμε upsert στη skill_score.
//     */
//    @PostMapping("/{id}/evaluations")
//    public ResponseEntity<SkillScoreResponseDTO> saveSkillEvaluation(
//            @PathVariable int id,
//            @RequestBody SkillScoreUpsertRequestDTO dto) {
//
//        if (dto == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body is required");
//        }
//        if (dto.questionId() == 0 || dto.skillId() == 0) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "questionId and skillId are required");
//        }
//        Integer rating = dto.score();
//        if (rating != null && (rating < 0 || rating > 100)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating must be between 0..100");
//        }
//
//        SkillScoreUpsertRequestDTO safeDto = new SkillScoreUpsertRequestDTO(
//                id,
//                dto.questionId(),
//                dto.skillId(),
//                dto.score(),
//                dto.comment(),
//                "system" // ή βάλε τον πραγματικό χρήστη όταν έχεις auth
//        );
//
//
//        // ⬇️ επιστρέφει DTO (ΟΧΙ entity)
//        SkillScoreResponseDTO saved = skillScoreService.upsert(safeDto);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//    }
//
//
//    // -------- CV DOWNLOAD --------
//    @GetMapping("/{id}/cv")
//    public ResponseEntity<Resource> downloadCv(@PathVariable Integer id) {
//        Candidate candidate = candidateService.getCandidate(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));
//
//        String cvPath = candidate.getCvPath();
//        if (cvPath == null || cvPath.isBlank()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CV not available");
//        }
//
//        Path filePath = Paths.get(cvBaseDir).resolve(cvPath).normalize();
//        if (!Files.exists(filePath)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CV file not found");
//        }
//
//        try {
//            Resource resource = new UrlResource(filePath.toUri());
//            String filename = filePath.getFileName().toString();
//            String contentType = Files.probeContentType(filePath);
//            if (contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
//                    .body(resource);
//
//        } catch (MalformedURLException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid CV path");
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CV download failed");
//        }
//    }
//
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<Void> updateCandidateStatus(
//            @PathVariable Integer id,
//            @RequestBody CandidateStatusDTO dto) {
//
//        if (dto == null || dto.getStatus() == null || dto.getStatus().isBlank()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
//        }
//
//        candidateService.updateStatus(id, dto); // mapper-based partial update
//        return ResponseEntity.noContent().build();
//    }
//
//
//    /**
//     * Επιστρέφει ΟΛΟΥΣ τους υποψηφίους ενός jobAd μαζί με το τελικό σκορ τους
//     * (ή null αν δεν έχουν καμία βαθμολογία), ταξινομημένους φθίνουσα.
//     */
//    @GetMapping("/jobad/{jobAdId}/final-scores")
//    public List<CandidateFinalScoreDTO> getFinalScoresForJobAd(@PathVariable Integer jobAdId) {
//        return candidateService.getCandidateFinalScoresForJobAd(jobAdId);
//    }
//
//
//
//
//}

package com.example.hiringProcess.Candidate;

import com.example.hiringProcess.SkillScore.SkillScoreResponseDTO;
import com.example.hiringProcess.SkillScore.SkillScoreService;
import com.example.hiringProcess.SkillScore.SkillScoreUpsertRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/candidates")
@CrossOrigin(origins = "http://localhost:3000") // dev
public class CandidateController {

    private final CandidateService candidateService;
    private final SkillScoreService skillScoreService; // 👈 προσθήκη

    @Autowired
    public CandidateController(CandidateService candidateService,
                               SkillScoreService skillScoreService) { // 👈 προσθήκη
        this.candidateService = candidateService;
        this.skillScoreService = skillScoreService;
    }

    // -------- DETAILS (Entity) --------
    @GetMapping("/{id}")
    public Candidate getCandidate(@PathVariable Integer id) {
        return candidateService.getCandidate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));
    }

    // -------- UPDATE (Entity) --------
    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(
            @PathVariable("id") Integer id,
            @RequestBody Candidate updatedCandidate) {
        Candidate updated = candidateService.updateCandidate(id, updatedCandidate);
        return ResponseEntity.ok(updated);
    }

    // -------- DELETE --------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Integer id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    // -------- LIST (DTO) --------
    @GetMapping
    public List<CandidateDTO> getCandidates() {
        return candidateService.getCandidateDTOs();
    }

    // -------- LIST BY JOB AD (DTO) --------
    @GetMapping("/jobad/{jobAdId}")
    public List<CandidateDTO> getCandidatesByJobAd(@PathVariable Integer jobAdId) {
        return candidateService.getCandidateDTOsByJobAd(jobAdId);
    }

    // -------- COMMENTS (write) --------
    @PatchMapping("/{id}/comments")
    public ResponseEntity<Void> saveCandidateComment(@PathVariable Integer id,
                                                     @RequestBody CandidateCommentDTO dto) {
        if (dto == null || dto.getComments() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "comments is required");
        }
        candidateService.updateComments(id, dto.getComments());
        return ResponseEntity.noContent().build();
    }

    // -------- EVALUATIONS (compat wrapper -> SkillScore upsert) --------
    /**
     * Συμβατότητα με παλιό front:
     * POST /api/v1/candidates/{id}/evaluations
     * Body: SkillEvaluationDTO { questionId, skillId, rating, comments }
     *
     * Μετατρέπουμε σε SkillScoreUpsertRequestDTO και κάνουμε upsert στη skill_score.
     */
    @PostMapping("/{id}/evaluations")
    public ResponseEntity<SkillScoreResponseDTO> saveSkillEvaluation(
            @PathVariable int id,
            @RequestBody SkillScoreUpsertRequestDTO dto) {

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body is required");
        }
        if (dto.questionId() == 0 || dto.skillId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "questionId and skillId are required");
        }
        Integer rating = dto.score();
        if (rating != null && (rating < 0 || rating > 100)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating must be between 0..100");
        }

        SkillScoreUpsertRequestDTO safeDto = new SkillScoreUpsertRequestDTO(
                id,
                dto.questionId(),
                dto.skillId(),
                dto.score(),
                dto.comment(),
                "system" // ή βάλε τον πραγματικό χρήστη όταν έχεις auth
        );

        // ⬇️ επιστρέφει DTO (ΟΧΙ entity)
        SkillScoreResponseDTO saved = skillScoreService.upsert(safeDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ---------- CV DOWNLOAD (flexible + fallback) ----------
    @GetMapping("/{id}/cv")
    public ResponseEntity<Resource> downloadCv(@PathVariable("id") int id) throws Exception {
        var cand = candidateService.getCandidate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate " + id + " not found"));

        String cvPath = cand.getCvPath();
        Resource res = resolveResource(cvPath);

        // Fallback αν λείπει/δεν διαβάζεται
        if (res == null || !res.exists() || !res.isReadable()) {
            res = new ClassPathResource("cv/SampleCV.pdf");
        }

        // Όνομα αρχείου για το download
        String first = Optional.ofNullable(cand.getFirstName()).orElse("Candidate");
        String last  = Optional.ofNullable(cand.getLastName()).orElse(String.valueOf(id));
        String fileName = sanitize(first + "_" + last) + "_CV.pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(fileName, StandardCharsets.UTF_8)
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(res);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateCandidateStatus(
            @PathVariable Integer id,
            @RequestBody CandidateStatusDTO dto) {

        if (dto == null || dto.getStatus() == null || dto.getStatus().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
        }

        candidateService.updateStatus(id, dto); // mapper-based partial update
        return ResponseEntity.noContent().build();
    }

    /**
     * Επιστρέφει ΟΛΟΥΣ τους υποψηφίους ενός jobAd μαζί με το τελικό σκορ τους
     * (ή null αν δεν έχουν καμία βαθμολογία), ταξινομημένους φθίνουσα.
     */
    @GetMapping("/jobad/{jobAdId}/final-scores")
    public List<CandidateFinalScoreDTO> getFinalScoresForJobAd(@PathVariable Integer jobAdId) {
        return candidateService.getCandidateFinalScoresForJobAd(jobAdId);
    }

    /** Δοκιμάζει: classpath:, http/https, file:, απόλυτο path, ή σχετικό path κάτω από uploads/cv */
    private Resource resolveResource(String path) {
        if (path == null || path.isBlank()) return null;
        try {
            if (path.startsWith("classpath:")) {
                return new ClassPathResource(path.substring("classpath:".length()));
            }
            if (path.startsWith("http://") || path.startsWith("https://")) {
                return new UrlResource(URI.create(path));
            }
            if (path.startsWith("file:")) {
                return new UrlResource(path);
            }

            Path p = Paths.get(path);
            if (!p.isAbsolute()) {
                // σχετικό: προσπάθησε πρώτα σε resources, μετά σε uploads/cv
                Resource cp = new ClassPathResource(path);
                if (cp.exists()) return cp;

                Path base = Paths.get("uploads", "cv").normalize();
                p = base.resolve(path).normalize();
                if (!p.startsWith(base)) return null; // ασφάλεια
            }
            if (Files.exists(p)) {
                return new FileSystemResource(p);
            }
        } catch (Exception ignored) { }
        return null;
    }

    /** Αφαίρεση “περίεργων” χαρακτήρων από το όνομα αρχείου */
    private String sanitize(String s) {
        return s == null ? "candidate"
                : s.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
