package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Skill.SkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/jobAds")
public class JobAdController {

    private final JobAdService jobAdService;

    @Autowired
    public JobAdController(JobAdService jobAdService) {
        this.jobAdService = jobAdService;
    }

    /* ===================== LIST / GET ONE ===================== */

    @GetMapping
    public List<JobAdSummaryDTO> getJobAds() {
        return jobAdService.getJobAdSummaries();
    }

    @GetMapping("/{jobAdId}")
    public Optional<JobAd> getJobAd(@PathVariable Integer jobAdId) {
        return jobAdService.getJobAd(jobAdId);
    }

    /* ===================== DETAILS (DTO) ===================== */

    // παλιό μοτίβο που ήδη χρησιμοποιεί ο frontend: /jobAds/details?jobAdId=...
    @GetMapping("/details")
    public ResponseEntity<JobAdDetailsDTO> getJobAdDetailsByQuery(@RequestParam("jobAdId") Integer jobAdId) {
        return jobAdService.getJobAdDetails(jobAdId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // και RESTful εναλλακτική: /jobAds/{jobAdId}/details
    @GetMapping("/{jobAdId}/details")
    public ResponseEntity<JobAdDetailsDTO> getJobAdDetailsByPath(@PathVariable Integer jobAdId) {
        return jobAdService.getJobAdDetails(jobAdId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update description + skills (frontend απλώς ελέγχει r.ok, δεν διαβάζει body)
    @PutMapping("/{jobAdId}/details")
    public ResponseEntity<Void> updateDetails(@PathVariable Integer jobAdId,
                                              @RequestBody JobAdUpdateDTO body) {
        jobAdService.updateDetails(jobAdId, body);
        return ResponseEntity.noContent().build(); // 204
    }

    /* ===================== CREATE / UPDATE / DELETE ===================== */

    @PostMapping
    public void addNewJobAd(@RequestBody JobAd jobAd) {
        jobAdService.addNewJobAd(jobAd);
    }

    @PostMapping("/by-names")
    public ResponseEntity<JobAd> addNewJobAdByNames(@RequestBody JobAdCreateByNamesRequest req) {
        // επιστρέφουμε το created entity (ή φτιάξε DTO αν προτιμάς)
        JobAd created = jobAdService.addNewJobAdByNames(req);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{jobAdId}")
    public void updateJobAd(@PathVariable Integer jobAdId, @RequestBody JobAd jobAd) {
        jobAdService.updateJobAd(jobAdId, jobAd);
    }

    @DeleteMapping("/{jobAdId}")
    public void deleteJobAd(@PathVariable Integer jobAdId) {
        jobAdService.deleteJobAd(jobAdId);
    }

    /* ===================== STATUS / PUBLISH ===================== */

    @PostMapping("/{jobAdId}/publish")
    public ResponseEntity<Void> publish(@PathVariable Integer jobAdId) {
        jobAdService.publish(jobAdId);
        return ResponseEntity.noContent().build();
    }

    /* ===================== SKILLS ===================== */

    // Skills που είναι δεμένα απευθείας στο job ad ( jobad_skill )
    @GetMapping("/{jobAdId}/skills")
    public ResponseEntity<List<JobAdSkillsDTO>> getSkillsForJobAd(@PathVariable Integer jobAdId) {
        Optional<JobAd> jobAdOpt = jobAdService.getJobAd(jobAdId);
        if (jobAdOpt.isEmpty()) return ResponseEntity.notFound().build();

        List<JobAdSkillsDTO> skills = jobAdOpt.get().getSkills().stream()
                .map(skill -> new JobAdSkillsDTO(skill.getTitle()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(skills);
    }

    // Alias για συμβατότητα με frontend (μερικές φορές ζητά "required-skills")
    @GetMapping("/{jobAdId}/required-skills")
    public ResponseEntity<List<JobAdSkillsDTO>> getRequiredSkillsForJobAd(@PathVariable Integer jobAdId) {
        return getSkillsForJobAd(jobAdId);
    }

    // Skills που προκύπτουν από το interview (βασισμένα στα steps/questions)
    @GetMapping("/{jobAdId}/interview-skills")
    public List<SkillDTO> getInterviewSkills(@PathVariable Integer jobAdId) {
        // ΕΠΙΣΗΜΑ: Διορθωμένο path. Δεν υπάρχει πλέον /jobAds/jobAds/...
        return jobAdService.getSkillsFromInterview(jobAdId);
    }
}
