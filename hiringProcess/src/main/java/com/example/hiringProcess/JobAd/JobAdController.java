package com.example.hiringProcess.JobAd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class JobAdController {

    private final JobAdService jobAdService;

    @Autowired
    public JobAdController(JobAdService jobAdService) {
        this.jobAdService = jobAdService;
    }

    @GetMapping(path="/jobAds")
    public List<JobAd> getJobAds(){
        return jobAdService.getJobAds();
    }

    @GetMapping (path="/jobAd")
    public Optional<JobAd> getJobAd(Integer jobAdId){
        return jobAdService.getJobAd(jobAdId);
    }

    @PostMapping(path="/newjobAd")
    public void addNewJobAd(JobAd jobAd){
        jobAdService.addNewJobAd(jobAd);
    }

    @GetMapping("/summaries")
    public List<JobAdSummaryDTO> getJobAdSummaries() {
        return jobAdService.getJobAdSummaries();
    }



    @DeleteMapping(path = "{jobAdId}")
    public void deleteJobAd(@PathVariable("jobAdId") Integer id){
        jobAdService.deleteJobAd(id);
    }

    @PutMapping(path = "/{jobAdId}")
    public void putUpdateJobAd(@PathVariable("jobAdId") Integer id, @RequestBody JobAd jobAd) {
        jobAdService.updateJobAd(id, jobAd);
    }
    
}