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

    @GetMapping
    public List<JobAdSummaryDTO> getJobAds() {
        return jobAdService.getJobAdSummaries();
    }

    @GetMapping("/{jobAdId}")
    public Optional<JobAd> getJobAd(@PathVariable Integer jobAdId) {
        return jobAdService.getJobAd(jobAdId);
    }

    @GetMapping("/details")
    public ResponseEntity<JobAdDetailsDTO> getJobAdDetails(@RequestParam("jobAdId") Integer jobAdId) {
        return jobAdService.getJobAdDetails(jobAdId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addNewJobAd(@RequestBody JobAd jobAd) {
        jobAdService.addNewJobAd(jobAd);
    }

    @PostMapping("/by-names")
    public ResponseEntity<JobAd> addNewJobAdByNames(@RequestBody JobAdCreateByNamesRequest req) {
        return ResponseEntity.ok(jobAdService.addNewJobAdByNames(req));
    }

    @DeleteMapping("/{jobAdId}")
    public void deleteJobAd(@PathVariable Integer jobAdId) {
        jobAdService.deleteJobAd(jobAdId);
    }

    @PutMapping("/{jobAdId}")
    public void updateJobAd(@PathVariable Integer jobAdId, @RequestBody JobAd jobAd) {
        jobAdService.updateJobAd(jobAdId, jobAd);
    }

    @GetMapping("/{jobAdId}/skills")
    public ResponseEntity<List<JobAdSkillsDTO>> getSkillsForJobAd(@PathVariable Integer jobAdId) {
        Optional<JobAd> jobAdOpt = jobAdService.getJobAd(jobAdId);
        if (jobAdOpt.isPresent()) {
            List<JobAdSkillsDTO> skills = jobAdOpt.get().getSkills().stream()
                    .map(skill -> new JobAdSkillsDTO(skill.getName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(skills);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- NEW: Update details (description + skills) ---
    @PutMapping("/{jobAdId}/details")
    public ResponseEntity<JobAdDetailsDTO> updateDetails(
            @PathVariable Integer jobAdId,
            @RequestBody JobAdUpdateDTO body) {
        JobAd updated = jobAdService.updateDetails(jobAdId, body);
        return jobAdService.getJobAdDetails(updated.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build());
    }

    // --- NEW: Publish ---
    @PostMapping("/{jobAdId}/publish")
    public ResponseEntity<Void> publish(@PathVariable Integer jobAdId) {
        jobAdService.publish(jobAdId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobAds/{jobAdId}/interview-skills")
    public List<SkillDTO> getInterviewSkills(@PathVariable Integer jobAdId) {
        return jobAdService.getSkillsFromInterview(jobAdId);
    }

}
