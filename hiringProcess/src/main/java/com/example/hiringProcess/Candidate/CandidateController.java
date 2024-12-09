package com.example.hiringProcess.Candidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/Candidate")

public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/")
    public List<Candidate> getCandidates(){
     return candidateService.getCandidates();
    }

//    @PostMapping
//    public void registerNewCandidate(@RequestBody Candidate candidate){
//        candidateService.addNewCandidate(candidate);
//    }
//
//    @DeleteMapping(path = "{candidateId}")
//    public void deleteCandidate(@PathVariable("candidateId") Integer id){
//    candidateService.deleteCandidate(id);
//    }
//
//    @PutMapping(path = "{candidateId}")
//    public void updateCandidate(@PathVariable("candidateId") Integer candidateId,
//                                @RequestParam(required  =false) String name) {
//        candidateService.updateCandidate(candidateId, name);
//    }
//@GetMapping("/")
//public String home() {
//    return "Welcome to the Hiring Process!";
//}
}
