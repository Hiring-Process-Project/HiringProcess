package com.example.hiringProcess.Interview;

import com.example.hiringProcess.Step.Step;
import com.example.hiringProcess.Step.StepResponseDTO;
import com.example.hiringProcess.Step.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class InterviewController {

    private final InterviewService interviewService;
    private final StepService stepService;

    @Autowired
    public InterviewController(InterviewService interviewService,
                               StepService stepService) {
        this.interviewService = interviewService;
        this.stepService = stepService;
    }

    // Λίστα όλων των interviews
    @GetMapping(path = "/interviews")
    public List<Interview> getInterviews() {
        return interviewService.getInterviews();
    }

    // Λεπτομέρειες interview για συγκεκριμένο JobAd (το χρησιμοποιεί ήδη το front)
    @GetMapping(path = "/jobAds/{jobAdId}/interview-details")
    public InterviewDetailsDTO getInterviewDetailsByJobAd(@PathVariable Integer jobAdId) {
        return interviewService.getInterviewDetailsByJobAd(jobAdId);
    }

    // Λήψη interview με id (path version)
    @GetMapping(path = "/interviews/{interviewId}")
    public ResponseEntity<Interview> getInterviewByPath(@PathVariable Integer interviewId) {
        Optional<Interview> it = interviewService.getInterview(interviewId);
        return it.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // (Προαιρετικά, αν κάπου χρησιμοποιείται) Λήψη interview με id (query param version)
    @GetMapping(path = "/interview")
    public Optional<Interview> getInterview(@RequestParam Integer interviewId) {
        return interviewService.getInterview(interviewId);
    }

    // Δημιουργία νέου interview (RESTful)
    @PostMapping(path = "/interviews")
    public void addInterview(@RequestBody Interview interview) {
        interviewService.addNewInterview(interview);
    }

    // Συμβατότητα με το παλιό endpoint σου
    @PostMapping(path = "/newinterview")
    public void addNewInterview(@RequestBody Interview interview) {
        interviewService.addNewInterview(interview);
    }

    // === ΝΕΟ: Δημιουργία Step μέσα σε συγκεκριμένο Interview ===
    // Primary endpoint που καλεί το modal: POST /interviews/{interviewId}/steps
    @PostMapping(path = "/interviews/{interviewId}/steps")
    public ResponseEntity<StepResponseDTO> addStepToInterview(
            @PathVariable Integer interviewId,
            @RequestBody StepResponseDTO body   // περιμένουμε { "title": "Technical" }
    ) {
        if (body == null || body.getTitle() == null || body.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Step created = stepService.createStep(interviewId, body.getTitle().trim());
        return ResponseEntity.ok(new StepResponseDTO(created.getId(), created.getTitle()));
    }

    @PutMapping("/interviews/{interviewId}/description")
    public ResponseEntity<Void> updateInterviewDescription(
            @PathVariable Integer interviewId,
            @RequestBody InterviewDescriptionDTO body) {

        if (body == null || body.description() == null) {
            return ResponseEntity.badRequest().build();
        }
        interviewService.updateDescription(interviewId, body.description());
        return ResponseEntity.ok().build();
    }

}
