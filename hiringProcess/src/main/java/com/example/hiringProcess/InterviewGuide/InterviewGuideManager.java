//package com.example.hiringProcess.InterviewGuide;
//
//import com.example.hiringProcess.JobAd.JobAd;
//import com.example.hiringProcess.Skill;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//public class InterviewGuideManager {
//
//    private final InterviewGuideRepository interviewGuideRepository;
//    private final JobAdRepository jobAdRepository;
//
//    public InterviewGuideManager(InterviewGuideRepository interviewGuideRepository, JobAdRepository jobAdRepository) {
//        this.interviewGuideRepository = interviewGuideRepository;
//        this.jobAdRepository = jobAdRepository;
//    }
//
//    public InterviewGuide createInterviewGuide(int jobAdId) {
//        JobAd jobAd = jobAdRepository.findById(jobAdId)
//                .orElseThrow(() -> new IllegalArgumentException("Job Ad not found"));
//
//        InterviewGuide guide = new InterviewGuide(jobAd);
//        return interviewGuideRepository.save(guide);
//    }
//
//    public Optional<InterviewGuide> getGuideByJobAd(int jobAdId) {
//        return interviewGuideRepository.findById(jobAdId);
//    }
//
//    public void addSkillToGuide(int guideId, Skill skill) {
//        InterviewGuide guide = interviewGuideRepository.findById(guideId)
//                .orElseThrow(() -> new IllegalArgumentException("Guide not found"));
//
//        guide.addSkill(skill);
//        interviewGuideRepository.save(guide);
//    }
//
//    public void removeSkillFromGuide(int guideId, Skill skill) {
//        InterviewGuide guide = interviewGuideRepository.findById(guideId)
//                .orElseThrow(() -> new IllegalArgumentException("Guide not found"));
//
//        guide.removeSkill(skill);
//        interviewGuideRepository.save(guide);
//    }
//}
//
