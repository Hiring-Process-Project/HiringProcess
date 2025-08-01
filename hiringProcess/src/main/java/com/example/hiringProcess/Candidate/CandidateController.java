package com.example.hiringProcess.Candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }



    // GET /api/v1/candidates - Λίστα όλων των υποψηφίων
    @GetMapping
    public List<CandidateDTO> getCandidates() {
        return candidateService.getCandidateDTOs();
    }


    // GET /api/v1/candidates/{id} - Ένας υποψήφιος με βάση το id
    @GetMapping("/{candidateId}")
    public Optional<Candidate> getCandidate(@PathVariable Integer candidateId) {
        return candidateService.getCandidate(candidateId);
    }

    // POST /api/v1/candidates - Προσθήκη νέου υποψηφίου
    @PostMapping
    public void addNewCandidate(@RequestBody Candidate candidate) {
        candidateService.addNewCandidate(candidate);
    }

    // DELETE /api/v1/candidates/{id} - Διαγραφή υποψηφίου
    @DeleteMapping("/{candidateId}")
    public void deleteCandidate(@PathVariable Integer candidateId) {
        candidateService.deleteCandidate(candidateId);
    }

    // PUT /api/v1/candidates/{id} - Ενημέρωση στοιχείων υποψηφίου
//    @PutMapping("/{candidateId}")
//    public void updateCandidate(
//            @PathVariable Integer candidateId,
//            @RequestBody Candidate candidateDetails
//    ) {
//        candidateService.updateCandidate(candidateId, candidateDetails);
//    }
}
