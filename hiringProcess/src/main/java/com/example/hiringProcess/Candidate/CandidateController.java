//package com.example.hiringProcess.Candidate;
//
//import com.example.hiringProcess.SkillScore.SkillScoreResponseDTO;
//import com.example.hiringProcess.SkillScore.SkillScoreService;
//import com.example.hiringProcess.SkillScore.SkillScoreUpsertRequestDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ContentDisposition;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import com.example.hiringProcess.JobAd.JobAd;
//import com.example.hiringProcess.JobAd.JobAdRepository;
//import com.example.hiringProcess.InterviewReport.InterviewReport;
//import com.example.hiringProcess.InterviewReport.InterviewReportRepository;
//
//import org.springframework.web.multipart.MultipartFile;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Map;
//
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/v1/candidates")
//@CrossOrigin(origins = "http://localhost:3000")
//public class CandidateController {
//
//    private final CandidateService candidateService;
//    private final SkillScoreService skillScoreService;
//    private final JobAdRepository jobAdRepository;
//    private final InterviewReportRepository interviewReportRepository;
//
//    @Autowired
//    public CandidateController(CandidateService candidateService,
//                               SkillScoreService skillScoreService,
//                               JobAdRepository jobAdRepository,
//                               InterviewReportRepository interviewReportRepository) {
//        this.candidateService = candidateService;
//        this.skillScoreService = skillScoreService;
//        this.jobAdRepository = jobAdRepository;
//        this.interviewReportRepository = interviewReportRepository;
//    }
//
//    // Επιστρέφει έναν συγκεκριμένο υποψήφιο (Entity)
//    @GetMapping("/{id}")
//    public Candidate getCandidate(@PathVariable Integer id) {
//        return candidateService.getCandidate(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));
//    }
//
//    // Ενημερώνει πεδία ενός υποψηφίου (Entity-based update)
//    @PutMapping("/{id}")
//    public ResponseEntity<Candidate> updateCandidate(
//            @PathVariable("id") Integer id,
//            @RequestBody Candidate updatedCandidate) {
//        Candidate updated = candidateService.updateCandidate(id, updatedCandidate);
//        return ResponseEntity.ok(updated);
//    }
//
//    // Διαγράφει έναν υποψήφιο
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCandidate(@PathVariable Integer id) {
//        candidateService.deleteCandidate(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Επιστρέφει όλους τους υποψήφιους (ως DTO)
//    @GetMapping
//    public List<CandidateDTO> getCandidates() {
//        return candidateService.getCandidateDTOs();
//    }
//
//    // Επιστρέφει όλους τους υποψήφιους ενός συγκεκριμένου JobAd (ως DTO)
//    @GetMapping("/jobad/{jobAdId}")
//    public List<CandidateDTO> getCandidatesByJobAd(@PathVariable Integer jobAdId) {
//        return candidateService.getCandidateDTOsByJobAd(jobAdId);
//    }
//
////    // COMMENTS (write)
////    @PatchMapping("/{id}/comments")
////    public ResponseEntity<Void> saveCandidateComment(@PathVariable Integer id,
////                                                     @RequestBody CandidateCommentDTO dto) {
////        if (dto == null || dto.getComments() == null) {
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "comments is required");
////        }
////        candidateService.updateComments(id, dto.getComments());
////        return ResponseEntity.noContent().build();
////    }
//
//    // Αποθηκεύει ή ενημερώνει αξιολόγηση δεξιοτήτων για έναν υποψήφιο
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
//                "system"
//        );
//
//        SkillScoreResponseDTO saved = skillScoreService.upsert(safeDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//    }
//
//    // Κατεβάζει το CV ενός υποψηφίου (αν δεν υπάρχει, επιστρέφει SampleCV)
//    @GetMapping("/{id}/cv")
//    public ResponseEntity<Resource> downloadCv(@PathVariable("id") int id) throws Exception {
//        var cand = candidateService.getCandidate(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate " + id + " not found"));
//
//        String cvPath = cand.getCvPath();
//        Resource res = resolveResource(cvPath);
//
//        // Fallback αν λείπει/δεν διαβάζεται
//        if (res == null || !res.exists() || !res.isReadable()) {
//            res = new ClassPathResource("cv/SampleCV.pdf");
//        }
//
//        // Κρατάμε το αρχικό όνομα του αρχείου όπως το ανέβασε ο χρήστης
//        String original = cand.getCvOriginalName();
//        String fileName;
//        if (original != null && !original.isBlank()) {
//            fileName = original;
//        } else {
//            String first = Optional.ofNullable(cand.getFirstName()).orElse("Candidate");
//            String last  = Optional.ofNullable(cand.getLastName()).orElse(String.valueOf(id));
//            fileName = sanitize(first + "_" + last) + "_CV.pdf";
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDisposition(
//                ContentDisposition.attachment()
//                        .filename(fileName, StandardCharsets.UTF_8)
//                        .build()
//        );
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(res);
//    }
//
//    // Ενημερώνει το status ενός υποψηφίου
//    public ResponseEntity<Void> updateCandidateStatus(
//            @PathVariable Integer id,
//            @RequestBody CandidateStatusDTO dto) {
//
//        if (dto == null || dto.getStatus() == null || dto.getStatus().isBlank()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
//        }
//
//        candidateService.updateStatus(id, dto);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Δημιουργεί νέο υποψήφιο και τον συνδέει με JobAd + InterviewReport
//    @PostMapping
//    public ResponseEntity<Candidate> createCandidate(
//            @RequestParam("jobAdId") Integer jobAdId,
//            @RequestParam(value = "interviewReportId", required = false) Integer ignoredInterviewReportId,
//            @RequestBody Candidate body) {
//
//        if (body == null
//                || body.getFirstName() == null || body.getFirstName().isBlank()
//                || body.getLastName()  == null || body.getLastName().isBlank()
//                || body.getEmail()     == null || body.getEmail().isBlank()
//                || jobAdId == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "firstName, lastName, email και jobAdId είναι υποχρεωτικά");
//        }
//
//        JobAd jobAd = jobAdRepository.findById(jobAdId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "JobAd " + jobAdId + " not found"));
//
//        body.setJobAd(jobAd);
//
//        if (body.getStatus() == null || body.getStatus().isBlank()) {
//            body.setStatus("Pending");
//        }
//        if (body.getComments() == null) {
//            body.setComments("");
//        }
//
//        InterviewReport newIr = new InterviewReport();
//        newIr.setInterview(jobAd.getInterview());
//        body.setInterviewReport(newIr);
//
//        candidateService.addNewCandidate(body);
//
//        return ResponseEntity
//                .created(URI.create("/api/v1/candidates/" + body.getId()))
//                .body(body);
//    }
//
//
//    // Επιστρέφει ΟΛΟΥΣ τους υποψηφίους ενός jobAd μαζί με το τελικό σκορ τους ταξινομημένους φθίνουσα.
//    @GetMapping("/jobad/{jobAdId}/final-scores")
//    public List<CandidateFinalScoreDTO> getFinalScoresForJobAd(@PathVariable Integer jobAdId) {
//        return candidateService.getCandidateFinalScoresForJobAd(jobAdId);
//    }
//
//    // Επιστρέφει το CV του υποψηφίου είτε από classpath (SampleCV.pdf) είτε από τον φάκελο uploads/cv
//    private Resource resolveResource(String path) {
//        if (path == null || path.isBlank()) return null;
//        try {
//            if (path.startsWith("classpath:")) {
//                return new ClassPathResource(path.substring("classpath:".length()));
//            }
//
//            Path base = Paths.get("uploads", "cv").normalize();
//            Path p = Paths.get(path).normalize();
//
//            if (p.isAbsolute()) {
//                return null;
//            }
//
//            if (!p.startsWith("uploads")) {
//                p = base.resolve(p).normalize();
//            }
//
//            if (!p.startsWith(base)) return null;
//
//            if (Files.exists(p) && Files.isReadable(p)) {
//                return new FileSystemResource(p);
//            }
//        } catch (Exception ignored) { }
//        return null;
//    }
//
//    // Κάνει hire έναν υποψήφιο και επιστρέφει ενημερωμένα στατιστικά JobAd
//    @PostMapping("/{id}/hire")
//    public CandidateAndJobAdStatusDTO hireCandidate(@PathVariable Integer id) {
//        return candidateService.hireCandidate(id);
//    }
//
//    // Upload CV (PDF) και αποθήκευση τοπικά στο uploads/cv
//    @PostMapping(value = "/upload-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Map<String, String> uploadCv(@RequestParam("file") MultipartFile file) {
//        try {
//            if (file == null || file.isEmpty()) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file");
//            }
//            if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF allowed");
//            }
//
//            Path base = Paths.get("uploads", "cv").normalize();
//            Files.createDirectories(base);
//
//            String ts = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS").format(LocalDateTime.now());
//            String original = file.getOriginalFilename();
//            String safe = (original == null ? "cv.pdf" : original.replaceAll("[^A-Za-z0-9._-]", "_"));
//            if (!safe.toLowerCase().endsWith(".pdf")) safe = safe + ".pdf";
//
//            Path out = base.resolve(ts + "_" + safe).normalize();
//            if (!out.startsWith(base)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad path");
//
//            Files.copy(file.getInputStream(), out);
//
//            String rel = base.resolve(out.getFileName()).toString().replace('\\', '/');
//
//            return Map.of(
//                    "path", rel,
//                    "originalName", (original != null && !original.isBlank()) ? original : "cv.pdf"
//            );
//        } catch (ResponseStatusException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed");
//        }
//    }
//
//    // Αφαίρεση “περίεργων” χαρακτήρων από το όνομα αρχείου
//    private String sanitize(String s) {
//        return s == null ? "candidate" : s.replaceAll("[^A-Za-z0-9._-]", "_");
//    }
//}
package com.example.hiringProcess.Candidate;

import com.example.hiringProcess.SkillScore.SkillScoreResponseDTO;
import com.example.hiringProcess.SkillScore.SkillScoreService;
import com.example.hiringProcess.SkillScore.SkillScoreUpsertRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.JobAd.JobAdRepository;
import com.example.hiringProcess.InterviewReport.InterviewReport;

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
@CrossOrigin(origins = "http://localhost:3000")
public class CandidateController {

    private final CandidateService candidateService;
    private final SkillScoreService skillScoreService;
    private final JobAdRepository jobAdRepository;

    @Autowired
    public CandidateController(CandidateService candidateService,
                               SkillScoreService skillScoreService,
                               JobAdRepository jobAdRepository) {
        this.candidateService = candidateService;
        this.skillScoreService = skillScoreService;
        this.jobAdRepository = jobAdRepository;
    }

    // Επιστροφή συγκεκριμένου υποψηφίου (Entity)
    @GetMapping("/{id}")
    public Candidate getCandidate(@PathVariable Integer id) {
        return candidateService.getCandidate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));
    }

    // Ενημέρωση στοιχείων υποψηφίου (Entity-based update)
    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(
            @PathVariable("id") Integer id,
            @RequestBody Candidate updatedCandidate) {
        Candidate updated = candidateService.updateCandidate(id, updatedCandidate);
        return ResponseEntity.ok(updated);
    }

    // Διαγραφή υποψηφίου
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Integer id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    // Λίστα όλων των υποψηφίων (DTO)
    @GetMapping
    public List<CandidateDTO> getCandidates() {
        return candidateService.getCandidateDTOs();
    }

    // Λίστα υποψηφίων για συγκεκριμένο Job Ad (DTO)
    @GetMapping("/jobad/{jobAdId}")
    public List<CandidateDTO> getCandidatesByJobAd(@PathVariable Integer jobAdId) {
        return candidateService.getCandidateDTOsByJobAd(jobAdId);
    }

    // Αποθήκευση/ενημέρωση αξιολόγησης δεξιοτήτων για υποψήφιο (upsert)
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

        // “Ασφαλές” DTO: το candidateId έρχεται από το path, όχι από το body
        SkillScoreUpsertRequestDTO safeDto = new SkillScoreUpsertRequestDTO(
                id,
                dto.questionId(),
                dto.skillId(),
                dto.score(),
                dto.comment()
        );

        SkillScoreResponseDTO saved = skillScoreService.upsert(safeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Λήψη CV υποψηφίου (fallback σε SampleCV αν λείπει)
    @GetMapping("/{id}/cv")
    public ResponseEntity<Resource> downloadCv(@PathVariable("id") int id) throws Exception {
        var cand = candidateService.getCandidate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate " + id + " not found"));

        String cvPath = cand.getCvPath();
        Resource res = resolveResource(cvPath);

        if (res == null || !res.exists() || !res.isReadable()) {
            res = new ClassPathResource("cv/SampleCV.pdf");
        }

        String original = cand.getCvOriginalName();
        String fileName;
        if (original != null && !original.isBlank()) {
            fileName = original;
        } else {
            String first = Optional.ofNullable(cand.getFirstName()).orElse("Candidate");
            String last  = Optional.ofNullable(cand.getLastName()).orElse(String.valueOf(id));
            fileName = sanitize(first + "_" + last) + "_CV.pdf";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(fileName, StandardCharsets.UTF_8)
                        .build()
        );

        return ResponseEntity.ok().headers(headers).body(res);
    }

    // Ενημέρωση status υποψηφίου (DTO)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateCandidateStatus(
            @PathVariable Integer id,
            @RequestBody CandidateStatusDTO dto) {
        if (dto == null || dto.getStatus() == null || dto.getStatus().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
        }
        candidateService.updateStatus(id, dto);
        return ResponseEntity.noContent().build();
    }

    // Δημιουργία νέου υποψηφίου + σύνδεση με JobAd & InterviewReport
    @PostMapping
    public ResponseEntity<Candidate> createCandidate(
            @RequestParam("jobAdId") Integer jobAdId,
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

        if (body.getStatus() == null || body.getStatus().isBlank()) {
            body.setStatus("Pending");
        }
        if (body.getComments() == null) {
            body.setComments("");
        }

        InterviewReport newIr = new InterviewReport();
        newIr.setInterview(jobAd.getInterview());
        body.setInterviewReport(newIr);

        candidateService.addNewCandidate(body);

        return ResponseEntity
                .created(URI.create("/api/v1/candidates/" + body.getId()))
                .body(body);
    }

    // Τελικά σκορ υποψηφίων για συγκεκριμένο Job Ad (φθίνουσα)
    @GetMapping("/jobad/{jobAdId}/final-scores")
    public List<CandidateFinalScoreDTO> getFinalScoresForJobAd(@PathVariable Integer jobAdId) {
        return candidateService.getCandidateFinalScoresForJobAd(jobAdId);
    }

    // Hire υποψηφίου και επιστροφή ενημερωμένου status
    @PostMapping("/{id}/hire")
    public CandidateAndJobAdStatusDTO hireCandidate(@PathVariable Integer id) {
        return candidateService.hireCandidate(id);
    }

    // Upload CV (PDF) στο uploads/cv και επιστροφή path + originalName
    @PostMapping(value = "/upload-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadCv(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No file");
            }
            if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF allowed");
            }

            Path base = Paths.get("uploads", "cv").normalize();
            Files.createDirectories(base);

            String ts = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS").format(LocalDateTime.now());
            String original = file.getOriginalFilename();
            String safe = (original == null ? "cv.pdf" : original.replaceAll("[^A-Za-z0-9._-]", "_"));
            if (!safe.toLowerCase().endsWith(".pdf")) safe = safe + ".pdf";

            Path out = base.resolve(ts + "_" + safe).normalize();
            if (!out.startsWith(base)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad path");

            Files.copy(file.getInputStream(), out);

            String rel = base.resolve(out.getFileName()).toString().replace('\\', '/');

            return Map.of(
                    "path", rel,
                    "originalName", (original != null && !original.isBlank()) ? original : "cv.pdf"
            );
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed");
        }
    }

    // Ενημερώνει το σχόλιο (comments) ενός υποψηφίου βάσει id
