package com.example.hiringProcess.InterviewGuide;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Candidate.CandidateService;
import com.example.hiringProcess.Evaluation.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/Candidate")
public class InterviewGuideController {
    private final InterviewGuideService interviewGuideService;

    @Autowired
    public InterviewGuideController(InterviewGuideService interviewGuideService) {
        this.interviewGuideService = interviewGuideService;
    }

    @GetMapping
    public List<Candidate> getCandidates(){
        return interviewGuideService.getCandidates();
    }

    @PostMapping
    public void registerNewCandidate(@RequestBody Candidate candidate){
        interviewGuideService.addNewCandidate(candidate);
    }

    @DeleteMapping(path = "{candidateId}")
    public void deleteCandidate(@PathVariable("candidateId") Integer id){
        interviewGuideService.deleteCandidate(id);
    }

    @PutMapping(path = "{candidateId}")
    public void updateCandidate(@PathVariable("candidateId") Integer candidateId,
                                @RequestParam(required  =false) String name) {
        interviewGuideService.updateCandidate(candidateId, name);
    }

}
