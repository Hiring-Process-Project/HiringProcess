package com.example.hiringProcess.Candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(path = "api/v1/Candidate")

public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

   @GetMapping (path="/candidates")
    public List<Candidate> getCandidates(){
        return candidateService.getCandidates();
    }

    @GetMapping (path="/candidate")
    public Optional<Candidate> getCandidate(Integer candidateId){
        return candidateService.getCandidate(candidateId);
    }

    @PostMapping (path="/newcandidate")
    public void addNewCandidate(Candidate candidate){
         candidateService.addNewCandidate(candidate);
    }


    @DeleteMapping(path = "{candidateId}")
    public void deleteCandidate(@PathVariable("candidateId") Integer id){
    candidateService.deleteCandidate(id);
    }

    @PutMapping(path = "{candidateId}")
    public void updateCandidate(@PathVariable("candidateId") Integer candidateId,
                                @RequestParam(required = false) String name) {
        candidateService.updateCandidate(candidateId, name);
    }

}