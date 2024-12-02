package com.example.hiringProcess.Evaluation;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Candidate.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/Candidate")

public class EvaluationController {

    private final EvaluationService evaluationService;

    @Autowired
    public EvaluationController(CandidateService candidateService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping
    public List<Candidate> getCandidates(){
        return evaluationService.getCandidates();
    }

    @PostMapping
    public void registerNewCandidate(@RequestBody Candidate candidate){
        evaluationService.addNewCandidate(candidate);
    }

    @DeleteMapping(path = "{candidateId}")
    public void deleteCandidate(@PathVariable("candidateId") Integer id){
        evaluationService.deleteCandidate(id);
    }

    @PutMapping(path = "{candidateId}")
    public void updateCandidate(@PathVariable("candidateId") Integer candidateId,
                                @RequestParam(required  =false) String name) {
        evaluationService.updateCandidate(candidateId, name);
    }

}