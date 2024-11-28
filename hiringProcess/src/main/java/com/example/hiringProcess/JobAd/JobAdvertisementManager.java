//package com.example.hiringProcess.JobAd;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//public class JobAdvertisementManager {
//
//    private final JobAdRepository jobAdRepository;
//
//    public JobAdvertisementManager(JobAdRepository jobAdRepository) {
//        this.jobAdRepository = jobAdRepository;
//    }
//
//    public JobAd createJobAd(JobAd jobAd) {
//        return jobAdRepository.save(jobAd);
//    }
//
//    public List<JobAd> getAllJobAds() {
//        return jobAdRepository.findAll();
//    }
//
//    public JobAd getJobAdById(int jobAdId) {
//        return jobAdRepository.findById(jobAdId)
//                .orElseThrow(() -> new IllegalArgumentException("Job Ad not found"));
//    }
//
//    public JobAd updateJobAd(JobAd jobAd) {
//        if (jobAdRepository.existsById(jobAd.getId())) {
//            return jobAdRepository.save(jobAd);
//        }
//        throw new IllegalArgumentException("Job Ad not found");
//    }
//
//    public void deleteJobAd(int jobAdId) {
//        jobAdRepository.deleteById(jobAdId);
//    }
//}
//