//    @PatchMapping("/{id}/comments")
//    public ResponseEntity<Void> saveCandidateComment(
//            @PathVariable Integer id,
//            @RequestBody CandidateCommentDTO dto) {
//
//        if (dto == null || dto.getComments() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "comments is required");
//        }
//        candidateService.updateComments(id, dto.getComments());
//        return ResponseEntity.noContent().build();
//    }
    @PatchMapping("/{id}/comments")
    public ResponseEntity<Void> updateCandidateComments(
            @PathVariable Integer id,
            @RequestBody CandidateCommentDTO dto) {

        if (dto == null || dto.getComments() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "comments are required");
        }
        candidateService.updateComments(id, dto.getComments());
        return ResponseEntity.noContent().build();
    }


    // Helper: επίλυση resource από classpath ή uploads/cv
    private Resource resolveResource(String path) {
        if (path == null || path.isBlank()) return null;
        try {
            if (path.startsWith("classpath:")) {
                return new ClassPathResource(path.substring("classpath:".length()));
            }
            Path base = Paths.get("uploads", "cv").normalize();
            Path p = Paths.get(path).normalize();
            if (p.isAbsolute()) return null;
            if (!p.startsWith("uploads")) {
                p = base.resolve(p).normalize();
            }
            if (!p.startsWith(base)) return null;
            if (Files.exists(p) && Files.isReadable(p)) {
                return new FileSystemResource(p);
            }
        } catch (Exception ignored) { }
        return null;
    }


    // Helper: καθαρισμός filename
    private String sanitize(String s) {
        return s == null ? "candidate" : s.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}

