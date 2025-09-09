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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.JobAd.JobAdRepository;
import com.example.hiringProcess.InterviewReport.InterviewReport;
import com.example.hiringProcess.InterviewReport.InterviewReportRepository;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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
    private final SkillScoreService skillScoreService;
    private final JobAdRepository jobAdRepository;
    private final InterviewReportRepository interviewReportRepository;

    @Autowired
    public CandidateController(CandidateService candidateService,
                               SkillScoreService skillScoreService,
                               JobAdRepository jobAdRepository,
                               InterviewReportRepository interviewReportRepository) {
        this.candidateService = candidateService;
        this.skillScoreService = skillScoreService;
        this.jobAdRepository = jobAdRepository;
        this.interviewReportRepository = interviewReportRepository;
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
                "system"
        );

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

    // ---------- CREATE CANDIDATE ----------
    @PostMapping
    public ResponseEntity<Candidate> createCandidate(
            @RequestParam("jobAdId") Integer jobAdId,
            @RequestParam(value = "interviewReportId", required = false) Integer ignoredInterviewReportId,
            @RequestBody Candidate body) {

        if (body == null
                || body.getFirstName() == null || body.getFirstName().isBlank()
                || body.getLastName()  == null || body.getLastName().isBlank()
                || body.getEmail()     == null || body.getEmail().isBlank()
                || jobAdId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "firstName, lastName, email και jobAdId είναι υποχρεωτικά");
        }

        JobAd jobAd = jobAdRepository.findById(jobAdId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "JobAd " + jobAdId + " not found"));

        body.setJobAd(jobAd);

        // Defaults
        if (body.getStatus() == null || body.getStatus().isBlank()) {
            body.setStatus("Pending");
        }
        if (body.getComments() == null) {
            body.setComments(""); // κενά σχόλια
        }

        // Δημιούργησε InterviewReport δεμένο με το Interview του JobAd
        InterviewReport newIr = new InterviewReport();
        newIr.setInterview(jobAd.getInterview());
        body.setInterviewReport(newIr);

        candidateService.addNewCandidate(body);

        return ResponseEntity
                .created(URI.create("/api/v1/candidates/" + body.getId()))
                .body(body);
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

    @PostMapping("/{id}/hire")
    public CandidateAndJobAdStatusDTO hireCandidate(@PathVariable Integer id) {
        return candidateService.hireCandidate(id);
    }

    // ===== NEW: Upload CV (PDF) =====
    @PostMapping(value = "/upload-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadCv(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file");
            }
            if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF allowed");
            }

            // φάκελος αποθήκευσης
            Path base = Paths.get("uploads", "cv").normalize();
            Files.createDirectories(base);

            String ts = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS").format(LocalDateTime.now());
            String safe = file.getOriginalFilename() == null ? "cv" : file.getOriginalFilename().replaceAll("[^A-Za-z0-9._-]", "_");
            if (!safe.toLowerCase().endsWith(".pdf")) safe = safe + ".pdf";

            Path out = base.resolve(ts + "_" + safe).normalize();
            if (!out.startsWith(base)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad path");

            Files.copy(file.getInputStream(), out);

            // επιστρέφουμε σχετική διαδρομή ώστε να μπορεί να χρησιμοποιηθεί ως cvPath
            String rel = base.resolve(out.getFileName()).toString().replace('\\', '/');
            return Map.of("path", rel);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed");
        }
    }

    /** Αφαίρεση “περίεργων” χαρακτήρων από το όνομα αρχείου */
    private String sanitize(String s) {
        return s == null ? "candidate"
                : s.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
