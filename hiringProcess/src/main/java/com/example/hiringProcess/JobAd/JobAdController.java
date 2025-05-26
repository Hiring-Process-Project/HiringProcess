package com.example.hiringProcess.JobAd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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


//    @DeleteMapping(path = "{jobAdId}")
//    public void deleteJobAd(@PathVariable("jobAdId") Integer id){
//        jobAdService.deleteJobAd(id);
//    }

//    @PutMapping(path = "{jobAdId}")
//    public void updateJobAd(@PathVariable("jobAdId") Integer jobAdId,
//                                @RequestParam(required = false) String name) {
//        jobAdService.updateJobAd(jobAdId, name);
//    }

}