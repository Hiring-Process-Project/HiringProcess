package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.InterviewGuide.InterviewGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class JobAdController {
    private final JobAdService jobAdService;

    @Autowired

    public JobAdController(JobAdService jobAdService) {
        this.jobAdService = jobAdService;
    }

    @GetMapping
    public List<JobAd> getCandidates(){
        return jobAdService.getCandidates();
    }

    @PostMapping
    public void registerNewCandidate(@RequestBody JobAd jobAd){
        jobAdService.addNewCandidate(jobAd);
    }

    @DeleteMapping(path = "{candidateId}")
    public void deleteCandidate(@PathVariable("candidateId") Integer id){
        jobAdService.deleteCandidate(id);
    }

    @PutMapping(path = "{candidateId}")
    public void updateCandidate(@PathVariable("candidateId") Integer candidateId,
                                @RequestParam(required  =false) String name) {
        jobAdService.updateCandidate(candidateId, name);
    }

}
